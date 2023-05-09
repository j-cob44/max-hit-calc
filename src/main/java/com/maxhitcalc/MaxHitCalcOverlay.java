/* MaxHitCalcOverlay.java
 * Code for plugin main display panel.
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

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;

public class MaxHitCalcOverlay extends Overlay
{
    private PanelComponent panelComponent = new PanelComponent();
    private MaxHitCalcPlugin plugin;
    private MaxHitCalcConfig config;

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

        int maxHit = (int)plugin.calculateMaxHit();
        int maxSpec = (int)plugin.calculateMaxSpec();
        int maxVsType = (int)plugin.calculateMaxAgainstType();
        int maxSpecVsType = (int)plugin.calculateMaxSpecAgainstType();

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

        return panelComponent.render(graphics);
    }
}
