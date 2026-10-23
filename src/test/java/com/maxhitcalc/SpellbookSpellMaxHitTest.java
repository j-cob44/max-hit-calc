/* SpellbookSpellMaxHitTest.java
 * Covers the max hit shown when hovering a spell in the spellbook.
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
 * This path takes the spell as an argument rather than reading the autocast varbit, since it is
 * driven by hovering a spell rather than by what is equipped.
 */
class SpellbookSpellMaxHitTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;
    private static final int MAGIC_LEVEL = 99;

    private SpellbookSpellMaxHit spellbook()
    {
        return new SpellbookSpellMaxHit(plugin, config, itemManager, client);
    }

    // The max the panel shows, floored the way calculateMaxes does
    private static int shown(double maxHit)
    {
        return (int) Math.floor(maxHit);
    }

    static Stream<Arguments> tieredSpells()
    {
        return Stream.of(
            // spell, highest base damage available in its tier at 99 Magic
            Arguments.of(CombatSpell.WIND_STRIKE, 8.0),      // fire strike is the best tier 1
            Arguments.of(CombatSpell.WIND_BOLT, 12.0),       // fire bolt
            Arguments.of(CombatSpell.WIND_BLAST, 16.0),      // fire blast
            Arguments.of(CombatSpell.FIRE_BOLT, 12.0)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_magic_hit")
    @DisplayName("A tiered spell uses the best in its tier")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("tieredSpells")
    void tieredSpellUsesBestInTier(CombatSpell spell, double expected)
    {
        assertEquals(expected, spellbook().getSpellBaseHit(equipment().build(), MAGIC_LEVEL, spell), DELTA);
    }

    @DataSource.Derived("a spell the level cannot cast has no base hit")
    @Test
    @DisplayName("A spell above the player's level cannot be cast")
    void spellAboveLevelCannotBeCast()
    {
        // fire blast needs 59 Magic
        assertEquals(-1, spellbook().getSpellBaseHit(equipment().build(), 10, CombatSpell.FIRE_BLAST), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_magic_hit")
    @Test
    @DisplayName("An untiered spell uses its own base damage")
    void untieredSpellUsesItsOwnDamage()
    {
        double actual = spellbook().getSpellBaseHit(
                equipment().build(), MAGIC_LEVEL, CombatSpell.INFERIOR_DEMONBANE);

        assertEquals(CombatSpell.INFERIOR_DEMONBANE.getBaseDamage(), actual, DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Magic_dart")
    @Test
    @DisplayName("Magic dart scales with Magic level")
    void magicDartScalesWithLevel()
    {
        assertEquals(19.0, spellbook().getSpellBaseHit(equipment().build(), 99, CombatSpell.MAGIC_DART), DELTA);
        assertEquals(15.0, spellbook().getSpellBaseHit(equipment().build(), 50, CombatSpell.MAGIC_DART), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Chaos_gauntlets")
    @Test
    @DisplayName("Chaos gauntlets add 3 to a bolt spell")
    void chaosGauntletsBoostBoltSpells()
    {
        Equipment worn = equipment().gloves("Chaos gauntlets");

        assertEquals(15.0, spellbook().getSpellBaseHit(worn.build(), MAGIC_LEVEL, CombatSpell.FIRE_BOLT), DELTA);
        assertEquals(16.0, spellbook().getSpellBaseHit(worn.build(), MAGIC_LEVEL, CombatSpell.FIRE_BLAST), DELTA);
    }

    static Stream<Arguments> godSpellCapes()
    {
        return Stream.of(
            Arguments.of(CombatSpell.CLAWS_OF_GUTHIX, "Guthix cape"),
            Arguments.of(CombatSpell.FLAMES_OF_ZAMORAK, "Zamorak cape"),
            Arguments.of(CombatSpell.SARADOMIN_STRIKE, "Saradomin max cape")
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/God_spells")
    @DisplayName("A charged god spell hits 30 with its cape")
    @ParameterizedTest(name = "{0} with {1}")
    @MethodSource("godSpellCapes")
    void chargedGodSpellWithCape(CombatSpell spell, String capeName)
    {
        varp(VarPlayerID.MAGEARENA_CHARGE, 1);

        assertEquals(30.0, spellbook().getSpellBaseHit(
                equipment().cape(capeName).build(), MAGIC_LEVEL, spell), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/God_spells")
    @DisplayName("A god spell hits 20 without the charge")
    @ParameterizedTest(name = "{0}")
    @MethodSource("godSpellCapes")
    void unchargedGodSpell(CombatSpell spell, String capeName)
    {
        varp(VarPlayerID.MAGEARENA_CHARGE, 0);

        assertEquals(20.0, spellbook().getSpellBaseHit(
                equipment().cape(capeName).build(), MAGIC_LEVEL, spell), DELTA);
    }

    static Stream<Arguments> tomes()
    {
        return Stream.of(
            Arguments.of("Tome of fire", CombatSpell.FIRE_BOLT, 1.1),
            Arguments.of("Tome of water", CombatSpell.WATER_BOLT, 1.1),
            Arguments.of("Tome of earth", CombatSpell.EARTH_BOLT, 1.1),
            Arguments.of("Tome of fire (empty)", CombatSpell.FIRE_BOLT, 1.0),
            Arguments.of("Tome of fire", CombatSpell.WATER_BOLT, 1.0)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Tome_of_fire",
        "https://oldschool.runescape.wiki/w/Tome_of_water",
        "https://oldschool.runescape.wiki/w/Tome_of_earth"
    })
    @DisplayName("Tome bonus")
    @ParameterizedTest(name = "{0} with {1} -> {2}x")
    @MethodSource("tomes")
    void tomeBonus(String tomeName, CombatSpell spell, double expected)
    {
        assertEquals(expected, spellbook().getTomeSpellBonus(
                equipment().shield(tomeName).build(), spell), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Maximum_magic_hit")
    @Test
    @DisplayName("Max hit combines the base hit with the magic damage bonus")
    void maxHitCombinesBaseAndGear()
    {
        skill(Skill.MAGIC, MAGIC_LEVEL);
        itemStats("Occult necklace", 0, 0, 10);

        // fire bolt's tier tops out at 12, and the necklace's 10% takes it to 13.2
        double actual = spellbook().calculateMagicMaxHit(
                equipment().amulet("Occult necklace").build(), CombatSpell.FIRE_BOLT);

        assertEquals(13, shown(actual));
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Tome_of_fire")
    @Test
    @DisplayName("A tome multiplies on top of the magic damage bonus")
    void tomeMultipliesOnTop()
    {
        skill(Skill.MAGIC, MAGIC_LEVEL);

        double actual = spellbook().calculateMagicMaxHit(
                equipment().shield("Tome of fire").build(), CombatSpell.FIRE_BOLT);

        // the tome gives 10% against NPCs, cut from 50% in the 05/29/24 rebalance
        assertEquals(13, shown(actual));
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Elemental_weakness")
    @Test
    @DisplayName("A weak NPC adds its elemental bonus from the base hit")
    void elementalWeaknessAddsFromBaseHit()
    {
        skill(Skill.MAGIC, MAGIC_LEVEL);
        selectedNPC("Albino Bat"); // Air 40%

        double plain = spellbook().calculateMagicMaxHit(equipment().build(), CombatSpell.FIRE_BOLT);
        double weakTo = spellbook().calculateMagicMaxHit(equipment().build(), CombatSpell.WIND_BOLT);

        assertEquals(12, shown(plain));          // fire is not its weakness
        assertEquals(16, shown(weakTo));         // 12 plus 40% of the base hit
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Twinflame_staff")
    @Test
    @DisplayName("Twinflame staff adds a second hit on the standard spellbook")
    void twinflameStaffAddsASecondHit()
    {
        skill(Skill.MAGIC, MAGIC_LEVEL);

        double bolt = spellbook().calculateMagicMaxHit(
                equipment().weapon("Twinflame staff").build(), CombatSpell.FIRE_BOLT);
        double strike = spellbook().calculateMagicMaxHit(
                equipment().weapon("Twinflame staff").build(), CombatSpell.FIRE_STRIKE);

        // the second hit is 40% of the first rounded down, and strikes and surges are excluded
        assertEquals(16, shown(bolt));
        assertEquals(8, shown(strike));
    }
}
