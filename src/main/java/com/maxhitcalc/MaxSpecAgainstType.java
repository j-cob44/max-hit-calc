package com.maxhitcalc;

import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import java.util.List;

public class MaxSpecAgainstType extends MaxAgainstType
{
    // Calculate Max Spec Hit Against Type
    public static double calculate(Client client, ItemManager itemManager, MaxHitCalcConfig config){
        // Get Current Equipment
        Item[] playerEquipment;
        if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
        {
            playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
        }
        else {
            return 0;
        }

        String weaponName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName();

        int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
        int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

        // Get Current Attack Style
        WeaponType weaponType = WeaponType.getWeaponType(weaponTypeID);
        AttackStyle[] weaponAttackStyles = weaponType.getAttackStyles();

        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];

        // Get Type modifier
        List<Double> typeModifiersList = getTypeBonus(client, attackStyle, weaponName, playerEquipment);

        if(!typeModifiersList.isEmpty())
        {
            // Get Max hit then calculate Spec
            double maxSpec = MaxSpec.calculate(client, itemManager, config);

            if(maxSpec != 0)
            {
                double maxSpecVsTypeHit = Math.floor(maxSpec);
                // Iterate through modifiers, flooring after multiplying
                for (double modifier: typeModifiersList)
                {
                    maxSpecVsTypeHit = Math.floor(maxSpecVsTypeHit * modifier);
                }

                return maxSpecVsTypeHit;
            }
        }

        return 0; // No Type Bonus
    }
}
