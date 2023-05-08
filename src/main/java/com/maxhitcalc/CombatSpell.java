//

package com.maxhitcalc;

import lombok.Getter;

enum CombatSpell {

    WIND_STRIKE(15, "WIND STRIKE", 2),
    WATER_STRIKE(17, "WATER STRIKE", 4),
    EARTH_STRIKE(19, "EARTH STRIKE", 6),
    FIRE_STRIKE(21, "FIRE STRIKE", 8),
    WIND_BOLT(23, "WIND BOLT", 9),
    WATER_BOLT(26, "WATER BOLT", 10),
    EARTH_BOLT(29, "EARTH BOLT", 11),
    FIRE_BOLT(32, "FIRE BOLT", 12),
    WIND_BLAST(35, "WIND BLAST", 13),
    WATER_BLAST(38, "WATER BLAST",14),
    EARTH_BLAST(40, "EARTH BLAST", 15),
    FIRE_BLAST(44, "FIRE BLAST", 16),
    WIND_WAVE(46,"WIND WAVE", 17),
    WATER_WAVE(48, "WATER WAVE", 18),
    EARTH_WAVE(51, "EARTH WAVE", 19),
    FIRE_WAVE(52, "FIRE WAVE", 20),
    WIND_SURGE(362,"WIND SURGE", 21),
    WATER_SURGE(363, "WATER SURGE", 22),
    EARTH_SURGE(364, "EARTH SURGE", 23),
    FIRE_SURGE(365, "FIRE SURGE", 24),
    CRUMBLE_UNDEAD(34, "CRUMBLE UNDEAD", 15),
    SARADOMIN_STRIKE(61, "SARADOMIN STRIKE", 20),
    CLAWS_OF_GUTHIX(60, "CLAWS OF GUTHIX", 20),
    FLAMES_OF_ZAMORAK(59, "FLAMES OF ZAMORAK", 20),
    IBANS_BLAST(59, "IBANS BLAST", 25),
    SMOKE_RUSH(329, "SMOKE RUSH", 13),
    SHADOW_RUSH(337, "SHADOW RUSH", 14),
    BLOOD_RUSH(333, "BLOOD RUSH", 15),
    ICE_RUSH(325, "ICE RUSH", 16),
    SMOKE_BURST(330, "SMOKE BURST", 17),
    SHADOW_BURST(338, "SHADOW BURST", 18),
    BLOOD_BURST(334, "BLOOD BURST", 21),
    ICE_BURST(326, "ICE BURST", 22),
    SMOKE_BLITZ(331, "SMOKE BLITZ", 23),
    SHADOW_BLITZ(339, "SHADOW BLITZ", 24),
    BLOOD_BLITZ(335, "BLOOD BLITZ", 25),
    ICE_BLITZ(327, "ICE BLITZ", 26),
    SMOKE_BARRAGE(332, "SMOKE BARRAGE", 27),
    SHADOW_BARRAGE(340, "SHADOW BARRAGE", 28),
    BLOOD_BARRAGE(336, "BLOOD BARRAGE", 29),
    ICE_BARRAGE(328, "ICE BARRAGE", 30),
    MAGIC_DART(324, "MAGIC DART", 10);

    @Getter
    private final int spriteID;
    @Getter
    private final String name;
    @Getter
    private final int baseDamage;

    public static CombatSpell getSpellBySpriteID(int spriteID){
        for (CombatSpell spell : CombatSpell.values()) {
            if(spell.spriteID == spriteID){
                return spell;
            }
        }

        // error, failure
        return null;
    }

    CombatSpell(int spriteID, String name, int baseDamage)
    {
        this.spriteID = spriteID;
        this.name = name;
        this.baseDamage = baseDamage;
    }
}
