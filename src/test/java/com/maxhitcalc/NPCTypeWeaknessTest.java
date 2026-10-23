/* NPCTypeWeaknessTest.java
 * Structural checks on the elemental weakness table.
 *
 * Copyright (c) 2023, Jacob Burton <https://github.com/j-cob44>
 * All rights reserved.
 */

package com.maxhitcalc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * These guard the shape of the table rather than individual weakness values, so they keep holding
 * as the list is updated from the wiki.
 */
@DataSource.Derived("the weakness table's shape, never its values")
class NPCTypeWeaknessTest
{
    @Test
    @DisplayName("No two entries share an NPC name")
    void noDuplicateNames()
    {
        Map<String, NPCTypeWeakness> seen = new HashMap<>();
        List<String> duplicates = new ArrayList<>();

        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            NPCTypeWeakness previous = seen.put(npc.getNPCName().toLowerCase(), npc);
            if (previous != null)
            {
                duplicates.add(previous.name() + " and " + npc.name() + " both named \"" + npc.getNPCName() + "\"");
            }
        }

        assertTrue(duplicates.isEmpty(), "Duplicate NPC names: " + duplicates);
    }

    @Test
    @DisplayName("Every entry is reachable by its own name")
    void everyEntryResolvesToItself()
    {
        List<String> unreachable = new ArrayList<>();

        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            if (npc == NPCTypeWeakness.None) continue;

            NPCTypeWeakness found = NPCTypeWeakness.findWeaknessByName(npc.getNPCName());
            if (found != npc)
            {
                unreachable.add(npc.name() + " (\"" + npc.getNPCName() + "\") resolved to "
                        + (found == null ? "null" : found.name()));
            }
        }

        assertTrue(unreachable.isEmpty(), "Entries shadowed by an earlier entry: " + unreachable);
    }

    @Test
    @DisplayName("Prefix names allow partial search")
    void prefixNamesAllowPartialSearch()
    {
        List<String> broken = new ArrayList<>();

        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            if (npc.getNPCName().endsWith("-") && !npc.isAllowPartialSearch())
            {
                broken.add(npc.name() + " (\"" + npc.getNPCName() + "\")");
            }
        }

        assertTrue(broken.isEmpty(), "Prefix names that can never match an NPC: " + broken);
    }

    @DisplayName("Name lookup is case insensitive")
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"vorkath", "VORKATH", "VoRkAtH"})
    void lookupIsCaseInsensitive(String name)
    {
        assertSame(NPCTypeWeakness.Vorkath, NPCTypeWeakness.findWeaknessByName(name));
    }

    static Stream<Arguments> familyVariants()
    {
        // Variants that only resolve through an entry's partial search
        return Stream.of(
            Arguments.of("Revenant imp"),
            Arguments.of("Revenant dragon"),
            Arguments.of("Undead druid"),
            Arguments.of("Undead chicken"),
            Arguments.of("TzKal-Zuk"),
            Arguments.of("TzHaar-Ket"),
            Arguments.of("Jal-Nib"),
            Arguments.of("Kalphite Queen"),
            Arguments.of("Nylocas Vasilias"),
            Arguments.of("The Jormungand")
        );
    }

    @DisplayName("Family variants resolve through partial search")
    @ParameterizedTest(name = "{0}")
    @MethodSource("familyVariants")
    void familyVariantsResolve(String npcName)
    {
        assertNotNull(NPCTypeWeakness.findWeaknessByName(npcName), npcName + " did not resolve");
    }

    @Test
    @DisplayName("Every entry carries a weakness or flat armour")
    void everyEntryCarriesAWeaknessOrFlatArmour()
    {
        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            if (npc == NPCTypeWeakness.None)
            {
                assertEquals(SpellType.NoType, npc.getElementalWeakness());
                assertEquals(0, npc.getWeaknessPercent());
                assertEquals(0, npc.getFlatArmour());
                continue;
            }

            boolean hasWeakness = npc.getElementalWeakness() != SpellType.NoType && npc.getWeaknessPercent() > 0;

            assertTrue(hasWeakness || npc.getFlatArmour() != 0,
                    npc.name() + " carries neither an elemental weakness nor flat armour");
        }
    }

    @Test
    @DisplayName("An element and a percent are set together or not at all")
    void elementAndPercentAreSetTogether()
    {
        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            boolean hasElement = npc.getElementalWeakness() != SpellType.NoType;
            boolean hasPercent = npc.getWeaknessPercent() > 0;

            assertEquals(hasElement, hasPercent,
                    npc.name() + " has element=" + npc.getElementalWeakness()
                            + " but percent=" + npc.getWeaknessPercent());
        }
    }

    @Test
    @DisplayName("Names are trimmed and non empty")
    void namesAreTrimmedAndNonEmpty()
    {
        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            String name = npc.getNPCName();
            assertTrue(name != null && !name.isEmpty(), npc.name() + " has no name");
            assertEquals(name.trim(), name, npc.name() + " has padded whitespace");
        }
    }

    @Test
    @DisplayName("Unknown and null names do not resolve")
    void unknownAndNullNamesDoNotResolve()
    {
        assertNull(NPCTypeWeakness.findWeaknessByName(null));
        assertNull(NPCTypeWeakness.findWeaknessByName("Not A Real Monster"));
        assertNull(NPCTypeWeakness.findWeaknessByName(""));
    }
}
