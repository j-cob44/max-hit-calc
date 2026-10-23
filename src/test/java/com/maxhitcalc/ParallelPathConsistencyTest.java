/* ParallelPathConsistencyTest.java
 * Checks that the duplicated calculators agree with each other.
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

import net.runelite.api.Item;
import net.runelite.api.Skill;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.plugins.itemstats.StatChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The max hit is calculated in several places that each keep their own copy of the weapon cases.
 * These tests assert the copies agree, so a bonus added to one and missed in another fails here
 * rather than showing a wrong number in one panel row.
 * <p>
 * To cover a new bonus, add a row to the relevant table. Cases where the paths are meant to differ
 * belong in that path's own test instead.
 */
@DataSource.Derived("that the duplicated calculators agree with each other")
class ParallelPathConsistencyTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    private static final double MAGIC_LEVEL = 99;

    /** No boosts active, so the stat changed path should match the plain one */
    private static final StatChange[] NO_CHANGES = new StatChange[0];

    private StatChangedMaxHit statChanged()
    {
        return new StatChangedMaxHit(plugin, config, itemManager, client);
    }

    static Stream<Arguments> meleeSpecialBonusSetups()
    {
        return Stream.of(
            Arguments.of("Full obsidian and a berserker necklace", "Toktz-xil-ak", true, true),
            Arguments.of("Full obsidian only", "Toktz-xil-ak", true, false),
            Arguments.of("Berserker necklace only", "Toktz-xil-ak", false, true),
            Arguments.of("Obsidian armour without an obsidian weapon", "Abyssal whip", true, true),
            Arguments.of("Obsidian rings are not a melee weapon", "Toktz-xil-ul", true, true),
            Arguments.of("Nothing relevant", "Abyssal whip", false, false)
        );
    }

    @DisplayName("MaxHit and StatChangedMaxHit agree on the melee special bonus")
    @ParameterizedTest(name = "{0}")
    @MethodSource("meleeSpecialBonusSetups")
    void meleeSpecialBonusMatches(String label, String weaponName, boolean obsidianArmour, boolean necklace)
    {
        maxedCombatStats();

        Equipment worn = equipment().weapon(weaponName);
        if (obsidianArmour)
        {
            worn.head("Obsidian helmet").body("Obsidian platebody").legs("Obsidian platelegs");
        }
        if (necklace)
        {
            worn.amulet("Berserker necklace");
        }

        Item[] built = worn.build();

        assertEquals(
            maxHit().getMeleeSpecialBonusMultiplier(built),
            statChanged().getMeleeSpecialBonusMultiplier(built, NO_CHANGES),
            label);
    }

    static Stream<Arguments> meleeFlatAdjustmentWeapons()
    {
        return Stream.of(
            Arguments.of("Osmumten's fang"),
            Arguments.of("Colossal blade"),
            Arguments.of("Bone mace"),
            Arguments.of("Abyssal whip")
        );
    }

    @DisplayName("MaxHit and StatChangedMaxHit agree on the melee max hit")
    @ParameterizedTest(name = "{0}")
    @MethodSource("meleeFlatAdjustmentWeapons")
    void meleeMaxHitMatches(String weaponName)
    {
        maxedCombatStats();
        itemStats(weaponName, 100, 0, 0);

        Item[] built = equipment().weapon(weaponName).build();

        assertEquals(
            maxHit().calculateMeleeMaxHit(built, AttackStyle.AGGRESSIVE, 0, false),
            statChanged().calculateMeleeMaxHit(built, AttackStyle.AGGRESSIVE, 0, false, NO_CHANGES),
            DELTA);
    }

    static Stream<Arguments> spellBaseHitSetups()
    {
        return Stream.of(
            // label, autocast spell, gloves, amulet
            Arguments.of("Plain fire bolt", CombatSpell.FIRE_BOLT, null, null),
            Arguments.of("Chaos gauntlets on a bolt", CombatSpell.FIRE_BOLT, "Chaos gauntlets", null),
            Arguments.of("Chaos gauntlets on a wave", CombatSpell.FIRE_WAVE, "Chaos gauntlets", null),
            Arguments.of("Matching elemental amulet", CombatSpell.FIRE_BOLT, null, "Amulet of fire"),
            Arguments.of("Mismatched elemental amulet", CombatSpell.FIRE_BOLT, null, "Amulet of air"),
            Arguments.of("Elemental amulet", CombatSpell.WIND_BOLT, null, "Elemental amulet"),
            Arguments.of("Both at once", CombatSpell.FIRE_BOLT, "Chaos gauntlets", "Amulet of fire")
        );
    }

    @DisplayName("All three spell paths agree on the base hit")
    @ParameterizedTest(name = "{0}")
    @MethodSource("spellBaseHitSetups")
    void spellBaseHitMatches(String label, CombatSpell spell, String gloves, String amulet)
    {
        maxedCombatStats();
        varbit(VarbitID.AUTOCAST_SPELL, spell.getAutocastVarbitValue());

        Equipment worn = equipment();
        if (gloves != null) worn.gloves(gloves);
        if (amulet != null) worn.amulet(amulet);

        Item[] built = worn.build();

        double plain = maxHit().getSpellBaseHit(built, AttackStyle.CASTING, MAGIC_LEVEL);
        double againstType = maxAgainstType().getSpellBaseHit(built, AttackStyle.CASTING);
        double spellbook = spellbookSpellMaxHit().getSpellBaseHit(built, (int) MAGIC_LEVEL, spell);

        assertEquals(plain, againstType, DELTA, label + ", vs type path");
        assertEquals(plain, spellbook, DELTA, label + ", spellbook path");
    }

    @DisplayName("MaxHit and StatChangedMaxHit agree on the ranged max hit")
    @ParameterizedTest(name = "{0}")
    @MethodSource("rangedWeapons")
    void rangedMaxHitMatches(String weaponName)
    {
        skill(Skill.RANGED, 99);
        itemStats(weaponName, 0, 85, 0);

        Item[] built = equipment().weapon(weaponName).head("Crystal helm").build();

        assertEquals(
            maxHit().calculateRangedMaxHit(built, AttackStyle.RANGING, 0),
            statChanged().calculateRangedMaxHit(built, AttackStyle.RANGING, 0, NO_CHANGES),
            DELTA);
    }

    static Stream<Arguments> rangedWeapons()
    {
        return Stream.of(
            Arguments.of("Bow of faerdhinen (c)"),
            Arguments.of("Eclipse atlatl"),
            Arguments.of("Bone shortbow"),
            Arguments.of("Magic shortbow")
        );
    }
}
