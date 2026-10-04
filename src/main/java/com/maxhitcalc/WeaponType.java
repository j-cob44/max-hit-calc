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
import net.runelite.api.StructComposition;
import net.runelite.api.SpriteID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.gameval.InterfaceID;

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

    public static boolean isCrushStyle(Client client, int attackStyleID)
    {
        int componentId;

        switch (attackStyleID)
        {
            case 0:
                componentId = InterfaceID.CombatInterface._0;
                break;
            case 1:
                componentId = InterfaceID.CombatInterface._1;
                break;
            case 2:
                componentId = InterfaceID.CombatInterface._2;
                break;
            case 3:
                componentId = InterfaceID.CombatInterface._3;
                break;
            default:
                return false;
        }

        Widget styleWidget = client.getWidget(componentId);

        if(styleWidget == null)
        {
            return false;
        }

        return containsCrushSprite(styleWidget);
    }

    private static boolean containsCrushSprite(Widget widget)
    {
        if(widget == null)
        {
            return false;
        }

        int spriteId = widget.getSpriteId();

        switch (spriteId)
        {
            case SpriteID.COMBAT_STYLE_AXE_SMASH:

            case SpriteID.COMBAT_STYLE_SPEAR_POUND:

            case SpriteID.COMBAT_STYLE_MACE_PUMMEL:
            case SpriteID.COMBAT_STYLE_MACE_POUND:
            case SpriteID.COMBAT_STYLE_MACE_BLOCK:

            case SpriteID.COMBAT_STYLE_UNARMED_KICK:
            case SpriteID.COMBAT_STYLE_UNARMED_BLOCK:

            case SpriteID.COMBAT_STYLE_STAFF_BASH:
            case SpriteID.COMBAT_STYLE_STAFF_POUND:
            case SpriteID.COMBAT_STYLE_STAFF_BLOCK:

            case SpriteID.COMBAT_STYLE_PICKAXE_SMASH:
            case SpriteID.COMBAT_STYLE_PICKAXE_BLOCK:

            case SpriteID.COMBAT_STYLE_HAMMER_POUND:
            case SpriteID.COMBAT_STYLE_HAMMER_PUMMEL:
            case SpriteID.COMBAT_STYLE_HAMMER_BLOCK:
                return true;
        }

        Widget[] children = widget.getChildren();

        if(children != null)
        {
            for(Widget child : children)
            {
                if(containsCrushSprite(child))
                {
                    return true;
                }
            }
        }

        Widget[] dynamicChildren = widget.getDynamicChildren();

        if(dynamicChildren != null)
        {
            for(Widget child : dynamicChildren)
            {
                if(containsCrushSprite(child))
                {
                    return true;
                }
            }
        }

        Widget[] staticChildren = widget.getStaticChildren();

        if(staticChildren != null)
        {
            for(Widget child : staticChildren)
            {
                if(containsCrushSprite(child))
                {
                    return true;
                }
            }
        }

        return false;
    }

}