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
        double strengthBonus = 0;

        // Get Melee Strength Bonus of each equipped Item
        for (Item equipmentItem: playerEquipment)
        {
            if(equipmentItem.getId() != -1)
            {
                int equipmentID = equipmentItem.getId();
                int equipmentStrengthStat = itemManager.getItemStats(equipmentID, false).getEquipment().getStr();

                strengthBonus += (double)equipmentStrengthStat;
            }
        }

        return strengthBonus;
    }

    public static double getVoidMeleeBonus(Client client, Item[] playerEquipment)
    {
        // Check for set bonus
        if (!client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Void melee")) return 1; // if not void melee helm

        if (!client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Void")
                && !client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("void")) return 1; // if not void top and not elite top

        if (!client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Void")
                && !client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("void")) return 1; // if not void robe and not elite robe

        if (!client.getItemDefinition(playerEquipment[EquipmentInventorySlot.GLOVES.getSlotIdx()].getId()).getName().contains("Void")) return 1; // if not void gloves

        // Void Set Complete, grant bonus
        return 1.1;
    }

    // Passive Melee Set effects
    public static double getMeleeSpecialBonusMultiplier(Client client, Item[] playerEquipment)
    {
        double specialBonus = 1; // Initialize Variable

        // Dharok's Set Check
        if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Dharok's"))
        {
            if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Dharok's"))
            {
                if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Dharok's"))
                {
                    if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Dharok's"))
                    {
                        // Passed Check, Dharok's Set Equipped, Apply Effect
                        double baseHP = client.getRealSkillLevel(Skill.HITPOINTS);
                        double currentHP = client.getBoostedSkillLevel(Skill.HITPOINTS);
                        specialBonus = (1 + (((baseHP - currentHP) / 100) * (baseHP / 100)));
                    }
                }
            }
        }

        // Berserker Necklace and Obisidian Melee Check
        if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("ket"))
        {
            if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Berserker"))
            {
                specialBonus += 0.2;
            }
        }

        // Obsidian Set Check
        if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Obsidian"))
        {
            if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Obsidian"))
            {
                if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Obsidian"))
                {
                    if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("ket"))
                    {
                        specialBonus += 0.1;
                    }
                }
            }
        }

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Special Bonus: " + specialBonus, null);

        return specialBonus;
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

        double effectiveStrength = Math.floor((Math.floor(Math.floor(strengthLevel) * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the base damage
        double strengthBonus = getMeleeStrengthBonus(client, itemManager, playerEquipment); // default 0

        double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
        double flooredBaseDamage = Math.floor(baseDamage);

        // Step 3: Calculate the bonus damage
        double specialBonusMultiplier = getMeleeSpecialBonusMultiplier(client, playerEquipment); // default 1

        double maxHit = (flooredBaseDamage * specialBonusMultiplier);

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
    public static double getRangedStrengthBonus(Client client, ItemManager itemManager, Item[] playerEquipment)
    {
        double rangedStrengthBonus = 0;

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Weapon Name: " + client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName(), null);

        // Crystal bow and Blowpipe skip ammo
        boolean skipAmmo = false;
        int ammoID = playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId();
        // Cases to skip ammo
        if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Crystal bow"))
        {
            skipAmmo = true;
        }

        if (client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("faerdhinen"))
        {
            skipAmmo = true;
        }

        if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("blowpipe"))
        {
            skipAmmo = true;
        }

        // Get Ranged Strength Bonus of each equipped Item
        for (Item equipmentItem: playerEquipment)
        {
            if(equipmentItem.getId() != -1)
            {
                int equipmentID = equipmentItem.getId();
                int equipmentStrengthStat = itemManager.getItemStats(equipmentID, false).getEquipment().getRstr();

                if (equipmentID == ammoID && skipAmmo)
                {
                    // skip ammo slot
                }
                else
                {
                    rangedStrengthBonus += (double)equipmentStrengthStat;
                }
            }
        }

        return rangedStrengthBonus;
    }

    public static double calculateRangedMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        // Calculate Ranged Max Hit
        // Step 1: Calculate effective ranged Strength
        int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED);
        double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
        int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = getVoidRangedBonus(client, playerEquipment); // default 1;

        double effectiveRangedStrength = Math.floor((Math.floor(Math.floor(rangedLevel) * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the max hit
        double equipmentRangedStrength = getRangedStrengthBonus(client, itemManager, playerEquipment);

        double maxHit = (0.5 + ((effectiveRangedStrength * (equipmentRangedStrength + 64))/640));

        // Step 3: Bonus damage from special attack and effects
        // Not used here.

        return maxHit;
    }

    public static double getSpellBaseHit(Client client, Item[] playerEquipment, AttackStyle weaponAttackStyle, double magicLevel)
    {
        int spellSpriteID = -1;
        double basehit = 0;

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Magic Weapon: " + client.getItemDefinition(playerItems[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName(), null);

        // Powered Staff Check
        // Trident of the Seas
        if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("of the seas"))
        {
            basehit = Math.max((Math.floor((Math.min(magicLevel, 125) - 15) / 3)), 1); // Corrected, thanks to Mod Ash
        }
        // Trident of the Swamp
        else if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("of the swamp"))
        {
            basehit = Math.max((Math.floor(((Math.min(magicLevel, 125) - 6) / 3))), 3); // Corrected, thanks to Mod Ash
        }
        // Sanquinesti Staff
        else if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Sanguinesti"))
        {
            basehit = Math.max((Math.floor(((Math.min(magicLevel, 125) - 3) / 3))), 4); // Corrected, thanks to Mod Ash
        }
        // Thammaron's Sceptre
        else if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Thammaron's"))
        {
            basehit = (Math.floor(magicLevel/3) - 8);
        }
        // Accursed Sceptre
        else if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Accursed"))
        {
            basehit = (Math.floor(magicLevel/3) - 6);
        }
        // Tumeken's Shadow
        else if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Tumeken"))
        {
            basehit = (Math.floor(magicLevel/3) + 1);
        }
        // Crystal staff (basic)
        else if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Crystal staff (basic)"))
        {
            basehit = 23;
        }
        // Crystal staff (attuned)
        else if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Crystal staff (attuned)"))
        {
            basehit = 31;
        }
        // Crystal staff (perfected)
        else if(client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Crystal staff (perfected)"))
        {
            basehit = 39;
        }
        // Autocasted Spell
        else
        {
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
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Selected Spell: " + selectedSpell, null);

            // Magic Dart Case
            if(selectedSpell.getName().equalsIgnoreCase("MAGIC DART"))
            {
                double magicDartDamage = Math.floor(magicLevel * (1/10)) + 10;

                basehit = magicDartDamage;
            }
            else
            {
                basehit = selectedSpell.getBaseDamage();
            }
        }

        return basehit;
    }

    public static double getMagicEquipmentBoost(Client client, ItemManager itemManager, Item[] playerEquipment)
    {
        double magicdamagebonus = 1;

        // Get Ranged Strength Bonus of each equipped Item
        for (Item equipmentItem: playerEquipment)
        {
            if (equipmentItem.getId() != -1)
            {
                int equipmentID = equipmentItem.getId();
                double equipmentMagicBonusStat = itemManager.getItemStats(equipmentID, false).getEquipment().getMdmg();

                magicdamagebonus += (equipmentMagicBonusStat/100);
            }
        }

        // Debug
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Bonus Magic Damage: " + magicdamagebonus, null);

        return magicdamagebonus;
    }

    public static double calculateMagicMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        double spellBaseMaxHit = getSpellBaseHit(client, playerEquipment, weaponAttackStyle, client.getBoostedSkillLevel(Skill.MAGIC));

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = getMagicEquipmentBoost(client, itemManager, playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Step 3: Calculate Type Bonuses
        // Not used here.

        return maxDamage;
    }
}
