/* MaxAgainstType.java
 * Contains the function for calculating the max hit against specific type.
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

import net.runelite.api.ChatMessageType; // for ingame debug
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;

public class MaxAgainstType {
    public static double getTypeBonus(Client client, AttackStyle attackStyle, String weaponName, Item[] playerEquipment)
    {
        double typeStatModifier = 1;

        // Melee Checks
        if(attackStyle == AttackStyle.AGGRESSIVE || attackStyle == AttackStyle.CONTROLLED || attackStyle == AttackStyle.ACCURATE || attackStyle == AttackStyle.DEFENSIVE)
        {
            // Vampires
            if(weaponName.contains("Blisterwood flail"))
            {
                typeStatModifier *= 1.25;
            }

            if(weaponName.contains("Ivandis flail"))
            {
                typeStatModifier *= 1.2;
            }

            // Kalphites, scarabs
            if(weaponName.contains("Keris"))
            {
                typeStatModifier *= 1.33;
            }

            // Wilderness
            if(weaponName.contains("Viggora's"))
            {
                typeStatModifier *= 1.5;
            }

            // Inquisitor's armor
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Inquisitor's"))
            {
                if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Inquisitor's"))
                {
                    if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Inquisitor's"))
                    {
                        typeStatModifier *= 1.025;
                    }
                }
            }

            // Slayer helm, does not stack with undead, take best
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet (e)"))
            {
                typeStatModifier *= 1.2;
            }
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(ei)"))
            {
                typeStatModifier *= 1.2;
            }
            // Black mask, does not stack with undead, take best
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Black mask"))
            {
                typeStatModifier *= 1.1667;
            }
            // Slayer helm, does not stack with undead, take best
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Slayer helmet"))
            {
                typeStatModifier *= 1.1667;
            }
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("slayer helmet"))
            {
                typeStatModifier *= 1.1667;
            }
            // undead, does not stack with slayer, take best
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet"))
            {
                typeStatModifier *= 1.15;
            }


        }
        // Ranged Checks
        else if (attackStyle == AttackStyle.RANGING || attackStyle == AttackStyle.LONGRANGE)
        {
            // Undead, does not stack with slayer
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(ei)"))
            {
                typeStatModifier *= 1.2;
            }
            // Undead, does not stack with slayer
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(i)"))
            {
                typeStatModifier *= 1.166666667;
            }
            // Slayer, does not stack with undead
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Black mask"))
            {
                if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("(i)"))
                {
                    typeStatModifier *= 1.15;
                }
            }
            // Slayer, does not stack with undead
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Slayer helmet (i)"))
            {
                typeStatModifier *= 1.15;
            }
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("slayer helmet (i)"))
            {
                typeStatModifier *= 1.15;
            }


        }
        // Magic Checks
        else if (attackStyle == AttackStyle.CASTING || attackStyle == AttackStyle.DEFENSIVE_CASTING)
        {
            // Undead, does not stack with slayer
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(ei)"))
            {
                typeStatModifier *= 1.2;
            }
            // Slayer, does not stack with undead
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Black mask"))
            {
                if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("(i)"))
                {
                    typeStatModifier *= 1.15;
                }
            }
            // Slayer, does not stack with undead
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Slayer helm (i)"))
            {
                typeStatModifier *= 1.15;
            }
            // Undead, does not stack with slayer
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(i)"))
            {
                typeStatModifier *= 1.15;
            }

            // Fire Spells
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()].getId()).getName().contains("Tome of fire"))
            {
                typeStatModifier *= 1.5;
            }
        }

        return typeStatModifier;
    }
}
