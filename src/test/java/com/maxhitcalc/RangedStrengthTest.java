/* RangedStrengthTest.java
 * Covers the weapons that change how ranged strength is gathered.
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
 * Stat values here are made up, they only need to be distinct enough to show which ones were added.
 */
@DataSource.Derived("made up stats, only the summing is under test")
class RangedStrengthTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    private static final int WEAPON_RSTR = 10;
    private static final int AMMO_RSTR = 49;

    @Test
    @DisplayName("A bow adds its own and its ammo's ranged strength")
    void bowAddsWeaponAndAmmo()
    {
        itemStats("Magic shortbow", 0, WEAPON_RSTR, 0);
        itemStats("Rune arrow", 0, AMMO_RSTR, 0);

        double actual = maxHit().getRangedStrengthBonus(
                equipment().weapon("Magic shortbow").ammo("Rune arrow").build());

        assertEquals(WEAPON_RSTR + AMMO_RSTR, actual, DELTA);
    }

    @Test
    @DisplayName("Ammo that does not fit the weapon is not counted")
    void mismatchedAmmoIsNotCounted()
    {
        itemStats("Magic shortbow", 0, WEAPON_RSTR, 0);
        itemStats("Runite bolts", 0, AMMO_RSTR, 0);

        double actual = maxHit().getRangedStrengthBonus(
                equipment().weapon("Magic shortbow").ammo("Runite bolts").build());

        assertEquals(WEAPON_RSTR, actual, DELTA);
    }

    static Stream<Arguments> ammolessWeapons()
    {
        return Stream.of(
            Arguments.of("Rune dart"),
            Arguments.of("Rune knife"),
            Arguments.of("Rune thrownaxe"),
            Arguments.of("Toktz-xil-ul"),
            Arguments.of("Morrigan's javelin"),
            Arguments.of("Morrigan's throwing axe"),
            Arguments.of("Mud pie"),
            Arguments.of("Crystal bow"),
            Arguments.of("Bow of faerdhinen"),
            Arguments.of("Webweaver bow"),
            Arguments.of("Craw's bow"),
            Arguments.of("Tonalztics of ralos")
        );
    }

    @DisplayName("Weapons that carry their own ammo ignore the ammo slot")
    @ParameterizedTest(name = "{0}")
    @MethodSource("ammolessWeapons")
    void ammolessWeaponsIgnoreTheAmmoSlot(String weaponName)
    {
        itemStats(weaponName, 0, WEAPON_RSTR, 0);
        itemStats("Rune arrow", 0, AMMO_RSTR, 0);

        double actual = maxHit().getRangedStrengthBonus(
                equipment().weapon(weaponName).ammo("Rune arrow").build());

        assertEquals(WEAPON_RSTR, actual, DELTA);
    }

    static Stream<Arguments> meleeStrengthWeapons()
    {
        return Stream.of(
            Arguments.of("Eclipse atlatl"),
            Arguments.of("Hunter's spear")
        );
    }

    @DisplayName("Atlatl and hunter's spear use melee strength instead")
    @ParameterizedTest(name = "{0}")
    @MethodSource("meleeStrengthWeapons")
    void meleeStrengthWeaponsUseStrength(String weaponName)
    {
        itemStats(weaponName, 25, 99, 0);      // ranged strength must be ignored
        itemStats("Bandos chestplate", 10, 99, 0);

        double actual = maxHit().getRangedStrengthBonus(
                equipment().weapon(weaponName).body("Bandos chestplate").build());

        assertEquals(35, actual, DELTA);
    }

    static Stream<Arguments> blowpipeDarts()
    {
        return Stream.of(
            // dart, ranged strength it contributes
            Arguments.of(BlowpipeDartType.MITHRIL, 9),
            Arguments.of(BlowpipeDartType.ADAMANT, 17),
            Arguments.of(BlowpipeDartType.RUNE, 26),
            Arguments.of(BlowpipeDartType.AMETHYST, 28),
            Arguments.of(BlowpipeDartType.DRAGON, 35)
        );
    }

    @DisplayName("Blowpipe adds the loaded dart's ranged strength")
    @ParameterizedTest(name = "{0} -> +{1}")
    @MethodSource("blowpipeDarts")
    void blowpipeAddsDartStrength(BlowpipeDartType dartType, int dartStrength)
    {
        blowpipeDart(dartType);
        itemStats("Toxic blowpipe", 0, WEAPON_RSTR, 0);

        double actual = maxHit().getRangedStrengthBonus(
                equipment().weapon("Toxic blowpipe").build());

        assertEquals(WEAPON_RSTR + dartStrength, actual, DELTA);
    }

    @Test
    @DisplayName("Items with no stats are skipped")
    void itemsWithoutStatsAreSkipped()
    {
        itemStats("Magic shortbow", 0, WEAPON_RSTR, 0);

        // the arrows are never given stats, so getItemStats returns null for them
        double actual = maxHit().getRangedStrengthBonus(
                equipment().weapon("Magic shortbow").ammo("Rune arrow").build());

        assertEquals(WEAPON_RSTR, actual, DELTA);
    }

    @Test
    @DisplayName("An empty equipment set has no ranged strength")
    void emptyEquipmentHasNoRangedStrength()
    {
        assertEquals(0, maxHit().getRangedStrengthBonus(equipment().build()), DELTA);
    }
}
