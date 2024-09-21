package com.maxhitcalc;

import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import java.util.List;

/**
 * Contains functions for calculating max hit of a special attack weapon vs specific types of npc.
 */
public class MaxSpecAgainstType extends MaxAgainstType
{
    private MaxSpec maxSpecs;

    MaxSpecAgainstType(MaxHitCalcPlugin plugin, MaxHitCalcConfig config, ItemManager itemManager, Client client)
    {
        super(plugin, config, itemManager, client);

        maxSpecs = new MaxSpec(plugin, config, itemManager, client);
    }

    /**
     * Caculates Max hit of a special attack against specific type bonuses
     *
     * @return Max hit as Double
     */
    public double calculate(){
        // Get Current Equipment
        Item[] playerEquipment = EquipmentItems.getCurrentlyEquipped(client);

        if (playerEquipment == null) return 0;

        int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
        int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

        // Get Current Attack Style
        AttackStyle[] weaponAttackStyles = WeaponType.getWeaponTypeStyles(client, weaponTypeID);
        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];
        if (attackStyle == null)
            return 0;

        // Get Type modifier
        List<Double> typeModifiersList = getTypeBonus(attackStyle, playerEquipment);

        if(!typeModifiersList.isEmpty())
        {
            // Get Max hit then calculate Spec
            double maxSpec = maxSpecs.calculate();

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
