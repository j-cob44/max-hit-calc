/* AttackStyleDetectionTest.java
 * Covers the attack style guessed from an inventory item's name.
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
 * This is the tooltip's guess for an item that is not equipped, so it only has the name to go on.
 */
@DataSource.Derived("the weapon type and style names the client reports")
class AttackStyleDetectionTest extends CalculatorTest
{
    static Stream<Arguments> rangedWeapons()
    {
        return Stream.of(
            Arguments.of("Magic shortbow"),
            Arguments.of("Rune crossbow"),
            Arguments.of("Red chinchompa"),
            Arguments.of("Heavy ballista"),
            Arguments.of("Rune dart"),
            Arguments.of("Rune knife"),
            Arguments.of("Rune thrownaxe"),
            Arguments.of("Toktz-xil-ul"),
            Arguments.of("Toxic blowpipe"),
            Arguments.of("Tonalztics of ralos"),
            Arguments.of("Eclipse atlatl"),
            Arguments.of("Hunter's spear")
        );
    }

    @DisplayName("Ranged weapons")
    @ParameterizedTest(name = "{0}")
    @MethodSource("rangedWeapons")
    void rangedWeapons(String weaponName)
    {
        assertEquals(AttackStyle.RANGING, inventoryItemMaxHit().determineAttackStyle(item(weaponName)));
    }

    static Stream<Arguments> magicWeapons()
    {
        return Stream.of(
            Arguments.of("Accursed sceptre"),
            Arguments.of("Ancient staff"),
            Arguments.of("Trident of the seas"),
            Arguments.of("Tumeken's shadow"),
            Arguments.of("Master wand"),
            Arguments.of("Ivandis crozier"),
            Arguments.of("Void knight mace"),
            Arguments.of("Blue moon spear"),
            Arguments.of("Eye of Ayak"),
            Arguments.of("Dawnbringer")
        );
    }

    @DisplayName("Magic weapons")
    @ParameterizedTest(name = "{0}")
    @MethodSource("magicWeapons")
    void magicWeapons(String weaponName)
    {
        assertEquals(AttackStyle.CASTING, inventoryItemMaxHit().determineAttackStyle(item(weaponName)));
    }

    static Stream<Arguments> meleeWeapons()
    {
        return Stream.of(
            Arguments.of("Abyssal whip"),
            Arguments.of("Dragon scimitar"),
            Arguments.of("Scythe of Vitur"),
            Arguments.of("Dragon warhammer"),
            Arguments.of("Osmumten's fang")
        );
    }

    @DisplayName("Anything else is assumed to be melee")
    @ParameterizedTest(name = "{0}")
    @MethodSource("meleeWeapons")
    void meleeWeapons(String weaponName)
    {
        assertEquals(AttackStyle.AGGRESSIVE, inventoryItemMaxHit().determineAttackStyle(item(weaponName)));
    }
}
