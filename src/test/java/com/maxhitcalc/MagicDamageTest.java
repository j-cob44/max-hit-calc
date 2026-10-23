/* MagicDamageTest.java
 * Covers the tome and magic damage bonuses that are matched on item names.
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
import net.runelite.api.gameval.VarbitID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MagicDamageTest extends CalculatorTest
{
    private static final double DELTA = 1e-8;

    private void autocast(CombatSpell spell)
    {
        varbit(VarbitID.AUTOCAST_SPELL, spell.getAutocastVarbitValue());
    }

    static Stream<Arguments> matchingTomes()
    {
        return Stream.of(
            // tome, a spell it boosts
            Arguments.of("Tome of fire", CombatSpell.FIRE_BOLT),
            Arguments.of("Tome of water", CombatSpell.WATER_BOLT),
            Arguments.of("Tome of earth", CombatSpell.EARTH_BOLT)
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Tome_of_fire",
        "https://oldschool.runescape.wiki/w/Tome_of_water",
        "https://oldschool.runescape.wiki/w/Tome_of_earth"
    })
    @DisplayName("A charged tome boosts its own element by 10%")
    @ParameterizedTest(name = "{0} with {1}")
    @MethodSource("matchingTomes")
    void chargedTomeBoostsItsElement(String tomeName, CombatSpell spell)
    {
        autocast(spell);

        double actual = maxHit().getTomeSpellBonus(
                equipment().weapon("Ancient staff").shield(tomeName).build(), AttackStyle.CASTING);

        assertEquals(1.1, actual, DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Tome_of_fire")
    @DisplayName("An empty tome gives no bonus")
    @ParameterizedTest(name = "{0} (empty)")
    @MethodSource("matchingTomes")
    void emptyTomeGivesNoBonus(String tomeName, CombatSpell spell)
    {
        autocast(spell);

        double actual = maxHit().getTomeSpellBonus(
                equipment().weapon("Ancient staff").shield(tomeName + " (empty)").build(), AttackStyle.CASTING);

        assertEquals(1, actual, DELTA);
    }

    static Stream<Arguments> mismatchedTomes()
    {
        return Stream.of(
            Arguments.of("Tome of fire", CombatSpell.WATER_BOLT),
            Arguments.of("Tome of water", CombatSpell.EARTH_BOLT),
            Arguments.of("Tome of earth", CombatSpell.FIRE_BOLT),
            Arguments.of("Tome of fire", CombatSpell.WIND_BOLT)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Tome_of_fire")
    @DisplayName("A tome does nothing for another element")
    @ParameterizedTest(name = "{0} with {1}")
    @MethodSource("mismatchedTomes")
    void tomeDoesNothingForAnotherElement(String tomeName, CombatSpell spell)
    {
        autocast(spell);

        double actual = maxHit().getTomeSpellBonus(
                equipment().weapon("Ancient staff").shield(tomeName).build(), AttackStyle.CASTING);

        assertEquals(1, actual, DELTA);
    }

    @DataSource.Derived("no spell selected, so no element to match")
    @Test
    @DisplayName("With no spell selected a tome gives no bonus")
    void noSpellMeansNoTomeBonus()
    {
        varbit(VarbitID.AUTOCAST_SPELL, 0);

        double actual = maxHit().getTomeSpellBonus(
                equipment().weapon("Ancient staff").shield("Tome of fire").build(), AttackStyle.CASTING);

        assertEquals(1, actual, DELTA);
    }

    @DataSource.Derived("made up stats, only the summing is under test")
    @Test
    @DisplayName("Magic damage is summed from the equipment stats")
    void magicDamageIsSummedFromStats()
    {
        itemStats("Occult necklace", 0, 0, 10);
        itemStats("Tormented bracelet", 0, 0, 5);

        double actual = maxHit().getMagicEquipmentBoost(
                equipment().amulet("Occult necklace").gloves("Tormented bracelet").build());

        assertEquals(1.15, actual, DELTA);
    }

    static Stream<Arguments> ancientSpells()
    {
        return Stream.of(
            Arguments.of(CombatSpell.SMOKE_RUSH),
            Arguments.of(CombatSpell.SHADOW_RUSH),
            Arguments.of(CombatSpell.BLOOD_RUSH),
            Arguments.of(CombatSpell.ICE_RUSH)
        );
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Virtus_robes")
    @DisplayName("Virtus adds 3% a piece for ancient spells")
    @ParameterizedTest(name = "{0}")
    @MethodSource("ancientSpells")
    void virtusBoostsAncientSpells(CombatSpell spell)
    {
        autocast(spell);

        Equipment worn = equipment()
                .head("Virtus mask")
                .body("Virtus robe top")
                .legs("Virtus robe bottom");

        assertEquals(1.09, maxHit().getMagicEquipmentBoost(worn.build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Virtus_robes")
    @Test
    @DisplayName("Virtus does nothing for a standard spell")
    void virtusDoesNothingForStandardSpells()
    {
        autocast(CombatSpell.FIRE_BOLT);

        Equipment worn = equipment()
                .head("Virtus mask")
                .body("Virtus robe top")
                .legs("Virtus robe bottom");

        assertEquals(1, maxHit().getMagicEquipmentBoost(worn.build()), DELTA);
    }

    static Stream<Arguments> standardSpellbookStaves()
    {
        return Stream.of(
            Arguments.of("Smoke battlestaff"),
            Arguments.of("Mystic smoke staff"),
            Arguments.of("Twinflame staff")
        );
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Smoke_battlestaff",
        "https://oldschool.runescape.wiki/w/Twinflame_staff"
    })
    @DisplayName("Smoke and twinflame staves add 10% on the standard spellbook")
    @ParameterizedTest(name = "{0}")
    @MethodSource("standardSpellbookStaves")
    void standardSpellbookStavesAddTenPercent(String weaponName)
    {
        autocast(CombatSpell.FIRE_BOLT);

        assertEquals(1.1, maxHit().getMagicEquipmentBoost(equipment().weapon(weaponName).build()), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Smoke_battlestaff",
        "https://oldschool.runescape.wiki/w/Twinflame_staff"
    })
    @DisplayName("Those staves do nothing on the ancient spellbook")
    @ParameterizedTest(name = "{0}")
    @MethodSource("standardSpellbookStaves")
    void standardSpellbookStavesDoNothingOnAncients(String weaponName)
    {
        autocast(CombatSpell.SMOKE_RUSH);

        assertEquals(1, maxHit().getMagicEquipmentBoost(equipment().weapon(weaponName).build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Tumeken's_shadow")
    @Test
    @DisplayName("Tumeken's shadow triples the gear bonus, capped at 100%")
    void tumekensShadowTriplesGearBonus()
    {
        itemStats("Occult necklace", 0, 0, 10);

        Equipment worn = equipment().weapon("Tumeken's shadow").amulet("Occult necklace");
        assertEquals(1.3, maxHit().getMagicEquipmentBoost(worn.build()), DELTA);

        // 50% of gear would triple past the cap
        itemStats("Occult necklace", 0, 0, 50);
        assertEquals(2, maxHit().getMagicEquipmentBoost(worn.build()), DELTA);
    }

    @DataSource.Wiki("https://oldschool.runescape.wiki/w/Augury")
    @Test
    @DisplayName("Augury adds 4%")
    void auguryAddsFourPercent()
    {
        varbit(VarbitID.PRAYER_AUGURY, 1);

        assertEquals(1.04, maxHit().getMagicEquipmentBoost(equipment().build()), DELTA);
    }

    @DataSource.Derived("nothing equipped, so nothing to sum")
    @Test
    @DisplayName("No gear and no prayer leaves the bonus at 1")
    void noGearLeavesBonusAtOne()
    {
        assertEquals(1, maxHit().getMagicEquipmentBoost(equipment().build()), DELTA);
        assertEquals(1, maxHit().getMagicEquipmentBoost(null), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Elemental_amulet",
        "https://oldschool.runescape.wiki/w/Maximum_magic_hit"
    })
    @Test
    @DisplayName("An elemental amulet's 2 is scaled by magic damage")
    void elementalAmuletScalesWithMagicDamage()
    {
        maxedCombatStats();
        autocast(CombatSpell.FIRE_BOLT);
        itemStats("Tormented bracelet", 0, 0, 50);

        double withAmulet = maxHit().calculateMagicMaxHit(
            equipment().amulet("Amulet of fire").gloves("Tormented bracelet").build(), AttackStyle.CASTING);
        double without = maxHit().calculateMagicMaxHit(
            equipment().gloves("Tormented bracelet").build(), AttackStyle.CASTING);

        // (12 + 2) * 1.5 against 12 * 1.5, so the 2 is worth 3 here
        assertEquals(21, withAmulet, DELTA);
        assertEquals(18, without, DELTA);
    }

    @DataSource.InGame("10/23/26")
    @Test
    @DisplayName("Fire strike with an amulet of fire on an ice warrior, confirmed in game")
    void amuletOfFireOnAnIceWarrior()
    {
        skill(Skill.MAGIC, 91);
        autocast(CombatSpell.FIRE_STRIKE);
        selectedNPC("Ice Warrior");

        // the strike tier caps at 8, +2 from the amulet, doubled by the 100% weakness
        // measured in game 10/23/26, a flat +2 would read 18 if this is ever rebalanced
        assertEquals(20, maxHit().calculateMagicMaxHit(
            equipment().amulet("Amulet of fire").build(), AttackStyle.CASTING), DELTA);
    }

    @DataSource.Wiki({
        "https://oldschool.runescape.wiki/w/Elemental_amulet",
        "https://oldschool.runescape.wiki/w/Elemental_weakness"
    })
    @Test
    @DisplayName("An elemental amulet's 2 is scaled by the type weakness")
    void elementalAmuletScalesWithTypeWeakness()
    {
        maxedCombatStats();
        autocast(CombatSpell.FIRE_BOLT);
        selectedNPC("Abyssal Portal"); // weak to fire by 50%

        double withAmulet = maxHit().calculateMagicMaxHit(
            equipment().amulet("Amulet of fire").build(), AttackStyle.CASTING);
        double without = maxHit().calculateMagicMaxHit(equipment().build(), AttackStyle.CASTING);

        // 14 + 14 * 0.5 against 12 + 12 * 0.5
        assertEquals(21, withAmulet, DELTA);
        assertEquals(18, without, DELTA);
    }
}
