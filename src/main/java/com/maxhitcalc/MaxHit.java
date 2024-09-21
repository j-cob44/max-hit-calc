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
import net.runelite.client.game.ItemManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains functions for calculating standard max hit.
 */
public class MaxHit {

    protected MaxHitCalcPlugin plugin;
    protected MaxHitCalcConfig config;
    protected ItemManager itemManager;
    protected Client client;

    MaxHit(MaxHitCalcPlugin plugin, MaxHitCalcConfig config, ItemManager itemManager, Client client)
    {
        this.plugin = plugin;
        this.config = config;
        this.itemManager = itemManager;
        this.client = client;
    }

    // Get Prayer Bonus for Max Hit Calculation
    protected double getPrayerBonus(AttackStyle weaponAttackStyle)
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

    protected double getSoulStackBonus()
    {
        int soulStack = client.getVarpValue(3784); // Should be replaced with VarPlayer.SOUL_STACK when implemented. See this PR for more info: https://github.com/runelite/runelite/pull/17390

        return 0.06 * soulStack;
    }

    // Get Attack Style Bonus for Melee or Ranged
    protected int getAttackStyleBonus(AttackStyle weaponAttackStyle, int attackStyleID)
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
    protected double getMeleeStrengthBonus(Item[] playerEquipment)
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

