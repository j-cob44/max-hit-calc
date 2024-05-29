/* CombatSpell.java
 * Separates spells by SpriteID. Useful for getting the selected auto-cast spell.
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

import lombok.Getter;

import java.util.ArrayList;

/**
 * Contains definitions of combat spells and functions for retrieving them.
 * Selected Autocast Varbit is Varbit 276
 *
 *
 */
public enum CombatSpell
{
    // In order of autocast varbit
    WIND_STRIKE(1, 15, 65, "WIND STRIKE", 1,2, "standard", 1),
    WATER_STRIKE(2, 17, 67, "WATER STRIKE", 5,4, "standard", 1),
    EARTH_STRIKE(3, 19, 69, "EARTH STRIKE", 9,6, "standard", 1),
    FIRE_STRIKE(4, 21, 71, "FIRE STRIKE", 13,8, "standard", 1),
    WIND_BOLT(5,23, 73, "WIND BOLT", 17,9, "standard", 2),
    WATER_BOLT(6,26, 76, "WATER BOLT", 23,10, "standard", 2),
    EARTH_BOLT(7,29, 79, "EARTH BOLT", 29,11, "standard", 2),
    FIRE_BOLT(8,32, 82, "FIRE BOLT", 35,12, "standard", 2),
    WIND_BLAST(9,35, 85, "WIND BLAST", 41,13, "standard", 3),
    WATER_BLAST(10,38, 88, "WATER BLAST",47,14, "standard", 3),
    EARTH_BLAST(11,40, 90, "EARTH BLAST", 53,15, "standard", 3),
    FIRE_BLAST(12,44, 94, "FIRE BLAST", 59,16, "standard", 3),
    WIND_WAVE(13,46, 96, "WIND WAVE", 62,17, "standard", 4),
    WATER_WAVE(14,48, 98, "WATER WAVE", 65,18, "standard", 4),
    EARTH_WAVE(15,51, 101, "EARTH WAVE", 70,19, "standard", 4),
    FIRE_WAVE(16,52, 102, "FIRE WAVE", 75,20, "standard", 4),
    CRUMBLE_UNDEAD(17,34, 84, "CRUMBLE UNDEAD", 39,15, "standard",0),
    MAGIC_DART(18, 324, 374, "MAGIC DART", 50,10, "standard",0),
    CLAWS_OF_GUTHIX(19, 60, 110, "CLAWS OF GUTHIX", 60,20, "standard",0),
    FLAMES_OF_ZAMORAK(20, 59, 109, "FLAMES OF ZAMORAK", 60,20, "standard",0),

