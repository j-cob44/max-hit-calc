package com.maxhitcalc;

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;

public class MaxAgainstType {
    public static double getTypeBonus(Client client, AttackStyle attackStyle, String weaponName, Item[] playerEquipment){
        double typeStatModifier = 1;

        // Melee Checks
        if(attackStyle == AttackStyle.AGGRESSIVE || attackStyle == AttackStyle.CONTROLLED || attackStyle == AttackStyle.ACCURATE || attackStyle == AttackStyle.DEFENSIVE){
            // Vampires
            if(weaponName.contains("Blisterwood flail")){
                typeStatModifier += 0.25;
            }
            if(weaponName.contains("Ivandis flail")){
                typeStatModifier += 0.2;
            }

            // Kalphites, scarabs
            if(weaponName.contains("Keris")){
                typeStatModifier += 0.33;
            }

            // Wilderness
            if(weaponName.contains("Viggora's")){
                typeStatModifier += 0.5;
            }

            // Slayer helm, does not stack with undead, take best
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet (e)")) {
                typeStatModifier += 0.2;
            }
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(ei)")) {
                typeStatModifier += 0.2;
            }
            // Black mask, does not stack with undead, take best
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Black mask")) {
                typeStatModifier += 0.1667;
            }
            // Slayer helm, does not stack with undead, take best
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Slayer helmet")) {
                typeStatModifier += 0.1667;
            }
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("slayer helmet")) {
                typeStatModifier += 0.1667;
            }
            // undead, does not stack with slayer, take best
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet")){
                typeStatModifier += 0.15;
            }

            // Inquisitor's armor
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Inquisitor's")) {
                if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Inquisitor's")) {
                    if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Inquisitor's")) {
                        typeStatModifier += 0.025;
                    }
                }
            }
        }
        // Ranged Checks
        else if (attackStyle == AttackStyle.RANGING || attackStyle == AttackStyle.LONGRANGE) {
            // Undead, does not stack with slayer
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(ei)")) {
                typeStatModifier += 0.2;
            }
            // Slayer, does not stack with undead
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Black mask")) {
                if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("(i)")) {
                    typeStatModifier += 0.1667;
                }
            }
            // Slayer, does not stack with undead
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Slayer helmet (i)")) {
                typeStatModifier += 0.1667;
            }
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("slayer helmet (i)")) {
                typeStatModifier += 0.1667;
            }

            // Undead, does not stack with slayer
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(i)")){
                typeStatModifier += 0.15;
            }
        }
        // Magic Checks
        else if (attackStyle == AttackStyle.CASTING || attackStyle == AttackStyle.DEFENSIVE_CASTING) {
            // Undead, does not stack with slayer
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(ei)")) {
                typeStatModifier += 0.2;
            }
            // Slayer, does not stack with undead
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Black mask")) {
                if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("(i)")) {
                    typeStatModifier += 0.1667;
                }
            }
            // Slayer, does not stack with undead
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Slayer helm (i)")) {
                typeStatModifier += 0.1667;
            }
            // Undead, does not stack with slayer
            else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Salve amulet(i)")){
                typeStatModifier += 0.15;
            }

            // Fire Spells
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()].getId()).getName().contains("Tome of fire")) {
                typeStatModifier += 0.5;
            }
        }

        return typeStatModifier;
    }
}
