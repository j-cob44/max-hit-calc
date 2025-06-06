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

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.game.ItemManager;

public class InventoryItemMaxHit
{
    protected MaxHitCalcConfig config;
    private MaxHit maxHits;
    protected ItemManager itemManager;
    protected Client client;

    InventoryItemMaxHit(MaxHitCalcPlugin plugin, MaxHitCalcConfig config, ItemManager itemManager, Client client)
    {
        this.config = config;
        this.maxHits = new MaxHit(plugin, config, itemManager, client);
        this.itemManager = itemManager;
        this.client = client;
    }

    // Crudely determines attack style based on an item's name
    private AttackStyle determineAttackStyle(int weaponID)
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
                || client.getItemDefinition(weaponID).getName().contains("blowpipe")
                || client.getItemDefinition(weaponID).getName().contains("Tonalztics of ralos")
                || client.getItemDefinition(weaponID).getName().contains("Eclipse atlatl")
                || client.getItemDefinition(weaponID).getName().contains("Hunter's spear"))
        {
            attackStyle = AttackStyle.RANGING;
        }
        // Magic
        else if (client.getItemDefinition(weaponID).getName().contains("sceptre")
                || client.getItemDefinition(weaponID).getName().contains("staff")
                || client.getItemDefinition(weaponID).getName().contains("Trident")
                || client.getItemDefinition(weaponID).getName().contains("Tumeken's shadow")
                || client.getItemDefinition(weaponID).getName().contains("Staff")
                || client.getItemDefinition(weaponID).getName().contains("wand")
                || client.getItemDefinition(weaponID).getName().contains("crozier")
                || client.getItemDefinition(weaponID).getName().contains("Void knight mace")
                || client.getItemDefinition(weaponID).getName().contains("Blue moon spear"))
        {

            attackStyle = AttackStyle.CASTING;
        }
        else {
            // Assume Melee
            attackStyle = AttackStyle.AGGRESSIVE;
        }

        return attackStyle;
    }

    private Item[] changeEquipment(Item[] currentEquipment, int itemID, int slotID)
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


    /**
     * Predicts max hit of a given item if it is equipped.
     *
     * @param itemID int of Item ID to do prediction on
     * @param slotID int of slot the item will replace
     * @return Max Hit Prediction as Double
     */
    public double predict(int itemID, int slotID)
    {
        // Initialize Variables
        int attackStyleID = client.getVarpValue(VarPlayerID.COM_MODE); // Varplayer: Attack Style
        int weaponTypeID = client.getVarbitValue(VarbitID.COMBAT_WEAPON_CATEGORY);  // Varbit: Equipped Weapon Type
        AttackStyle attackStyle = null;

        // Get Current Equipment
        Item[] playerEquipment = EquipmentItems.getCurrentlyEquipped(client);

        // Determine if Attack Style is correct
        if(slotID == 3)
        {
            // IS A WEAPON
            attackStyle = determineAttackStyle(itemID);
        }
        else
        {
            // Get Current Attack Style
            AttackStyle[] weaponAttackStyles = WeaponType.getWeaponTypeStyles(client, weaponTypeID);
            attackStyle = weaponAttackStyles[attackStyleID];
        }

        // Get corrected slot ID if player is not fully equipped
        //slotID = InventoryItemMaxHit.getCorrectedSlotID(client, slotID);

        // Change equipment slot to new item
        playerEquipment = changeEquipment(playerEquipment, itemID, slotID);

        // Find what type to calculate
        if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE))
        {
            return maxHits.calculateMeleeMaxHit(playerEquipment, attackStyle, attackStyleID, false);
        }
        else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
        {
            return maxHits.calculateRangedMaxHit(playerEquipment, attackStyle, attackStyleID);
        }
        else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
        {
            double magicMaxHit = maxHits.calculateMagicMaxHit(playerEquipment, attackStyle);

            // If -1, error, skip
            if (magicMaxHit > -1){
                return magicMaxHit;
            }
        }

        return -1;

    }
}
