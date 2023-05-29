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

enum CombatSpell
{
    WIND_STRIKE(15, 65, "WIND STRIKE", 2),
    WATER_STRIKE(17, 67, "WATER STRIKE", 4),
    EARTH_STRIKE(19, 69, "EARTH STRIKE", 6),
    FIRE_STRIKE(21, 71, "FIRE STRIKE", 8),
    WIND_BOLT(23, 73, "WIND BOLT", 9),
    WATER_BOLT(26, 76, "WATER BOLT", 10),
    EARTH_BOLT(29, 79, "EARTH BOLT", 11),
    FIRE_BOLT(32, 82, "FIRE BOLT", 12),
    WIND_BLAST(35, 85, "WIND BLAST", 13),
    WATER_BLAST(38, 88, "WATER BLAST",14),
    EARTH_BLAST(40, 90, "EARTH BLAST", 15),
    FIRE_BLAST(44, 94, "FIRE BLAST", 16),
    WIND_WAVE(46, 96, "WIND WAVE", 17),
    WATER_WAVE(48, 98, "WATER WAVE", 18),
    EARTH_WAVE(51, 101, "EARTH WAVE", 19),
    FIRE_WAVE(52, 102, "FIRE WAVE", 20),
    WIND_SURGE(362, 412, "WIND SURGE", 21),
    WATER_SURGE(363, 413, "WATER SURGE", 22),
    EARTH_SURGE(364, 414, "EARTH SURGE", 23),
    FIRE_SURGE(365, 415, "FIRE SURGE", 24),
    CRUMBLE_UNDEAD(34, 84, "CRUMBLE UNDEAD", 15),
    SARADOMIN_STRIKE(61, 111, "SARADOMIN STRIKE", 20),
    CLAWS_OF_GUTHIX(60, 110, "CLAWS OF GUTHIX", 20),
    FLAMES_OF_ZAMORAK(59, 109, "FLAMES OF ZAMORAK", 20),
    IBAN_BLAST(53, 103, "IBAN BLAST", 25),
    SMOKE_RUSH(329, 379, "SMOKE RUSH", 13),
    SHADOW_RUSH(337, 387, "SHADOW RUSH", 14),
    BLOOD_RUSH(333, 383, "BLOOD RUSH", 15),
    ICE_RUSH(325, 375, "ICE RUSH", 16),
    SMOKE_BURST(330, 380, "SMOKE BURST", 17),
    SHADOW_BURST(338, 388, "SHADOW BURST", 18),
    BLOOD_BURST(334, 384, "BLOOD BURST", 21),
    ICE_BURST(326, 381, "ICE BURST", 22),
    SMOKE_BLITZ(331, 389, "SMOKE BLITZ", 23),
    SHADOW_BLITZ(339, 389, "SHADOW BLITZ", 24),
    BLOOD_BLITZ(335, 385, "BLOOD BLITZ", 25),
    ICE_BLITZ(327, 377, "ICE BLITZ", 26),
    SMOKE_BARRAGE(332, 382, "SMOKE BARRAGE", 27),
    SHADOW_BARRAGE(340, 390, "SHADOW BARRAGE", 28),
    BLOOD_BARRAGE(336, 386, "BLOOD BARRAGE", 29),
    ICE_BARRAGE(328, 378, "ICE BARRAGE", 30),
    MAGIC_DART(324, 374, "MAGIC DART", 10),
    GHOSTLY_GRASP(1267, 1292, "GHOSTLY GRASP", 12),
    SKELETAL_GRASP(1268, 1293, "SKELETAL GRASP", 17),
    UNDEAD_GRASP(1269, 1294, "UNDEAD GRASP", 24),
    INFERIOR_DEMONBANE(1302, 1321, "INFERIOR DEMONBANE", 16),
    SUPERIOR_DEMONBANE(1303, 1322, "SUPERIOR DEMONBANE", 23),
    DARK_DEMONBANE(1304, 1323, "DARK DEMONBANE", 30);

    @Getter
    private final int spriteID;
    @Getter
    private final int disabledSpriteID;
    @Getter
    private final String name;
    @Getter
    private final int baseDamage;

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

    CombatSpell(int spriteID, int disabledSpriteID, String name, int baseDamage)
    {
        this.spriteID = spriteID;
        this.disabledSpriteID = disabledSpriteID;
        this.name = name;
        this.baseDamage = baseDamage;
    }
}
