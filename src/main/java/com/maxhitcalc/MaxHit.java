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

public class MaxHit {
    // Get Prayer Bonus for Max Hit Calculation
    public static double getPrayerBonus(Client client, AttackStyle weaponAttackStyle)
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
    public static int getAttackStyleBonus(AttackStyle weaponAttackStyle, int attackStyleID)
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
    public static double getMeleeStrengthBonus(Client client, ItemManager itemManager, Item[] playerEquipment)
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

    public static double getVoidMeleeBonus(Client client, Item[] playerEquipment)
    {
        if (playerEquipment == null) return 1;

        // Check for set bonus
        if (playerEquipment.length > EquipmentInventorySlot.HEAD.getSlotIdx()
                && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Void melee"))
        {
            if (playerEquipment.length > EquipmentInventorySlot.GLOVES.getSlotIdx()
                    && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.GLOVES.getSlotIdx()].getId()).getName().contains("Void"))
            {
                // Melee helm and gloves, check for elite or not
                if (playerEquipment.length > EquipmentInventorySlot.BODY.getSlotIdx()
                        && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Void"))
                {
                    if (playerEquipment.length > EquipmentInventorySlot.LEGS.getSlotIdx()
                            && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Void"))
                    {
                        // Normal void set
                        return 1.1;
                    }
                }
                else if (playerEquipment.length > EquipmentInventorySlot.BODY.getSlotIdx()
                        && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Elite void"))
                {
                    if(playerEquipment.length > EquipmentInventorySlot.LEGS.getSlotIdx()
                            && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Elite void"))
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
    public static List<Double> getMeleeSpecialBonusMultiplier(Client client, Item[] playerEquipment)
    {
        List<Double> specialBonusesToApply = new ArrayList<>();

        if (playerEquipment == null) return specialBonusesToApply;

        double specialBonus = 1; // Initialize Variable

        String weaponItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.WEAPON.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()] != null)
        {
            weaponItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName();
        }

        // SPECIAL BONUSES MUST BE ORDERED CORRECTLY.
        // Dharok's Set Check
        if (playerEquipment.length > EquipmentInventorySlot.HEAD.getSlotIdx()
                && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Dharok's"))
        {
            if(playerEquipment.length > EquipmentInventorySlot.BODY.getSlotIdx()
                    && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Dharok's"))
            {
                if(playerEquipment.length > EquipmentInventorySlot.LEGS.getSlotIdx()
                        && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Dharok's"))
                {
                    if(playerEquipment.length > EquipmentInventorySlot.WEAPON.getSlotIdx()
                            && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Dharok's"))
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
        if (playerEquipment.length > EquipmentInventorySlot.HEAD.getSlotIdx()
                && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Obsidian"))
        {
            if(playerEquipment.length > EquipmentInventorySlot.BODY.getSlotIdx()
                    && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Obsidian"))
            {
                if(playerEquipment.length > EquipmentInventorySlot.LEGS.getSlotIdx()
                        && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Obsidian"))
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
            if(playerEquipment.length > EquipmentInventorySlot.AMULET.getSlotIdx()
                    && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Berserker"))
            {
                specialBonusesToApply.add(0.2);
            }
        }

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Special Bonus: " + specialBonus, null);

        return specialBonusesToApply;
    }

    // Calculate Melee Max Hit
    public static double calculateMeleeMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
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

        // Complete
        return maxHit;
    }

    // Get Ranged Void Bonus for Elite and Normal Sets
    public static double getVoidRangedBonus(Client client, Item[] playerEquipment)
    {
        // Check for set bonus
        if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Void ranger"))
        {
            if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.GLOVES.getSlotIdx()].getId()).getName().contains("Void"))
            {
                // Ranged helm and gloves, check for elite or not
                if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Void"))
                {
                    if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Void"))
                    {
                        // Normal void set
                        return 1.1;
                    }
                }
                else if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Elite void"))
                {
                    if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Elite void"))
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
    public static double getRangedStrengthBonus(Client client, ItemManager itemManager, Item[] playerEquipment, MaxHitCalcConfig.BlowpipeDartType dartType)
    {
        double rangedStrengthBonus = 0;

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Weapon Name: " + client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName(), null);

        String weaponItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.WEAPON.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()] != null)
        {
            weaponItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName();
        }

        String ammoItemName = "";
        int ammoID = -1;
        boolean skipAmmo = false;
        if(playerEquipment.length > EquipmentInventorySlot.AMMO.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()] != null)
        {
            ammoItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId()).getName();
            ammoID = playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId();
        }
        else
        {
            skipAmmo = true;
        }

        // Crystal bow and Blowpipe skip ammo
        // Cases to skip ammo
        if (weaponItemName.contains("Crystal bow")
                || weaponItemName.contains("faerdhinen"))
        {
            skipAmmo = true;
        }

        if(weaponItemName.contains("blowpipe"))
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
                        rangedStrengthBonus += equipmentStrengthStat;
                    }
                    // else, ammo slot skip it
                }
            }
        }

        return rangedStrengthBonus;
    }

    // Get Gear Boost, for instance Crystal Armor set bonus
    public static double getRangeGearBoost(Client client, Item[] playerEquipment){
        double damagePercentBonus = 1;

        String weaponItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.WEAPON.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()] != null)
        {
            weaponItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName();
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


        if (weaponItemName.contains("Crystal bow")
                || weaponItemName.contains("faerdhinen"))
        {
            // Crystal Armor Damage bonus
            if (headItemName.contains("Crystal helm"))
            {
                if(!headItemName.contains("(basic)"))
                {
                    damagePercentBonus += 0.025;
                }
            }

            if (bodyItemName.contains("Crystal body"))
            {
                if(!bodyItemName.contains("(basic)"))
                {
                    damagePercentBonus += 0.075;
                }
            }

            if (legsItemName.contains("Crystal legs"))
            {
                if(!legsItemName.contains("(basic)"))
                {
                    damagePercentBonus += 0.05;
                }
            }
        }

        return damagePercentBonus;
    }

    public static double calculateRangedMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, MaxHitCalcConfig.BlowpipeDartType dartType)
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

    public static double getMagicEquipmentBoost(Client client, ItemManager itemManager, Item[] playerEquipment)
    {
        double magicdamagebonus = 1;

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

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Bonus Magic Damage: " + magicdamagebonus, null);

        // Get Void 2.5% bonus if necessary, otherwise +0
        magicdamagebonus += getVoidMagicBonus(client, playerEquipment);

        return magicdamagebonus;
    }

    public static double getVoidMagicBonus(Client client, Item[] playerEquipment)
    {
        if (playerEquipment == null) return 0;

        // Check for set bonus
        if (playerEquipment.length > EquipmentInventorySlot.HEAD.getSlotIdx()
                && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Void mage"))
        {
            if (playerEquipment.length > EquipmentInventorySlot.GLOVES.getSlotIdx()
                    && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.GLOVES.getSlotIdx()].getId()).getName().contains("Void"))
            {
                if (playerEquipment.length > EquipmentInventorySlot.BODY.getSlotIdx()
                        && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Elite void"))
                {
                    if(playerEquipment.length > EquipmentInventorySlot.LEGS.getSlotIdx()
                            && client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Elite void"))
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

    public static double getTomeSpellBonus(Client client, Item[] playerEquipment, AttackStyle weaponAttackStyle)
    {
        int spellSpriteID = -1;

        String shieldItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.SHIELD.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()] != null)
        {
            shieldItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()].getId()).getName();
        }

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
}
