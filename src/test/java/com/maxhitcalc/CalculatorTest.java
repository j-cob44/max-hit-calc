/* CalculatorTest.java
 * Shared harness for the weapon case tests.
 *
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

import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.EnumID;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ParamID;
import net.runelite.api.StructComposition;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Base class for tests that drive the calculators with a fake equipped player.
 * <p>
 * To cover a new weapon, add a row to the relevant subclass's {@code @MethodSource} table. To cover
 * a new kind of game state, add a setter here.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
abstract class CalculatorTest
{
    private static final int EQUIPMENT_SLOT_COUNT = 14;

    @Mock
    protected Client client;

    @Mock
    protected MaxHitCalcConfig config;

    @Mock
    protected ItemManager itemManager;

    // Real instance so public state like NPCSize keeps its field default
    protected final MaxHitCalcPlugin plugin = new MaxHitCalcPlugin();

    private final Map<String, Integer> itemIds = new HashMap<>();
    private int nextItemId = 1;

    protected MaxHit maxHit()
    {
        return new MaxHit(plugin, config, itemManager, client);
    }

    protected MaxSpec maxSpec()
    {
        return new MaxSpec(plugin, config, itemManager, client);
    }

    protected MaxAgainstType maxAgainstType()
    {
        return new MaxAgainstType(plugin, config, itemManager, client);
    }

    protected SpellbookSpellMaxHit spellbookSpellMaxHit()
    {
        return new SpellbookSpellMaxHit(plugin, config, itemManager, client);
    }

    protected InventoryItemMaxHit inventoryItemMaxHit()
    {
        return new InventoryItemMaxHit(plugin, config, itemManager, client);
    }

    protected void npcSize(int size)
    {
        plugin.NPCSize = size;
    }

    protected void selectedNPC(String npcName)
    {
        plugin.selectedNPCName = npcName;
    }

    protected void itemStats(String gameName, int meleeStrength, int rangedStrength, float magicDamage)
    {
        ItemStats stats = new ItemStats(true, 0, 0, ItemEquipmentStats.builder()
                .str(meleeStrength)
                .rstr(rangedStrength)
                .mdmg(magicDamage)
                .build());

        when(itemManager.getItemStats(item(gameName))).thenReturn(stats);
    }

    protected void blowpipeDart(BlowpipeDartType dartType)
    {
        plugin.selectedDartType = dartType;
    }

    /** Style names must be ones AttackStyle can parse, so "Defensive" not "Defensive Casting" */
    protected void weaponAttackStyles(int weaponType, int selectedStyleIndex, String... styleNames)
    {
        varbit(VarbitID.COMBAT_WEAPON_CATEGORY, weaponType);
        varp(VarPlayerID.COM_MODE, selectedStyleIndex);

        int styleEnumId = 9000;
        int[] structIds = new int[styleNames.length];

        for (int i = 0; i < styleNames.length; i++)
        {
            structIds[i] = 9100 + i;

            StructComposition struct = mock(StructComposition.class);
            when(struct.getStringValue(ParamID.ATTACK_STYLE_NAME)).thenReturn(styleNames[i]);
            when(client.getStructComposition(structIds[i])).thenReturn(struct);
        }

        EnumComposition weaponStyles = mock(EnumComposition.class);
        when(weaponStyles.getIntValue(weaponType)).thenReturn(styleEnumId);
        when(client.getEnum(EnumID.WEAPON_STYLES)).thenReturn(weaponStyles);

        EnumComposition styleEnum = mock(EnumComposition.class);
        when(styleEnum.getIntVals()).thenReturn(structIds);
        when(client.getEnum(styleEnumId)).thenReturn(styleEnum);
    }

    protected void aggressiveMeleeWeapon()
    {
        weaponAttackStyles(0, 0, "Aggressive", "Accurate", "Defensive");
    }

    /** Inquisitor's armour only applies on crush */
    protected void crushAttackStyle(int attackStyleID)
    {
        varp(VarPlayerID.COM_MODE, attackStyleID);

        Widget styleWidget = mock(Widget.class);
        when(styleWidget.getSpriteId()).thenReturn(SpriteID.COMBAT_STYLE_MACE_PUMMEL);
        when(client.getWidget(anyInt())).thenReturn(styleWidget);
    }

    protected int item(String gameName)
    {
        Integer existing = itemIds.get(gameName);
        if (existing != null)
        {
            return existing;
        }

        int id = nextItemId++;
        ItemComposition composition = mock(ItemComposition.class);
        when(composition.getName()).thenReturn(gameName);
        when(client.getItemDefinition(id)).thenReturn(composition);
        itemIds.put(gameName, id);

        return id;
    }

    protected void skill(Skill skill, int level)
    {
        skill(skill, level, level);
    }

    /** Separate real and boosted levels, for drain and boost effects like Dharok's */
    protected void skill(Skill skill, int realLevel, int boostedLevel)
    {
        when(client.getRealSkillLevel(skill)).thenReturn(realLevel);
        when(client.getBoostedSkillLevel(skill)).thenReturn(boostedLevel);
    }

    protected void varbit(int varbitId, int value)
    {
        when(client.getVarbitValue(varbitId)).thenReturn(value);
    }

    protected void varp(int varpId, int value)
    {
        when(client.getVarpValue(varpId)).thenReturn(value);
    }

    protected void maxedCombatStats()
    {
        skill(Skill.MAGIC, 99);
        skill(Skill.RANGED, 99);
        skill(Skill.STRENGTH, 99);
        skill(Skill.ATTACK, 99);
        skill(Skill.HITPOINTS, 99);
        skill(Skill.PRAYER, 99);
    }

    protected Equipment equipment()
    {
        return new Equipment();
    }

    // Fluent builder for an equipped item set, unset slots read back as an empty name
    protected class Equipment
    {
        private final Item[] items = new Item[EQUIPMENT_SLOT_COUNT];

        Equipment slot(EquipmentInventorySlot slot, String gameName)
        {
            items[slot.getSlotIdx()] = new Item(item(gameName), 1);
            return this;
        }

        Equipment weapon(String gameName)
        {
            return slot(EquipmentInventorySlot.WEAPON, gameName);
        }

        Equipment ammo(String gameName)
        {
            return slot(EquipmentInventorySlot.AMMO, gameName);
        }

        Equipment head(String gameName)
        {
            return slot(EquipmentInventorySlot.HEAD, gameName);
        }

        Equipment body(String gameName)
        {
            return slot(EquipmentInventorySlot.BODY, gameName);
        }

        Equipment legs(String gameName)
        {
            return slot(EquipmentInventorySlot.LEGS, gameName);
        }

        Equipment amulet(String gameName)
        {
            return slot(EquipmentInventorySlot.AMULET, gameName);
        }

        Equipment cape(String gameName)
        {
            return slot(EquipmentInventorySlot.CAPE, gameName);
        }

        Equipment gloves(String gameName)
        {
            return slot(EquipmentInventorySlot.GLOVES, gameName);
        }

        Equipment shield(String gameName)
        {
            return slot(EquipmentInventorySlot.SHIELD, gameName);
        }

        Equipment ring(String gameName)
        {
            return slot(EquipmentInventorySlot.RING, gameName);
        }

        Item[] build()
        {
            return items;
        }

        /** Same as build, but also publishes the set as the worn item container */
        Item[] wear()
        {
            ItemContainer worn = mock(ItemContainer.class);
            when(worn.getItems()).thenReturn(items);
            when(client.getItemContainer(InventoryID.WORN)).thenReturn(worn);
            return items;
        }
    }
}
