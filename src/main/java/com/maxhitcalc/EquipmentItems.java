/* EquipmentItems.java
 * Contains Functions for retrieving information about currently equipped items.
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
import net.runelite.api.InventoryID;
import net.runelite.api.Item;

public class EquipmentItems
{
    /**
     * Gets Currently Equipped Items
     *
     * @param client
     *
     * @return Item[] of all Equipped Items
     */
    public static Item[] getCurrentlyEquipped(Client client)
    {
        Item[] playerEquipment;
        if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
        {
            playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
        }
        else
        {
            playerEquipment = null;
        }

        return playerEquipment;
    }

    /**
     * Get Item Name in Specific Slot of Specified Equipment Set <br>
     *
     * @param client
     * @param ITEMSLOT the item slot id that will be checked
     * @param playerEquipment specified Item list
     * @return string of item name, returns empty string if ITEMSLOT is empty
     */
    public static String getItemNameInGivenSetSlot(Client client, Item[] playerEquipment, EquipmentInventorySlot ITEMSLOT)
    {
        String itemName = "";

        if(playerEquipment != null){
            if(playerEquipment.length > ITEMSLOT.getSlotIdx()
                    && playerEquipment[ITEMSLOT.getSlotIdx()] != null)
            {
                itemName = client.getItemDefinition(playerEquipment[ITEMSLOT.getSlotIdx()].getId()).getName();
            }
        }

        return itemName;
    }

    /**
     * Get Item ID in Specific Slot with a specified equipment list <br>
     *
     * @param playerEquipment specified Item list
     * @param ITEMSLOT the item slot id that will be checked
     *
     * @return int of item id, returns -1 if empty
     */
    public static int getItemIdInGivenSetSlot(Item[] playerEquipment, EquipmentInventorySlot ITEMSLOT)
    {
        int itemID = -1;

        if(playerEquipment != null){
            if(playerEquipment.length > ITEMSLOT.getSlotIdx()
                    && playerEquipment[ITEMSLOT.getSlotIdx()] != null)
            {
                itemID = playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId();
            }
        }

        return itemID;
    }
}
