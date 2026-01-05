/* MaxSpec.java
 * Contains the function for getting the damage bonus from special weapons.
 *
 *
 * Copyright (c) 2023, Jacob Burton <https://github.com/j-cob44>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.maxhitcalc;

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.Skill;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.game.ItemManager;

/**
 * Contains functions for calculating max hit from a special attack weapon.
 */
public class MaxSpec
{
    private MaxHitCalcConfig config;
    private MaxHit maxHits;
    private ItemManager itemManager;
    private Client client;

    MaxSpec(MaxHitCalcPlugin plugin, MaxHitCalcConfig config, ItemManager itemManager, Client client)
    {
        this.config = config;
        this.maxHits = new MaxHit(plugin, config, itemManager, client);
        this.itemManager = itemManager;
        this.client = client;
    }

    double getSpecWeaponStat(Item[] playerEquipment)
    {
        String weaponName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        String ammoItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.AMMO);

        // Check if we even have a spec weapon
        // ***********************************************************************
        // Melee Spec weapon checks
        if(weaponName.contains("Armadyl godsword"))
        {
            return 1.375;
        }

        if(weaponName.contains("Ancient godsword"))
        {
            return 1.1;
        }

        if(weaponName.contains("Bandos godsword"))
        {
            return 1.21;
        }

        if(weaponName.contains("Saradomin godsword"))
        {
            return 1.1;
        }

        if(weaponName.contains("Zamorak godsword"))
        {
            return 1.1;
        }

        if(weaponName.contains("Saradomin sword"))
        {
            return 1.1;
        }

        if(weaponName.contains("Dragon dagger"))
        {
            return 1.15;
        }

        if(weaponName.contains("Dragon sword"))
        {
            return 1.25;
        }

        if(weaponName.contains("Dragon halberd"))
        {
            return 1.1;
        }

        if(weaponName.contains("Crystal halberd"))
        {
            if(!weaponName.contains("(basic)") && !weaponName.contains("(attuned)") && !weaponName.contains("(perfected)"))
            {
                return 1.1;
            }
        }

        if(weaponName.contains("Barrelchest"))
        {
            return 1.1;
        }

        if(weaponName.contains("Dragon hasta"))
        {
            return 1.0 + (0.5 * ((double)client.getVarpValue(VarPlayerID.SA_ENERGY) /1000)); // Varplayer: Special Attack Percent
        }

        if(weaponName.contains("Dragon longsword"))
        {
            return 1.25;
        }

        if(weaponName.contains("Dragon mace"))
        {
            return 1.5;
        }

        if(weaponName.contains("Dragon warhammer"))
        {
            return 1.5;
        }

        if(weaponName.contains("Rune claws"))
        {
            return 1.1;
        }

        if(weaponName.contains("Abyssal bludgeon"))
        {
            double currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
            double totalPrayer = client.getRealSkillLevel(Skill.PRAYER);
            return (1 +(0.005 * (totalPrayer - currentPrayer)));
        }

        if(weaponName.contains("Saradomin's blessed sword"))
        {
            return 1.25;
        }

        if (weaponName.contains("Osmumten's fang"))
        {
            return 1.16666667;
        }

        if(weaponName.contains("Voidwaker"))
        {
            return 1.5;
        }

        if(weaponName.contains("Vesta's longsword"))
        {
            return 1.20;
        }

        if(weaponName.contains("Statius's warhammer"))
        {
            return 1.25;
        }

        if(weaponName.contains("Soulreaper axe"))
        {
            return 1 + maxHits.getSoulStackBonus();
        }

        if(weaponName.contains("Dual macuahuitl"))
        {
            return 1.25;
        }

        if(weaponName.contains("Arkan blade"))
        {
            return 1.5;
        }

