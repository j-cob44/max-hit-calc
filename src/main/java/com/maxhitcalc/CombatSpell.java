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
 */
public enum CombatSpell
{
    // In order of autocast varbit
    WIND_STRIKE(1, 15, 65, "WIND STRIKE", 2),
    WATER_STRIKE(2, 17, 67, "WATER STRIKE", 4),
    EARTH_STRIKE(3, 19, 69, "EARTH STRIKE", 6),
    FIRE_STRIKE(4, 21, 71, "FIRE STRIKE", 8),
    WIND_BOLT(5,23, 73, "WIND BOLT", 9),
    WATER_BOLT(6,26, 76, "WATER BOLT", 10),
    EARTH_BOLT(7,29, 79, "EARTH BOLT", 11),
    FIRE_BOLT(8,32, 82, "FIRE BOLT", 12),
    WIND_BLAST(9,35, 85, "WIND BLAST", 13),
    WATER_BLAST(10,38, 88, "WATER BLAST",14),
    EARTH_BLAST(11,40, 90, "EARTH BLAST", 15),
    FIRE_BLAST(12,44, 94, "FIRE BLAST", 16),
    WIND_WAVE(13,46, 96, "WIND WAVE", 17),
    WATER_WAVE(14,48, 98, "WATER WAVE", 18),
    EARTH_WAVE(15,51, 101, "EARTH WAVE", 19),
    FIRE_WAVE(16,52, 102, "FIRE WAVE", 20),
    CRUMBLE_UNDEAD(17,34, 84, "CRUMBLE UNDEAD", 15),
    MAGIC_DART(18, 324, 374, "MAGIC DART", 10),
    CLAWS_OF_GUTHIX(19, 60, 110, "CLAWS OF GUTHIX", 20),
    FLAMES_OF_ZAMORAK(20, 59, 109, "FLAMES OF ZAMORAK", 20),

    SMOKE_RUSH(31, 329, 379, "SMOKE RUSH", 13),
    SHADOW_RUSH(32, 337, 387, "SHADOW RUSH", 14),
    BLOOD_RUSH(33,333, 383, "BLOOD RUSH", 15),
    ICE_RUSH(34,325, 375, "ICE RUSH", 16),
    SMOKE_BURST(35, 330, 380, "SMOKE BURST", 17),
    SHADOW_BURST(36, 338, 388, "SHADOW BURST", 18),
    BLOOD_BURST(37, 334, 384, "BLOOD BURST", 21),
    ICE_BURST(38, 326, 381, "ICE BURST", 22),
    SMOKE_BLITZ(39, 331, 389, "SMOKE BLITZ", 23),
    SHADOW_BLITZ(40, 339, 389, "SHADOW BLITZ", 24),
    BLOOD_BLITZ(41, 335, 385, "BLOOD BLITZ", 25),
    ICE_BLITZ(42, 327, 377, "ICE BLITZ", 26),
    SMOKE_BARRAGE(43, 332, 382, "SMOKE BARRAGE", 27),
    SHADOW_BARRAGE(44, 340, 390, "SHADOW BARRAGE", 28),
    BLOOD_BARRAGE(45, 336, 386, "BLOOD BARRAGE", 29),
    ICE_BARRAGE(46, 328, 378, "ICE BARRAGE", 30),
    IBAN_BLAST(47, 53, 103, "IBAN BLAST", 25),
    WIND_SURGE(48, 362, 412, "WIND SURGE", 21),
    WATER_SURGE(49, 363, 413, "WATER SURGE", 22),
    EARTH_SURGE(50,364, 414, "EARTH SURGE", 23),
    FIRE_SURGE(51, 365, 415, "FIRE SURGE", 24),
    SARADOMIN_STRIKE(52, 61, 111, "SARADOMIN STRIKE", 20),
    INFERIOR_DEMONBANE(53,1302, 1321, "INFERIOR DEMONBANE", 16),
    SUPERIOR_DEMONBANE(54, 1303, 1322, "SUPERIOR DEMONBANE", 23),
    DARK_DEMONBANE(55, 1304, 1323, "DARK DEMONBANE", 30),
    GHOSTLY_GRASP(56,1267, 1292, "GHOSTLY GRASP", 12),
    SKELETAL_GRASP(57,1268, 1293, "SKELETAL GRASP", 17),
    UNDEAD_GRASP(58, 1269, 1294, "UNDEAD GRASP", 24);


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

    CombatSpell(int autocastVarbitValue, int spriteID, int disabledSpriteID, String name, int baseDamage)
    {
        this.autocastVarbitValue = autocastVarbitValue;
        this.spriteID = spriteID;
        this.disabledSpriteID = disabledSpriteID;
        this.name = name;
        this.baseDamage = baseDamage;
    }
}
