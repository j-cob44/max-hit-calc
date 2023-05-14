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

import net.runelite.api.*;

public class MaxSpec
{
    public static double getSpecWeaponStat(Client client, String weaponName, Item[] playerEquipment)
    {
        String ammoItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.AMMO.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()] != null)
        {
            ammoItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId()).getName();
        }


        // Check if we even have a spec weapon

        // Melee Checks
        if(weaponName.contains("Armadyl godsword"))
        {
            return 1.375;
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

        if(weaponName.contains("Dragon dagger"))
        {
            return 1.15;
        }

        if(weaponName.contains("Dragon halberd"))
        {
            return 1.1;
        }

        if(weaponName.contains("Crystal halberd"))
        {
            return 1.1;
        }

        if(weaponName.contains("Dragon hasta"))
        {
            return 1.0 + (0.5 * ((double)client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) /1000));
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

        if(weaponName.contains("Abyssal dagger"))
        {
            return 0.85;
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

        // Ranged Checks
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

        return 0; // Not a spec weapon with a damage boost
    }
}
