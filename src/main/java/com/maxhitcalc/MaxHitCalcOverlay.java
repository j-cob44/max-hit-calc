/* MaxHitCalcOverlay.java
 * Code for plugin main display panel and tooltip.
 *
 *
 * Copyright (c) 2023, Jacob Burton <https://github.com/j-cob44>
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

import net.runelite.api.*;
import net.runelite.api.Menu;
import net.runelite.api.widgets.*;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.game.ItemStats;
import net.runelite.client.game.ItemManager;
import javax.inject.Inject;
import java.awt.*;
import java.util.List;

public class MaxHitCalcOverlay extends OverlayPanel
{
    private final MaxHitCalcPlugin plugin;
    private final MaxHitCalcConfig config;
    @Inject
    private TooltipManager tooltipManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private Client client;

    @Inject
    MaxHitCalcOverlay(MaxHitCalcPlugin plugin, MaxHitCalcConfig config)
    {
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        setLayer(OverlayLayer.ABOVE_SCENE);

        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();

        // Don't Display if 0, or -1 (error)
        if(plugin.maxHit > 0 && config.showMaxHit())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Max Hit:")
                    .right(Integer.toString(plugin.maxHit))
                    .build());
        }

        // Don't Display if 0 (not useful) or turned off
        if(plugin.maxSpec > 0 && config.showSpec())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Max Spec Hit:")
                    .right(Integer.toString(plugin.maxSpec))
                    .build());
        }

        // Don't Display if 0 (not useful) or turned off
        if(plugin.maxVsType > 0 && config.showType())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Max Hit vs Type:")
                    .right(Integer.toString(plugin.maxVsType))
                    .build());
        }

        // Don't Display if 0 (not useful) or turned off
        if(plugin.maxSpecVsType > 0 && config.showSpecVsType())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Max Spec vs Type:")
                    .right(Integer.toString(plugin.maxSpecVsType))
                    .build());
        }

        // Tooltip for Prediction
        if (config.showPredictionTooltip())
        {
            // Check for mouse
            if(this.getBounds().contains(
                client.getMouseCanvasPosition().getX(),
                client.getMouseCanvasPosition().getY()))
            {
                String tooltipString = predictedMaxHitTooltip();

                // Check if useful
                if(tooltipString != null)
                {
                    // Display tooltip
                    tooltipManager.add(new Tooltip(tooltipString));
                }
            }
        }

        // Bank & Inventory Item Tooltip
        if (config.showInventoryTooltip() || config.showBankTooltip())
        {
            getInventoryMaxHitTooltip(plugin.maxHit);
        }

        // Spellbook Spell Tooltip
        if (config.showSpellbookTooltip())
        {
            getSpellbookMaxHitTooltip();
        }

        // Autocast Selection Tooltip
        if(config.showAutocastSelectionTooltip())
        {
            getAutocastSelectionMaxHitTooltip();
        }

        return super.render(graphics);
    }

    private String predictedMaxHitTooltip()
    {
        PredictNextMax predictNextMaxes = new PredictNextMax(plugin, config, itemManager, client);
        List<Object> prediction = predictNextMaxes.predict();

        String result = "Next Max Hit at: </br>";

        if (prediction.get(0).equals("melee"))
        {
            result += prediction.get(1) + " Strength Levels </br>" + prediction.get(2) + " Strength Bonus </br>" + (int)((double)prediction.get(3) * 100) + "% Prayer Bonus";
            return result;
        }
        else if (prediction.get(0).equals("ranged"))
        {
            result += prediction.get(1) + " Ranged Levels </br>" + prediction.get(2) + " Ranged Strength Bonus </br>" + (int)((double)prediction.get(3) * 100) + "% Prayer Bonus";
            return result;
        }
        else if (prediction.get(0).equals("magic"))
        {
            // Magic Level not always used for max hit
            if (!prediction.get(1).equals(0))
            {
                result += prediction.get(1) + " Magic Levels </br>";
            }

            // Magic Damage Bonus always used for max hit
            result += (int)((double)prediction.get(2) * 100) + "% Magic Damage Bonus";

            return result;
        }
        else
        {
            return null;
        }
    }

    private void getInventoryMaxHitTooltip(int maxHit){
        // Tooltip on item in inventory
        Menu clientMenu = client.getMenu();
        MenuEntry[] menu = clientMenu.getMenuEntries();
        int menuSize = menu.length;
        if (menuSize == 0)
        {
            return;
        }

        // Get Inventory
        MenuEntry entry = menu[menuSize - 1];
        Widget widget = entry.getWidget();
        if (widget == null)
        {
            return;
        }

        // Get Hovered Item
        int itemID = -1;
        // Check if inventory item
        if(config.showInventoryTooltip())
        {
            if (WidgetUtil.componentToInterface(widget.getId()) == InterfaceID.INVENTORY)
            {
                itemID = widget.getItemId();
            }
        }
        // Check if Bank item
        if (config.showBankTooltip())
        {
            if (WidgetUtil.componentToInterface(widget.getId()) == InterfaceID.BANK)
            {
                itemID = widget.getItemId();
            }
        }
        // Check if "Bank Invetory" Item, shows with either option true
        if (config.showInventoryTooltip() || config.showBankTooltip())
        {
            if (WidgetUtil.componentToInterface(widget.getId()) == InterfaceID.BANK_INVENTORY)
            {
                itemID = widget.getItemId();
            }
        }


        // Prepare Tooltip
        if (itemID != -1)
        {
            ItemStats stats = itemManager.getItemStats(itemID);
            if(stats != null)
            {
                if(stats.getEquipment() != null)
                {
                    int slotID = stats.getEquipment().getSlot();

                    InventoryItemMaxHit inventoryMaxHits = new InventoryItemMaxHit(plugin, config, itemManager, client);
                    int maxWithItem = (int) inventoryMaxHits.predict(itemID, slotID);

                    // If no error
                    if (maxWithItem != -1){
                        int deltaMax = maxWithItem - maxHit;

                        // Display depending on Negative or Positive Increase
                        String tooltip = "";
                        if(deltaMax < 0)
                        {
                            // Negative
                            tooltip = "Max hit: " + ColorUtil.wrapWithColorTag("-" + Math.abs(deltaMax), Color.RED);
                        }
                        else if (deltaMax > 0)
                        {
                            tooltip = "Max hit: " + ColorUtil.wrapWithColorTag("+" + deltaMax, Color.GREEN);
                        }
                        else
                        {
                            return;
                        }

                        tooltipManager.add(new Tooltip(tooltip));
                    }

                }

            }

        }
    }

    private void getSpellbookMaxHitTooltip(){
        // Tooltip on item in inventory
        Menu clientMenu = client.getMenu();
        MenuEntry[] menu = clientMenu.getMenuEntries();
        int menuSize = menu.length;
        if (menuSize == 0)
        {
            return;
        }

        // Get Spellbook
        MenuEntry entry = menu[menuSize - 1];
        Widget widget = entry.getWidget();
        if (widget == null)
        {
            return;
        }

        final int group = WidgetUtil.componentToInterface(widget.getId());
        int spellSpriteID = -1;

        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Group ID: " + group, ""); // DEBUG
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Spell Sprite ID: " + widget.getSpriteId(), ""); // DEBUG

        // Get Spell Sprite ID if actually in Spellbook
        if(group == InterfaceID.SPELLBOOK)
        {
            spellSpriteID = widget.getSpriteId();
        }

        // Prepare Tooltip
        if (spellSpriteID != -1)
        {
            // Get Combat Spell Info
            CombatSpell spell = CombatSpell.getSpellBySpriteID(spellSpriteID);

            // Check if spell is disabled
            if (spell == null && config.showTooltipOnDisabledSpells()){
                spell = CombatSpell.getSpellByDisabledSpriteID(spellSpriteID);
            }

            if(spell != null)
            {
                // Spell is a combat spell, continue with calc

                // Get Current Equipment
                Item[] playerEquipment;
                if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
                {
                    playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
                }
                else {
                    playerEquipment = null;
                }

                // Calculate Max Hit
                SpellbookSpellMaxHit spellbookMaxHits = new SpellbookSpellMaxHit(plugin, config, itemManager, client);
                int spellbookMaxHit = (int)spellbookMaxHits.calculateMagicMaxHit(playerEquipment, spell);

                // Error Check
                if (spellbookMaxHit > 0)
                {
                    String tooltip = "Max hit: " + spellbookMaxHit;
                    tooltipManager.add(new Tooltip(tooltip));
                }
            }
        }
    }

    private void getAutocastSelectionMaxHitTooltip(){
        // Tooltip on item in inventory
        Menu clientMenu = client.getMenu();
        MenuEntry[] menu = clientMenu.getMenuEntries();
        int menuSize = menu.length;
        if (menuSize == 0)
        {
            return;
        }

        // Get Autocast Selection Screen
        MenuEntry entry = menu[menuSize - 1];
        Widget widget = entry.getWidget();
        if (widget == null)
        {
            return;
        }

        final int group = WidgetUtil.componentToInterface(widget.getId());
        int spellSpriteID = -1;

        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Group ID: " + group, ""); // DEBUG
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Spell Sprite ID: " + widget.getSpriteId(), ""); // DEBUG

        // Get Spell Sprite ID if actually in Autocast Selection
        if(group == 201)
        {
            spellSpriteID = widget.getSpriteId();
        }

        // Prepare Tooltip
        if (spellSpriteID != -1)
        {
            // Get Combat Spell Info
            CombatSpell spell = CombatSpell.getSpellBySpriteID(spellSpriteID);

            // Check if spell is disabled
            if (spell == null && config.showTooltipOnDisabledSpells()){
                spell = CombatSpell.getSpellByDisabledSpriteID(spellSpriteID);
            }

            if(spell != null)
            {
                // Spell is a combat spell, continue with calc

                // Get Current Equipment
                Item[] playerEquipment;
                if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
                {
                    playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
                }
                else {
                    playerEquipment = null;
                }

                // Calculate Max Hit
                SpellbookSpellMaxHit spellbookMaxHits = new SpellbookSpellMaxHit(plugin, config, itemManager, client);
                int spellbookMaxHit = (int)spellbookMaxHits.calculateMagicMaxHit(playerEquipment, spell);

                // Error Check
                if (spellbookMaxHit > 0)
                {
                    String tooltip = "Max hit: " + spellbookMaxHit;
                    tooltipManager.add(new Tooltip(tooltip));
                }
            }
        }
    }
}
