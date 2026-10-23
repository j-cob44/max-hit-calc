/* MaxSpecAgainstTypeTest.java
 * Covers the special attack max hit against a weak NPC.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * At 99 Strength with no strength bonus the melee max is 11, so a dragon warhammer spec is
 * floor(11) * 1.5 = 16 once floored.
 */
class MaxSpecAgainstTypeTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;
    private static final double SPEC_MAX = 16;

    private MaxSpecAgainstType maxSpecAgainstType()
    {
        return new MaxSpecAgainstType(plugin, config, itemManager, client);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Special_attack",
        "https://oldschool.runescape.wiki/w/Slayer_helmet"
    })
    @Test
    @DisplayName("A type bonus scales the spec max hit")
    void typeBonusScalesTheSpecMax()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        equipment().weapon("Dragon warhammer").head("Slayer helmet").wear();

        // floor(16 * 1.1667)
        assertEquals(18, maxSpecAgainstType().calculate(), DELTA);
    }

    @DataSource.Derived("no bonus and no armour, so there is nothing to show")
    @Test
    @DisplayName("No type bonus and no flat armour means nothing to show")
    void nothingToShowWithoutABonus()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        equipment().weapon("Dragon warhammer").wear();

        assertEquals(0, maxSpecAgainstType().calculate(), DELTA);
    }

    @DataSource.Derived("an armour value covered in FlatArmourTest")
    @Test
    @DisplayName("Flat armour alone is enough to produce a value")
    void flatArmourAloneProducesAValue()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        selectedNPC("Veiled kraken"); // 15 flat armour, no elemental weakness
        equipment().weapon("Dragon warhammer").wear();

        // the armour itself is taken off by the plugin, this is the pre armour spec max
        assertEquals(SPEC_MAX, maxSpecAgainstType().calculate(), DELTA);
    }

    @DataSource.Derived("a weapon with no spec, covered in SpecWeaponStatTest")
    @Test
    @DisplayName("A weapon with no spec has no spec max against a type")
    void weaponWithoutASpecHasNothing()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        equipment().weapon("Abyssal whip").head("Slayer helmet").wear();

        assertEquals(0, maxSpecAgainstType().calculate(), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Special_attack",
        "https://oldschool.runescape.wiki/w/Slayer_helmet",
        "https://oldschool.runescape.wiki/w/Salve_amulet"
    })
    @Test
    @DisplayName("Several type bonuses are applied in turn")
    void severalTypeBonusesApplyInTurn()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        // arclight has no damage spec, so pair the slayer helmet with a weapon that does
        equipment().weapon("Dragon warhammer").head("Slayer helmet").amulet("Berserker necklace").wear();

        // the necklace only matters for obsidian weapons, so the result is unchanged
        assertEquals(18, maxSpecAgainstType().calculate(), DELTA);
    }

    @DataSource.Derived("nothing equipped")
    @Test
    @DisplayName("No equipment gives nothing")
    void noEquipmentGivesNothing()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        equipment().wear();

        assertEquals(0, maxSpecAgainstType().calculate(), DELTA);
    }
}
