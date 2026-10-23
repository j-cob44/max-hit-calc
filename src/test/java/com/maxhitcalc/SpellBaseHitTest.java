/* SpellBaseHitTest.java
 * Covers the base hit of every powered staff, and the gear that modifies an autocast spell.
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

import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Case table for {@link MaxHit#getSpellBaseHit}.
 * <p>
 * To cover a new powered staff, add a row with its base hit at {@value #MAGIC_LEVEL} Magic. The
 * tests at the bottom cover autocast spells instead.
 */
class SpellBaseHitTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    private static final double MAGIC_LEVEL = 99;

    static Stream<Arguments> poweredStaves()
    {
        return Stream.of(
            // weapon, base hit at 99 Magic
            Arguments.of("Trident of the seas", 28.0),           // floor((99 - 15) / 3)
            Arguments.of("Trident of the swamp", 31.0),          // floor((99 - 6) / 3)
            Arguments.of("Sanguinesti staff", 32.0),             // floor((99 - 3) / 3)
            Arguments.of("Thammaron's sceptre", 25.0),           // floor(99 / 3) - 8
            Arguments.of("Accursed sceptre", 27.0),              // floor(99 / 3) - 6
            Arguments.of("Tumeken's shadow", 34.0),              // floor(99 / 3) + 1
            Arguments.of("Warped sceptre", 24.0),                // floor((8 * 99 + 96) / 37)
            Arguments.of("Crystal staff (basic)", 23.0),
            Arguments.of("Crystal staff (attuned)", 31.0),
            Arguments.of("Crystal staff (perfected)", 39.0),
            Arguments.of("Corrupted staff (basic)", 23.0),
            Arguments.of("Corrupted staff (attuned)", 31.0),
            Arguments.of("Corrupted staff (perfected)", 39.0),
            Arguments.of("Rat bone staff", 38.0),                // floor(99 / 3) + 5
            Arguments.of("Eye of Ayak", 27.0),                   // floor(99 / 3) - 6
            Arguments.of("Dawnbringer", 31.0)                    // floor(99 / 3) - 2
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_magic_hit")
    @DisplayName("Powered staff base hit")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("poweredStaves")
    void poweredStaffBaseHit(String weaponName, double expectedBaseHit)
    {
        double actual = maxHit().getSpellBaseHit(
            equipment().weapon(weaponName).build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        assertEquals(expectedBaseHit, actual, DELTA);
    }

    static Stream<Arguments> poweredStaffLowLevelFloors()
    {
        return Stream.of(
            // A powered staff never falls below its floor, however low the caster's Magic level
            Arguments.of("Trident of the seas", 1.0),
            Arguments.of("Trident of the swamp", 3.0),
            Arguments.of("Sanguinesti staff", 4.0),
            Arguments.of("Dawnbringer", 2.0)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_magic_hit")
    @DisplayName("Powered staff base hit floor at low Magic")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("poweredStaffLowLevelFloors")
    void poweredStaffBaseHitFloor(String weaponName, double expectedFloor)
    {
        double actual = maxHit().getSpellBaseHit(
            equipment().weapon(weaponName).build(),
            AttackStyle.CASTING,
            1);

        assertEquals(expectedFloor, actual, DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_magic_hit")
    @Test
    @DisplayName("A powered staff caps its scaling at 125 Magic")
    void poweredStaffCapsAtMagic125()
    {
        double atCap = maxHit().getSpellBaseHit(
            equipment().weapon("Sanguinesti staff").build(), AttackStyle.CASTING, 125);
        double beyondCap = maxHit().getSpellBaseHit(
            equipment().weapon("Sanguinesti staff").build(), AttackStyle.CASTING, 200);

        assertEquals(atCap, beyondCap, DELTA);
    }

    @DataSource.Derived("no spell and no powered staff, so there is no base hit")
    @Test
    @DisplayName("No autocast spell and no powered staff is an error")
    void noAutocastSpellIsAnError()
    {
        varbit(VarbitID.AUTOCAST_SPELL, 0);

        double actual = maxHit().getSpellBaseHit(
            equipment().weapon("Ancient staff").build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        assertEquals(-1, actual, DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Magic_dart")
    @Test
    @DisplayName("Magic dart scales with Magic level")
    void magicDartScalesWithMagicLevel()
    {
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.MAGIC_DART.getAutocastVarbitValue());

        double actual = maxHit().getSpellBaseHit(
            equipment().weapon("Slayer's staff").build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        assertEquals(19.0, actual, DELTA); // floor(99 / 10) + 10
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Chaos_gauntlets")
    @Test
    @DisplayName("Chaos gauntlets add 3 damage to a bolt spell")
    void chaosGauntletsBoostBoltSpells()
    {
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.FIRE_BOLT.getAutocastVarbitValue());

        double withoutGauntlets = maxHit().getSpellBaseHit(
            equipment().weapon("Ancient staff").build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        double withGauntlets = maxHit().getSpellBaseHit(
            equipment().weapon("Ancient staff").gloves("Chaos gauntlets").build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        assertEquals(12.0, withoutGauntlets, DELTA); // highest tier 2 bolt spell
        assertEquals(15.0, withGauntlets, DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Chaos_gauntlets")
    @Test
    @DisplayName("Chaos gauntlets do not boost a non-bolt spell")
    void chaosGauntletsDoNotBoostOtherSpells()
    {
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.FIRE_BLAST.getAutocastVarbitValue());

        double withGauntlets = maxHit().getSpellBaseHit(
            equipment().weapon("Ancient staff").gloves("Chaos gauntlets").build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        assertEquals(16.0, withGauntlets, DELTA); // highest tier 3 blast spell, unmodified
    }

    static Stream<Arguments> godSpellCapes()
    {
        return Stream.of(
            // spell, matching cape, base hit with the cape, base hit without it
            Arguments.of(CombatSpell.CLAWS_OF_GUTHIX, "Guthix cape"),
            Arguments.of(CombatSpell.CLAWS_OF_GUTHIX, "Guthix max cape"),
            Arguments.of(CombatSpell.FLAMES_OF_ZAMORAK, "Zamorak cape"),
            Arguments.of(CombatSpell.FLAMES_OF_ZAMORAK, "Zamorak max cape"),
            Arguments.of(CombatSpell.SARADOMIN_STRIKE, "Saradomin cape"),
            Arguments.of(CombatSpell.SARADOMIN_STRIKE, "Saradomin max cape")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/God_spells")
    @DisplayName("A charged god spell hits 30 with its matching cape")
    @ParameterizedTest(name = "{0} with {1}")
    @MethodSource("godSpellCapes")
    void chargedGodSpellWithMatchingCape(CombatSpell spell, String capeName)
    {
        varbit(VarbitID.AUTOCAST_SPELL, spell.getAutocastVarbitValue());
        varp(VarPlayerID.MAGEARENA_CHARGE, 1); // charged

        double actual = maxHit().getSpellBaseHit(
            equipment().weapon("Ancient staff").cape(capeName).build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        assertEquals(30.0, actual, DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/God_spells")
    @DisplayName("A charged god spell hits 20 without its matching cape")
    @ParameterizedTest(name = "{0} without a matching cape")
    @MethodSource("godSpellCapes")
    void chargedGodSpellWithoutMatchingCape(CombatSpell spell, String unusedCapeName)
    {
        varbit(VarbitID.AUTOCAST_SPELL, spell.getAutocastVarbitValue());
        varp(VarPlayerID.MAGEARENA_CHARGE, 1); // charged

        double actual = maxHit().getSpellBaseHit(
            equipment().weapon("Ancient staff").cape("Ava's assembler").build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        assertEquals(20.0, actual, DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/God_spells")
    @DisplayName("An uncharged god spell hits 20 even with its matching cape")
    @ParameterizedTest(name = "{0} with {1}, uncharged")
    @MethodSource("godSpellCapes")
    void unchargedGodSpellIgnoresCape(CombatSpell spell, String capeName)
    {
        varbit(VarbitID.AUTOCAST_SPELL, spell.getAutocastVarbitValue());
        varp(VarPlayerID.MAGEARENA_CHARGE, 0); // not charged

        double actual = maxHit().getSpellBaseHit(
            equipment().weapon("Ancient staff").cape(capeName).build(),
            AttackStyle.CASTING,
            MAGIC_LEVEL);

        assertEquals(20.0, actual, DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Slayer's_staff_(e)",
        "https://oldschool.runescape.wiki/w/Magic_dart"
    })
    @Test
    @DisplayName("Slayer's staff (e) improves Magic dart against a weak type")
    void slayersStaffEnchancedImprovesMagicDart()
    {
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.MAGIC_DART.getAutocastVarbitValue());

        double plainStaff = maxAgainstType().getSpellBaseHit(
            equipment().weapon("Slayer's staff").build(), AttackStyle.CASTING);
        double enchantedStaff = maxAgainstType().getSpellBaseHit(
            equipment().weapon("Slayer's staff (e)").build(), AttackStyle.CASTING);

        assertEquals(19.0, plainStaff, DELTA);       // floor(99 / 10) + 10
        assertEquals(29.0, enchantedStaff, DELTA);   // floor(99 / 6) + 13
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Purging_staff")
    @Test
    @DisplayName("Purging staff doubles a demonbane spell against a weak type")
    void purgingStaffDoublesDemonbaneSpells()
    {
        // MaxAgainstType reads the Magic level from the client rather than taking it as an argument
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.INFERIOR_DEMONBANE.getAutocastVarbitValue());

        double actual = maxAgainstType().getSpellBaseHit(
            equipment().weapon("Purging staff").build(),
            AttackStyle.CASTING);

        assertEquals(CombatSpell.INFERIOR_DEMONBANE.getBaseDamage() * 2, actual, DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Purging_staff")
    @Test
    @DisplayName("A plain staff leaves a demonbane spell undoubled")
    void plainStaffLeavesDemonbaneSpellsAlone()
    {
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.INFERIOR_DEMONBANE.getAutocastVarbitValue());

        double actual = maxAgainstType().getSpellBaseHit(
            equipment().weapon("Ancient staff").build(),
            AttackStyle.CASTING);

        assertEquals(CombatSpell.INFERIOR_DEMONBANE.getBaseDamage(), actual, DELTA);
    }

    /** Every bolt is upgraded to Fire bolt's base hit once the whole tier is unlocked */
    private static final double BOLT_TIER_BASE = 12;

    static Stream<Arguments> elementalAmulets()
    {
        return Stream.of(
            // amulet, autocast spell, bonus on top of the tier base
            Arguments.of("Amulet of air", CombatSpell.WIND_BOLT, 2),
            Arguments.of("Amulet of air", CombatSpell.FIRE_BOLT, 0),
            Arguments.of("Amulet of water", CombatSpell.WATER_BOLT, 2),
            Arguments.of("Amulet of water", CombatSpell.EARTH_BOLT, 0),
            Arguments.of("Amulet of earth", CombatSpell.EARTH_BOLT, 2),
            Arguments.of("Amulet of earth", CombatSpell.WIND_BOLT, 0),
            Arguments.of("Amulet of fire", CombatSpell.FIRE_BOLT, 2),
            Arguments.of("Amulet of fire", CombatSpell.WATER_BOLT, 0),
            Arguments.of("Elemental amulet", CombatSpell.WIND_BOLT, 2),
            Arguments.of("Elemental amulet", CombatSpell.WATER_BOLT, 2),
            Arguments.of("Elemental amulet", CombatSpell.EARTH_BOLT, 2),
            Arguments.of("Elemental amulet", CombatSpell.FIRE_BOLT, 2)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Elemental_amulet")
    @DisplayName("Elemental amulets add 2 to their own spell type")
    @ParameterizedTest(name = "{0} with {1}")
    @MethodSource("elementalAmulets")
    void elementalAmuletsAddTwo(String amuletName, CombatSpell spell, int bonus)
    {
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, spell.getAutocastVarbitValue());

        double actual = maxAgainstType().getSpellBaseHit(
            equipment().amulet(amuletName).build(),
            AttackStyle.CASTING);

        assertEquals(BOLT_TIER_BASE + bonus, actual, DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Elemental_amulet",
        "https://oldschool.runescape.wiki/w/Maximum_magic_hit"
    })
    @Test
    @DisplayName("An amulet of air beats an amulet of fire on a maxed bolt tier")
    void airAmuletOutdamagesFireAmuletWithinATier()
    {
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.WIND_BOLT.getAutocastVarbitValue());

        double airAmulet = maxAgainstType().getSpellBaseHit(
            equipment().amulet("Amulet of air").build(), AttackStyle.CASTING);
        double fireAmulet = maxAgainstType().getSpellBaseHit(
            equipment().amulet("Amulet of fire").build(), AttackStyle.CASTING);

        // wind bolt is upgraded to 12, so the air amulet takes it past fire's 12
        assertEquals(14, airAmulet, DELTA);
        assertEquals(12, fireAmulet, DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Elemental_amulet",
        "https://oldschool.runescape.wiki/w/Magic_dart"
    })
    @Test
    @DisplayName("An elemental amulet does nothing for a spell with no element")
    void elementalAmuletDoesNothingForTypelessSpells()
    {
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.MAGIC_DART.getAutocastVarbitValue());

        double actual = maxAgainstType().getSpellBaseHit(
            equipment().amulet("Elemental amulet").build(),
            AttackStyle.CASTING);

        assertEquals(19, actual, DELTA); // floor(99 / 10) + 10, magic dart's own formula
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Chaos_gauntlets")
    @Test
    @DisplayName("Chaos gauntlets apply on the vs type path too")
    void chaosGauntletsApplyAgainstAType()
    {
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.FIRE_BOLT.getAutocastVarbitValue());

        double actual = maxAgainstType().getSpellBaseHit(
            equipment().gloves("Chaos gauntlets").build(),
            AttackStyle.CASTING);

        assertEquals(BOLT_TIER_BASE + 3, actual, DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Elemental_amulet",
        "https://oldschool.runescape.wiki/w/Chaos_gauntlets"
    })
    @Test
    @DisplayName("An elemental amulet stacks additively with chaos gauntlets")
    void elementalAmuletStacksWithChaosGauntlets()
    {
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, CombatSpell.FIRE_BOLT.getAutocastVarbitValue());

        double actual = maxHit().getSpellBaseHit(
            equipment().amulet("Amulet of fire").gloves("Chaos gauntlets").build(),
            AttackStyle.CASTING, MAGIC_LEVEL);

        // the tier's 12, plus 3 from the gauntlets and 2 from the amulet
        assertEquals(BOLT_TIER_BASE + 5, actual, DELTA);
    }
}
