/* MultiHitWeaponTest.java
 * Covers the weapons that land more than one hit in a single attack.
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Case tables for the weapons that hit several times per attack.
 * <p>
 * To cover a new multi-hit weapon, add a row to the matching table.
 */
class MultiHitWeaponTest extends CalculatorTest
{
    private static final int SINGLE_HIT = 100;

    static Stream<Arguments> specMultiHitWeapons()
    {
        return Stream.of(
            // weapon, total damage across all hits of the spec
            Arguments.of("Dragon dagger", 200),          // two hits
            Arguments.of("Dragon claws", 197),           // 99 + 49 + 24 + 25
            Arguments.of("Abyssal dagger", 185),         // 100 + 85
            Arguments.of("Saradomin sword", 116),        // hit + 16 magic damage
            Arguments.of("Magic shortbow", 200),         // two arrows
            Arguments.of("Dragon knife", 200),           // two knives
            Arguments.of("Webweaver bow", 400),          // four hits
            Arguments.of("Burning claws", 173)           // 43 + 43 + 87 of a 175 absolute max
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Special_attack")
    @DisplayName("Spec multi-hit total")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("specMultiHitWeapons")
    void specMultiHitWeapons(String weaponName, int expectedTotal)
    {
        maxedCombatStats();
        equipment().weapon(weaponName).wear();

        assertEquals(expectedTotal, maxSpec().getSpecMultiHit(SINGLE_HIT));
    }

    static Stream<Arguments> singleHitSpecWeapons()
    {
        return Stream.of(
            Arguments.of("Dragon warhammer"),
            Arguments.of("Armadyl godsword"),
            Arguments.of("Voidwaker"),
            Arguments.of("Abyssal whip")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Special_attack")
    @DisplayName("Single-hit spec weapons report no multi-hit total")
    @ParameterizedTest(name = "{0}")
    @MethodSource("singleHitSpecWeapons")
    void singleHitSpecWeapons(String weaponName)
    {
        maxedCombatStats();
        equipment().weapon(weaponName).wear();

        assertEquals(0, maxSpec().getSpecMultiHit(SINGLE_HIT));
    }

    static Stream<Arguments> passiveMultiHitWeapons()
    {
        return Stream.of(
            // Hallowfell adds two hits of half the max on top of the first
            Arguments.of("Hallowfell", 200),
            Arguments.of("Abyssal whip", 0)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Scythe_of_vitur",
        "https://oldschool.runescape.wiki/w/Tonalztics_of_ralos"
    })
    @DisplayName("Passive multi-hit total")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("passiveMultiHitWeapons")
    void passiveMultiHit(String weaponName, int expectedTotal)
    {
        maxedCombatStats();
        npcSize(3);

        assertEquals(
            expectedTotal,
            maxHit().getPassiveMultiHit(SINGLE_HIT, equipment().weapon(weaponName).build()));
    }

    static Stream<Arguments> scytheVariants()
    {
        return Stream.of(
            Arguments.of("Scythe of Vitur"),
            Arguments.of("Holy Scythe of Vitur"),
            Arguments.of("Sanguine Scythe of Vitur"),
            Arguments.of("Corrupted Scythe of Vitur")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Scythe_of_vitur")
    @DisplayName("Every scythe variant hits three times on a large target")
    @ParameterizedTest(name = "{0}")
    @MethodSource("scytheVariants")
    void scytheVariantsAllMultiHit(String weaponName)
    {
        maxedCombatStats();
        npcSize(3);

        assertEquals(
            175, // 100 + 50 + 25
            maxHit().getPassiveMultiHit(SINGLE_HIT, equipment().weapon(weaponName).build()));
    }

    static Stream<Arguments> scytheTargetSizes()
    {
        return Stream.of(
            // target size, total damage
            Arguments.of(1, 0),     // single tile, hit once, so no multi-hit total
            Arguments.of(2, 150),   // 100 + 50
            Arguments.of(3, 175),   // 100 + 50 + 25
            Arguments.of(5, 175)    // never more than three hits
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Scythe_of_vitur")
    @DisplayName("Scythe of Vitur hit count follows target size")
    @ParameterizedTest(name = "size {0} -> {1}")
    @MethodSource("scytheTargetSizes")
    void scytheHitCountFollowsTargetSize(int targetSize, int expectedTotal)
    {
        maxedCombatStats();
        npcSize(targetSize);

        assertEquals(
            expectedTotal,
            maxHit().getPassiveMultiHit(SINGLE_HIT, equipment().weapon("Scythe of Vitur").build()));
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Scythe_of_vitur")
    @DisplayName("Scythe of Vitur halving rounds down")
    @ParameterizedTest(name = "base {0} -> {1}")
    @MethodSource("scytheWorkedExamples")
    void scytheHalvingRoundsDown(int baseMax, int expectedTotal)
    {
        maxedCombatStats();
        npcSize(3);

        assertEquals(
            expectedTotal,
            maxHit().getPassiveMultiHit(baseMax, equipment().weapon("Scythe of Vitur").build()));
    }

    static Stream<Arguments> scytheWorkedExamples()
    {
        return Stream.of(
            Arguments.of(47, 81),   // 47 + 23 + 11, the wiki's example
            Arguments.of(48, 84),   // 48 + 24 + 12
            Arguments.of(1, 1),     // 1 + 0 + 0
            Arguments.of(3, 4)      // 3 + 1 + 0
        );
    }
}
