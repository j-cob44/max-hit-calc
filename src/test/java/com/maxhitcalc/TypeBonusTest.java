/* TypeBonusTest.java
 * Covers every weapon and gear bonus that applies only against a weak NPC type.
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Case table for {@link MaxAgainstType#getTypeBonus}.
 * <p>
 * To cover a new type bonus, add a row to the table for the attack style it applies to. Bonuses
 * that differ per style, like the dragon hunter weapons, need a row each.
 */
class TypeBonusTest extends CalculatorTest
{
    static Stream<Arguments> meleeWeaponBonuses()
    {
        return Stream.of(
            // weapon, multiplier applied against the weak type
            Arguments.of("Silverlight", 1.6),
            Arguments.of("Darklight", 1.6),
            Arguments.of("Gadderhammer", 1.25),
            Arguments.of("Arclight", 1.7),
            Arguments.of("Emberlight", 1.7),
            Arguments.of("Leaf-bladed battleaxe", 1.175),
            Arguments.of("Dragon hunter lance", 1.2),
            Arguments.of("Ivandis flail", 1.2),
            Arguments.of("Viggora's chainmace", 1.5),
            Arguments.of("Ursine chainmace", 1.5),
            Arguments.of("Blisterwood flail", 1.25),
            Arguments.of("Blisterwood sickle", 1.15),
            Arguments.of("Blisterwood sickle (e)", 1.15),
            Arguments.of("Hallowed flail", 1.25),
            Arguments.of("Barronite mace", 1.15),
            Arguments.of("Granite hammer", 1.3),
            Arguments.of("Keris partisan", 1.33),
            Arguments.of("Keris partisan of breaching", 1.33),
            Arguments.of("Keris partisan of the sun", 1.33),
            Arguments.of("Keris partisan of Amascut", 1.15),
            Arguments.of("Burning claws", 1.05),
            Arguments.of("Sunspear", 1.5)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Demonbane_weapons",
        "https://oldschool.runescape.wiki/w/Gadderhammer",
        "https://oldschool.runescape.wiki/w/Leaf-bladed_battleaxe",
        "https://oldschool.runescape.wiki/w/Dragon_hunter_lance",
        "https://oldschool.runescape.wiki/w/Ivandis_flail",
        "https://oldschool.runescape.wiki/w/Viggora's_chainmace",
        "https://oldschool.runescape.wiki/w/Ursine_chainmace",
        "https://oldschool.runescape.wiki/w/Blisterwood_flail",
        "https://oldschool.runescape.wiki/w/Blisterwood_sickle",
        "https://oldschool.runescape.wiki/w/Hallowed_flail",
        "https://oldschool.runescape.wiki/w/Barronite_mace",
        "https://oldschool.runescape.wiki/w/Granite_hammer",
        "https://oldschool.runescape.wiki/w/Keris_partisan",
        "https://oldschool.runescape.wiki/w/Keris_partisan_of_Amascut",
        "https://oldschool.runescape.wiki/w/Burning_claws",
        "https://oldschool.runescape.wiki/w/Sunspear"
    })
    @DisplayName("Melee weapon type bonus")
    @ParameterizedTest(name = "{0} -> {1}x")
    @MethodSource("meleeWeaponBonuses")
    void meleeWeaponBonuses(String weaponName, double expectedMultiplier)
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            AttackStyle.AGGRESSIVE,
            equipment().weapon(weaponName).build());

        assertEquals(Collections.singletonList(expectedMultiplier), actual);
    }

    static Stream<Arguments> rangedWeaponBonuses()
    {
        return Stream.of(
            Arguments.of("Dragon hunter crossbow", 1.25),
            Arguments.of("Craw's bow", 1.5),
            Arguments.of("Webweaver bow", 1.5),
            Arguments.of("Scorching bow", 1.3),
            Arguments.of("Blisterwood stake", 1.25)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Dragon_hunter_crossbow",
        "https://oldschool.runescape.wiki/w/Craw's_bow",
        "https://oldschool.runescape.wiki/w/Webweaver_bow",
        "https://oldschool.runescape.wiki/w/Scorching_bow",
        "https://oldschool.runescape.wiki/w/Blisterwood_stake"
    })
    @DisplayName("Ranged weapon type bonus")
    @ParameterizedTest(name = "{0} -> {1}x")
    @MethodSource("rangedWeaponBonuses")
    void rangedWeaponBonuses(String weaponName, double expectedMultiplier)
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            AttackStyle.RANGING,
            equipment().weapon(weaponName).build());

        assertEquals(Collections.singletonList(expectedMultiplier), actual);
    }

    static Stream<Arguments> magicWeaponBonuses()
    {
        return Stream.of(
            Arguments.of("Dragon hunter wand", 1.4),
            Arguments.of("Thammaron's sceptre", 1.5),
            Arguments.of("Accursed sceptre", 1.5)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Dragon_hunter_wand",
        "https://oldschool.runescape.wiki/w/Thammaron's_sceptre",
        "https://oldschool.runescape.wiki/w/Accursed_sceptre"
    })
    @DisplayName("Magic weapon type bonus")
    @ParameterizedTest(name = "{0} -> {1}x")
    @MethodSource("magicWeaponBonuses")
    void magicWeaponBonuses(String weaponName, double expectedMultiplier)
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            AttackStyle.CASTING,
            equipment().weapon(weaponName).build());

        assertEquals(Collections.singletonList(expectedMultiplier), actual);
    }

    static Stream<Arguments> unchargedWildernessWeapons()
    {
        return Stream.of(
            // The uncharged forms of the wilderness weapons lose their type bonus
            Arguments.of(AttackStyle.AGGRESSIVE, "Viggora's chainmace (u)"),
            Arguments.of(AttackStyle.AGGRESSIVE, "Ursine chainmace (u)"),
            Arguments.of(AttackStyle.RANGING, "Craw's bow (u)"),
            Arguments.of(AttackStyle.RANGING, "Webweaver bow (u)"),
            Arguments.of(AttackStyle.CASTING, "Thammaron's sceptre (u)"),
            Arguments.of(AttackStyle.CASTING, "Accursed sceptre (u)")
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Viggora's_chainmace",
        "https://oldschool.runescape.wiki/w/Ursine_chainmace",
        "https://oldschool.runescape.wiki/w/Craw's_bow",
        "https://oldschool.runescape.wiki/w/Webweaver_bow",
        "https://oldschool.runescape.wiki/w/Thammaron's_sceptre",
        "https://oldschool.runescape.wiki/w/Accursed_sceptre"
    })
    @DisplayName("Uncharged wilderness weapons have no type bonus")
    @ParameterizedTest(name = "{1}")
    @MethodSource("unchargedWildernessWeapons")
    void unchargedWildernessWeaponsHaveNoBonus(AttackStyle attackStyle, String weaponName)
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            attackStyle,
            equipment().weapon(weaponName).build());

        assertEquals(Collections.emptyList(), actual);
    }

    static Stream<Arguments> dragonHunterWeaponsByStyle()
    {
        return Stream.of(
            // The dragonbane bonus differs per combat style
            Arguments.of("Dragon hunter lance", AttackStyle.AGGRESSIVE, 1.2),
            Arguments.of("Dragon hunter crossbow", AttackStyle.RANGING, 1.25),
            Arguments.of("Dragon hunter wand", AttackStyle.CASTING, 1.4)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Dragon_hunter_lance",
        "https://oldschool.runescape.wiki/w/Dragon_hunter_crossbow",
        "https://oldschool.runescape.wiki/w/Dragon_hunter_wand"
    })
    @DisplayName("Dragonbane bonus per combat style")
    @ParameterizedTest(name = "{0} ({1}) -> {2}x")
    @MethodSource("dragonHunterWeaponsByStyle")
    void dragonHunterBonusPerStyle(String weaponName, AttackStyle attackStyle, double expectedMultiplier)
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            attackStyle,
            equipment().weapon(weaponName).build());

        assertEquals(Collections.singletonList(expectedMultiplier), actual);
    }

    static Stream<Arguments> meleeSlayerAndUndeadGear()
    {
        return Stream.of(
            // The undead and slayer bonuses are mutually exclusive, highest first
            Arguments.of("Amulet of avarice", null, 1.2),
            Arguments.of("Salve amulet (e)", null, 1.2),
            Arguments.of("Salve amulet(ei)", null, 1.2),
            Arguments.of("Salve amulet", null, 1.15),
            Arguments.of(null, "Black mask", 1.1667),
            Arguments.of(null, "Black mask (i)", 1.1667),
            Arguments.of(null, "Slayer helmet", 1.1667),
            Arguments.of(null, "Slayer helmet (i)", 1.1667)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Slayer_helmet",
        "https://oldschool.runescape.wiki/w/Salve_amulet",
        "https://oldschool.runescape.wiki/w/Amulet_of_avarice"
    })
    @DisplayName("Melee slayer and undead gear bonus")
    @ParameterizedTest(name = "amulet={0} head={1} -> {2}x")
    @MethodSource("meleeSlayerAndUndeadGear")
    void meleeSlayerAndUndeadGear(String amuletName, String headName, double expectedMultiplier)
    {
        Equipment worn = equipment();
        if (amuletName != null)
        {
            worn.amulet(amuletName);
        }
        if (headName != null)
        {
            worn.head(headName);
        }

        assertEquals(
            Collections.singletonList(expectedMultiplier),
            maxAgainstType().getTypeBonus(AttackStyle.AGGRESSIVE, worn.build()));
    }

    static Stream<Arguments> rangedSlayerAndUndeadGear()
    {
        return Stream.of(
            Arguments.of("Amulet of avarice", null, 1.2),
            Arguments.of("Salve amulet(ei)", null, 1.2),
            Arguments.of("Salve amulet(i)", null, 1.1667),
            Arguments.of(null, "Black mask (i)", 1.15),
            Arguments.of(null, "Slayer helmet (i)", 1.15)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Slayer_helmet",
        "https://oldschool.runescape.wiki/w/Salve_amulet",
        "https://oldschool.runescape.wiki/w/Amulet_of_avarice"
    })
    @DisplayName("Ranged slayer and undead gear bonus")
    @ParameterizedTest(name = "amulet={0} head={1} -> {2}x")
    @MethodSource("rangedSlayerAndUndeadGear")
    void rangedSlayerAndUndeadGear(String amuletName, String headName, double expectedMultiplier)
    {
        Equipment worn = equipment();
        if (amuletName != null)
        {
            worn.amulet(amuletName);
        }
        if (headName != null)
        {
            worn.head(headName);
        }

        assertEquals(
            Collections.singletonList(expectedMultiplier),
            maxAgainstType().getTypeBonus(AttackStyle.RANGING, worn.build()));
    }

    static Stream<Arguments> magicSlayerAndUndeadGear()
    {
        return Stream.of(
            Arguments.of("Amulet of avarice", null, 1.2),
            Arguments.of("Salve amulet(ei)", null, 1.2),
            Arguments.of("Salve amulet(i)", null, 1.15),
            Arguments.of(null, "Black mask (i)", 1.15),
            Arguments.of(null, "Slayer helmet (i)", 1.15)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Slayer_helmet",
        "https://oldschool.runescape.wiki/w/Salve_amulet",
        "https://oldschool.runescape.wiki/w/Amulet_of_avarice"
    })
    @DisplayName("Magic slayer and undead gear bonus")
    @ParameterizedTest(name = "amulet={0} head={1} -> {2}x")
    @MethodSource("magicSlayerAndUndeadGear")
    void magicSlayerAndUndeadGear(String amuletName, String headName, double expectedMultiplier)
    {
        Equipment worn = equipment();
        if (amuletName != null)
        {
            worn.amulet(amuletName);
        }
        if (headName != null)
        {
            worn.head(headName);
        }

        assertEquals(
            Collections.singletonList(expectedMultiplier),
            maxAgainstType().getTypeBonus(AttackStyle.CASTING, worn.build()));
    }

    static Stream<Arguments> efaritayStyles()
    {
        return Stream.of(
            Arguments.of(AttackStyle.AGGRESSIVE),
            Arguments.of(AttackStyle.RANGING)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Efaritay's_aid")
    @DisplayName("Efaritay's aid adds a vampyre bonus on its own")
    @ParameterizedTest(name = "{0}")
    @MethodSource("efaritayStyles")
    void efaritaysAidAddsABonus(AttackStyle attackStyle)
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            attackStyle,
            equipment().ring("Efaritay's aid").build());

        assertEquals(Collections.singletonList(1.1), actual);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Efaritay's_aid",
        "https://oldschool.runescape.wiki/w/Vampyrebane_weapons"
    })
    @Test
    @DisplayName("Efaritay's aid stacks with a vampyrebane weapon")
    void efaritaysAidStacksWithVampyrebaneWeapons()
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            AttackStyle.AGGRESSIVE,
            equipment().weapon("Sunspear").ring("Efaritay's aid").build());

        // Efaritay's aid is applied first, matching the chronological order the file keeps
        assertEquals(Arrays.asList(1.1, 1.5), actual);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Vampyrebane_weapons")
    @Test
    @DisplayName("Vampyrebane flails are mutually exclusive")
    void vampyrebaneFlailsAreMutuallyExclusive()
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            AttackStyle.AGGRESSIVE,
            equipment().weapon("Blisterwood flail").build());

        assertEquals(Collections.singletonList(1.25), actual);
    }

    static Stream<Arguments> weaponsWithoutATypeBonus()
    {
        return Stream.of(
            Arguments.of(AttackStyle.AGGRESSIVE, "Abyssal whip"),
            Arguments.of(AttackStyle.AGGRESSIVE, "Dragon scimitar"),
            Arguments.of(AttackStyle.RANGING, "Twisted bow"),
            Arguments.of(AttackStyle.CASTING, "Tumeken's shadow")
        );
    }

    @DataSource.Derived("weapons with no type bonus, so there is nothing to apply")
    @DisplayName("Weapons with no type bonus yield no multipliers")
    @ParameterizedTest(name = "{1} ({0})")
    @MethodSource("weaponsWithoutATypeBonus")
    void weaponsWithoutATypeBonus(AttackStyle attackStyle, String weaponName)
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            attackStyle,
            equipment().weapon(weaponName).build());

        assertEquals(Collections.emptyList(), actual);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Slayer_helmet",
        "https://oldschool.runescape.wiki/w/Salve_amulet"
    })
    @DisplayName("A salve amulet and a slayer helmet do not stack")
    @ParameterizedTest(name = "{0}")
    @MethodSource("meleeStackingPairs")
    void undeadAndSlayerBonusesAreMutuallyExclusive(
            String description, String amuletName, String headName, double expected)
    {
        List<Double> actual = maxAgainstType().getTypeBonus(
            AttackStyle.AGGRESSIVE,
            equipment().amulet(amuletName).head(headName).build());

        assertEquals(Collections.singletonList(expected), actual);
    }

    static Stream<Arguments> meleeStackingPairs()
    {
        return Stream.of(
            Arguments.of("Avarice wins over a slayer helmet", "Amulet of avarice", "Slayer helmet (i)", 1.2),
            Arguments.of("Salve (e) wins over a slayer helmet", "Salve amulet (e)", "Slayer helmet (i)", 1.2),
            Arguments.of("Black mask wins over a plain salve amulet", "Salve amulet", "Black mask", 1.1667)
        );
    }
}
