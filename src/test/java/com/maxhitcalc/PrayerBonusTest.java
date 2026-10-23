/* PrayerBonusTest.java
 * Covers the damage multiplier of every combat prayer.
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

import net.runelite.api.gameval.VarbitID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Zeal, Deadeye and Mystic Vigour share a slot with the prayer they upgrade, so each is told apart
 * by its own unlocked varbit.
 */
class PrayerBonusTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    static Stream<Arguments> meleePrayers()
    {
        return Stream.of(
            Arguments.of(VarbitID.PRAYER_BURSTOFSTRENGTH, 1.05),
            Arguments.of(VarbitID.PRAYER_SUPERHUMANSTRENGTH, 1.1),
            Arguments.of(VarbitID.PRAYER_ULTIMATESTRENGTH, 1.15),
            Arguments.of(VarbitID.PRAYER_CHIVALRY, 1.18),
            Arguments.of(VarbitID.PRAYER_PIETY, 1.23)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Prayer")
    @DisplayName("Melee prayer multiplier")
    @ParameterizedTest(name = "varbit {0} -> {1}x")
    @MethodSource("meleePrayers")
    void meleePrayers(int prayerVarbit, double expected)
    {
        varbit(prayerVarbit, 1);

        assertEquals(expected, maxHit().getPrayerBonus(AttackStyle.AGGRESSIVE), DELTA);
    }

    static Stream<Arguments> rangedPrayers()
    {
        return Stream.of(
            Arguments.of(VarbitID.PRAYER_SHARPEYE, 1.05),
            Arguments.of(VarbitID.PRAYER_HAWKEYE, 1.1),
            Arguments.of(VarbitID.PRAYER_EAGLEEYE, 1.15),
            Arguments.of(VarbitID.PRAYER_RIGOUR, 1.23)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Prayer")
    @DisplayName("Ranged prayer multiplier")
    @ParameterizedTest(name = "varbit {0} -> {1}x")
    @MethodSource("rangedPrayers")
    void rangedPrayers(int prayerVarbit, double expected)
    {
        varbit(prayerVarbit, 1);

        assertEquals(expected, maxHit().getPrayerBonus(AttackStyle.RANGING), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Zeal",
        "https://oldschool.runescape.wiki/w/Piety"
    })
    @Test
    @DisplayName("Zeal replaces Piety once unlocked")
    void zealReplacesPiety()
    {
        varbit(VarbitID.PRAYER_ZEAL, 1);

        varbit(VarbitID.PRAYER_ZEAL_UNLOCKED, 0);
        assertEquals(1.23, maxHit().getPrayerBonus(AttackStyle.AGGRESSIVE), DELTA);

        varbit(VarbitID.PRAYER_ZEAL_UNLOCKED, 1);
        assertEquals(1.28, maxHit().getPrayerBonus(AttackStyle.AGGRESSIVE), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Zeal",
        "https://oldschool.runescape.wiki/w/Piety"
    })
    @Test
    @DisplayName("Piety's slot reads as Zeal once unlocked")
    void pietySlotReadsAsZealOnceUnlocked()
    {
        varbit(VarbitID.PRAYER_PIETY, 1);

        varbit(VarbitID.PRAYER_ZEAL_UNLOCKED, 0);
        assertEquals(1.23, maxHit().getPrayerBonus(AttackStyle.AGGRESSIVE), DELTA);

        varbit(VarbitID.PRAYER_ZEAL_UNLOCKED, 1);
        assertEquals(1.28, maxHit().getPrayerBonus(AttackStyle.AGGRESSIVE), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Deadeye")
    @Test
    @DisplayName("Deadeye replaces Eagle Eye once unlocked")
    void deadeyeReplacesEagleEye()
    {
        varbit(VarbitID.PRAYER_DEADEYE, 1);

        varbit(VarbitID.PRAYER_DEADEYE_UNLOCKED, 0);
        assertEquals(1.15, maxHit().getPrayerBonus(AttackStyle.RANGING), DELTA);

        varbit(VarbitID.PRAYER_DEADEYE_UNLOCKED, 1);
        assertEquals(1.18, maxHit().getPrayerBonus(AttackStyle.RANGING), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Mystic_Vigour")
    @Test
    @DisplayName("Mystic Vigour replaces Mystic Might once unlocked")
    void mysticVigourReplacesMysticMight()
    {
        varbit(VarbitID.PRAYER_MYSTICMIGHT, 1);

        varbit(VarbitID.PRAYER_MYSTIC_VIGOUR_UNLOCKED, 0);
        assertEquals(1.02, maxHit().getMagicEquipmentBoost(equipment().build()), DELTA);

        varbit(VarbitID.PRAYER_MYSTIC_VIGOUR_UNLOCKED, 1);
        assertEquals(1.03, maxHit().getMagicEquipmentBoost(equipment().build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Mystic_Lore")
    @Test
    @DisplayName("Mystic Lore adds 1% magic damage")
    void mysticLoreAddsOnePercent()
    {
        varbit(VarbitID.PRAYER_MYSTICLORE, 1);

        assertEquals(1.01, maxHit().getMagicEquipmentBoost(equipment().build()), DELTA);
    }

    static Stream<Arguments> styleMismatches()
    {
        return Stream.of(
            // a melee prayer does nothing for ranged, and the other way round
            Arguments.of(VarbitID.PRAYER_PIETY, AttackStyle.RANGING),
            Arguments.of(VarbitID.PRAYER_ZEAL, AttackStyle.RANGING),
            Arguments.of(VarbitID.PRAYER_RIGOUR, AttackStyle.AGGRESSIVE),
            Arguments.of(VarbitID.PRAYER_EAGLEEYE, AttackStyle.CONTROLLED)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Prayer")
    @DisplayName("A prayer does nothing for another combat style")
    @ParameterizedTest(name = "varbit {0} with {1}")
    @MethodSource("styleMismatches")
    void prayersDoNothingForAnotherStyle(int prayerVarbit, AttackStyle attackStyle)
    {
        varbit(prayerVarbit, 1);

        assertEquals(1, maxHit().getPrayerBonus(attackStyle), DELTA);
    }

    @DataSource.Derived("no prayer active")
    @Test
    @DisplayName("No prayer active leaves the multiplier at 1")
    void noPrayerLeavesMultiplierAtOne()
    {
        assertEquals(1, maxHit().getPrayerBonus(AttackStyle.AGGRESSIVE), DELTA);
        assertEquals(1, maxHit().getPrayerBonus(AttackStyle.RANGING), DELTA);
        assertEquals(1, maxHit().getPrayerBonus(AttackStyle.CASTING), DELTA);
    }
}
