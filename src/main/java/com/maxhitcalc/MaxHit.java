/* MaxHit.java
 * Contains all functions necessary for calculating the normal max hit per attack type.
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

/**
 * Contains functions for calculating standard max hit.
 */
public class MaxHit {
    // Get Prayer Bonus for Max Hit Calculation
    protected static double getPrayerBonus(Client client, AttackStyle weaponAttackStyle)
    {
        // Melee Prayers
        if(weaponAttackStyle == AttackStyle.ACCURATE || weaponAttackStyle == AttackStyle.AGGRESSIVE || weaponAttackStyle == AttackStyle.CONTROLLED || weaponAttackStyle == AttackStyle.DEFENSIVE)
        {
            if(client.isPrayerActive((Prayer.BURST_OF_STRENGTH))) return 1.05;

            if(client.isPrayerActive((Prayer.SUPERHUMAN_STRENGTH))) return 1.1;

            if(client.isPrayerActive((Prayer.ULTIMATE_STRENGTH))) return 1.15;

            if(client.isPrayerActive((Prayer.CHIVALRY))) return 1.18;

            if(client.isPrayerActive((Prayer.PIETY))) return 1.23;
        }

        // Ranged Prayers
        if(weaponAttackStyle == AttackStyle.RANGING || weaponAttackStyle == AttackStyle.LONGRANGE)
        {
            if(client.isPrayerActive((Prayer.SHARP_EYE))) return 1.05;

            if(client.isPrayerActive((Prayer.HAWK_EYE))) return 1.1;

            if(client.isPrayerActive((Prayer.EAGLE_EYE))) return 1.15;

            if(client.isPrayerActive((Prayer.RIGOUR))) return 1.23;
        }

        return 1; // default
    }

    // Get Attack Style Bonus for Melee or Ranged
    protected static int getAttackStyleBonus(AttackStyle weaponAttackStyle, int attackStyleID)
    {
        // Return attack style bonus
        // Melee bonuses
        if (weaponAttackStyle.getName().equalsIgnoreCase("Aggressive")) return 3;

        if (weaponAttackStyle.getName().equalsIgnoreCase("Controlled")) return 1;

        // Ranged bonus
        if (weaponAttackStyle.getName().equalsIgnoreCase("RANGING"))
        {
            if(attackStyleID == 0) return 3;
        }

        return 0; // default
    }

    // Get Melee Strength Bonus from Weapon and armor
    protected static double getMeleeStrengthBonus(Client client, ItemManager itemManager, Item[] playerEquipment)
    {
        if (playerEquipment == null) return 0;

        double strengthBonus = 0;

        // Get Melee Strength Bonus of each equipped Item
        for (Item equipmentItem: playerEquipment)
        {
            if (equipmentItem != null)
            {
                if(equipmentItem.getId() != -1)
                {
                    int equipmentID = equipmentItem.getId();
                    int equipmentStrengthStat = itemManager.getItemStats(equipmentID, false).getEquipment().getStr();

                    strengthBonus += equipmentStrengthStat;
                }
            }
        }

        return strengthBonus;
    }

