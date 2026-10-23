/* SetBonusTest.java
 * Covers the armour set bonuses that are matched on item names.
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Void, Inquisitor's and crystal armour all key off item names, so each needs its own set check.
 */
class SetBonusTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    private Equipment voidSet(String helm, String top, String robe, String gloves)
    {
        return equipment().head(helm).body(top).legs(robe).gloves(gloves);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Void_knight_equipment")
    @Test
    @DisplayName("Void melee set gives 10%")
    void voidMeleeSet()
    {
        Equipment normal = voidSet("Void melee helm", "Void knight top", "Void knight robe", "Void knight gloves");
        assertEquals(1.1, maxHit().getVoidMeleeBonus(normal.build()), DELTA);

        // Elite adds nothing for melee
        Equipment elite = voidSet("Void melee helm", "Elite void top", "Elite void robe", "Void knight gloves");
        assertEquals(1.1, maxHit().getVoidMeleeBonus(elite.build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Void_knight_equipment")
    @Test
    @DisplayName("Void ranged set gives 10%, elite gives 12.5%")
    void voidRangedSet()
    {
        Equipment normal = voidSet("Void ranger helm", "Void knight top", "Void knight robe", "Void knight gloves");
        assertEquals(1.1, maxHit().getVoidRangedBonus(normal.build()), DELTA);

        Equipment elite = voidSet("Void ranger helm", "Elite void top", "Elite void robe", "Void knight gloves");
        assertEquals(1.125, maxHit().getVoidRangedBonus(elite.build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Void_knight_equipment")
    @Test
    @DisplayName("Elite void magic set gives 5% magic damage")
    void voidMagicSet()
    {
        Equipment elite = voidSet("Void mage helm", "Elite void top", "Elite void robe", "Void knight gloves");
        assertEquals(0.05, maxHit().getVoidMagicBonus(elite.build()), DELTA);

        // Magic only benefits from the elite pieces
        Equipment normal = voidSet("Void mage helm", "Void knight top", "Void knight robe", "Void knight gloves");
        assertEquals(0, maxHit().getVoidMagicBonus(normal.build()), DELTA);
    }

    static Stream<Arguments> incompleteVoidSets()
    {
        return Stream.of(
            // helm, top, robe, gloves
            Arguments.of("Void ranger helm", "Void knight top", "Void knight robe", "Barrows gloves"),
            Arguments.of("Void ranger helm", "Void knight top", "Bandos tassets", "Void knight gloves"),
            Arguments.of("Void ranger helm", "Bandos chestplate", "Void knight robe", "Void knight gloves"),
            Arguments.of("Void melee helm", "Void knight top", "Void knight robe", "Void knight gloves")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Void_knight_equipment")
    @DisplayName("An incomplete void ranged set gives nothing")
    @ParameterizedTest(name = "{0}, {1}, {2}, {3}")
    @MethodSource("incompleteVoidSets")
    void incompleteVoidRangedSet(String helm, String top, String robe, String gloves)
    {
        assertEquals(1, maxHit().getVoidRangedBonus(voidSet(helm, top, robe, gloves).build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Void_knight_equipment")
    @Test
    @DisplayName("Mixing elite and normal void pieces gives the normal bonus")
    void mixedVoidPiecesGiveTheNormalBonus()
    {
        Equipment mixed = voidSet("Void ranger helm", "Elite void top", "Void knight robe", "Void knight gloves");

        assertEquals(1.1, maxHit().getVoidRangedBonus(mixed.build()), DELTA);
    }

    @DataSource.Derived("nothing equipped")
    @Test
    @DisplayName("No equipment gives no void bonus")
    void noEquipmentGivesNoVoidBonus()
    {
        assertEquals(1, maxHit().getVoidMeleeBonus(null), DELTA);
        assertEquals(0, maxHit().getVoidMagicBonus(null), DELTA);
        assertEquals(1, maxHit().getVoidMeleeBonus(equipment().build()), DELTA);
    }

    static Stream<Arguments> crystalArmour()
    {
        return Stream.of(
            // head, body, legs, expected multiplier
            Arguments.of("Crystal helm", "Crystal body", "Crystal legs", 1.15),
            Arguments.of("Crystal helm", null, null, 1.025),
            Arguments.of(null, "Crystal body", null, 1.075),
            Arguments.of(null, null, "Crystal legs", 1.05),
            Arguments.of(null, null, null, 1.0)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Crystal_armour")
    @DisplayName("Crystal armour boosts a crystal bow")
    @ParameterizedTest(name = "{0}, {1}, {2} -> {3}x")
    @MethodSource("crystalArmour")
    void crystalArmourBoost(String head, String body, String legs, double expected)
    {
        Equipment worn = equipment().weapon("Crystal bow");
        if (head != null) worn.head(head);
        if (body != null) worn.body(body);
        if (legs != null) worn.legs(legs);

        assertEquals(expected, maxHit().getRangeGearBoost(worn.build()), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Crystal_armour",
        "https://oldschool.runescape.wiki/w/Bow_of_faerdhinen"
    })
    @Test
    @DisplayName("Bow of faerdhinen gets the same crystal armour boost")
    void bowOfFaerdhinenGetsTheBoost()
    {
        Equipment worn = equipment()
            .weapon("Bow of faerdhinen")
            .head("Crystal helm")
            .body("Crystal body")
            .legs("Crystal legs");

        assertEquals(1.15, maxHit().getRangeGearBoost(worn.build()), DELTA);
    }

    static Stream<Arguments> gauntletCrystalVariants()
    {
        return Stream.of(
            Arguments.of("(basic)"),
            Arguments.of("(attuned)"),
            Arguments.of("(perfected)")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Crystal_armour")
    @DisplayName("Gauntlet crystal armour variants give no boost")
    @ParameterizedTest(name = "{0}")
    @MethodSource("gauntletCrystalVariants")
    void gauntletCrystalVariantsGiveNoBoost(String variant)
    {
        Equipment worn = equipment()
            .weapon("Crystal bow")
            .head("Crystal helm " + variant)
            .body("Crystal body " + variant)
            .legs("Crystal legs " + variant);

        assertEquals(1, maxHit().getRangeGearBoost(worn.build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Crystal_armour")
    @Test
    @DisplayName("Crystal armour does nothing without a crystal bow")
    void crystalArmourNeedsACrystalBow()
    {
        Equipment worn = equipment()
            .weapon("Twisted bow")
            .head("Crystal helm")
            .body("Crystal body")
            .legs("Crystal legs");

        assertEquals(1, maxHit().getRangeGearBoost(worn.build()), DELTA);
    }

    static Stream<Arguments> inquisitorPieces()
    {
        return Stream.of(
            // head, body, legs, expected multiplier
            Arguments.of("Inquisitor's great helm", "Inquisitor's hauberk", "Inquisitor's plateskirt", 1.025),
            Arguments.of("Inquisitor's great helm", null, null, 1.005),
            Arguments.of(null, "Inquisitor's hauberk", null, 1.01),
            Arguments.of(null, null, "Inquisitor's plateskirt", 1.01),
            Arguments.of(null, null, null, 1.0)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Inquisitor's_armour")
    @DisplayName("Inquisitor's armour boosts crush attacks")
    @ParameterizedTest(name = "{0}, {1}, {2} -> {3}x")
    @MethodSource("inquisitorPieces")
    void inquisitorBoostsCrush(String head, String body, String legs, double expected)
    {
        crushAttackStyle(0);

        Equipment worn = equipment().weapon("Inquisitor's mace");
        if (head != null) worn.head(head);
        if (body != null) worn.body(body);
        if (legs != null) worn.legs(legs);

        assertEquals(expected, maxHit().getInquisitorDamageBonus(worn.build(), AttackStyle.AGGRESSIVE), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Inquisitor's_armour")
    @Test
    @DisplayName("Inquisitor's armour does nothing outside crush")
    void inquisitorNeedsCrush()
    {
        varp(net.runelite.api.gameval.VarPlayerID.COM_MODE, 0); // no crush widget stubbed

        Equipment worn = equipment()
            .weapon("Inquisitor's mace")
            .head("Inquisitor's great helm")
            .body("Inquisitor's hauberk")
            .legs("Inquisitor's plateskirt");

        assertEquals(1, maxHit().getInquisitorDamageBonus(worn.build(), AttackStyle.AGGRESSIVE), DELTA);
    }

    @DataSource.Derived("nothing equipped")
    @Test
    @DisplayName("No equipment gives no Inquisitor bonus")
    void noEquipmentGivesNoInquisitorBonus()
    {
        crushAttackStyle(0);

        assertEquals(1, maxHit().getInquisitorDamageBonus(null, AttackStyle.AGGRESSIVE), DELTA);
    }
}
