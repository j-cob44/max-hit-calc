/* StatChangedMaxHitTest.java
 * Covers the max hit preview shown when hovering a boosting item.
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
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.itemstats.stats.Stats;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This path recalculates the max hit using the boosted level an item would give, rather than the
 * player's current level.
 */
@DataSource.Derived("formulas covered elsewhere, re-run at a boosted level")
class StatChangedMaxHitTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    private StatChangedMaxHit statChanged()
    {
        return new StatChangedMaxHit(plugin, config, itemManager, client);
    }

    private static StatChange change(Stat stat, int absolute)
    {
        StatChange statChange = new StatChange();
        statChange.setStat(stat);
        statChange.setAbsolute(absolute);
        return statChange;
    }

    private static StatChange[] changes(StatChange... all)
    {
        return all;
    }

    static Stream<Arguments> presentSkills()
    {
        return Stream.of(
            Arguments.of(Skill.STRENGTH, true),
            Arguments.of(Skill.RANGED, false),
            Arguments.of(Skill.MAGIC, false)
        );
    }

    @DisplayName("A boost is recognised for the skill it changes")
    @ParameterizedTest(name = "{0} present: {1}")
    @MethodSource("presentSkills")
    void boostIsRecognisedForItsSkill(Skill skill, boolean expected)
    {
        StatChange[] boost = changes(change(Stats.STRENGTH, 118));

        assertEquals(expected, statChanged().checkIfStatChanged(boost, skill));
    }

    @Test
    @DisplayName("The boosted value is read back for the right skill")
    void boostedValueIsReadBack()
    {
        StatChange[] boost = changes(
                change(Stats.STRENGTH, 118),
                change(Stats.ATTACK, 112));

        assertEquals(118, statChanged().getChangedStatValue(boost, Skill.STRENGTH));
        assertEquals(112, statChanged().getChangedStatValue(boost, Skill.ATTACK));
    }

    @Test
    @DisplayName("A skill with no boost reads back as 0")
    void unboostedSkillReadsZero()
    {
        StatChange[] boost = changes(change(Stats.STRENGTH, 118));

        assertEquals(0, statChanged().getChangedStatValue(boost, Skill.RANGED));
        assertFalse(statChanged().checkIfStatChanged(boost, Skill.RANGED));
    }

    @Test
    @DisplayName("An empty boost changes nothing")
    void emptyBoostChangesNothing()
    {
        StatChange[] none = changes();

        assertFalse(statChanged().checkIfStatChanged(none, Skill.STRENGTH));
        assertEquals(0, statChanged().getChangedStatValue(none, Skill.STRENGTH));
    }

    @Test
    @DisplayName("A strength boost raises the predicted melee max hit")
    void strengthBoostRaisesMeleeMax()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        equipment().wear();

        // 118 strength gives floor((118 + 3 + 8) * 1) = 129 effective, so floor(0.5 + 129 * 0.1) = 13
        double boosted = statChanged().predict(changes(change(Stats.STRENGTH, 118)));

        assertEquals(13, boosted, DELTA);
    }

    @Test
    @DisplayName("A boost to an unrelated skill leaves the melee max alone")
    void unrelatedBoostLeavesMaxAlone()
    {
        skill(Skill.STRENGTH, 99);
        aggressiveMeleeWeapon();
        equipment().wear();

        double unchanged = statChanged().predict(changes(change(Stats.FISHING, 99)));

        assertEquals(11, unchanged, DELTA);
    }

    @Test
    @DisplayName("A ranged boost raises the predicted ranged max hit")
    void rangedBoostRaisesRangedMax()
    {
        skill(Skill.RANGED, 99);
        weaponAttackStyles(0, 0, "Ranging", "Ranging", "Longrange");
        equipment().wear();

        double boosted = statChanged().predict(changes(change(Stats.RANGED, 112)));

        // effective ranged is 112 + 3 + 8 = 123, so the base is floor(0.5 + 123 * 64 / 640)
        assertEquals(12, boosted, DELTA);
    }

    @Test
    @DisplayName("Dharok's uses the boosted hitpoints")
    void dharoksUsesBoostedHitpoints()
    {
        skill(Skill.STRENGTH, 99);
        skill(Skill.HITPOINTS, 99, 99);
        aggressiveMeleeWeapon();
        equipment()
                .head("Dharok's helm")
                .body("Dharok's platebody")
                .legs("Dharok's platelegs")
                .weapon("Dharok's greataxe")
                .wear();

        // at full hitpoints the set adds nothing, so this is the plain max
        double atFullHp = statChanged().predict(changes(change(Stats.HITPOINTS, 99)));

        assertEquals(11, atFullHp, DELTA);
    }
}
