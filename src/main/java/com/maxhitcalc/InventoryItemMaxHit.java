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
    private static AttackStyle determineAttackStyle(Client client, int weaponID)
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

    private static Item[] changeEquipment(Client client, int slotID, int itemID, Item[] currentEquipment)
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


    // Calculate Max Hit for an inventory item
    public static double calculate(Client client, ItemManager itemManager, MaxHitCalcConfig config, int slotID, int itemID)
    {
        // Initialize Variables
        int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
        int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
        AttackStyle attackStyle = null;

        // Get Current Equipment
        Item[] playerEquipment;
        if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
        {
            playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
        }
        else {
            playerEquipment = null;
        }

        // Determine if Attack Style is correct
        if(slotID == 3)
        {
            // IS A WEAPON
            attackStyle = InventoryItemMaxHit.determineAttackStyle(client, itemID);
        }
        else
        {
            // Get Current Attack Style
            WeaponType weaponType = WeaponType.getWeaponType(weaponTypeID);
            AttackStyle[] weaponAttackStyles = weaponType.getAttackStyles();

            attackStyle = weaponAttackStyles[attackStyleID];
        }

        // Get corrected slot ID if player is not fully equipped
        //slotID = InventoryItemMaxHit.getCorrectedSlotID(client, slotID);

        // Change equipment slot to new item
        playerEquipment = InventoryItemMaxHit.changeEquipment(client, slotID, itemID, playerEquipment);

        // Find what type to calculate
        if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE))
        {
            return MaxHit.calculateMeleeMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
        }
        else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
        {
            return MaxHit.calculateRangedMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID, config.blowpipeDartType());
        }
        else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
        {
            double magicMaxHit = MaxHit.calculateMagicMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);

            // If -1, error, skip
            if (magicMaxHit > -1){
                return magicMaxHit;
            }
        }

        return -1;

    }
}
