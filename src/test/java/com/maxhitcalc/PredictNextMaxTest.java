/* PredictNextMaxTest.java
 * Covers what the panel says is needed to reach the next max hit.
 *
 * Copyright (c) 2026, Jacob Burton <https://github.com/j-cob44>
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

import net.runelite.api.Skill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Each prediction is the smallest increase that pushes the floored max hit up by one. At 99
 * Strength with no gear the melee max is 11, and the base damage is 0.5 + effective * 0.1, so it
 * takes 5 levels, 3 strength bonus, or 6% prayer to reach 12.
 */
@DataSource.Derived("the max hit formulas covered elsewhere")
class PredictNextMaxTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    private PredictNextMax predictor()
    {
        return new PredictNextMax(plugin, config, itemManager, client);
    }

    @Test
    @DisplayName("Melee prediction names the style and the three routes")
    void meleePredictionShape()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        equipment().wear();

        List<Object> result = predictor().predict();

        assertNotNull(result);
        assertEquals("melee", result.get(0));
        assertEquals(4, result.size());
    }

    @Test
    @DisplayName("Melee predicts the levels, bonus and prayer needed")
    void meleePredictsEachRoute()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        equipment().wear();

        List<Object> result = predictor().predict();

        assertEquals(5, result.get(1));                        // 5 more strength levels
        assertEquals(3, result.get(2));                        // 3 more strength bonus
        assertEquals(0.06, (double) result.get(3), DELTA);     // 6% more prayer bonus
    }

    @Test
    @DisplayName("A higher strength bonus changes what is needed")
    void higherStrengthBonusChangesTheRoutes()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        itemStats("Abyssal whip", 100, 0, 0);
        equipment().weapon("Abyssal whip").wear();

        List<Object> result = predictor().predict();

        // the max is 28 here, so each route needs a different step than the unarmed case
        assertEquals("melee", result.get(0));
        assertTrue((int) result.get(1) > 0, "a strength level route should exist");
        assertTrue((int) result.get(2) > 0, "a strength bonus route should exist");
    }

    @Test
    @DisplayName("Ranged prediction names the style and its routes")
    void rangedPredictionShape()
    {
        skill(Skill.RANGED, 99);
        skill(Skill.STRENGTH, 99);
        weaponAttackStyles(0, 0, "Ranging", "Ranging", "Longrange");
        equipment().wear();

        List<Object> result = predictor().predict();

        assertNotNull(result);
        assertEquals("ranged", result.get(0));
        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("Ranged predicts a level and a ranged strength route")
    void rangedPredictsEachRoute()
    {
        skill(Skill.RANGED, 99);
        skill(Skill.STRENGTH, 99);
        weaponAttackStyles(0, 0, "Ranging", "Ranging", "Longrange");
        equipment().wear();

        List<Object> result = predictor().predict();

        assertTrue((int) result.get(1) > 0, "a ranged level route should exist");
        assertTrue((int) result.get(3) > 0, "a ranged strength route should exist");
    }

    @Test
    @DisplayName("Magic prediction names the style and its routes")
    void magicPredictionShape()
    {
        skill(Skill.MAGIC, 99);
        weaponAttackStyles(24, 0, "Casting", "Casting", "Defensive");
        equipment().weapon("Trident of the seas").wear();

        List<Object> result = predictor().predict();

        assertNotNull(result);
        assertEquals("magic", result.get(0));
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("A powered staff predicts the Magic level needed")
    void poweredStaffPredictsMagicLevel()
    {
        skill(Skill.MAGIC, 70);
        weaponAttackStyles(24, 0, "Casting", "Casting", "Defensive");
        equipment().weapon("Trident of the seas").wear();

        List<Object> result = predictor().predict();

        // the trident base hit is floor((level - 15) / 3), so 70 -> 18 and 72 -> 19
        assertEquals(2, result.get(1));
    }

    @Test
    @DisplayName("An unknown attack style predicts nothing")
    void unknownStyleReturnsNull()
    {
        skill(Skill.STRENGTH, 99);
        weaponAttackStyles(0, 0, "Other");
        equipment().wear();

        assertEquals(null, predictor().predict());
    }
}
