/* InventoryItemMaxHit.java
 * Contains functions required for calculating max hit based on hovered equipment in inventory.
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
import net.runelite.client.game.ItemManager;

import java.util.List;

public class InventoryItemMaxHit extends MaxHit
{
    public static AttackStyle determineAttackStyle(Client client, int weaponID)
    {
        AttackStyle attackStyle;

        // Ranged
        if (client.getItemDefinition(weaponID).getName().contains("bow")
                || client.getItemDefinition(weaponID).getName().contains("Bow")
                || client.getItemDefinition(weaponID).getName().contains("chinchompa")
                || client.getItemDefinition(weaponID).getName().contains("Chinchompa")
                || client.getItemDefinition(weaponID).getName().contains("ballista")
                || client.getItemDefinition(weaponID).getName().contains("dart")
                || client.getItemDefinition(weaponID).getName().contains("knife")
                || client.getItemDefinition(weaponID).getName().contains("thrownaxe")
                || client.getItemDefinition(weaponID).getName().contains("Toktz-xil-ul")
                || client.getItemDefinition(weaponID).getName().contains("blowpipe"))
        {
            attackStyle = AttackStyle.RANGING;
        }
        // Magic
        else if (client.getItemDefinition(weaponID).getName().contains("sceptre")
                || client.getItemDefinition(weaponID).getName().contains("staff")
                || client.getItemDefinition(weaponID).getName().contains("Trident")
                || client.getItemDefinition(weaponID).getName().contains("Tumeken's Shadow")
                || client.getItemDefinition(weaponID).getName().contains("Staff")
                || client.getItemDefinition(weaponID).getName().contains("wand")
                || client.getItemDefinition(weaponID).getName().contains("crozier")
                || client.getItemDefinition(weaponID).getName().contains("Void knight mace"))
        {

            attackStyle = AttackStyle.CASTING;
        }
        else {
            // Assume Melee
            attackStyle = AttackStyle.AGGRESSIVE;
        }

        return attackStyle;
    }

    public static int getCorrectedSlotID(Client client, int slotToCheck){
        int correctSlotID = 0;

        switch (slotToCheck)
        {
            case 0:
                correctSlotID = EquipmentInventorySlot.HEAD.getSlotIdx();
                break;
            case 1:
                correctSlotID = EquipmentInventorySlot.CAPE.getSlotIdx();
                break;
            case 2:
                correctSlotID = EquipmentInventorySlot.AMULET.getSlotIdx();
                break;
            case 3:
                correctSlotID = EquipmentInventorySlot.WEAPON.getSlotIdx();
                break;
            case 4:
                correctSlotID = EquipmentInventorySlot.BODY.getSlotIdx();
                break;
            case 5:
                correctSlotID = EquipmentInventorySlot.SHIELD.getSlotIdx();
                break;
            case 7:
                correctSlotID = EquipmentInventorySlot.LEGS.getSlotIdx();
                break;
            case 9:
                correctSlotID = EquipmentInventorySlot.GLOVES.getSlotIdx();
                break;
            case 10:
                correctSlotID = EquipmentInventorySlot.BOOTS.getSlotIdx();
                break;
            case 12:
                correctSlotID = EquipmentInventorySlot.RING.getSlotIdx();
                break;
            case 13:
                correctSlotID = EquipmentInventorySlot.AMMO.getSlotIdx();
                break;
        }

        return correctSlotID;
    }

    public static Item[] changeEquipment(Client client, int slotID, int itemID, Item[] currentEquipment)
    {
        Item[] newEquipment = new Item[14];

        for(int i = 0; i < newEquipment.length; i++)
        {
            if(currentEquipment != null)
            {
                if(i < currentEquipment.length)
                {
                    if (currentEquipment[i] != null)
                    {
                        newEquipment[i] = currentEquipment[i]; // Set new slot item to old slot item
                    }
                }
            }

            if (newEquipment[i] == null)
            {
                newEquipment[i] = new Item(-1, 1);
            }
        }

        newEquipment[slotID] = new Item(itemID, 1);

        return newEquipment;
    }

    public static double calculateMeleeMaxHit(Client client, ItemManager itemManager, Item[] currentPlayerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, int slotID, int itemID)
    {
        // Change equipment slot to new item
        Item[] playerEquipment = changeEquipment(client, slotID, itemID, currentPlayerEquipment);

        // Calculate Melee Max Hit
        // Step 1: Calculate effective Strength
        int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
        double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
        int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = getVoidMeleeBonus(client, playerEquipment); // default 1;

        double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the base damage
        double strengthBonus = getMeleeStrengthBonus(client, itemManager, playerEquipment); // default 0

        double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
        double flooredBaseDamage = Math.floor(baseDamage);

        // Step 3: Calculate the bonus damage
        List<Double> specialBonusMultipliers = getMeleeSpecialBonusMultiplier(client, playerEquipment); // default empty

        double maxHit = flooredBaseDamage;

        if(!specialBonusMultipliers.isEmpty())
        {
            for (double bonus: specialBonusMultipliers)
            {
                maxHit += Math.floor(maxHit * bonus);
            }
        }

        // Complete
        return maxHit;
    }

    public static double calculateRangedMaxHit(Client client, ItemManager itemManager, Item[] currentPlayerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, int slotID, int itemID, MaxHitCalcConfig.BlowpipeDartType dartType)
    {
        // Change equipment slot to new item
        Item[] playerEquipment = changeEquipment(client, slotID, itemID, currentPlayerEquipment);

        // Calculate Ranged Max Hit
        // Step 1: Calculate effective ranged Strength
        int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED);
        double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
        int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = getVoidRangedBonus(client, playerEquipment); // default 1;

        double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the max hit
        double equipmentRangedStrength = getRangedStrengthBonus(client, itemManager, playerEquipment, dartType);

        double maxHit = (0.5 + ((effectiveRangedStrength * (equipmentRangedStrength + 64))/640));

        // Step 3: Bonus damage from special attack and effects
        // Not used here.

        return maxHit;
    }

    public static double calculateMagicMaxHit(Client client, ItemManager itemManager, Item[] currentPlayerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, int slotID, int itemID)
    {
        // Change equipment slot to new item
        Item[] playerEquipment = changeEquipment(client, slotID, itemID, currentPlayerEquipment);

        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        double spellBaseMaxHit = getSpellBaseHit(client, playerEquipment, weaponAttackStyle, client.getBoostedSkillLevel(Skill.MAGIC));

        if (spellBaseMaxHit == 0)
        {
            return -1;
        }

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = getMagicEquipmentBoost(client, itemManager, playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Step 3: Calculate Bonuses
        // Tome Bonuses
        double correctTomeSpellBonus = getTomeSpellBonus(client, playerEquipment, weaponAttackStyle); // default 1
        maxDamage = maxDamage * correctTomeSpellBonus;

        return maxDamage;
    }
}
