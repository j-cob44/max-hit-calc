package com.maxhitcalc;

import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class PanelIcons {
    public static final ImageIcon AIR_RUNE_ICON;
    public static final ImageIcon WATER_RUNE_ICON;
    public static final ImageIcon EARTH_RUNE_ICON;
    public static final ImageIcon FIRE_RUNE_ICON;
    public static final ImageIcon EMPTY_RUNE_ICON;
    public static final Dimension RUNE_ICON_SIZE; // largest icon sizes for UI

    static {
        // Rune icons for weakness display
        final BufferedImage AIR_ICON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/elemental_icons/air.png");
        final BufferedImage WATER_ICON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/elemental_icons/water.png");
        final BufferedImage EARTH_ICON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/elemental_icons/earth.png");
        final BufferedImage FIRE_ICON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/elemental_icons/fire.png");
        final BufferedImage EMPTY_RUNE_ICON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/elemental_icons/no_type.png");

        AIR_RUNE_ICON = new ImageIcon(AIR_ICON_IMG);
        WATER_RUNE_ICON = new ImageIcon(WATER_ICON_IMG);
        EARTH_RUNE_ICON = new ImageIcon(EARTH_ICON_IMG);
        FIRE_RUNE_ICON = new ImageIcon(FIRE_ICON_IMG);
        EMPTY_RUNE_ICON = new ImageIcon(EMPTY_RUNE_ICON_IMG);

        int widest = 0;
        int tallest = 0;
        for (ImageIcon icon : new ImageIcon[]{AIR_RUNE_ICON, WATER_RUNE_ICON, EARTH_RUNE_ICON, FIRE_RUNE_ICON, EMPTY_RUNE_ICON})
        {
            widest = Math.max(widest, icon.getIconWidth());
            tallest = Math.max(tallest, icon.getIconHeight());
        }

        RUNE_ICON_SIZE = new Dimension(widest, tallest);
    }
}