    protected static double getVoidMeleeBonus(Client client, Item[] playerEquipment)
    {
        if (playerEquipment == null) return 1;

        // Get required items for void check
        String headItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.HEAD);
        String bodyItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.BODY);
        String legsItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.LEGS);
        String glovesItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.GLOVES);

        // Check for set bonus
        if (headItemName.contains("Void melee"))
        {
            if (glovesItemName.contains("Void"))
            {
                // Melee helm and gloves, check for elite or not
                if (bodyItemName.contains("Void"))
                {
                    if (legsItemName.contains("Void"))
                    {
                        // Normal void set
                        return 1.1;
                    }
                }
                else if (bodyItemName.contains("Elite void"))
                {
                    if(legsItemName.contains("Elite void"))
                    {
                        // Elite void set
                        return 1.1; // same for melee
                    }
                }
            }
        }

        // Void Set incomplete, no bonus
        return 1;
    }

    // Passive Melee Set effects
    protected static List<Double> getMeleeSpecialBonusMultiplier(Client client, Item[] playerEquipment)
    {
        List<Double> specialBonusesToApply = new ArrayList<>();

        if (playerEquipment == null) return specialBonusesToApply;

        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        String headItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.HEAD);
        String bodyItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.BODY);
        String legsItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.LEGS);
        String ammuletItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.AMULET);

        // SPECIAL BONUSES MUST BE ORDERED CORRECTLY.
        // Dharok's Set Check
        if (headItemName.contains("Dharok's"))
        {
            if(bodyItemName.contains("Dharok's"))
            {
                if(legsItemName.contains("Dharok's"))
                {
                    if(weaponItemName.contains("Dharok's"))
                    {
                        // Passed Check, Dharok's Set Equipped, Apply Effect
                        double baseHP = client.getRealSkillLevel(Skill.HITPOINTS);
                        double currentHP = client.getBoostedSkillLevel(Skill.HITPOINTS);
                        double dharokBonus = ((((baseHP - currentHP) / 100) * (baseHP / 100)));

                        specialBonusesToApply.add(dharokBonus);
                    }
                }
            }
        }

        // Obsidian Set Check
        if (headItemName.contains("Obsidian"))
        {
            if(bodyItemName.contains("Obsidian"))
            {
                if(legsItemName.contains("Obsidian"))
                {
                    if(weaponItemName.contains("ket") || weaponItemName.contains("xil"))
                    {
                        specialBonusesToApply.add(0.1);
                    }
                }
            }
        }

        // Berserker Necklace and Obisidian Melee Check
        if(weaponItemName.contains("ket") || weaponItemName.contains("xil"))
        {
            if(ammuletItemName.contains("Berserker"))
            {
                specialBonusesToApply.add(0.2);
            }
        }

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Special Bonus: " + specialBonus, null);

        return specialBonusesToApply;
    }

    // Calculate Melee Max Hit
    protected static double calculateMeleeMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        // Calculate Melee Max Hit
        // Step 1: Calculate effective Strength
        int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
        double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
        int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = getVoidMeleeBonus(client, playerEquipment); // default 1;

        double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the base damage
        double strengthBonus = getMeleeStrengthBonus(client, itemManager, playerEquipment); // default 0

        double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
        double flooredBaseDamage = Math.floor(baseDamage);

        // Step 3: Calculate the bonus damage
        List<Double> specialBonusMultipliers = getMeleeSpecialBonusMultiplier(client, playerEquipment); // default empty

        double maxHit = flooredBaseDamage;

        if(!specialBonusMultipliers.isEmpty())
        {
            for (double bonus: specialBonusMultipliers)
            {
                maxHit += Math.floor(maxHit * bonus);
            }
        }

        // Osmumten's Fang Decrease
        String weaponName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        if (weaponName.contains("Osmumten's fang"))
        {
            maxHit = maxHit * 0.85 + 1;
        }

        // Complete
        return maxHit;
    }

    // Get Ranged Void Bonus for Elite and Normal Sets
    protected static double getVoidRangedBonus(Client client, Item[] playerEquipment)
    {
        // Get required items for void check
        String headItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.HEAD);
        String bodyItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.BODY);
        String legsItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.LEGS);
        String glovesItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.GLOVES);

        // Check for set bonus
        if (headItemName.contains("Void ranger"))
        {
            if (glovesItemName.contains("Void"))
            {
                // Ranged helm and gloves, check for elite or not
                if (bodyItemName.contains("Void"))
                {
                    if (legsItemName.contains("Void"))
                    {
                        // Normal void set
                        return 1.1;
                    }
                }
                else if (bodyItemName.contains("Elite void"))
                {
                    if(legsItemName.contains("Elite void"))
                    {
                        // Elite void set
                        return 1.125;
                    }
                }
            }
        }

        // Void Set incomplete, no bonus
        return 1;
    }

    // Get Ranged Strength Bonus from Equipment
    protected static double getRangedStrengthBonus(Client client, ItemManager itemManager, Item[] playerEquipment, MaxHitCalcConfig.BlowpipeDartType dartType)
    {
        double rangedStrengthBonus = 0;

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Weapon Name: " + client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName(), null);

        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        String ammoItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.AMMO);
        int ammoID = EquipmentItems.getItemIdInGivenSetSlot(playerEquipment, EquipmentInventorySlot.AMMO);

        boolean skipAmmo = false;

        // Crystal bow and Blowpipe skip ammo
        // Cases to skip ammo
        if (weaponItemName.contains("Crystal bow")
                || weaponItemName.contains("faerdhinen"))
        {
            skipAmmo = true;
        }
        else if(weaponItemName.contains("blowpipe"))
        {
            skipAmmo = true;

            if(dartType == MaxHitCalcConfig.BlowpipeDartType.ADAMANT)
            {
                rangedStrengthBonus += 17;
            }
            else if(dartType == MaxHitCalcConfig.BlowpipeDartType.RUNE)
            {
                rangedStrengthBonus += 26;
            }
            else if(dartType == MaxHitCalcConfig.BlowpipeDartType.AMETHYST)
            {
                rangedStrengthBonus += 28;
            }
            else if(dartType == MaxHitCalcConfig.BlowpipeDartType.DRAGON)
            {
                rangedStrengthBonus += 35;
            }
            else
            {
                rangedStrengthBonus += 9; // default and lowest (mithril)
            }

        }

        // Get Ranged Strength Bonus of each equipped Item
        for (Item equipmentItem: playerEquipment)
        {
            if (equipmentItem != null)
            {
                if(equipmentItem.getId() != -1)
                {
                    int equipmentID = equipmentItem.getId();

                    int equipmentStrengthStat = itemManager.getItemStats(equipmentID, false).getEquipment().getRstr();

                    if (equipmentID != ammoID || !skipAmmo) {
                        // If equipment ID == Ammo, skip if skipAmmo is true
                        rangedStrengthBonus += equipmentStrengthStat;
                    }
                }
            }
        }

        return rangedStrengthBonus;
    }

    // Get Gear Boost, for instance Crystal Armor set bonus
    protected static double getRangeGearBoost(Client client, Item[] playerEquipment){
        double damagePercentBonus = 1;

        // Get Required Item Names for checks
        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        String headItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.HEAD);
        String bodyItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.BODY);
        String legsItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.LEGS);


        if (weaponItemName.contains("Crystal bow") || weaponItemName.contains("faerdhinen"))
        {
            // Crystal Armor Damage bonus
            if (headItemName.contains("Crystal helm"))
            {
                if(!headItemName.contains("(basic)")
                        || !headItemName.contains("(attuned)")
                        || !headItemName.contains("(perfected)"))
                {
                    damagePercentBonus += 0.025;
                }
            }

            if (bodyItemName.contains("Crystal body"))
            {
                if(!bodyItemName.contains("(basic)")
                        || !bodyItemName.contains("(attuned)")
                        || !bodyItemName.contains("(perfected)"))
                {
                    damagePercentBonus += 0.075;
                }
            }

            if (legsItemName.contains("Crystal legs"))
            {
                if(!legsItemName.contains("(basic)")
                        || !legsItemName.contains("(attuned)")
                        || !legsItemName.contains("(perfected)"))
                {
                    damagePercentBonus += 0.05;
                }
            }
        }

        return damagePercentBonus;
    }

    protected static double calculateRangedMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, MaxHitCalcConfig.BlowpipeDartType dartType)
    {
        // Calculate Ranged Max Hit
        // Step 1: Calculate effective ranged Strength
        int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED);
        double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
        int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = getVoidRangedBonus(client, playerEquipment); // default 1;

        double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the max hit
        double equipmentRangedStrength = getRangedStrengthBonus(client, itemManager, playerEquipment, dartType);
        double gearBonus = getRangeGearBoost(client, playerEquipment);

        double maxHit = (0.5 + (((effectiveRangedStrength * (equipmentRangedStrength + 64))/640) * gearBonus) );

        // Step 3: Bonus damage from special attack and effects
        // Not used here.

        return maxHit;
    }

    protected static double getSpellBaseHit(Client client, Item[] playerEquipment, AttackStyle weaponAttackStyle, double magicLevel)
    {
        int spellSpriteID = -1;
        double basehit = 0;

        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        String capeItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.CAPE);

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
        // Corrupted staff (basic)
        else if(weaponItemName.contains("Corrupted staff (basic)"))
        {
            basehit = 23;
        }
        // Corrupted staff (attuned)
        else if(weaponItemName.contains("Corrupted staff (attuned)"))
        {
            basehit = 31;
        }
        // Corrupted staff (perfected)
        else if(weaponItemName.contains("Corrupted staff (perfected)"))
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

    protected static double getMagicEquipmentBoost(Client client, ItemManager itemManager, Item[] playerEquipment)
    {
        if (playerEquipment == null) return 1;

        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        double magicdamagebonus = 0;

        // Get Magic Strength Bonus of each equipped Item
        for (Item equipmentItem: playerEquipment)
        {
            if (equipmentItem != null)
            {
                if (equipmentItem.getId() != -1)
                {
                    int equipmentID = equipmentItem.getId();
                    double equipmentMagicBonusStat = itemManager.getItemStats(equipmentID, false).getEquipment().getMdmg();

                    magicdamagebonus += (equipmentMagicBonusStat/100);
                }
            }
        }

        // Get Void 2.5% bonus if necessary, otherwise +0
        magicdamagebonus += getVoidMagicBonus(client, playerEquipment);

        // Get Tumeken's Shadow Bonus
        if(weaponItemName.contains("Tumeken"))
        {
            magicdamagebonus *= 3;

            magicdamagebonus = Math.min(magicdamagebonus, 1);
        }

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Bonus Magic Damage: " + magicdamagebonus*100 + "%", null);

        return 1 + magicdamagebonus; // Default is 1.
    }

    protected static double getVoidMagicBonus(Client client, Item[] playerEquipment)
    {
        if (playerEquipment == null) return 0;

        // Get required items for void check
        String headItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.HEAD);
        String bodyItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.BODY);
        String legsItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.LEGS);
        String glovesItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.GLOVES);

        // Check for set bonus
        if (headItemName.contains("Void mage"))
        {
            if (glovesItemName.contains("Void"))
            {
                if (bodyItemName.contains("Elite void"))
                {
                    if(legsItemName.contains("Elite void"))
                    {
                        // Elite void set
                        return 0.025; // 2.5% magic dmg bonus
                    }
                }
            }
        }

        // Elite Void Set incomplete, no bonus
        return 0;
    }

    protected static double getTomeSpellBonus(Client client, Item[] playerEquipment, AttackStyle weaponAttackStyle)
    {
        int spellSpriteID = -1;

        String shieldItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.SHIELD);

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

        // Spell is selected, not casting from weapon
        if (selectedSpell != null)
        {
            if (selectedSpell.getName().toLowerCase().contains("fire"))
            {
                // Check for tome of fire
                if (shieldItemName.contains("Tome of fire"))
                {
                    if (!shieldItemName.contains("(empty)"))
                    {
                        return 1.5;
                    }
                }
            }

            if (selectedSpell.getName().toLowerCase().contains("water"))
            {
                // Check for tome of water
                if (shieldItemName.contains("Tome of water"))
                {
                    if (!shieldItemName.contains("(empty)"))
                    {
                        return 1.2;
                    }
                }
            }
        }

        return 1;
    }

    // Public Function, Calculate Standard Magic Max Hit
    protected static double calculateMagicMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle)
    {
        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        double spellBaseMaxHit = getSpellBaseHit(client, playerEquipment, weaponAttackStyle, client.getBoostedSkillLevel(Skill.MAGIC));

        if (spellBaseMaxHit == 0)
        {
            return -1;
        }

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = getMagicEquipmentBoost(client, itemManager, playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Step 3: Calculate Bonuses
        // Tome Bonuses
        double correctTomeSpellBonus = getTomeSpellBonus(client, playerEquipment, weaponAttackStyle); // default 1
        maxDamage = maxDamage * correctTomeSpellBonus;

        return maxDamage;
    }

    /**
     * Calculates the standard max hit based on current equipment and player status.
     *
     * @param client
     * @param itemManager
     * @param config
     * @return Max Hit as Double
     */
    public static double calculate(Client client, ItemManager itemManager, MaxHitCalcConfig config)
    {
        int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
        int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

        // Get Current Attack Style
        WeaponType weaponType = WeaponType.getWeaponType(weaponTypeID);
        AttackStyle[] weaponAttackStyles = weaponType.getAttackStyles();

        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];

        // Get Current Equipment
        Item[] playerEquipment = EquipmentItems.getCurrentlyEquipped(client);

        // Find what type to calculate
        if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE))
        {
            return MaxHit.calculateMeleeMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
        }
        else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
        {
            return MaxHit.calculateRangedMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID, config.blowpipeDartType());
        }
        else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
        {
            return MaxHit.calculateMagicMaxHit(client, itemManager, playerEquipment, attackStyle);
        }
        else
        {
            return -1;
        }
    }

}
