/* SpecWeaponStatTest.java
 * Covers the special attack damage multiplier of every spec weapon the calculator knows.
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
import net.runelite.api.gameval.VarPlayerID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Case table for {@link MaxSpec#getSpecWeaponStat}.
 * <p>
 * To cover a new spec weapon, add a row below. Weapons whose multiplier depends on more than the
 * equipped items get their own test at the bottom.
 */
class SpecWeaponStatTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    static Stream<Arguments> meleeSpecWeapons()
    {
        return Stream.of(
            Arguments.of("Armadyl godsword", 1.375),
            Arguments.of("Ancient godsword", 1.1),
            Arguments.of("Bandos godsword", 1.21),
            Arguments.of("Saradomin godsword", 1.1),
            Arguments.of("Zamorak godsword", 1.1),
            Arguments.of("Saradomin sword", 1.1),
            Arguments.of("Dragon dagger", 1.15),
            Arguments.of("Dragon dagger(p++)", 1.15),
            Arguments.of("Dragon sword", 1.25),
            Arguments.of("Dragon halberd", 1.1),
            Arguments.of("Crystal halberd", 1.1),
            Arguments.of("Barrelchest anchor", 1.1),
            Arguments.of("Dragon longsword", 1.25),
            Arguments.of("Dragon mace", 1.5),
            Arguments.of("Dragon warhammer", 1.5),
            Arguments.of("Rune claws", 1.1),
            Arguments.of("Saradomin's blessed sword", 1.25),
            Arguments.of("Osmumten's fang", 1.0),
            Arguments.of("Voidwaker", 1.5),
            Arguments.of("Vesta's longsword", 1.20),
            Arguments.of("Statius's warhammer", 1.25),
            Arguments.of("Dual macuahuitl", 1.25),
            Arguments.of("Arkan blade", 1.5)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Special_attack")
    @DisplayName("Melee spec weapon multiplier")
    @ParameterizedTest(name = "{0} -> {1}x")
    @MethodSource("meleeSpecWeapons")
    void meleeSpecWeapons(String weaponName, double expectedMultiplier)
    {
        maxedCombatStats();

        double actual = maxSpec().getSpecWeaponStat(equipment().weapon(weaponName).build());

        assertEquals(expectedMultiplier, actual, DELTA);
    }

    static Stream<Arguments> rangedSpecWeapons()
    {
        return Stream.of(
            // weapon, ammo, expected multiplier
            Arguments.of("Dark bow", "Dragon arrow", 1.5),
            Arguments.of("Dark bow", "Rune arrow", 1.3),
            Arguments.of("Zaryte crossbow", "Ruby dragon bolts (e)", 1.1),
            Arguments.of("Dragon crossbow", "Runite bolts", 1.2),
            Arguments.of("Rune crossbow", "Diamond bolts (e)", 1.15),
            Arguments.of("Rune crossbow", "Dragonstone bolts (e)", 1.45),
            Arguments.of("Rune crossbow", "Onyx bolts (e)", 1.15),
            Arguments.of("Rune crossbow", "Opal bolts (e)", 1.25),
            Arguments.of("Morrigan's throwing axe", null, 1.25),
            Arguments.of("Webweaver bow", null, 0.4),
            Arguments.of("Toxic blowpipe", null, 1.5),
            Arguments.of("Heavy ballista", null, 1.25)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Special_attack")
    @DisplayName("Ranged spec weapon multiplier")
    @ParameterizedTest(name = "{0} with {1} -> {2}x")
    @MethodSource("rangedSpecWeapons")
    void rangedSpecWeapons(String weaponName, String ammoName, double expectedMultiplier)
    {
        maxedCombatStats();

        Equipment worn = equipment().weapon(weaponName);
        if (ammoName != null)
        {
            worn.ammo(ammoName);
        }

        assertEquals(expectedMultiplier, maxSpec().getSpecWeaponStat(worn.build()), DELTA);
    }

    static Stream<Arguments> magicSpecWeapons()
    {
        return Stream.of(
            Arguments.of("Eye of Ayak", 1.3)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Special_attack")
    @DisplayName("Magic spec weapon multiplier")
    @ParameterizedTest(name = "{0} -> {1}x")
    @MethodSource("magicSpecWeapons")
    void magicSpecWeapons(String weaponName, double expectedMultiplier)
    {
        maxedCombatStats();

        assertEquals(expectedMultiplier, maxSpec().getSpecWeaponStat(equipment().weapon(weaponName).build()), DELTA);
    }

    static Stream<Arguments> weaponsWithoutADamageSpec()
    {
        return Stream.of(
            Arguments.of("Abyssal whip"),
            Arguments.of("Granite maul"),
            Arguments.of("Dragon scimitar"),
            Arguments.of("Magic shortbow"),
            Arguments.of("Dragon claws"),
            Arguments.of("Abyssal dagger"),
            // A crossbow only earns a multiplier from the enchanted bolts the calculator knows
            Arguments.of("Rune crossbow")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Special_attack")
    @DisplayName("Weapons with no damage-boosting spec return 0")
    @ParameterizedTest(name = "{0}")
    @MethodSource("weaponsWithoutADamageSpec")
    void weaponsWithoutADamageSpec(String weaponName)
    {
        maxedCombatStats();

        assertEquals(0, maxSpec().getSpecWeaponStat(equipment().weapon(weaponName).build()), DELTA);
    }

    static Stream<Arguments> crystalHalberdVariants()
    {
        return Stream.of(
            Arguments.of("Crystal halberd (basic)"),
            Arguments.of("Crystal halberd (attuned)"),
            Arguments.of("Crystal halberd (perfected)")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Crystal_halberd")
    @DisplayName("Crystal halberd variants are excluded from the halberd spec")
    @ParameterizedTest(name = "{0}")
    @MethodSource("crystalHalberdVariants")
    void crystalHalberdVariantsAreExcluded(String weaponName)
    {
        maxedCombatStats();

        assertEquals(0, maxSpec().getSpecWeaponStat(equipment().weapon(weaponName).build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Dragon_hasta")
    @Test
    @DisplayName("Dragon hasta scales with special attack energy")
    void dragonHastaScalesWithSpecEnergy()
    {
        maxedCombatStats();

        varp(VarPlayerID.SA_ENERGY, 1000); // full spec bar
        assertEquals(1.5, maxSpec().getSpecWeaponStat(equipment().weapon("Dragon hasta").build()), DELTA);

        varp(VarPlayerID.SA_ENERGY, 500); // half spec bar
        assertEquals(1.25, maxSpec().getSpecWeaponStat(equipment().weapon("Dragon hasta").build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Abyssal_bludgeon")
    @Test
    @DisplayName("Abyssal bludgeon scales with missing prayer points")
    void abyssalBludgeonScalesWithMissingPrayer()
    {
        maxedCombatStats();

        skill(Skill.PRAYER, 99, 99); // no prayer drained
        assertEquals(1.0, maxSpec().getSpecWeaponStat(equipment().weapon("Abyssal bludgeon").build()), DELTA);

        skill(Skill.PRAYER, 99, 0); // fully drained: 99 missing points at 0.5% each
        assertEquals(1.495, maxSpec().getSpecWeaponStat(equipment().weapon("Abyssal bludgeon").build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Soulreaper_axe")
    @Test
    @DisplayName("Soulreaper axe scales with soul stacks")
    void soulreaperAxeScalesWithSoulStacks()
    {
        maxedCombatStats();

        varp(VarPlayerID.SOULREAPER_STACKS, 0);
        assertEquals(1.0, maxSpec().getSpecWeaponStat(equipment().weapon("Soulreaper axe").build()), DELTA);

        varp(VarPlayerID.SOULREAPER_STACKS, 5); // 5 stacks at 6% each
        assertEquals(1.3, maxSpec().getSpecWeaponStat(equipment().weapon("Soulreaper axe").build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Blue_moon_spear")
    @Test
    @DisplayName("Blue moon spear needs the full Blue Moon set")
    void blueMoonSpearNeedsTheFullSet()
    {
        maxedCombatStats();

        Equipment fullSet = equipment()
            .weapon("Blue Moon spear")
            .head("Blue Moon helm")
            .body("Blue Moon chestplate")
            .legs("Blue Moon tassets");
        assertEquals(2.125, maxSpec().getSpecWeaponStat(fullSet.build()), DELTA);

        Equipment spearOnly = equipment().weapon("Blue Moon spear");
        assertEquals(0, maxSpec().getSpecWeaponStat(spearOnly.build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Blue_moon_helm")
    @Test
    @DisplayName("Broken Blue Moon pieces do not count towards the set")
    void brokenBlueMoonPiecesDoNotCount()
    {
        maxedCombatStats();

        Equipment brokenSet = equipment()
            .weapon("Blue Moon spear")
            .head("Blue Moon helm (broken)")
            .body("Blue Moon chestplate")
            .legs("Blue Moon tassets");

        assertEquals(0, maxSpec().getSpecWeaponStat(brokenSet.build()), DELTA);
    }

    static Stream<Arguments> flatBonusSpecWeapons()
    {
        return Stream.of(
            Arguments.of("Granite hammer", 5.0),
            Arguments.of("Dragon warhammer", 0.0),
            Arguments.of("Abyssal whip", 0.0)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Granite_hammer")
    @DisplayName("Flat damage spec bonus")
    @ParameterizedTest(name = "{0} -> +{1}")
    @MethodSource("flatBonusSpecWeapons")
    void flatBonusSpecWeapons(String weaponName, double expectedFlatBonus)
    {
        maxedCombatStats();

        assertEquals(expectedFlatBonus, maxSpec().getSpecFlatBonus(equipment().weapon(weaponName).build()), DELTA);
    }

    static Stream<Arguments> absoluteMaxHitSpecWeapons()
    {
        // No item stats are mocked, so the magic damage multiplier is 1 and these are the base hits
        return Stream.of(
            Arguments.of("Eldritch Nightmare staff", 99, 44.0),
            Arguments.of("Volatile Nightmare staff", 99, 58.0),
            Arguments.of("Corrupted Volatile Nightmare staff", 99, 58.0),
            Arguments.of("Eldritch Nightmare staff", 50, 23.0),   // floor(44 * 50 / 99 + 1)
            Arguments.of("Volatile Nightmare staff", 50, 30.0),   // floor(58 * 50 / 99 + 1)
            Arguments.of("Harmonised Nightmare staff", 99, 0.0),  // no damage spec
            Arguments.of("Abyssal whip", 99, 0.0)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Eldritch_nightmare_staff",
        "https://oldschool.runescape.wiki/w/Volatile_nightmare_staff"
    })
    @DisplayName("Spec weapons that replace the max hit")
    @ParameterizedTest(name = "{0} at {1} Magic -> {2}")
    @MethodSource("absoluteMaxHitSpecWeapons")
    void absoluteMaxHitSpecWeapons(String weaponName, int magicLevel, double expectedMaxHit)
    {
        maxedCombatStats();
        skill(Skill.MAGIC, magicLevel);

        assertEquals(
            expectedMaxHit,
            maxSpec().getSpecAbsoluteMaxHit(equipment().weapon(weaponName).build()),
            DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Volatile_nightmare_staff")
    @Test
    @DisplayName("Nightmare staff base hit caps above 97 Magic")
    void nightmareStaffBaseHitCaps()
    {
        maxedCombatStats();

        skill(Skill.MAGIC, 99);
        double at99 = maxSpec().getSpecAbsoluteMaxHit(equipment().weapon("Volatile Nightmare staff").build());

        skill(Skill.MAGIC, 120);
        double at120 = maxSpec().getSpecAbsoluteMaxHit(equipment().weapon("Volatile Nightmare staff").build());

        assertEquals(at99, at120, DELTA);
    }

    @DataSource.Derived("nothing equipped")
    @Test
    @DisplayName("An empty weapon slot is not a spec weapon")
    void emptyWeaponSlotIsNotASpecWeapon()
    {
        maxedCombatStats();

        assertEquals(0, maxSpec().getSpecWeaponStat(equipment().build()), DELTA);
    }
}
