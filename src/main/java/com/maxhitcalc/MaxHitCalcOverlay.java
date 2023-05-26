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

import net.runelite.api.ChatMessageType; // For debug
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
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

        int maxHit = (int)Math.floor(plugin.calculateMaxHit());
        int maxSpec = (int)Math.floor(plugin.calculateMaxSpec());
        int maxVsType = (int)Math.floor(plugin.calculateMaxAgainstType());
        int maxSpecVsType = (int)Math.floor(plugin.calculateMaxSpecAgainstType());

        if(config.showMaxHit())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Max Hit:")
                    .right(Integer.toString(maxHit))
                    .build());
        }

        // Don't Display if 0 (not useful) or turned off
        if(maxSpec != 0 && config.showSpec())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Max Spec Hit:")
                    .right(Integer.toString(maxSpec))
                    .build());
        }

        // Don't Display if 0 (not useful) or turned off
        if(maxVsType != 0 && config.showType())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Max Hit vs Type:")
                    .right(Integer.toString(maxVsType))
                    .build());
        }

        // Don't Display if 0 (not useful) or turned off
        if(maxSpecVsType != 0 && config.showSpecVsType())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Max Spec vs Type:")
                    .right(Integer.toString(maxSpecVsType))
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

        return super.render(graphics);
    }

    private String predictedMaxHitTooltip()
    {
        List<Object> prediction = plugin.predictNextMaxHit();

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
            result += prediction.get(1) + " Magic Levels </br>" + (int)((double)prediction.get(2) * 100) + "% Magic Damage Bonus";
            return result;
        }
        else
        {
            return null;
        }
    }
}