    SMOKE_RUSH(31, 329, 379, "SMOKE RUSH", 50,13, "ancients",0),
    SHADOW_RUSH(32, 337, 387, "SHADOW RUSH", 52,14, "ancients",0),
    BLOOD_RUSH(33,333, 383, "BLOOD RUSH", 56,15, "ancients",0),
    ICE_RUSH(34,325, 375, "ICE RUSH", 58,16, "ancients",0),
    SMOKE_BURST(35, 330, 380, "SMOKE BURST", 62,17, "ancients",0),
    SHADOW_BURST(36, 338, 388, "SHADOW BURST", 64,18, "ancients",0),
    BLOOD_BURST(37, 334, 384, "BLOOD BURST", 68,21, "ancients",0),
    ICE_BURST(38, 326, 381, "ICE BURST", 70,22, "ancients",0),
    SMOKE_BLITZ(39, 331, 389, "SMOKE BLITZ", 74,23, "ancients",0),
    SHADOW_BLITZ(40, 339, 389, "SHADOW BLITZ", 76,24, "ancients",0),
    BLOOD_BLITZ(41, 335, 385, "BLOOD BLITZ", 80,25, "ancients",0),
    ICE_BLITZ(42, 327, 377, "ICE BLITZ", 82,26, "ancients",0),
    SMOKE_BARRAGE(43, 332, 382, "SMOKE BARRAGE", 86,27, "ancients",0),
    SHADOW_BARRAGE(44, 340, 390, "SHADOW BARRAGE", 88,28, "ancients",0),
    BLOOD_BARRAGE(45, 336, 386, "BLOOD BARRAGE", 92,29, "ancients",0),
    ICE_BARRAGE(46, 328, 378, "ICE BARRAGE", 94,30, "ancients",0),
    IBAN_BLAST(47, 53, 103, "IBAN BLAST", 50,25, "standard",0),
    WIND_SURGE(48, 362, 412, "WIND SURGE", 81,21, "standard",5),
    WATER_SURGE(49, 363, 413, "WATER SURGE", 85,22, "standard",5),
    EARTH_SURGE(50,364, 414, "EARTH SURGE", 90,23, "standard",5),
    FIRE_SURGE(51, 365, 415, "FIRE SURGE", 95,24, "standard",5),
    SARADOMIN_STRIKE(52, 61, 111, "SARADOMIN STRIKE", 60, 20, "standard",0),
    INFERIOR_DEMONBANE(53,1302, 1321, "INFERIOR DEMONBANE", 44,16, "arceuss",0),
    SUPERIOR_DEMONBANE(54, 1303, 1322, "SUPERIOR DEMONBANE", 62,23, "arceuss",0),
    DARK_DEMONBANE(55, 1304, 1323, "DARK DEMONBANE", 82,30, "arceuss",0),
    GHOSTLY_GRASP(56,1267, 1292, "GHOSTLY GRASP", 35,12, "arceuss",0),
    SKELETAL_GRASP(57,1268, 1293, "SKELETAL GRASP", 56,17, "arceuss",0),
    UNDEAD_GRASP(58, 1269, 1294, "UNDEAD GRASP", 79,24, "arceuss",0);


    @Getter
    private final int autocastVarbitValue;
    @Getter
    private final int spriteID;
    @Getter
    private final int disabledSpriteID;
    @Getter
    private final String name;
    @Getter
    private final int reqLevel;
    @Getter
    private final int baseDamage;
    @Getter
    private final String spellbook;
    @Getter
    private final int tier;

    /**
     * Gets a CombatSpell from a Varbit ID
     * @param value int, varbit value of spell
     * @return Spell, or null for failure
     */
    public static CombatSpell getSpellbyVarbitValue(int value)
    {
        for (CombatSpell spell : CombatSpell.values())
        {
            if(spell.autocastVarbitValue == value){
                return spell;
            }
        }

        // error, failure
        return null;
    }

    /**
     * Finds a CombatSpell based on sprite ID
     * @param spriteID int
     * @return Spell, or null for failure
     */
    public static CombatSpell getSpellBySpriteID(int spriteID)
    {
        for (CombatSpell spell : CombatSpell.values())
        {
            if(spell.spriteID == spriteID){
                return spell;
            }
        }

        // error, failure
        return null;
    }

    /**
     * Finds a CombatSpell based on its disabled sprite ID
     * @param spriteID int
     * @return Spell, or null for failure
     */
    public static CombatSpell getSpellByDisabledSpriteID(int spriteID)
    {
        // Spells that
        for (CombatSpell spell : CombatSpell.values())
        {
            if(spell.disabledSpriteID == spriteID){
                return spell;
            }
        }

        // error, failure
        return null;
    }

    public static CombatSpell[] getSpellsOfTier(int tier, String spellbook){
        ArrayList<CombatSpell> results = new ArrayList<>();

        for (CombatSpell spell : CombatSpell.values())
        {
            if (spell.getSpellbook().equals(spellbook))
            {
                if (spell.getTier() == tier)
                {
                    // found spell of correct tier and spellbook
                    results.add(spell);
                }
            }
        }

        return results.toArray(new CombatSpell[results.size()]);
    }

    CombatSpell(int autocastVarbitValue, int spriteID, int disabledSpriteID, String name, int reqLevel, int baseDamage, String spellbook, int tier)
    {
        this.autocastVarbitValue = autocastVarbitValue;
        this.spriteID = spriteID;
        this.disabledSpriteID = disabledSpriteID;
        this.name = name;
        this.reqLevel = reqLevel;
        this.baseDamage = baseDamage;
        this.spellbook = spellbook;
        this.tier = tier;
    }
}
