/* NPCTypeWeakness.java
 * Separates spells by SpriteID. Useful for getting the selected auto-cast spell.
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
    AbyssalPortal("Abyssal portal", SpellType.Fire, 50),
    AdamantDragon("Adamant dragon", SpellType.Earth, 50),
    Ahrim("Ahrim", SpellType.Air, 50),
    Araxxor("Araxxor", SpellType.Fire, 50),
    Araxyte("Araxyte", SpellType.Fire, 50),
    ArcaneScarab("Arcane scarab", SpellType.Fire, 50),
    ArmadyleanGuard("Armadylean guard", SpellType.Air, 30),
    Aviansie("Aviansie", SpellType.Air, 45),
    BlackDragon("Black dragon", SpellType.Water, 50), // Brutal, Baby, and normal black dragons all have the same %
    BlackDemon("Black demon", SpellType.Water, 40),
    BlueDragon("Blue dragon", SpellType.Water, 50), // Brutal, Baby, and normal blue dragons all have the same %
    BronzeDragon("Bronze dragon", SpellType.Earth, 50),
    Cerberus("Cerberus", SpellType.Water, 40),
    DemonicGorilla("Demonic gorilla", SpellType.Water, 35),
    Dharok("Dharok", SpellType.Air, 50),
    Drake("Drake", SpellType.Water, 50),
    FireGiant("Fire giant", SpellType.Water, 100),
    FlightKilisa("Flight kilisa", SpellType.Air, 30),
    FlockleaderGeerin("Flockleader geerin", SpellType.Air, 30),
    Ghost("Ghost", SpellType.Air, 50),
    GiantMole("Giant mole", SpellType.Water, 50),
    GiantSpider("Giant spider", SpellType.Fire, 50),
    GreaterDemon("Greater demon", SpellType.Water, 40),
    GreenDragon("Green dragon", SpellType.Water, 50), // Brutal, Baby, and normal blue dragons all have the same %
    Guthan("Guthan", SpellType.Air, 50),
    Hellhound("Hellhound", SpellType.Water, 50),
    Hespori("Hespori", SpellType.Fire, 100),
    IceDemon("Ice demon", SpellType.Fire, 150),
    IceGiant("Ice giant", SpellType.Fire, 100),
    IceTroll("Ice troll", SpellType.Fire, 100), // Ordinary, Male, Female, and Grunt all have the same %
    IceTrollRunt("Ice troll runt", SpellType.Fire, 50),
    IceWarrior("Ice warrior", SpellType.Fire, 100),
    Icefiend("Icefiend", SpellType.Fire, 100),
    IronDragon("Iron dragon", SpellType.Earth, 50),
    Karil("Karil", SpellType.Air, 50),
    Kephri("Kephri", SpellType.Fire, 35), // 40% with shield, not possible to calculate
    KingBlackDragon("King black dragon", SpellType.Water, 50),
    Kreearra("Kree'arra", SpellType.Air, 30),
    KrilTsutsaroth("K'ril Tsutsaroth", SpellType.Water, 30),
    LavaDragon("Lava dragon", SpellType.Water, 50),
    LesserDemon("Lava dragon", SpellType.Water, 40),
    MithrilDragon("Mithril dragon", SpellType.Earth, 50),
    MossGiant("Moss gian", SpellType.Fire, 50),
    MountainTroll("Mountain troll", SpellType.Fire, 50),
    Pyrefiend("Pyrefiend", SpellType.Water, 100),
    RedDragon("Red dragon", SpellType.Water, 50), // Brutal, Baby, and normal black dragons all have the same %
    RuneDragon("Rune dragon", SpellType.Earth, 50),
    ScarabMage("Scarab mage", SpellType.Fire, 50),
    ScarabSwarm("Scarab swarm", SpellType.Fire, 50),
    Skeleton("Skeleton", SpellType.Air, 35),
    SoldierScarab("Soldier scarab", SpellType.Fire, 50),
    SpittingScarab("Spitting scarab", SpellType.Fire, 50),
    // Spiritual Warrior (Zaros), find how to differentiate between all different versions
    SteelDragon("Steel dragon", SpellType.Earth, 50),
    Torag("Torag", SpellType.Air, 50),
    Verac("Verac", SpellType.Air, 50),
    Vespula("Vespula", SpellType.Fire, 50),
    Waterfiend("Vespula", SpellType.Earth, 50),
    Wyrm("Wyrm", SpellType.Earth, 50),
    Wyrmling("Wyrmling", SpellType.Earth, 50),
    Zulrah("Zulrah", SpellType.Fire, 50);

    @Getter
    private final String NPCName;
    @Getter
    private final SpellType elementalWeakness;
    @Getter
    private final int weaknessPercent;

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

    NPCTypeWeakness(String NPCName, SpellType elementalWeakness, int weaknessPercent)
    {
        this.NPCName = NPCName;
        this.elementalWeakness = elementalWeakness;
        this.weaknessPercent = weaknessPercent;
    }
}
