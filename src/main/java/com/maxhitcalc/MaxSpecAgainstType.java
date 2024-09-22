/* MaxSpecAgainstType.java
 * Calculates Max hit for special attacks vs enemy types.
 *
 *
 * Copyright (c) 2024, Jacob Burton <https://github.com/j-cob44>
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
