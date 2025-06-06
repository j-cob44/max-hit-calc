/* InventoryItemMaxHit.java
 * Contains functions required for calculating max hit based on hovered equipment in inventory.
 *
 *
 * Copyright (c) 2025, Jacob Burton <https://github.com/j-cob44>
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

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.Skill;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.itemstats.StatChange;

import java.util.ArrayList;
import java.util.List;

public class StatChangedMaxHit
{
    protected MaxHitCalcPlugin plugin;
    protected MaxHitCalcConfig config;
    private MaxHit maxHits;
    protected ItemManager itemManager;
    protected Client client;

    StatChangedMaxHit(MaxHitCalcPlugin plugin, MaxHitCalcConfig config, ItemManager itemManager, Client client)
    {
        this.config = config;
        this.maxHits = new MaxHit(plugin, config, itemManager, client);
        this.itemManager = itemManager;
        this.client = client;
        this.plugin = plugin;
    }

    boolean checkIfStatChanged(StatChange[] stats, Skill skill)
    {
        String skillName = skill.getName().toLowerCase();

        for(StatChange stat: stats)
        {
            if(stat.getStat().getName().toLowerCase().contains(skillName))
            {
                return true;
            }
        }

        return false;
    }

    int getChangedStatValue(StatChange[] stats, Skill skill)
    {
        String skillName = skill.getName().toLowerCase();

        for(StatChange stat: stats)
        {
            if(stat.getStat().getName().toLowerCase().contains(skillName))
            {
                return stat.getAbsolute();
            }
        }

        return 0;
    }

    // Passive Melee Set effects
    protected List<Double> getMeleeSpecialBonusMultiplier(Item[] playerEquipment, StatChange[] changedStats)
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

                        double currentHP = 1;
                        if(checkIfStatChanged(changedStats, Skill.HITPOINTS))
                        {
                            currentHP = getChangedStatValue(changedStats, Skill.HITPOINTS);
                        }
                        else
                        {
                            currentHP = client.getBoostedSkillLevel(Skill.HITPOINTS);
                        }

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

    // Calculate Melee max hit with chagned stats
    protected double calculateMeleeMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, boolean isSpecialAttack, StatChange[] changedStats)
    {
        // Calculate Melee Max Hit
        // Step 1: Calculate effective Strength
        int strengthLevel = 1;
        if(checkIfStatChanged(changedStats, Skill.STRENGTH))
        {
            strengthLevel = getChangedStatValue(changedStats, Skill.STRENGTH);
        }
        else
        {
            strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
        }


        double prayerBonus = maxHits.getPrayerBonus(weaponAttackStyle);
        int styleBonus = maxHits.getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = maxHits.getVoidMeleeBonus(playerEquipment); // default 1;
        double soulStackBonus = maxHits.getSoulStackBonus();
        double effectiveSoulStackLevel = 0;

        if (!isSpecialAttack)
        {
            effectiveSoulStackLevel = Math.floor(strengthLevel * soulStackBonus);
        }

        double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + effectiveSoulStackLevel + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the base damage
        double strengthBonus = maxHits.getMeleeStrengthBonus(playerEquipment); // default 0

        double baseDamage = (0.5 + (effectiveStrength * ((strengthBonus + 64)/640)));
        double flooredBaseDamage = Math.floor(baseDamage);

        // Step 3: Calculate the bonus damage
        List<Double> specialBonusMultipliers = getMeleeSpecialBonusMultiplier(playerEquipment, changedStats); // default empty

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

    // Calculate Ranged max hit with chagned stats
    protected double calculateRangedMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, StatChange[] changedStats)
    {
        // Calculate Ranged Max Hit
        // Step 1: Calculate effective ranged Strength
        int rangedLevel = 1;
        if(checkIfStatChanged(changedStats, Skill.RANGED))
        {
            rangedLevel = getChangedStatValue(changedStats, Skill.RANGED);
        }
        else
        {
            rangedLevel = client.getBoostedSkillLevel(Skill.RANGED);
        }

        double prayerBonus = maxHits.getPrayerBonus(weaponAttackStyle);
        int styleBonus = maxHits.getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = maxHits.getVoidRangedBonus(playerEquipment); // default 1;

        double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the max hit
        double equipmentRangedStrength = maxHits.getRangedStrengthBonus(playerEquipment);
        double gearBonus = maxHits.getRangeGearBoost(playerEquipment);

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

    // Calculate magic max hit with chagned stats
    protected double calculateMagicMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, StatChange[] changedStats)
    {
        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        int magicLevel = 1;
        if(checkIfStatChanged(changedStats, Skill.MAGIC))
        {
            magicLevel = getChangedStatValue(changedStats, Skill.MAGIC);
        }
        else
        {
            magicLevel = client.getBoostedSkillLevel(Skill.MAGIC);
        }

        double spellBaseMaxHit = maxHits.getSpellBaseHit(playerEquipment, weaponAttackStyle, magicLevel);

        if (spellBaseMaxHit == 0)
        {
            return -1;
        }

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = maxHits.getMagicEquipmentBoost(playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Step 3: Calculate Bonuses
        // Tome Bonuses
        double correctTomeSpellBonus = maxHits.getTomeSpellBonus(playerEquipment, weaponAttackStyle); // default 1
        maxDamage = maxDamage * correctTomeSpellBonus;

        // Final step: Calculate and add spell type weakness Bonus
        CombatSpell spell = maxHits.getSpell();
        if (spell != null && spell.hasType())
        {
            if (plugin.selectedNPCName != null)
            {
                NPCTypeWeakness weaknessBonus = NPCTypeWeakness.findWeaknessByName(plugin.selectedNPCName);
                if (weaknessBonus != null)
                {
                    if(spell.getSpellType() == weaknessBonus.getElementalWeakness())
                    {
                        int bonusPercent = weaknessBonus.getWeaknessPercent();

                        double typeBonusDamage = maxDamage * ((double) bonusPercent / (double)100);
                        maxDamage = maxDamage + typeBonusDamage;
                    }
                }
            }
        }

        // Twinflame Staff Double Hit bonus
        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        if (weaponItemName.toLowerCase().contains("twinflame staff"))
        {
            if (spell != null && spell.getSpellbook().contains("standard"))
            {
                if(!spell.getName().toLowerCase().contains("strike") && !spell.getName().toLowerCase().contains("surge"))
                {
                    double bonusHit = Math.floor(maxDamage) * 0.4;
                    maxDamage = Math.floor(maxDamage) + Math.floor(bonusHit);
                }
            }
        }

        return maxDamage;
    }

    /**
     * Predicts max hit with a given list of changed stats.
     *
     * @param changedStats list of stats that have changed
     * @return Max Hit Prediction as Double
     */
    public double predict(StatChange[] changedStats)
    {
        // Initialize Variables
        int attackStyleID = client.getVarpValue(VarPlayerID.COM_MODE); // Varplayer: Attack Style
        int weaponTypeID = client.getVarbitValue(VarbitID.COMBAT_WEAPON_CATEGORY);  // Varbit: Equipped Weapon Type

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
            return calculateMeleeMaxHit(playerEquipment, attackStyle, attackStyleID, false, changedStats);
        }
        else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
        {
            return calculateRangedMaxHit(playerEquipment, attackStyle, attackStyleID, changedStats);
        }
        else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
        {
            double magicMaxHit = calculateMagicMaxHit(playerEquipment, attackStyle, changedStats);

            // If -1, error, skip
            if (magicMaxHit > -1){
                return magicMaxHit;
            }
        }

        return -1;

    }
}
