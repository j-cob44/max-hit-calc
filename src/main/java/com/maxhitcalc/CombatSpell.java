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

/**
 * Contains definitions of combat spells and functions for retrieving them.
 * Selected Autocast Varbit is Varbit 276
 *
 *
 */
public enum CombatSpell
{
    // In order of autocast varbit
    WIND_STRIKE(1, 15, 65, "WIND STRIKE", 2, "standard"),
    WATER_STRIKE(2, 17, 67, "WATER STRIKE", 4, "standard"),
    EARTH_STRIKE(3, 19, 69, "EARTH STRIKE", 6, "standard"),
    FIRE_STRIKE(4, 21, 71, "FIRE STRIKE", 8, "standard"),
    WIND_BOLT(5,23, 73, "WIND BOLT", 9, "standard"),
    WATER_BOLT(6,26, 76, "WATER BOLT", 10, "standard"),
    EARTH_BOLT(7,29, 79, "EARTH BOLT", 11, "standard"),
    FIRE_BOLT(8,32, 82, "FIRE BOLT", 12, "standard"),
    WIND_BLAST(9,35, 85, "WIND BLAST", 13, "standard"),
    WATER_BLAST(10,38, 88, "WATER BLAST",14, "standard"),
    EARTH_BLAST(11,40, 90, "EARTH BLAST", 15, "standard"),
    FIRE_BLAST(12,44, 94, "FIRE BLAST", 16, "standard"),
    WIND_WAVE(13,46, 96, "WIND WAVE", 17, "standard"),
    WATER_WAVE(14,48, 98, "WATER WAVE", 18, "standard"),
    EARTH_WAVE(15,51, 101, "EARTH WAVE", 19, "standard"),
    FIRE_WAVE(16,52, 102, "FIRE WAVE", 20, "standard"),
    CRUMBLE_UNDEAD(17,34, 84, "CRUMBLE UNDEAD", 15, "standard"),
    MAGIC_DART(18, 324, 374, "MAGIC DART", 10, "standard"),
    CLAWS_OF_GUTHIX(19, 60, 110, "CLAWS OF GUTHIX", 20, "standard"),
    FLAMES_OF_ZAMORAK(20, 59, 109, "FLAMES OF ZAMORAK", 20, "standard"),

    SMOKE_RUSH(31, 329, 379, "SMOKE RUSH", 13, "ancients"),
    SHADOW_RUSH(32, 337, 387, "SHADOW RUSH", 14, "ancients"),
    BLOOD_RUSH(33,333, 383, "BLOOD RUSH", 15, "ancients"),
    ICE_RUSH(34,325, 375, "ICE RUSH", 16, "ancients"),
    SMOKE_BURST(35, 330, 380, "SMOKE BURST", 17, "ancients"),
    SHADOW_BURST(36, 338, 388, "SHADOW BURST", 18, "ancients"),
    BLOOD_BURST(37, 334, 384, "BLOOD BURST", 21, "ancients"),
    ICE_BURST(38, 326, 381, "ICE BURST", 22, "ancients"),
    SMOKE_BLITZ(39, 331, 389, "SMOKE BLITZ", 23, "ancients"),
    SHADOW_BLITZ(40, 339, 389, "SHADOW BLITZ", 24, "ancients"),
    BLOOD_BLITZ(41, 335, 385, "BLOOD BLITZ", 25, "ancients"),
    ICE_BLITZ(42, 327, 377, "ICE BLITZ", 26, "ancients"),
    SMOKE_BARRAGE(43, 332, 382, "SMOKE BARRAGE", 27, "ancients"),
    SHADOW_BARRAGE(44, 340, 390, "SHADOW BARRAGE", 28, "ancients"),
    BLOOD_BARRAGE(45, 336, 386, "BLOOD BARRAGE", 29, "ancients"),
    ICE_BARRAGE(46, 328, 378, "ICE BARRAGE", 30, "ancients"),
    IBAN_BLAST(47, 53, 103, "IBAN BLAST", 25, "standard"),
    WIND_SURGE(48, 362, 412, "WIND SURGE", 21, "standard"),
    WATER_SURGE(49, 363, 413, "WATER SURGE", 22, "standard"),
    EARTH_SURGE(50,364, 414, "EARTH SURGE", 23, "standard"),
    FIRE_SURGE(51, 365, 415, "FIRE SURGE", 24, "standard"),
    SARADOMIN_STRIKE(52, 61, 111, "SARADOMIN STRIKE", 20, "standard"),
    INFERIOR_DEMONBANE(53,1302, 1321, "INFERIOR DEMONBANE", 16, "arceuss"),
    SUPERIOR_DEMONBANE(54, 1303, 1322, "SUPERIOR DEMONBANE", 23, "arceuss"),
    DARK_DEMONBANE(55, 1304, 1323, "DARK DEMONBANE", 30, "arceuss"),
    GHOSTLY_GRASP(56,1267, 1292, "GHOSTLY GRASP", 12, "arceuss"),
    SKELETAL_GRASP(57,1268, 1293, "SKELETAL GRASP", 17, "arceuss"),
    UNDEAD_GRASP(58, 1269, 1294, "UNDEAD GRASP", 24, "arceuss");


    @Getter
    private final int autocastVarbitValue;
    @Getter
    private final int spriteID;
    @Getter
    private final int disabledSpriteID;
    @Getter
    private final String name;
    @Getter
    private final int baseDamage;
    @Getter
    private final String spellbook;

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

    CombatSpell(int autocastVarbitValue, int spriteID, int disabledSpriteID, String name, int baseDamage, String spellbook)
    {
        this.autocastVarbitValue = autocastVarbitValue;
        this.spriteID = spriteID;
        this.disabledSpriteID = disabledSpriteID;
        this.name = name;
        this.baseDamage = baseDamage;
        this.spellbook = spellbook;
    }
}
