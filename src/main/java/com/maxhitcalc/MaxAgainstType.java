/* MaxAgainstType.java
 * Contains the function for calculating the max hit against specific type.
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

import net.runelite.api.*;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import java.util.ArrayList;
import java.util.List;

public class MaxAgainstType extends MaxHit {
    static List<Double> getTypeBonus(Client client, AttackStyle attackStyle, String weaponName, Item[] playerEquipment)
    {
        List<Double> typeBonusToApply = new ArrayList<>();

        String amuletItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.AMULET.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()] != null)
        {
            amuletItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName();
        }

        String headItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.HEAD.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()] != null)
        {
            headItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName();
        }

        String bodyItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.BODY.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()] != null)
        {
            bodyItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName();
        }

        String legsItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.LEGS.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()] != null)
        {
            legsItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName();
        }

        String shieldItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.SHIELD.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()] != null)
        {
            shieldItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()].getId()).getName();
        }

        /*
         Order bonuses by when the bonus was added to the game, not when the item was added
        */

        // Melee Checks
        if(attackStyle == AttackStyle.AGGRESSIVE || attackStyle == AttackStyle.CONTROLLED || attackStyle == AttackStyle.ACCURATE || attackStyle == AttackStyle.DEFENSIVE)
        {
            // Undead and Slayer checks, they are mutually exclusive
            // Salve Amulet (e), Added 22 January 2007
            if (amuletItemName.contains("Salve amulet (e)"))
            {
                typeBonusToApply.add(1.2);
            }
            else if (amuletItemName.contains("Salve amulet(ei)"))
            {
                typeBonusToApply.add(1.2);
            }
            // Black Mask, Added 4 July 2006
            else if (headItemName.contains("Black mask"))
            {
                typeBonusToApply.add(1.1667);
            }
            // Slayer Helm, same as black mask, Attribute Added 4 July 2006
            else if (headItemName.contains("Slayer helmet"))
            {
                typeBonusToApply.add(1.1667);
            }
            else if (headItemName.contains("slayer helmet"))
            {
                typeBonusToApply.add(1.1667);
            }
            // Salve Amulet, Added 21 December 2004
            else if (amuletItemName.contains("Salve amulet"))
            {
                typeBonusToApply.add(1.15);
            }

            // Demonbane, added 4 January 2001
            if(weaponName.contains("Silverlight"))
            {
                typeBonusToApply.add(1.6) ;
            }
            else if(weaponName.contains("Darklight"))
            {
                typeBonusToApply.add(1.6); // same bonus as silverlight
            }

            // Shades, added 22 March 2006
            if(weaponName.contains("Gadderhammer"))
            {
                typeBonusToApply.add(1.25);
            }

            // Demonbane, added 9 June 2016
            if(weaponName.contains("Arclight"))
            {
                typeBonusToApply.add(1.7); // different from silverlight and darklight
            }

            // Leaf-bladed Battleaxe vs Turoths and Kurasks, 15 September 2016
            if(weaponName.contains("Leaf-bladed battleaxe"))
            {
                typeBonusToApply.add(1.175);
            }

            // Dragonbane, added 5 January 2017
            if(weaponName.contains("Dragon hunter"))
            {
                typeBonusToApply.add(1.2); // same as dragon hunter crossbow boost which was added first
            }

            // Vampyre, added 24 May 2018
            if(weaponName.contains("Ivandis flail"))
            {
                typeBonusToApply.add(1.2);
            }

            // Wilderness, added 26 July 2018
            if(weaponName.contains("Viggora's"))
            {
                if(!weaponName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Wilderness, added 26 July 2018
            if(weaponName.contains("Ursine chainmace"))
            {
                if(!weaponName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Inquisitor's armor, added 6 February 2020, set effect added 16 April 2020
            int inquisitorPieces = 0;
            if (headItemName.contains("Inquisitor's"))
            {
                inquisitorPieces += 1;
                typeBonusToApply.add(1.025);
            }

            if (bodyItemName.contains("Inquisitor's"))
            {
                inquisitorPieces += 1;
            }

            if (legsItemName.contains("Inquisitor's"))
            {
                inquisitorPieces += 1;
            }

            // Get total for inquisitors
            if(inquisitorPieces != 0)
            {
                double inquisitorTotal = inquisitorPieces * 0.05;

                if(inquisitorPieces == 3)
                {
                    inquisitorTotal += 0.1;
                }

                typeBonusToApply.add(inquisitorTotal);
            }

            // Vampyre, added 4 June 2020
            if(weaponName.contains("Blisterwood flail"))
            {
                typeBonusToApply.add(1.25);
            }

            // Golem bonus, added 14 April 2021, updated on 28 April 2021
            if(weaponName.contains("Barronite mace"))
            {
                typeBonusToApply.add(1.15);
            }

            // Kalphite, acording to Mod Ash, added with Partisan, 27 April 2022
            if(weaponName.contains("Keris"))
            {
                typeBonusToApply.add(1.33);
            }

        }
        // Ranged Checks
        else if (attackStyle == AttackStyle.RANGING || attackStyle == AttackStyle.LONGRANGE)
        {
            // Salve Amulet (ei), added 1 May 2014
            if (amuletItemName.contains("Salve amulet(ei)"))
            {
                typeBonusToApply.add(1.2);
            }
            // Salve Amulet (i), added 1 May 2014
            else if (amuletItemName.contains("Salve amulet(i)"))
            {
                typeBonusToApply.add(1.1667) ;
            }
            // Black Mask (i), added 26 September 2013
            else if (headItemName.contains("Black mask"))
            {
                if (headItemName.contains("(i)"))
                {
                    typeBonusToApply.add(1.15);
                }
            }
            // Slayer helm (i)
            else if (headItemName.contains("Slayer helmet (i)"))
            {
                typeBonusToApply.add(1.15); // same as black mask (i) boost which was added first
            }
            else if (headItemName.contains("slayer helmet (i)"))
            {
                typeBonusToApply.add(1.15); // same as black mask (i) boost which was added first
            }

            // Dragonbane, added 5 January 2017
            if(weaponName.contains("Dragon hunter"))
            {
                typeBonusToApply.add(1.25);
            }

            // Wilderness, added 26 July 2018
            if(weaponName.contains("Craw's bow"))
            {
                if(!weaponName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Wilderness, added 26 July 2018
            if(weaponName.contains("Webweaver bow"))
            {
                if(!weaponName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

        }
        // Magic Checks
        else if (attackStyle == AttackStyle.CASTING || attackStyle == AttackStyle.DEFENSIVE_CASTING)
        {
            // Salve Amulet (ei), added 1 May 2014
            if (amuletItemName.contains("Salve amulet(ei)"))
            {
                typeBonusToApply.add(1.2);
            }
            // Salve Amulet (i), added 1 May 2014
            else if (amuletItemName.contains("Salve amulet(i)"))
            {
                typeBonusToApply.add(1.15) ;
            }
            // Black Mask (i), added 26 September 2013
            else if (headItemName.contains("Black mask"))
            {
                if (headItemName.contains("(i)"))
                {
                    typeBonusToApply.add(1.15);
                }
            }
            // Slayer helm (i)
            else if (headItemName.contains("Slayer helmet (i)"))
            {
                typeBonusToApply.add(1.15); // same as black mask (i) boost which was added first
            }
            else if (headItemName.contains("slayer helmet (i)"))
            {
                typeBonusToApply.add(1.15); // same as black mask (i) boost which was added first
            }

            // Wilderness, added 26 July 2018
            if(weaponName.contains("Thammaron's sceptre"))
            {
                if(!weaponName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Wilderness, added 26 July 2018
            if(weaponName.contains("Accursed sceptre"))
            {
                if(!weaponName.contains("u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }
        }

        return typeBonusToApply; // List of Modifiers
    }

    // Needed in Magic for Slayer Staff (e)
    public static double getSpellBaseHit(Client client, Item[] playerEquipment, AttackStyle weaponAttackStyle, double magicLevel)
    {
        int spellSpriteID = -1;
        double basehit = 0;

        String weaponItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.WEAPON.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()] != null)
        {
            weaponItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName();
        }

        String capeItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.CAPE.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.CAPE.getSlotIdx()] != null)
        {
            capeItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.CAPE.getSlotIdx()].getId()).getName();
        }

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Magic Weapon: " + client.getItemDefinition(playerItems[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName(), null);

        // Powered Staff Check
        // Trident of the Seas
        if(weaponItemName.contains("of the seas"))
        {
            basehit = Math.max((Math.floor((Math.min(magicLevel, 125) - 15) / 3)), 1); // Corrected, thanks to Mod Ash
        }
        // Trident of the Swamp
        else if(weaponItemName.contains("of the swamp"))
        {
            basehit = Math.max((Math.floor(((Math.min(magicLevel, 125) - 6) / 3))), 3); // Corrected, thanks to Mod Ash
        }
        // Sanquinesti Staff
        else if(weaponItemName.contains("Sanguinesti"))
        {
            basehit = Math.max((Math.floor(((Math.min(magicLevel, 125) - 3) / 3))), 4); // Corrected, thanks to Mod Ash
        }
        // Thammaron's Sceptre
        else if(weaponItemName.contains("Thammaron's"))
        {
            basehit = (Math.floor(magicLevel/3) - 8);
        }
        // Accursed Sceptre
        else if(weaponItemName.contains("Accursed"))
        {
            basehit = (Math.floor(magicLevel/3) - 6);
        }
        // Tumeken's Shadow
        else if(weaponItemName.contains("Tumeken"))
        {
            basehit = (Math.floor(magicLevel/3) + 1);
        }
        // Crystal staff (basic)
        else if(weaponItemName.contains("Crystal staff (basic)"))
        {
            basehit = 23;
        }
        // Crystal staff (attuned)
        else if(weaponItemName.contains("Crystal staff (attuned)"))
        {
            basehit = 31;
        }
        // Crystal staff (perfected)
        else if(weaponItemName.contains("Crystal staff (perfected)"))
        {
            basehit = 39;
        }
        // Autocasted Spell
        else
        {
            // Check if casting without spell selected
            if(client.getWidget(WidgetInfo.COMBAT_SPELL_ICON) == null)
            {
                return -1; // error
            }

            // Get Spell Sprite ID
            if (weaponAttackStyle.equals(AttackStyle.CASTING))
            {
                spellSpriteID = client.getWidget(WidgetInfo.COMBAT_SPELL_ICON).getSpriteId();
            }
            else if (weaponAttackStyle.equals(AttackStyle.DEFENSIVE_CASTING))
            {
                spellSpriteID = client.getWidget(WidgetInfo.COMBAT_DEFENSIVE_SPELL_ICON).getSpriteId();
            }

            CombatSpell selectedSpell = CombatSpell.getSpellBySpriteID(spellSpriteID);

            // Debug
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Selected Spell Sprite ID: " + spellSpriteID, null);
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Selected Spell: " + selectedSpell, null);

            // Specific Selected Spell Cases
            if (selectedSpell != null)
            {
                // Magic Dart Case
                if(selectedSpell == CombatSpell.MAGIC_DART)
                {
                    double magicDartDamage = Math.floor(magicLevel * ((double)1/10)) + 10;

                    if(weaponItemName.contains("Slayer's staff (e)"))
                    {
                        magicDartDamage = Math.floor(magicLevel * ((double)1/6)) + 13;
                    }

                    basehit = magicDartDamage;
                }
                else
                {
                    basehit = selectedSpell.getBaseDamage();
                }

                // God Spell Cases with Charge
                if((selectedSpell == CombatSpell.FLAMES_OF_ZAMORAK) || (selectedSpell == CombatSpell.CLAWS_OF_GUTHIX) || (selectedSpell == CombatSpell.SARADOMIN_STRIKE))
                {
                    if (client.getVarpValue(VarPlayer.CHARGE_GOD_SPELL) > 0)
                    {
                        if(selectedSpell == CombatSpell.CLAWS_OF_GUTHIX &&
                                (capeItemName.toLowerCase().contains("guthix cape") ||  capeItemName.toLowerCase().contains("guthix max cape")))
                        {
                            basehit = 30;
                        }
                        else if(selectedSpell == CombatSpell.FLAMES_OF_ZAMORAK &&
                                (capeItemName.toLowerCase().contains("zamorak cape") || capeItemName.toLowerCase().contains("zamorak max cape")))
                        {
                            basehit = 30;
                        }
                        else if(selectedSpell == CombatSpell.SARADOMIN_STRIKE &&
                                (capeItemName.toLowerCase().contains("saradomin cape") || capeItemName.toLowerCase().contains("saradomin max cape")))
                        {
                            basehit = 30;
                        }
                        else
                        {
                            basehit = 20;
                        }
                    }
                    else
                    {
                        basehit = 20;
                    }
                }
            }
        }

        return basehit;
    }

    public static double calculateMagicMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        double spellBaseMaxHit = getSpellBaseHit(client, playerEquipment, weaponAttackStyle, client.getBoostedSkillLevel(Skill.MAGIC));

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = getMagicEquipmentBoost(client, itemManager, playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Step 3: Calculate Bonuses
        // Tome Bonuses
        double correctTomeSpellBonus = getTomeSpellBonus(client, playerEquipment, weaponAttackStyle); // default 1
        maxDamage = maxDamage * correctTomeSpellBonus;

        return maxDamage;
    }

    private static double calculateTypeMaxHit(Client client, ItemManager itemManager, MaxHitCalcConfig config)
    {
        int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
        int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

        // Get Current Attack Style
        WeaponType weaponType = WeaponType.getWeaponType(weaponTypeID);
        AttackStyle[] weaponAttackStyles = weaponType.getAttackStyles();

        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];

        // Get Current Equipment
        Item[] playerEquipment;
        if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
        {
            playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
        }
        else {
            playerEquipment = null;
        }

        // Find what type to calculate
        if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE))
        {
            return calculateMeleeMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
        }
        else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
        {
            return calculateRangedMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID, config.blowpipeDartType());
        }
        else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
        {
            return calculateMagicMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
        }
        else
        {
            return -1;
        }
    }

    // Calculate Max Hit against Type bonus
    public static double calculate(Client client, ItemManager itemManager, MaxHitCalcConfig config)
    {
        // Get Current Equipment
        Item[] playerEquipment;
        if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
        {
            playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
        }
        else
        {
            return 0;
        }

        String weaponName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName();

        int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
        int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

        // Get Current Attack Style
        WeaponType weaponType = WeaponType.getWeaponType(weaponTypeID);
        AttackStyle[] weaponAttackStyles = weaponType.getAttackStyles();

        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];

        // Get Type modifier
        List<Double> typeModifiersList = MaxAgainstType.getTypeBonus(client, attackStyle, weaponName, playerEquipment);

        // Debug Modifiers
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Type Modifiers: " + typeModifiersList.toString(), null);

        // Get Max hit
        double maxHit = MaxHit.calculate(client, itemManager, config); // Normal Max
        double maxHitVsType = Math.floor(calculateTypeMaxHit(client, itemManager, config)); // Vs Type Max

        // Iterate through modifiers, flooring after multiplying
        if(!typeModifiersList.isEmpty())
        {
            for (double modifier: typeModifiersList)
            {
                maxHitVsType = Math.floor(maxHitVsType * modifier);
            }
        }

        if(maxHit >= maxHitVsType)
        {
            return 0; // No Type Bonus
        }
        else
        {
            return maxHitVsType;
        }

    }
}
