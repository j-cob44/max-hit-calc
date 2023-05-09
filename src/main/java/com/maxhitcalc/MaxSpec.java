package com.maxhitcalc;

import net.runelite.api.*;

public class MaxSpec {
    public static double getSpecWeaponStat(Client client, String weaponName, Item[] playerEquipment){
        // Check if we even have a spec weapon

        // Melee Checks
        if(weaponName.contains("Armadyl godsword")){
            return 1.375;
        }
        if(weaponName.contains("Bandos godsword")){
            return 1.21;
        }
        if(weaponName.contains("Saradomin godsword")){
            return 1.1;
        }
        if(weaponName.contains("Zamorak godsword")){
            return 1.1;
        }
        if(weaponName.contains("Dragon dagger")){
            return 1.15;
        }
        if(weaponName.contains("Dragon halberd")){
            return 1.1;
        }
        if(weaponName.contains("Crystal halberd")){
            return 1.1;
        }
        if(weaponName.contains("Dragon hasta")){
            return 1.0 + (0.5 * (client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT)/1000));
        }
        if(weaponName.contains("Dragon longsword")){
            return 1.25;
        }
        if(weaponName.contains("Dragon mace")){
            return 1.5;
        }
        if(weaponName.contains("Dragon warhammer")){
            return 1.5;
        }
        if(weaponName.contains("Rune claws")){
            return 1.1;
        }
        if(weaponName.contains("Abyssal dagger")){
            return 0.85;
        }
        if(weaponName.contains("Abyssal bludgeon")){
            double currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
            double totalPrayer = client.getRealSkillLevel(Skill.PRAYER);
            return (1 +(0.005 * (totalPrayer - currentPrayer)));
        }
        if(weaponName.contains("Saradomin's blessed sword")){
            return 1.25;
        }
        // Ranged Checks
        if(weaponName.contains("Dark bow")){
            String ammoName = "";
            if(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()] != null){
                ammoName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId()).getName();
            }

            if(ammoName.contains("Dragon arrow")){
                return 1.5;
            }
            else if (ammoName.contains("arrow")){
                return 1.3;
            }
        }
        if(weaponName.contains("Zaryte crossbow")){
            String ammoName = "";
            if(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()] != null){
                ammoName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId()).getName();
            }

            if(ammoName.contains("bolts (e)")){
                return 1.1;
            }
        }
        if(weaponName.contains("crossbow")){
            String ammoName = "";
            if(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()] != null){
                ammoName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId()).getName();
            }

            if(ammoName.contains("Diamond bolts (e)")){
                return 1.15;
            }
            if(ammoName.contains("Dragonstone bolts (e)")){
                return 1.45;
            }
            if(ammoName.contains("Onyx bolts (e)")){
                return 1.15;
            }
            if(ammoName.contains("Opal bolts (e)")){
                return 1.25;
            }
        }

        return 0; // Not a spec weapon
    }
}
