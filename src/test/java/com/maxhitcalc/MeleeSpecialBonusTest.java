/* MeleeSpecialBonusTest.java
 * Covers the melee set effects that depend on the names of the equipped items.
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Case table for {@link MaxHit#getMeleeSpecialBonusMultiplier}.
 * <p>
 * To cover a new set effect, add a case below.
 */
class MeleeSpecialBonusTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    static Stream<Arguments> obsidianWeapons()
    {
        return Stream.of(
            Arguments.of("Toktz-xil-ak"),      // sword
            Arguments.of("Tzhaar-ket-em"),     // maul
            Arguments.of("Tzhaar-ket-om"),     // maul
            Arguments.of("Toktz-xil-ek")       // knife
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Obsidian_armour")
    @DisplayName("A full obsidian set boosts an obsidian weapon by 10%")
    @ParameterizedTest(name = "{0}")
    @MethodSource("obsidianWeapons")
    void fullObsidianSetBoostsObsidianWeapons(String weaponName)
    {
        maxedCombatStats();

        List<Double> actual = maxHit().getMeleeSpecialBonusMultiplier(equipment()
            .head("Obsidian helmet")
            .body("Obsidian platebody")
            .legs("Obsidian platelegs")
            .weapon(weaponName)
            .build());

        assertEquals(Collections.singletonList(0.1), actual);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Berserker_necklace")
    @DisplayName("A berserker necklace boosts an obsidian weapon by 20%")
    @ParameterizedTest(name = "{0}")
    @MethodSource("obsidianWeapons")
    void berserkerNecklaceBoostsObsidianWeapons(String weaponName)
    {
        maxedCombatStats();

        List<Double> actual = maxHit().getMeleeSpecialBonusMultiplier(equipment()
            .weapon(weaponName)
            .amulet("Berserker necklace")
            .build());

        assertEquals(Collections.singletonList(0.2), actual);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Maximum_melee_hit",
        "https://oldschool.runescape.wiki/w/Obsidian_armour",
        "https://oldschool.runescape.wiki/w/Berserker_necklace"
    })
    @Test
    @DisplayName("A full obsidian set and a berserker necklace stack")
    void obsidianSetAndBerserkerNecklaceStack()
    {
        maxedCombatStats();

        List<Double> actual = maxHit().getMeleeSpecialBonusMultiplier(equipment()
            .head("Obsidian helmet")
            .body("Obsidian platebody")
            .legs("Obsidian platelegs")
            .weapon("Toktz-xil-ak")
            .amulet("Berserker necklace")
            .build());

        // One combined bonus, the game multiplies the base hit by 1.3 once
        assertEquals(Collections.singletonList(0.3), actual);
    }

    static Stream<Arguments> nonMeleeObsidianWeapons()
    {
        return Stream.of(
            Arguments.of("Toktz-xil-ul"),      // throwing rings, ranged
            Arguments.of("Toktz-mej-tal")      // staff, magic
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Obsidian_armour")
    @DisplayName("Obsidian weapons that are not melee get no bonus")
    @ParameterizedTest(name = "{0}")
    @MethodSource("nonMeleeObsidianWeapons")
    void nonMeleeObsidianWeaponsGetNoBonus(String weaponName)
    {
        maxedCombatStats();

        List<Double> actual = maxHit().getMeleeSpecialBonusMultiplier(equipment()
            .head("Obsidian helmet")
            .body("Obsidian platebody")
            .legs("Obsidian platelegs")
            .weapon(weaponName)
            .amulet("Berserker necklace")
            .build());

        assertEquals(Collections.emptyList(), actual);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Obsidian_armour")
    @Test
    @DisplayName("An incomplete obsidian set gives no bonus")
    void incompleteObsidianSetGivesNoBonus()
    {
        maxedCombatStats();

        List<Double> actual = maxHit().getMeleeSpecialBonusMultiplier(equipment()
            .head("Obsidian helmet")
            .body("Obsidian platebody")
            .weapon("Toktz-xil-ak")
            .build());

        assertEquals(Collections.emptyList(), actual);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Obsidian_armour")
    @Test
    @DisplayName("A full obsidian set gives no bonus to a non-obsidian weapon")
    void obsidianSetDoesNotBoostOtherWeapons()
    {
        maxedCombatStats();

        List<Double> actual = maxHit().getMeleeSpecialBonusMultiplier(equipment()
            .head("Obsidian helmet")
            .body("Obsidian platebody")
            .legs("Obsidian platelegs")
            .weapon("Abyssal whip")
            .build());

        assertEquals(Collections.emptyList(), actual);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Dharok's_equipment")
    @Test
    @DisplayName("Dharok's scales with missing hitpoints")
    void dharoksScalesWithMissingHitpoints()
    {
        maxedCombatStats();
        skill(Skill.HITPOINTS, 99, 1); // 98 hitpoints missing

        List<Double> actual = dharoksSet();

        assertEquals(1, actual.size());
        assertEquals((98.0 / 100) * (99.0 / 100), actual.get(0), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Dharok's_equipment")
    @Test
    @DisplayName("Dharok's at full hitpoints gives no bonus")
    void dharoksAtFullHitpointsGivesNoBonus()
    {
        maxedCombatStats();
        skill(Skill.HITPOINTS, 99, 99); // no hitpoints missing

        List<Double> actual = dharoksSet();

        assertEquals(1, actual.size());
        assertEquals(0.0, actual.get(0), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Dharok's_equipment")
    @Test
    @DisplayName("An incomplete Dharok's set gives no bonus")
    void incompleteDharoksSetGivesNoBonus()
    {
        maxedCombatStats();
        skill(Skill.HITPOINTS, 99, 1);

        List<Double> actual = maxHit().getMeleeSpecialBonusMultiplier(equipment()
            .head("Dharok's helm")
            .body("Dharok's platebody")
            .legs("Dharok's platelegs")
            .weapon("Abyssal whip") // wrong weapon breaks the set
            .build());

        assertEquals(Collections.emptyList(), actual);
    }

    @DataSource.Derived("nothing equipped")
    @Test
    @DisplayName("No equipment at all gives no bonus")
    void nullEquipmentGivesNoBonus()
    {
        maxedCombatStats();

        assertTrue(maxHit().getMeleeSpecialBonusMultiplier(null).isEmpty());
        assertTrue(maxHit().getMeleeSpecialBonusMultiplier(equipment().build()).isEmpty());
    }

    private List<Double> dharoksSet()
    {
        return maxHit().getMeleeSpecialBonusMultiplier(equipment()
            .head("Dharok's helm")
            .body("Dharok's platebody")
            .legs("Dharok's platelegs")
            .weapon("Dharok's greataxe")
            .build());
    }
}