        // ***********************************************************************
        // Ranged Spec weapon checks
        if(weaponName.contains("Dark bow"))
        {
            if(ammoItemName.contains("Dragon arrow"))
            {
                return 1.5;
            }
            else if (ammoItemName.contains("arrow"))
            {
                return 1.3;
            }
        }
        if(weaponName.contains("Zaryte crossbow"))
        {
            if(ammoItemName.contains("bolts (e)"))
            {
                return 1.1;
            }
        }
        if(weaponName.contains("Dragon crossbow"))
        {
            return 1.2;
        }
        if(weaponName.contains("crossbow"))
        {
            if(ammoItemName.contains("Diamond bolts (e)"))
            {
                return 1.15;
            }

            if(ammoItemName.contains("Dragonstone bolts (e)"))
            {
                return 1.45;
            }

            if(ammoItemName.contains("Onyx bolts (e)"))
            {
                return 1.15;
            }

            if(ammoItemName.contains("Opal bolts (e)"))
            {
                return 1.25;
            }
        }

        if(weaponName.contains("Morrigan's throwing axe"))
        {
            return 1.25;
        }

        if(weaponName.contains("Webweaver bow"))
        {
            return 0.4;
        }

        if(weaponName.contains("Toxic blowpipe"))
        {
            return 1.5;
        }

        if(weaponName.contains("ballista"))
        {
            return 1.25;
        }

        // ***********************************************************************
        // Magic Spec weapon checks
        if(weaponName.contains("Eye of Ayak"))
        {
            return 1.3;
        }

        return 0; // Not a spec weapon with a damage boost
    }

    // Returns the maximum hit of a spec weapon that hits multiple times in one move.
    // Returns 0 if not a multi hit spec weapon.
    public int getSpecMultiHit(int hit)
    {
        // Get Current Equipment
        Item[] playerEquipment = EquipmentItems.getCurrentlyEquipped(client);

        String weaponName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);

        // Melee Double Hit Spec Weapons
        if(weaponName.contains("Dragon dagger"))
        {
            return hit * 2;
        }

        if(weaponName.contains("Dragon claws"))
        {
            int first = hit - 1;
            int second = (hit - (hit/2)) - 1;
            int third = (hit - ((hit*3/4))) - 1;
            int fourth = third + 1;

            // Debug
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "First: " + first, null);
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Second: " + second, null);
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Third: " + third, null);
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Fourth: " + fourth, null);

            return first + second + third + fourth;
        }

        if(weaponName.contains("Abyssal dagger"))
        {
            int secondHit = (int)(hit * 0.85);
            return hit + secondHit;
        }

        if(weaponName.contains("Saradomin sword"))
        {
            return hit + 16;
        }

        // Range Double Hit Spec Weapons
        if(weaponName.contains("Magic shortbow"))
        {
            return hit * 2;
        }

        if (weaponName.contains("Dragon knife"))
        {
            return hit * 2;
        }

        if(weaponName.contains("Webweaver bow"))
        {
            return hit * 4;
        }

        if(weaponName.contains("Burning claws"))
        {
            int absoluteMax = (int) (hit * 1.75);
            int realMax = (int) (absoluteMax * 0.25) + (int) (absoluteMax * 0.25) + (int) (absoluteMax * 0.5);
            return realMax;
        }

        // else
        return 0;
    }

    /**
     * Calculates Max Hit of a Special Attack.
     *
     * @return Max Hit of Special Attack as Double
     */
    public double calculate()
    {
        // Get Current Equipment
        Item[] playerEquipment = EquipmentItems.getCurrentlyEquipped(client);

        // Get Config Settings
        boolean doubleHitSetting = config.displayMultiHitWeaponsAsOneHit();

        // Get Spec modifier
        double specialAttackWeapon = this.getSpecWeaponStat(playerEquipment);

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Spec Modifier: " + specialAttackWeapon, null);

        double maxHit = maxHits.calculate(true);
        if(specialAttackWeapon != 0)
        {
            // Get Max hit then calculate Spec
            double maxSpecHit = Math.floor(maxHit) * specialAttackWeapon;

            return maxSpecHit;
        }
        else if (doubleHitSetting && (this.getSpecMultiHit((int)Math.floor(maxHit)) != 0))
        {
            // Niche cases where Special Attack does not increase Damage, but does hit twice. E.g: Dragon Knives, Magic Shortbow
            return maxHit;
        }

        return 0; // No spec attack on weapon
    }
}
