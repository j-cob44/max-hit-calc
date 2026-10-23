/* AmmoMatchesWeaponTest.java
 * Covers the pairing of every kind of ammo with the weapons that can fire it.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Case table for {@link EquipmentItems#doesAmmoMatchWeapon}.
 * <p>
 * To cover a new ammo or launcher, add a row to the matching table.
 */
class AmmoMatchesWeaponTest
{
    static Stream<Arguments> matchingPairs()
    {
        return Stream.of(
            // ammo, weapon
            Arguments.of("rune arrow", "magic shortbow"),
            Arguments.of("dragon arrow", "twisted bow"),
            Arguments.of("ogre arrow", "ogre bow"),
            Arguments.of("brutal black dragon arrow", "comp ogre bow"),
            Arguments.of("runite bolts", "rune crossbow"),
            Arguments.of("diamond bolts (e)", "armadyl crossbow"),
            Arguments.of("kebbit bolts", "hunters' crossbow"),
            Arguments.of("long kebbit bolts", "hunters' crossbow"),
            Arguments.of("antler bolts", "hunters' sunlight crossbow"),
            Arguments.of("bone bolts", "dorgeshuun crossbow"),
            Arguments.of("bolt rack", "karil's crossbow"),
            Arguments.of("javelin", "heavy ballista"),
            Arguments.of("dragon javelin", "light ballista"),
            Arguments.of("harralander tar", "orange salamander"),
            Arguments.of("guam tar", "red salamander"),
            Arguments.of("guam tar", "black salamander"),
            Arguments.of("guam tar", "tecu salamander"),
            Arguments.of("guam tar", "swamp lizard"),
            Arguments.of("harralander tar", "swamp lizard")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Ammunition")
    @DisplayName("Ammo fits the weapon")
    @ParameterizedTest(name = "{0} fits {1}")
    @MethodSource("matchingPairs")
    void ammoMatchesWeapon(String ammoName, String weaponName)
    {
        assertTrue(EquipmentItems.doesAmmoMatchWeapon(ammoName, weaponName));
    }

    static Stream<Arguments> mismatchedPairs()
    {
        return Stream.of(
            // ammo, weapon it cannot be fired from
            Arguments.of("rune arrow", "rune crossbow"),          // arrows are not for crossbows
            Arguments.of("ogre arrow", "magic shortbow"),         // ogre arrows need an ogre bow
            Arguments.of("runite bolts", "magic shortbow"),       // bolts are not for bows
            Arguments.of("bone bolts", "rune crossbow"),          // bone bolts need the Dorgeshuun
            Arguments.of("kebbit bolts", "rune crossbow"),        // kebbit bolts need the hunters'
            Arguments.of("antler bolts", "hunters' crossbow"),    // antler bolts need the sunlight
            Arguments.of("bolt rack", "rune crossbow"),           // racks are Karil's only
            Arguments.of("javelin", "rune crossbow"),             // javelins need a ballista
            Arguments.of("guam tar", "magic shortbow"),           // tar needs a salamander
            Arguments.of("rune arrow", "abyssal whip"),           // not a launcher at all
            Arguments.of("", "magic shortbow"),                   // empty ammo slot
            Arguments.of("rune arrow", "")                        // empty weapon slot
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Ammunition")
    @DisplayName("Ammo does not fit the weapon")
    @ParameterizedTest(name = "{0} does not fit {1}")
    @MethodSource("mismatchedPairs")
    void ammoDoesNotMatchWeapon(String ammoName, String weaponName)
    {
        assertFalse(EquipmentItems.doesAmmoMatchWeapon(ammoName, weaponName));
    }

}
