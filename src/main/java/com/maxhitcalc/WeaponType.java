/*
 * Copyright (c) 2017, honeyhoney <https://github.com/honeyhoney>
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

// From AttackStyles RuneLite plugin
// Located at: https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/attackstyles/WeaponType.java
// or net.runelite.client.plugins.attackstyles.AttackStyle

package com.maxhitcalc;

import net.runelite.api.Client;
import net.runelite.api.EnumID;
import net.runelite.api.ParamID;
import net.runelite.api.Varbits;
import net.runelite.api.StructComposition;

import static com.maxhitcalc.AttackStyle.*;

public class WeaponType
{
    // Modified From runelite.client.plugins.attackstyles.AttackStyle
    protected static AttackStyle[] getWeaponTypeStyles(Client client, int weaponType)
    {
        // Keris partisan returns 30; values can only be from 0-28 ?
        if (weaponType == 30)
            weaponType = 17; // set to equivalent weapon type; accurate, aggr, aggr, defensive

        // Blue moon spear returns 22; enum undefined at 22, change to regular bladed spear: 21
        if (weaponType == 22)
            weaponType = 21;

        int weaponStyleEnum = client.getEnum(EnumID.WEAPON_STYLES).getIntValue(weaponType);
        int[] weaponStyleStructs = client.getEnum(weaponStyleEnum).getIntVals();

        AttackStyle[] styles = new AttackStyle[weaponStyleStructs.length];
        int i = 0;
        for (int style : weaponStyleStructs)
        {
            StructComposition attackStyleStruct = client.getStructComposition(style);
            String attackStyleName = attackStyleStruct.getStringValue(ParamID.ATTACK_STYLE_NAME);

            AttackStyle attackStyle = AttackStyle.valueOf(attackStyleName.toUpperCase());
            if (attackStyle == OTHER)
            {
                // "Other" is used for no style
                ++i;
                continue;
            }

            // "Defensive" is used for Defensive and also Defensive casting
            if (attackStyle == DEFENSIVE)
            {
                // Powered Staves Check, has "defensive" but should be "defensive casting"
                if (weaponType == 24)
                    attackStyle = DEFENSIVE_CASTING;
            }

            styles[i++] = attackStyle;
        }
        return styles;
    }

}