                    // Ensure not null
                    if(itemManager.getItemStats(equipmentID, false) != null)
                    {
                        int equipmentStrengthStat = itemManager.getItemStats(equipmentID, false).getEquipment().getStr();

                        strengthBonus += equipmentStrengthStat;
                    }
                }
            }
        }

        return strengthBonus;
    }

    protected double getVoidMeleeBonus(Item[] playerEquipment)
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
    protected List<Double> getMeleeSpecialBonusMultiplier(Item[] playerEquipment)
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
    protected double calculateMeleeMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, boolean isSpecialAttack)
    {
        // Calculate Melee Max Hit
        // Step 1: Calculate effective Strength
        int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
        double prayerBonus = getPrayerBonus(weaponAttackStyle);
        int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = getVoidMeleeBonus(playerEquipment); // default 1;
        double soulStackBonus = getSoulStackBonus();
        double effectiveSoulStackLevel = 0;

        if (!isSpecialAttack)
        {
            effectiveSoulStackLevel = Math.floor(strengthLevel * soulStackBonus);
        }

        double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + effectiveSoulStackLevel + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the base damage
        double strengthBonus = getMeleeStrengthBonus(playerEquipment); // default 0

        double baseDamage = (0.5 + (effectiveStrength * ((strengthBonus + 64)/640)));
        double flooredBaseDamage = Math.floor(baseDamage);

        // Step 3: Calculate the bonus damage
        List<Double> specialBonusMultipliers = getMeleeSpecialBonusMultiplier(playerEquipment); // default empty

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

        // Colossal Blade Base Increase
        if(weaponName.contains("Colossal blade")){
            maxHit = maxHit + 2;
        }

        // Rat Default +10 damage Bonus
        if(weaponName.contains("Bone mace"))
        {
            maxHit = maxHit + 10;
        }

        // Complete
        return maxHit;
    }

    // Get Ranged Void Bonus for Elite and Normal Sets
    protected double getVoidRangedBonus(Item[] playerEquipment)
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
    protected double getRangedStrengthBonus(Item[] playerEquipment)
    {
        double rangedStrengthBonus = 0;

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Weapon Name: " + client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName(), null);

        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        int weaponID = EquipmentItems.getItemIdInGivenSetSlot(playerEquipment, EquipmentInventorySlot.WEAPON);

        String ammoItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.AMMO);
        int ammoID = EquipmentItems.getItemIdInGivenSetSlot(playerEquipment, EquipmentInventorySlot.AMMO);

        boolean skipAmmo = false;
        boolean calcWithMelee = false;

        // Cases to skip ammo: throwing weapons, crystal bow, blowpipe
        if (weaponItemName.contains("dart") || weaponItemName.contains("knife") || weaponItemName.contains("thrownaxe") || weaponItemName.contains("Toktz-xil-ul"))
        {
            // "Stackable" Throwing weapons
            skipAmmo = true;
        }
        else if(weaponItemName.contains("Morrigan's javelin") || weaponItemName.contains("Morrigan's throwing axe") || weaponItemName.contains("Mud pie"))
        {
            // Not Stackable Throwing Weapons
            skipAmmo = true;
        }
        else if (weaponItemName.contains("Crystal bow")
                || weaponItemName.contains("faerdhinen")
                || weaponItemName.contains("Webweaver")
                || weaponItemName.contains("Craw's"))
        {
            skipAmmo = true;
        }
        else if(weaponItemName.contains("blowpipe"))
        {
            skipAmmo = true;

            if(config.blowpipeDartType() == MaxHitCalcConfig.BlowpipeDartType.ADAMANT)
            {
                rangedStrengthBonus += 17;
            }
            else if(config.blowpipeDartType() == MaxHitCalcConfig.BlowpipeDartType.RUNE)
            {
                rangedStrengthBonus += 26;
            }
            else if(config.blowpipeDartType() == MaxHitCalcConfig.BlowpipeDartType.AMETHYST)
            {
                rangedStrengthBonus += 28;
            }
            else if(config.blowpipeDartType() == MaxHitCalcConfig.BlowpipeDartType.DRAGON)
            {
                rangedStrengthBonus += 35;
            }
            else
            {
                rangedStrengthBonus += 9; // default and lowest (mithril)
            }
        }
        else if(weaponItemName.contains("Tonalztics"))
        {
            skipAmmo = true;
        }
        else if(weaponItemName.contains("atlatl"))
        {
            skipAmmo = true;
            calcWithMelee = true;
        }
        else if(weaponItemName.contains("Hunter's spear"))
        {
            skipAmmo = true;
            calcWithMelee = true;
        }

        // Get Ranged Strength Bonus of each equipped Item
        for (Item equipmentItem: playerEquipment)
        {
            if (equipmentItem != null)
            {
                if(equipmentItem.getId() != -1)
                {
                    int equipmentID = equipmentItem.getId();

                    // Ensure not null
                    if(itemManager.getItemStats(equipmentID, false) != null)
                    {
                        if(!calcWithMelee)
                        {
                            int equipmentStrengthStat = itemManager.getItemStats(equipmentID, false).getEquipment().getRstr();

                            if (equipmentID != ammoID || !skipAmmo)
                            {
                                // If equipment ID == Ammo, skip if skipAmmo is true
                                rangedStrengthBonus += equipmentStrengthStat;
                            }
                        }
                        else
                        {
                            int equipmentStrengthStat = itemManager.getItemStats(equipmentID, false).getEquipment().getStr();

                            if (equipmentID != ammoID || !skipAmmo)
                            {
                                // If equipment ID == Ammo, skip if skipAmmo is true
                                rangedStrengthBonus += equipmentStrengthStat;
                            }
                        }

                    }
                }
            }
        }

        return rangedStrengthBonus;
    }

    // Get Gear Boost, for instance Crystal Armor set bonus
    protected double getRangeGearBoost(Item[] playerEquipment){
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

    // Calculate Ranged Max Hit
    protected double calculateRangedMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        // Calculate Ranged Max Hit
        // Step 1: Calculate effective ranged Strength
        int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED);
        double prayerBonus = getPrayerBonus(weaponAttackStyle);
        int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = getVoidRangedBonus(playerEquipment); // default 1;

        double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the max hit
        double equipmentRangedStrength = getRangedStrengthBonus(playerEquipment);
        double gearBonus = getRangeGearBoost(playerEquipment);

        double maxHit = (0.5 + (((effectiveRangedStrength * (equipmentRangedStrength + 64))/640) * gearBonus) );

        // Step 3: Bonus damage from special attack and effects
        // Rat Default +10 damage Bonus
        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        if(weaponItemName.contains("Bone shortbow"))
        {
            maxHit = maxHit + 10;
        }

        // Tonalztics of Ralos (uncharged) max hit is 75% of normal
        if(weaponItemName.contains("Tonalztics of ralos"))
        {
            maxHit = Math.floor(maxHit * 0.75); // unknown if flooring here causes miscalcs
            if(!weaponItemName.contains("(uncharged)"))
            {
                maxHit = maxHit * 2;
            }
        }

        return maxHit;
    }

    protected double getSpellBaseHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, double magicLevel)
    {
        int spellSpriteID = -1;
        double basehit = 0;

        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        String capeItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.CAPE);
        String glovesItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.GLOVES);

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
        // Warped sceptre
        else if(weaponItemName.contains("Warped sceptre"))
        {
            // Current Wiki Value
            basehit = Math.floor(((8*magicLevel)+96)/37);

            /* My math estimate
            if(magicLevel >= 99)
            {
                basehit = Math.floor((magicLevel/5) + 4.2);
            }
            else
            {
                basehit = Math.floor((magicLevel/5) + 3.6);
            }
            */
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
        // Bone Staff
        else if(weaponItemName.contains("Bone staff"))
        {
            basehit = Math.floor(magicLevel/3) + 5;
        }
        // Autocasted Spell
        else
        {
            int selectedSpellId = client.getVarbitValue(276); // Varbit 276 is Selected Autocasted Spell
            if (selectedSpellId == 0)
            {
                // no spell selected
                return -1; // error
            }

            CombatSpell selectedSpell = CombatSpell.getSpellbyVarbitValue(selectedSpellId); // returns null as default

            // Debug
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Selected Spell: " + selectedSpell, null);

            // Specific Selected Spell Cases
            if (selectedSpell == null)
            {
                System.out.println("Error");
                return -1; // error
            }
            else
            {
                // Magic Dart Case
                if(selectedSpell == CombatSpell.MAGIC_DART)
                {
                    double magicDartDamage = Math.floor(magicLevel * ((double)1/10)) + 10;

                    basehit = magicDartDamage;
                }
                else
                {
                    // FIND TIER, FIND HIGHEST IN TIER
                    if (selectedSpell.getTier() == 0)
                    {
                        // NO TIER,
                        basehit = selectedSpell.getBaseDamage();
                    }
                    else
                    {
                        // GET TIER, Get highest tier in level
                        int spellTier = selectedSpell.getTier();
                        String spellbook = selectedSpell.getSpellbook();

                        CombatSpell[] spellsInTier = CombatSpell.getSpellsOfTier(spellTier, spellbook);

                        for(CombatSpell spell : spellsInTier)
                        {
                            if(magicLevel >= spell.getReqLevel())
                            {
                                if (basehit <= spell.getBaseDamage())
                                {
                                    // new highest found
                                    basehit = spell.getBaseDamage();
                                }
                            }
                        }

                        // Error, didn't find usable spell
                        if (basehit == 0)
                            return -1; // error
                    }


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

                // Chaos Gauntlet Bonus Check
                if(selectedSpell.getName().toLowerCase().contains("bolt"))
                {
                    if (glovesItemName.toLowerCase().contains("chaos gauntlets"))
                    {
                        basehit += 3;
                    }
                }
            }
        }

        return basehit;
    }

    // Get Spell Info
    protected CombatSpell getSpell(){
        int selectedSpellId = client.getVarbitValue(276); // Varbit 276 is Selected Autocasted Spell
        if (selectedSpellId == 0)
        {
            // no spell selected
            return null; // error
        }

        CombatSpell selectedSpell = CombatSpell.getSpellbyVarbitValue(selectedSpellId); // returns null as default

        return selectedSpell;
    }

    protected double getMagicEquipmentBoost(Item[] playerEquipment)
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

                    double equipmentMagicBonusStat = 0;

                    // Ensure not null
                    if(itemManager.getItemStats(equipmentID, false) != null)
                    {
                        equipmentMagicBonusStat = itemManager.getItemStats(equipmentID, false).getEquipment().getMdmg();

                        magicdamagebonus += (equipmentMagicBonusStat/100);
                    }

                }
            }
        }

        // Get Void 2.5% bonus if necessary, otherwise +0
        magicdamagebonus += getVoidMagicBonus(playerEquipment);

        // Get Tumeken's Shadow Bonus
        if(weaponItemName.contains("Tumeken"))
        {
            magicdamagebonus *= 3;

            magicdamagebonus = Math.min(magicdamagebonus, 1);
        }

        // Get Virtus Robe's Damage Bonus for Ancient Magicks
        CombatSpell selectedSpell = CombatSpell.getSpellbyVarbitValue(client.getVarbitValue(276));
        if(selectedSpell != null){
            if (selectedSpell.getName().toLowerCase().contains("smoke")
                    || selectedSpell.getName().toLowerCase().contains("shadow")
                    || selectedSpell.getName().toLowerCase().contains("blood")
                    || selectedSpell.getName().toLowerCase().contains("ice"))
            {
                // Get Item names
                String headItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.HEAD);
                String bodyItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.BODY);
                String legsItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.LEGS);

                // Add bonus per robe
                if(headItemName.toLowerCase().contains("virtus"))
                {
                    magicdamagebonus += 0.03; // 1% added normally, add 3% to make total 4% bonus
                }

                if(bodyItemName.toLowerCase().contains("virtus"))
                {
                    magicdamagebonus += 0.03;
                }

                if(legsItemName.toLowerCase().contains("virtus"))
                {
                    magicdamagebonus += 0.03;
                }
            }
        }

        // Prayer Bonuses
        if(client.isPrayerActive(Prayer.MYSTIC_LORE)) magicdamagebonus += 0.01;

        if(client.isPrayerActive(Prayer.MYSTIC_MIGHT)) magicdamagebonus += 0.02;

        if(client.isPrayerActive(Prayer.AUGURY)) magicdamagebonus += 0.04;


        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Bonus Magic Damage: " + magicdamagebonus*100 + "%", null);

        return 1 + magicdamagebonus; // Default is 1.
    }

    protected double getVoidMagicBonus(Item[] playerEquipment)
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
                        return 0.05; // 5% magic dmg bonus
                    }
                }
            }
        }

        // Elite Void Set incomplete, no bonus
        return 0;
    }

    protected double getTomeSpellBonus(Item[] playerEquipment, AttackStyle weaponAttackStyle)
    {
        String shieldItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.SHIELD);

        // Check if casting without spell selected
        int selectedSpellId = client.getVarbitValue(276); // Varbit 276 is Selected Autocasted Spell
        if (selectedSpellId == 0)
        {
            // no spell selected
            return 1; // no bonus, default
        }

        CombatSpell selectedSpell = CombatSpell.getSpellbyVarbitValue(selectedSpellId); // returns null as default

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Selected Spell Sprite ID: " + spellSpriteID, null);
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Selected Spell: " + selectedSpell, null);

        // Spell is selected, not casting from weapon
        if (selectedSpell == null)
        {
            return 1; // no bonus, default
        }
        else
        {
            if (selectedSpell.getName().toLowerCase().contains("fire"))
            {
                // Check for tome of fire
                if (shieldItemName.contains("Tome of fire"))
                {
                    if (!shieldItemName.contains("(empty)"))
                    {
                        return 1.1;
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
                        return 1.1;
                    }
                }
            }
        }

        return 1;
    }

    // Calculate Standard Magic Max Hit
    protected double calculateMagicMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle)
    {
        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        double spellBaseMaxHit = getSpellBaseHit(playerEquipment, weaponAttackStyle, client.getBoostedSkillLevel(Skill.MAGIC));

        if (spellBaseMaxHit == 0)
        {
            return -1;
        }

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = getMagicEquipmentBoost(playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Step 3: Calculate Bonuses
        // Tome Bonuses
        double correctTomeSpellBonus = getTomeSpellBonus(playerEquipment, weaponAttackStyle); // default 1
        maxDamage = maxDamage * correctTomeSpellBonus;

        CombatSpell spell = getSpell();

        // Smoke Battlestaff Bonus
        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        if (weaponItemName.toLowerCase().contains("smoke battlestaff") || weaponItemName.toLowerCase().contains("smoke staff"))
        {
            if (spell != null && spell.getSpellbook().contains("standard")) {
                double SmokeStandardSpellsBonus = maxDamage * 0.1f;
                maxDamage = maxDamage + SmokeStandardSpellsBonus;
            }
        }

        // Final step: Calculate and add spell type weakness Bonus
        if (spell != null && spell.hasType())
        {
            if (plugin.clickedNPC != null)
            {
                NPCTypeWeakness weaknessBonus = NPCTypeWeakness.findWeaknessByName(plugin.clickedNPC.getName());
                if (weaknessBonus != null)
                {
                    int bonusPercent = weaknessBonus.getWeaknessPercent();

                    double typeBonusDamage = maxDamage * ((double) bonusPercent / 100);
                    maxDamage = maxDamage + typeBonusDamage;
                }
            }
        }

        return maxDamage;
    }

    /**
     * Calculates the standard max hit based on current equipment and player status.
     *
     * @param isSpecialAttack boolean
     * @return Max Hit as Double
     */
    public double calculate(boolean isSpecialAttack)
    {
        int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
        int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

        // Get Current Attack Style
        AttackStyle[] weaponAttackStyles = WeaponType.getWeaponTypeStyles(client, weaponTypeID);
        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];
        if (attackStyle == null)
            return -1;

        // Get Current Equipment
        Item[] playerEquipment = EquipmentItems.getCurrentlyEquipped(client);

        // Find what type to calculate
        if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE))
        {
            return this.calculateMeleeMaxHit(playerEquipment, attackStyle, attackStyleID, isSpecialAttack);
        }
        else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
        {
            return this.calculateRangedMaxHit(playerEquipment, attackStyle, attackStyleID);
        }
        else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
        {
            return this.calculateMagicMaxHit(playerEquipment, attackStyle);
        }
        else
        {
            return -1;
        }
    }
}
