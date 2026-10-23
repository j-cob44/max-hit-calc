/* FlatArmourTest.java
 * Covers the flat armour of the selected target.
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
 * Flat armour comes off each hitsplat of a successful melee or ranged attack, and player magic
 * ignores it entirely.
 */
class FlatArmourTest extends CalculatorTest
{
    private static final int SINGLE_HIT = 100;

    static Stream<Arguments> armourValues()
    {
        return Stream.of(
            // selected NPC, its flat armour
            Arguments.of("Veiled kraken", 15),
            Arguments.of("Elite Black Knight", 7),
            Arguments.of("Eclipse Moon", 6),
            Arguments.of("Drake", 2),
            Arguments.of("Wyrm", 1),
            Arguments.of("Dusk", -1),
            Arguments.of("Shade", -3),
            Arguments.of("Earthen Nagua", -4),
            Arguments.of("Blue Moon", -5)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Elemental_weakness")
    @DisplayName("Flat armour is read from the selected NPC")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("armourValues")
    void flatArmourIsReadFromTheSelectedNPC(String npcName, int expected)
    {
        assertEquals(expected, NPCTypeWeakness.findWeaknessByName(npcName).getFlatArmour());
    }

    static Stream<Arguments> meleeAndRangedStyles()
    {
        return Stream.of(
            Arguments.of(AttackStyle.AGGRESSIVE),
            Arguments.of(AttackStyle.ACCURATE),
            Arguments.of(AttackStyle.CONTROLLED),
            Arguments.of(AttackStyle.DEFENSIVE),
            Arguments.of(AttackStyle.RANGING),
            Arguments.of(AttackStyle.LONGRANGE)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Elemental_weakness")
    @DisplayName("Melee and ranged attacks take flat armour off")
    @ParameterizedTest(name = "{0}")
    @MethodSource("meleeAndRangedStyles")
    void meleeAndRangedTakeFlatArmour(AttackStyle attackStyle)
    {
        selectedNPC("Veiled kraken");

        assertEquals(15, maxHit().getNPCFlatArmour(attackStyle));
    }

    static Stream<Arguments> castingStyles()
    {
        return Stream.of(
            Arguments.of(AttackStyle.CASTING),
            Arguments.of(AttackStyle.DEFENSIVE_CASTING)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Elemental_weakness")
    @DisplayName("Magic ignores flat armour")
    @ParameterizedTest(name = "{0}")
    @MethodSource("castingStyles")
    void magicIgnoresFlatArmour(AttackStyle attackStyle)
    {
        selectedNPC("Veiled kraken");

        assertEquals(0, maxHit().getNPCFlatArmour(attackStyle));
    }

    @DataSource.Derived("no target selected, so there is nothing to read")
    @Test
    @DisplayName("No selected NPC means no flat armour")
    void noSelectedNPCMeansNoFlatArmour()
    {
        selectedNPC(null);

        assertEquals(0, maxHit().getNPCFlatArmour(AttackStyle.AGGRESSIVE));
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Vorkath")
    @Test
    @DisplayName("An NPC with a weakness but no armour contributes nothing")
    void npcWithoutArmourContributesNothing()
    {
        selectedNPC("Vorkath");

        assertEquals(0, maxHit().getNPCFlatArmour(AttackStyle.AGGRESSIVE));
    }

    @DataSource.Derived("arithmetic on an armour value covered above")
    @Test
    @DisplayName("A successful hit is never reduced below zero")
    void hitsNeverGoBelowZero()
    {
        assertEquals(0, MaxHit.applyFlatArmour(10, 15));
        assertEquals(0, MaxHit.applyFlatArmour(0, 2));
        assertEquals(5, MaxHit.applyFlatArmour(10, 5));
        assertEquals(12, MaxHit.applyFlatArmour(10, -2));
    }

    static Stream<Arguments> scytheHitsplats()
    {
        return Stream.of(
            // flat armour, total across the three hits of 100/50/25
            Arguments.of(0, 175),
            Arguments.of(2, 169),    // 98 + 48 + 23
            Arguments.of(-5, 190),   // 105 + 55 + 30
            Arguments.of(30, 90)     // 70 + 20 + 0, the third hitsplat floors at 0
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Scythe_of_vitur",
        "https://oldschool.runescape.wiki/w/Elemental_weakness"
    })
    @DisplayName("Scythe of Vitur takes flat armour off each hitsplat")
    @ParameterizedTest(name = "armour {0} -> {1}")
    @MethodSource("scytheHitsplats")
    void scytheAppliesArmourPerHitsplat(int flatArmour, int expectedTotal)
    {
        maxedCombatStats();
        npcSize(3);

        assertEquals(
            expectedTotal,
            maxHit().getPassiveMultiHit(SINGLE_HIT, equipment().weapon("Scythe of Vitur").build(), flatArmour));
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Scythe_of_vitur",
        "https://oldschool.runescape.wiki/w/Elemental_weakness"
    })
    @Test
    @DisplayName("Negative flat armour rewards multi hit weapons")
    void negativeArmourRewardsMultiHitWeapons()
    {
        maxedCombatStats();
        npcSize(3);

        int single = MaxHit.applyFlatArmour(SINGLE_HIT, -5);
        int scythe = maxHit().getPassiveMultiHit(
                SINGLE_HIT, equipment().weapon("Scythe of Vitur").build(), -5);

        assertEquals(105, single);   // one hitsplat gains 5
        assertEquals(190, scythe);   // three hitsplats gain 5 each
    }

    static Stream<Arguments> specMultiHitArmour()
    {
        return Stream.of(
            // weapon, flat armour, total
            Arguments.of("Dragon dagger", 2, 196),      // 2 hits of 98
            Arguments.of("Dragon dagger", -2, 204),     // 2 hits of 102
            Arguments.of("Magic shortbow", 2, 196),
            Arguments.of("Webweaver bow", 2, 392),      // 4 hits of 98
            Arguments.of("Dragon claws", 2, 189),       // 97 + 47 + 22 + 23
            Arguments.of("Abyssal dagger", 2, 181),     // 98 + 83
            Arguments.of("Burning claws", 2, 167)       // 41 + 41 + 85
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Special_attack",
        "https://oldschool.runescape.wiki/w/Elemental_weakness"
    })
    @DisplayName("Spec multi hit weapons take flat armour off each hitsplat")
    @ParameterizedTest(name = "{0} with armour {1} -> {2}")
    @MethodSource("specMultiHitArmour")
    void specMultiHitAppliesArmourPerHitsplat(String weaponName, int flatArmour, int expectedTotal)
    {
        maxedCombatStats();
        equipment().weapon(weaponName).wear();

        assertEquals(expectedTotal, maxSpec().getSpecMultiHit(SINGLE_HIT, flatArmour));
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Saradomin_sword")
    @Test
    @DisplayName("Saradomin sword's magic damage ignores flat armour")
    void saradominSwordMagicDamageIgnoresArmour()
    {
        maxedCombatStats();
        equipment().weapon("Saradomin sword").wear();

        // the melee hit loses 2, the 16 magic damage does not
        assertEquals(114, maxSpec().getSpecMultiHit(SINGLE_HIT, 2));
    }
}
