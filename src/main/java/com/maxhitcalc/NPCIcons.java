/* NPCIcons.java
 * Static references to all icon resources.
 *
 *
 * Copyright (c) 2024, Jacob Burton <https://github.com/j-cob44>
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

import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class NPCIcons
{
    public static final ImageIcon ABYSSAL_PORTAL_ICON;
    public static final ImageIcon ADAMANT_DRAGON_ICON;
    public static final ImageIcon AHRIM_ICON;
    public static final ImageIcon ARAXXOR_ICON;
    public static final ImageIcon ARCANE_SCARAB_ICON;
    public static final ImageIcon AVIANSIE_ICON;
    public static final ImageIcon BLACK_DEMON_ICON;
    public static final ImageIcon BLACK_DRAGON_ICON;
    public static final ImageIcon BLUE_DRAGON_ICON;
    public static final ImageIcon BRONZE_DRAGON_ICON;
    public static final ImageIcon CERBERUS_ICON;
    public static final ImageIcon DEMONIC_GORILLA_ICON;
    public static final ImageIcon DHAROK_ICON;
    public static final ImageIcon DRAKE_ICON;
    public static final ImageIcon FIRE_GIANT_ICON;
    public static final ImageIcon GHOST_ICON;
    public static final ImageIcon GIANT_MOLE_ICON;
    public static final ImageIcon GIANT_SPIDER_ICON;
    public static final ImageIcon GREATER_DEMON_ICON;
    public static final ImageIcon GREEN_DRAGON_ICON;
    public static final ImageIcon GUTHAN_ICON;
    public static final ImageIcon HELLHOUND_ICON;
    public static final ImageIcon HESPORI_ICON;
    public static final ImageIcon ICE_DEMON_ICON;
    public static final ImageIcon ICE_GIANT_ICON;
    public static final ImageIcon ICE_TROLL_ICON;
    public static final ImageIcon ICE_TROLL_RUNT_ICON;
    public static final ImageIcon ICE_WARRIOR_ICON;
    public static final ImageIcon ICEFIEND_ICON;
    public static final ImageIcon IRON_DRAGON_ICON;
    public static final ImageIcon KARIL_ICON;
    public static final ImageIcon KEPHRI_ICON;
    public static final ImageIcon KBD_ICON;
    public static final ImageIcon KREE_ICON;
    public static final ImageIcon KRIL_ICON;
    public static final ImageIcon LAVA_DRAGON_ICON;
    public static final ImageIcon LESSER_DEMON_ICON;
    public static final ImageIcon MITHRIL_DRAGON_ICON;
    public static final ImageIcon MOSS_GIANT_ICON;
    public static final ImageIcon MOUNTAIN_TROLL_ICON;
    public static final ImageIcon PYREFIEND_ICON;
    public static final ImageIcon RED_DRAGON_ICON;
    public static final ImageIcon RUNE_DRAGON_ICON;
    public static final ImageIcon SCARAB_MAGE_ICON;
    public static final ImageIcon SCARAB_SWARM_ICON;
    public static final ImageIcon SKELETON_ICON;
    public static final ImageIcon SOLDIER_SCARAB_ICON;
    public static final ImageIcon SPITTING_SCARAB_ICON;
    public static final ImageIcon STEEL_DRAGON_ICON;
    public static final ImageIcon TORAG_ICON;
    public static final ImageIcon VERAC_ICON;
    public static final ImageIcon VESPULA_ICON;
    public static final ImageIcon WATERFIEND_ICON;
    public static final ImageIcon WYRM_ICON;
    public static final ImageIcon WYRMLING_ICON;
    public static final ImageIcon ZULRAH_ICON;

    static {
        final BufferedImage ABYSSAL_PORTAL_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/abyssal_portal.png");
        final BufferedImage ADAMANT_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/adamant_dragon.png");
        final BufferedImage AHRIM_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/ahrim.png");
        final BufferedImage ARAXXOR_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/araxxor.png");
        final BufferedImage ARCANE_SCARAB_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/arcane_scarab.png");
        final BufferedImage AVIANSIE_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/aviansie.png");
        final BufferedImage BLACK_DEMON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/black_demon.png");
        final BufferedImage BLACK_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/black_dragon.png");
        final BufferedImage BLUE_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/blue_dragon.png");
        final BufferedImage BRONZE_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/bronze_dragon.png");
        final BufferedImage CERBERUS_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/cerberus.png");
        final BufferedImage DEMONIC_GORILLA_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/demonic_gorilla.png");
        final BufferedImage DHAROK_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/dharok.png");
        final BufferedImage DRAKE_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/drake.png");
        final BufferedImage FIRE_GIANT_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/fire_giant.png");
        final BufferedImage GHOST_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/ghost.png");
        final BufferedImage GIANT_MOLE_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/giant_mole.png");
        final BufferedImage GIANT_SPIDER_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/giant_spider.png");
        final BufferedImage GREATER_DEMON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/greater_demon.png");
        final BufferedImage GREEN_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/green_dragon.png");
        final BufferedImage GUTHAN_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/guthan.png");
        final BufferedImage HELLHOUND_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/hellhound.png");
        final BufferedImage HESPORI_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/hespori.png");
        final BufferedImage ICE_DEMON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/ice_demon.png");
        final BufferedImage ICE_GIANT_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/ice_giant.png");
        final BufferedImage ICE_TROLL_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/ice_troll.png");
        final BufferedImage ICE_TROLL_RUNT_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/ice_troll_runt.png");
        final BufferedImage ICE_WARRIOR_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/ice_warrior.png");
        final BufferedImage ICEFIEND_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/icefiend.png");
        final BufferedImage IRON_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/iron_dragon.png");
        final BufferedImage KARIL_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/karil.png");
        final BufferedImage KEPHRI_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/kephri.png");
        final BufferedImage KBD_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/kbd.png");
        final BufferedImage KREE_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/kree.png");
        final BufferedImage KRIL_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/kril.png");
        final BufferedImage LAVA_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/lava_dragon.png");
        final BufferedImage LESSER_DEMON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/lesser_demon.png");
        final BufferedImage MITHRIL_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/mithril_dragon.png");
        final BufferedImage MOSS_GIANT_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/moss_giant.png");
        final BufferedImage MOUNTAIN_TROLL_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/mountain_troll.png");
        final BufferedImage PYREFIEND_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/pyrefiend.png");
        final BufferedImage RED_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/red_dragon.png");
        final BufferedImage RUNE_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/rune_dragon.png");
        final BufferedImage SCARAB_MAGE_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/scarab_mage.png");
        final BufferedImage SCARAB_SWARM_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/scarab_swarm.png");
        final BufferedImage SKELETON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/skeleton.png");
        final BufferedImage SOLDIER_SCARAB_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/soldier_scarab.png");
        final BufferedImage SPITTING_SCARAB_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/spitting_scarab.png");
        final BufferedImage STEEL_DRAGON_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/steel_dragon.png");
        final BufferedImage TORAG_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/torag.png");
        final BufferedImage VERAC_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/verac.png");
        final BufferedImage VESPULA_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/vespula.png");
        final BufferedImage WATERFIEND_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/waterfiend.png");
        final BufferedImage WYRM_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/wyrm.png");
        final BufferedImage WYRMLING_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/wyrmling.png");
        final BufferedImage ZULRAH_IMG = ImageUtil.loadImageResource(MaxHitCalcPlugin.class, "/npc_icons/zulrah.png");

        ABYSSAL_PORTAL_ICON = new ImageIcon(ABYSSAL_PORTAL_IMG);
        ADAMANT_DRAGON_ICON = new ImageIcon(ADAMANT_DRAGON_IMG);
        AHRIM_ICON = new ImageIcon(AHRIM_IMG);
        ARAXXOR_ICON = new ImageIcon(ARAXXOR_IMG);
        ARCANE_SCARAB_ICON = new ImageIcon(ARCANE_SCARAB_IMG);
        AVIANSIE_ICON = new ImageIcon(AVIANSIE_IMG);
        BLACK_DEMON_ICON = new ImageIcon(BLACK_DEMON_IMG);
        BLACK_DRAGON_ICON = new ImageIcon(BLACK_DRAGON_IMG);
        BLUE_DRAGON_ICON = new ImageIcon(BLUE_DRAGON_IMG);
        BRONZE_DRAGON_ICON = new ImageIcon(BRONZE_DRAGON_IMG);
        CERBERUS_ICON = new ImageIcon(CERBERUS_IMG);
        DEMONIC_GORILLA_ICON = new ImageIcon(DEMONIC_GORILLA_IMG);
        DHAROK_ICON = new ImageIcon(DHAROK_IMG);
        DRAKE_ICON = new ImageIcon(DRAKE_IMG);
        FIRE_GIANT_ICON = new ImageIcon(FIRE_GIANT_IMG);
        GHOST_ICON = new ImageIcon(GHOST_IMG);
        GIANT_MOLE_ICON = new ImageIcon(GIANT_MOLE_IMG);
        GIANT_SPIDER_ICON = new ImageIcon(GIANT_SPIDER_IMG);
        GREATER_DEMON_ICON = new ImageIcon(GREATER_DEMON_IMG);
        GREEN_DRAGON_ICON = new ImageIcon(GREEN_DRAGON_IMG);
        GUTHAN_ICON = new ImageIcon(GUTHAN_IMG);
        HELLHOUND_ICON = new ImageIcon(HELLHOUND_IMG);
        HESPORI_ICON = new ImageIcon(HESPORI_IMG);
        ICE_DEMON_ICON = new ImageIcon(ICE_DEMON_IMG);
        ICE_GIANT_ICON = new ImageIcon(ICE_GIANT_IMG);
        ICE_TROLL_ICON = new ImageIcon(ICE_TROLL_IMG);
        ICE_TROLL_RUNT_ICON = new ImageIcon(ICE_TROLL_RUNT_IMG);
        ICE_WARRIOR_ICON = new ImageIcon(ICE_WARRIOR_IMG);
        ICEFIEND_ICON = new ImageIcon(ICEFIEND_IMG);
        IRON_DRAGON_ICON = new ImageIcon(IRON_DRAGON_IMG);
        KARIL_ICON = new ImageIcon(KARIL_IMG);
        KEPHRI_ICON = new ImageIcon(KEPHRI_IMG);
        KBD_ICON = new ImageIcon(KBD_IMG);
        KREE_ICON = new ImageIcon(KREE_IMG);
        KRIL_ICON = new ImageIcon(KRIL_IMG);
        LAVA_DRAGON_ICON = new ImageIcon(LAVA_DRAGON_IMG);
        LESSER_DEMON_ICON = new ImageIcon(LESSER_DEMON_IMG);
        MITHRIL_DRAGON_ICON = new ImageIcon(MITHRIL_DRAGON_IMG);
        MOSS_GIANT_ICON = new ImageIcon(MOSS_GIANT_IMG);
        MOUNTAIN_TROLL_ICON = new ImageIcon(MOUNTAIN_TROLL_IMG);
        PYREFIEND_ICON = new ImageIcon(PYREFIEND_IMG);
        RED_DRAGON_ICON = new ImageIcon(RED_DRAGON_IMG);
        RUNE_DRAGON_ICON = new ImageIcon(RUNE_DRAGON_IMG);
        SCARAB_MAGE_ICON = new ImageIcon(SCARAB_MAGE_IMG);
        SCARAB_SWARM_ICON = new ImageIcon(SCARAB_SWARM_IMG);
        SKELETON_ICON = new ImageIcon(SKELETON_IMG);
        SOLDIER_SCARAB_ICON = new ImageIcon(SOLDIER_SCARAB_IMG);
        SPITTING_SCARAB_ICON = new ImageIcon(SPITTING_SCARAB_IMG);
        STEEL_DRAGON_ICON = new ImageIcon(STEEL_DRAGON_IMG);
        TORAG_ICON = new ImageIcon(TORAG_IMG);
        VERAC_ICON = new ImageIcon(VERAC_IMG);
        VESPULA_ICON = new ImageIcon(VESPULA_IMG);
        WATERFIEND_ICON = new ImageIcon(WATERFIEND_IMG);
        WYRM_ICON = new ImageIcon(WYRM_IMG);
        WYRMLING_ICON = new ImageIcon(WYRMLING_IMG);
        ZULRAH_ICON = new ImageIcon(ZULRAH_IMG);
    }
}
