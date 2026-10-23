/* MaxHitCalculationTest.java
 * Covers the melee and ranged max hit calculations end to end.
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
import net.runelite.api.gameval.VarbitID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The flat adjustments at the end of each calculation only show up here, so these cover the whole
 * path rather than a single helper.
 * <p>
 * Both styles happen to land on 11 at 99 with nothing contributing strength, which most of the
 * expected values below build on. For ranged that means a bow with no arrows, since bows carry no
 * ranged strength of their own.
 */
class MaxHitCalculationTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    private static final double NO_STRENGTH_MELEE_MAX = 11;
    private static final double NO_STRENGTH_RANGED_MAX = 11;

    /** Attack style id 0, which is what the ranged style bonus looks for */
    private static final int FIRST_STYLE = 0;

    private double meleeMax(Equipment worn)
    {
        return maxHit().calculateMeleeMaxHit(worn.build(), AttackStyle.AGGRESSIVE, FIRST_STYLE, false);
    }

    private double rangedMax(Equipment worn)
    {
        return maxHit().calculateRangedMaxHit(worn.build(), AttackStyle.RANGING, FIRST_STYLE);
    }

    // The max the panel shows, floored the way calculateMaxes does
    private int rangedMaxShown(Equipment worn)
    {
        return (int) Math.floor(rangedMax(worn));
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_melee_hit")
    @Test
    @DisplayName("Melee max hit unarmed")
    void meleeMaxHitWithNoEquipment()
    {
        skill(Skill.STRENGTH, 99);

        assertEquals(NO_STRENGTH_MELEE_MAX, meleeMax(equipment()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_melee_hit")
    @Test
    @DisplayName("Melee max hit scales with strength bonus")
    void meleeMaxHitScalesWithStrengthBonus()
    {
        skill(Skill.STRENGTH, 99);
        itemStats("Abyssal whip", 100, 0, 0);

        // floor(0.5 + 110 * (100 + 64) / 640)
        assertEquals(28, meleeMax(equipment().weapon("Abyssal whip")), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Maximum_melee_hit",
        "https://oldschool.runescape.wiki/w/Piety"
    })
    @Test
    @DisplayName("Melee max hit scales with a prayer")
    void meleeMaxHitScalesWithPrayer()
    {
        skill(Skill.STRENGTH, 99);
        varbit(VarbitID.PRAYER_PIETY, 1);

        // floor(99 * 1.23) = 121, so effective strength is 132
        assertEquals(13, meleeMax(equipment()), DELTA);
    }

    static Stream<Arguments> meleeFlatAdjustments()
    {
        return Stream.of(
            // weapon, resulting max hit on top of the unarmed 11
            Arguments.of("Colossal blade", 13.0),            // +2
            Arguments.of("Bone mace", 21.0),                 // +10 against rats
            Arguments.of("Osmumten's fang", 10.0)            // 11 - floor(11 * 0.15)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Colossal_blade",
        "https://oldschool.runescape.wiki/w/Bone_mace",
        "https://oldschool.runescape.wiki/w/Osmumten's_fang"
    })
    @DisplayName("Melee flat adjustments")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("meleeFlatAdjustments")
    void meleeFlatAdjustments(String weaponName, double expected)
    {
        skill(Skill.STRENGTH, 99);

        assertEquals(expected, meleeMax(equipment().weapon(weaponName)), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Maximum_ranged_hit",
        "https://oldschool.runescape.wiki/w/Magic_shortbow"
    })
    @Test
    @DisplayName("Ranged max hit with a bow and no arrows")
    void rangedMaxHitWithNoRangedStrength()
    {
        skill(Skill.RANGED, 99);
        itemStats("Magic shortbow", 0, 0, 0); // bows carry no ranged strength themselves

        assertEquals(NO_STRENGTH_RANGED_MAX, rangedMax(equipment().weapon("Magic shortbow")), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_ranged_hit")
    @Test
    @DisplayName("Ranged max hit scales with ranged strength")
    void rangedMaxHitScalesWithRangedStrength()
    {
        skill(Skill.RANGED, 99);
        itemStats("Magic shortbow", 0, 0, 0);
        itemStats("Rune arrow", 0, 49, 0);

        // floor(0.5 + 110 * (49 + 64) / 640)
        assertEquals(19, rangedMax(equipment().weapon("Magic shortbow").ammo("Rune arrow")), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Bone_shortbow")
    @Test
    @DisplayName("Bone shortbow adds 10 against rats")
    void boneShortbowAddsTen()
    {
        skill(Skill.RANGED, 99);
        itemStats("Bone shortbow", 0, 0, 0);

        assertEquals(NO_STRENGTH_RANGED_MAX + 10, rangedMax(equipment().weapon("Bone shortbow")), DELTA);
    }

    static Stream<Arguments> tonalzticsCharges()
    {
        return Stream.of(
            // floor(11 * 0.75) = 8, doubled only while charged
            Arguments.of("Tonalztics of ralos", 16.0),
            Arguments.of("Tonalztics of ralos (uncharged)", 8.0)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Tonalztics_of_ralos")
    @DisplayName("Tonalztics of ralos throws twice only while charged")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("tonalzticsCharges")
    void tonalzticsCharges(String weaponName, double expected)
    {
        skill(Skill.RANGED, 99);
        itemStats(weaponName, 0, 0, 0);

        assertEquals(expected, rangedMax(equipment().weapon(weaponName)), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Maximum_melee_hit",
        "https://oldschool.runescape.wiki/w/Obsidian_armour",
        "https://oldschool.runescape.wiki/w/Berserker_necklace"
    })
    @Test
    @DisplayName("Obsidian set and berserker necklace apply as a single 1.3")
    void obsidianAndBerserkerApplyOnce()
    {
        skill(Skill.STRENGTH, 99);
        itemStats("Toktz-xil-ak", 225, 0, 0);

        Equipment worn = equipment()
                .weapon("Toktz-xil-ak")
                .head("Obsidian helmet")
                .body("Obsidian platebody")
                .legs("Obsidian platelegs")
                .amulet("Berserker necklace");

        // base is 50, so the pair gives 50 + floor(50 * 0.3) = 65
        // applying them one after the other would give 55 then 66
        assertEquals(65, meleeMax(worn), DELTA);
    }

    static Stream<Arguments> crystalArmourPieces()
    {
        return Stream.of(
            // head, body, legs, max hit shown at 85 ranged strength, where the base hit is 26
            Arguments.of(null, null, null, 26),                                  // no bonus
            Arguments.of("Crystal helm", null, null, 26),                        // floor(26 * 1.025)
            Arguments.of(null, "Crystal body", null, 27),                        // floor(26 * 1.075)
            Arguments.of(null, null, "Crystal legs", 27),                        // floor(26 * 1.05)
            Arguments.of("Crystal helm", "Crystal body", "Crystal legs", 29)     // floor(26 * 1.15)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Crystal_armour",
        "https://oldschool.runescape.wiki/w/Bow_of_faerdhinen"
    })
    @DisplayName("Bow of faerdhinen with crystal armour")
    @ParameterizedTest(name = "{0}, {1}, {2} -> {3}")
    @MethodSource("crystalArmourPieces")
    void bowOfFaerdhinenWithCrystalArmour(String head, String body, String legs, int expected)
    {
        skill(Skill.RANGED, 99);
        itemStats("Bow of faerdhinen (c)", 0, 85, 0);

        Equipment worn = equipment().weapon("Bow of faerdhinen (c)");
        if (head != null) worn.head(head);
        if (body != null) worn.body(body);
        if (legs != null) worn.legs(legs);

        assertEquals(expected, rangedMaxShown(worn));
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Crystal_helm",
        "https://oldschool.runescape.wiki/w/Crystal_armour"
    })
    @Test
    @DisplayName("An inactive crystal piece gives no bonus")
    void inactiveCrystalPieceGivesNoBonus()
    {
        skill(Skill.RANGED, 99);
        itemStats("Bow of faerdhinen (c)", 0, 168, 0);

        Equipment worn = equipment()
                .weapon("Bow of faerdhinen (c)")
                .head("Crystal helm (inactive)")
                .body("Crystal body")
                .legs("Crystal legs");

        // base is 40, and an inactive helm drops the set to 1.125, so floor(40 * 1.125) = 45
        assertEquals(45, rangedMaxShown(worn));
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Maximum_ranged_hit",
        "https://oldschool.runescape.wiki/w/Crystal_armour"
    })
    @Test
    @DisplayName("The crystal bonus applies to the floored base hit")
    void crystalBonusAppliesToTheFlooredBase()
    {
        skill(Skill.RANGED, 99);
        itemStats("Crystal bow", 0, 80, 0);

        Equipment worn = equipment()
                .weapon("Crystal bow")
                .head("Crystal helm")
                .body("Crystal body")
                .legs("Crystal legs");

        // base is floor(0.5 + 110 * 144 / 640) = 25, so the set shows floor(25 * 1.15) = 28
        // scaling the 0.5 too would give 25.25 * 1.15 = 29.04 and show 29
        assertEquals(28, rangedMaxShown(worn));
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Eclipse_atlatl")
    @Test
    @DisplayName("Eclipse atlatl calculates from Strength, not Ranged")
    void atlatlCalculatesFromStrength()
    {
        skill(Skill.RANGED, 1);
        skill(Skill.STRENGTH, 99);
        itemStats("Eclipse atlatl", 0, 0, 0);

        assertEquals(NO_STRENGTH_RANGED_MAX, rangedMax(equipment().weapon("Eclipse atlatl")), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Soulreaper_axe")
    @Test
    @DisplayName("A soulreaper axe's stacks only count outside a special attack")
    void soulStacksOnlyCountOutsideASpec()
    {
        skill(Skill.STRENGTH, 99);
        varp(net.runelite.api.gameval.VarPlayerID.SOULREAPER_STACKS, 5);

        // 5 stacks add floor(99 * 0.3) = 29 effective levels
        double normal = maxHit().calculateMeleeMaxHit(
                equipment().build(), AttackStyle.AGGRESSIVE, FIRST_STYLE, false);
        double duringSpec = maxHit().calculateMeleeMaxHit(
                equipment().build(), AttackStyle.AGGRESSIVE, FIRST_STYLE, true);

        assertEquals(14, normal, DELTA);
        assertEquals(NO_STRENGTH_MELEE_MAX, duringSpec, DELTA);
    }
}
