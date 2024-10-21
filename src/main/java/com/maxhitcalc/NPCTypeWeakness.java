/* NPCTypeWeakness.java
 * Enum for all NPC's with a type weakness.
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

import lombok.Getter;

import javax.swing.*;


/**
 *
 * Contains all known type bonuses against different NPC's.
 * Updated based on https://oldschool.runescape.wiki/w/Elemental_weakness
 * Last updated on 9/21/24
 *
 */

public enum NPCTypeWeakness
{
    // In Alphabetical Order
    AbyssalPortal("Abyssal portal", SpellType.Fire, 50, NPCIcons.ABYSSAL_PORTAL_ICON),
    AdamantDragon("Adamant dragon", SpellType.Earth, 50, NPCIcons.ADAMANT_DRAGON_ICON),
    Ahrim("Ahrim", SpellType.Air, 50, NPCIcons.AHRIM_ICON),
    Amoxliatl("Amoxliatl", SpellType.Fire, 30, NPCIcons.AMOXLIATL),
    Araxxor("Araxxor", SpellType.Fire, 50, NPCIcons.ARAXXOR_ICON),
    Araxyte("Araxyte", SpellType.Fire, 50, NPCIcons.ARAXXOR_ICON), // Needs Icon
    ArcaneScarab("Arcane scarab", SpellType.Fire, 50, NPCIcons.ARCANE_SCARAB_ICON),
    ArmadyleanGuard("Armadylean guard", SpellType.Air, 30, NPCIcons.AVIANSIE_ICON), // Needs Icon
    Aviansie("Aviansie", SpellType.Air, 45, NPCIcons.AVIANSIE_ICON),
    BlackDemon("Black demon", SpellType.Water, 40, NPCIcons.BLACK_DEMON_ICON),
    BlackDragon("Black dragon", SpellType.Water, 50, NPCIcons.BLACK_DRAGON_ICON), // Brutal, Baby, and normal black dragons all have the same %
    BlueDragon("Blue dragon", SpellType.Water, 50, NPCIcons.BLUE_DRAGON_ICON), // Brutal, Baby, and normal blue dragons all have the same %
    BronzeDragon("Bronze dragon", SpellType.Earth, 50, NPCIcons.BRONZE_DRAGON_ICON),
    Cerberus("Cerberus", SpellType.Water, 40, NPCIcons.CERBERUS_ICON),
    ChilledJelly("Chilled jelly", SpellType.Fire, 50, NPCIcons.CHILLED_JELLY_ICON),
    DemonicGorilla("Demonic gorilla", SpellType.Water, 35, NPCIcons.DEMONIC_GORILLA_ICON),
    Dharok("Dharok", SpellType.Air, 50, NPCIcons.DHAROK_ICON),
    Drake("Drake", SpellType.Water, 50, NPCIcons.DRAKE_ICON),
    FireGiant("Fire giant", SpellType.Water, 100, NPCIcons.FIRE_GIANT_ICON),
    FlightKilisa("Flight kilisa", SpellType.Air, 30, NPCIcons.AVIANSIE_ICON), // Needs icon
    FlockleaderGeerin("Flockleader geerin", SpellType.Air, 30, NPCIcons.AVIANSIE_ICON), // Needs icon
    FrostCrab("Frost Crab", SpellType.Fire, 100, NPCIcons.FROST_CRAB),
    FrostNagua("Frost Nagua",SpellType.Fire, 50, NPCIcons.FROST_NAGUA),
    Ghost("Ghost", SpellType.Air, 50, NPCIcons.GHOST_ICON),
    GiantMole("Giant mole", SpellType.Water, 50, NPCIcons.GIANT_MOLE_ICON),
    GiantSpider("Giant spider", SpellType.Fire, 50, NPCIcons.GIANT_SPIDER_ICON),
    GreaterDemon("Greater demon", SpellType.Water, 40, NPCIcons.GREATER_DEMON_ICON),
    GreenDragon("Green dragon", SpellType.Water, 50, NPCIcons.GREEN_DRAGON_ICON), // Brutal, Baby, and normal blue dragons all have the same %
    Guthan("Guthan", SpellType.Air, 50, NPCIcons.GUTHAN_ICON),
    Hellhound("Hellhound", SpellType.Water, 50, NPCIcons.HELLHOUND_ICON),
    Hespori("Hespori", SpellType.Fire, 100, NPCIcons.HESPORI_ICON),
    Hueycoatl("hueycoatl", SpellType.Earth, 60, NPCIcons.HUEYCOATL_ICON),
    IceDemon("Ice demon", SpellType.Fire, 150, NPCIcons.ICE_DEMON_ICON),
    IceGiant("Ice giant", SpellType.Fire, 100, NPCIcons.ICE_GIANT_ICON),
    IceSpider("Ice spider", SpellType.Fire, 100, NPCIcons.ICE_SPIDER_ICON),
    IceTroll("Ice troll", SpellType.Fire, 100, NPCIcons.ICE_TROLL_ICON), // Ordinary, Male, Female, and Grunt all have the same %
    IceTrollRunt("Ice troll runt", SpellType.Fire, 50, NPCIcons.ICE_TROLL_RUNT_ICON),
    IceWarrior("Ice warrior", SpellType.Fire, 100, NPCIcons.ICE_WARRIOR_ICON),
    Icefiend("Icefiend", SpellType.Fire, 100, NPCIcons.ICEFIEND_ICON),
    IronDragon("Iron dragon", SpellType.Earth, 50, NPCIcons.IRON_DRAGON_ICON),
    Karil("Karil", SpellType.Air, 50, NPCIcons.KARIL_ICON),
    Kephri("Kephri", SpellType.Fire, 35, NPCIcons.KEPHRI_ICON), // 40% with shield, not possible to calculate
    KingBlackDragon("King black dragon", SpellType.Water, 50, NPCIcons.KBD_ICON),
    Kreearra("Kree'arra", SpellType.Air, 30, NPCIcons.KREE_ICON),
    KrilTsutsaroth("K'ril Tsutsaroth", SpellType.Water, 30, NPCIcons.KRIL_ICON),
    LavaDragon("Lava dragon", SpellType.Water, 50, NPCIcons.LAVA_DRAGON_ICON),
    LesserDemon("Lava dragon", SpellType.Water, 40, NPCIcons.LESSER_DEMON_ICON),
    MithrilDragon("Mithril dragon", SpellType.Earth, 50, NPCIcons.MITHRIL_DRAGON_ICON),
    MossGiant("Moss gian", SpellType.Fire, 50, NPCIcons.MOSS_GIANT_ICON),
    MountainTroll("Mountain troll", SpellType.Fire, 50, NPCIcons.MOUNTAIN_TROLL_ICON),
    Pyrefiend("Pyrefiend", SpellType.Water, 100, NPCIcons.PYREFIEND_ICON),
    RedDragon("Red dragon", SpellType.Water, 50, NPCIcons.RED_DRAGON_ICON), // Brutal, Baby, and normal black dragons all have the same %
    RuneDragon("Rune dragon", SpellType.Earth, 50, NPCIcons.RUNE_DRAGON_ICON),
    ScarabMage("Scarab mage", SpellType.Fire, 50, NPCIcons.SCARAB_MAGE_ICON),
    ScarabSwarm("Scarab swarm", SpellType.Fire, 50, NPCIcons.SCARAB_SWARM_ICON),
    Skeleton("Skeleton", SpellType.Air, 35, NPCIcons.SKELETON_ICON),
    SoldierScarab("Soldier scarab", SpellType.Fire, 50, NPCIcons.SOLDIER_SCARAB_ICON),
    SpittingScarab("Spitting scarab", SpellType.Fire, 50, NPCIcons.SPITTING_SCARAB_ICON),
    // Spiritual Warrior (Zaros), find how to differentiate between all different spiritual warriors
    SteelDragon("Steel dragon", SpellType.Earth, 50, NPCIcons.STEEL_DRAGON_ICON),
    Torag("Torag", SpellType.Air, 50, NPCIcons.TORAG_ICON),
    Verac("Verac", SpellType.Air, 50, NPCIcons.VERAC_ICON),
    Vespula("Vespula", SpellType.Fire, 50, NPCIcons.VESPULA_ICON),
    Waterfiend("Waterfiend", SpellType.Earth, 50, NPCIcons.WATERFIEND_ICON),
    Wyrm("Wyrm", SpellType.Earth, 50, NPCIcons.WYRM_ICON),
    Wyrmling("Wyrmling", SpellType.Earth, 50, NPCIcons.WYRMLING_ICON),
    Zulrah("Zulrah", SpellType.Fire, 50, NPCIcons.ZULRAH_ICON);

    @Getter
    private final String NPCName;
    @Getter
    private final SpellType elementalWeakness;
    @Getter
    private final int weaknessPercent;
    @Getter
    private final ImageIcon icon;

    /**
     * Gets an NPC's Weakness by searching their name
     * @param name string
     * @return NPCTypeWeakness, or null for failure
     */
    public static NPCTypeWeakness findWeaknessByName(String name)
    {
        if(name == null)
            return null;

        // Search exact name first
        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            if(npc.NPCName.equalsIgnoreCase(name)){
                return npc;
            }
        }

        // Search names that aren't complete, e.g: baby (black dragon)
        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            if(npc.NPCName.toLowerCase().contains(name.toLowerCase())){
                return npc;
            }
        }

        // error, failure
        return null;
    }

    NPCTypeWeakness(String NPCName, SpellType elementalWeakness, int weaknessPercent, ImageIcon icon)
    {
        this.NPCName = NPCName;
        this.elementalWeakness = elementalWeakness;
        this.weaknessPercent = weaknessPercent;
        this.icon = icon;
    }
}
