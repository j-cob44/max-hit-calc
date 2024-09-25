/* PredictNextMax.java
 * Predicts the required stats to have a new max hit.
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
import java.util.Arrays;
import java.util.List;

public class PredictNextMax extends MaxHit
{
    PredictNextMax(MaxHitCalcPlugin plugin, MaxHitCalcConfig config, ItemManager itemManager, Client client)
    {
        super(plugin, config, itemManager, client);
    }

    private List<Object> predictNextMeleeMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        int nextStrengthLevel = 0;
        int nextStrengthBonus = 0;
        double nextPrayerBonus = 0;

        double currentMaxHit = Math.floor(calculateMeleeMaxHit(playerEquipment, weaponAttackStyle, attackStyleID, false));

        // Predict Next Prayer Bonus for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Melee Max Hit
            // Step 1: Calculate effective Strength
            int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
            double prayerBonus = getPrayerBonus(weaponAttackStyle) + (i * 0.01);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidMeleeBonus(playerEquipment); // default 1;
            double soulStackBonus = getSoulStackBonus();
            double effectiveSoulStackLevel = Math.floor(strengthLevel * soulStackBonus);

            double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + effectiveSoulStackLevel + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the base damage
            double strengthBonus = getMeleeStrengthBonus(playerEquipment); // default 0

            double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
            double flooredBaseDamage = Math.floor(baseDamage);

            // Step 3: Calculate the bonus damage
            List<Double> specialBonusMultipliers = getMeleeSpecialBonusMultiplier(playerEquipment); // default empty

            double predictedMaxHit = flooredBaseDamage;

            if(!specialBonusMultipliers.isEmpty())
            {
                for (double bonus: specialBonusMultipliers)
                {
                    predictedMaxHit += Math.floor(predictedMaxHit * bonus);
                }
            }

            // Osmumten's Fang Decrease
            String weaponName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
            if (weaponName.contains("Osmumten's fang"))
            {
                predictedMaxHit = predictedMaxHit * 0.85 + 1;
            }

            // Colossal Blade Base Increase
            if(weaponName.contains("Colossal blade")){
                predictedMaxHit = predictedMaxHit + 2;
            }

            // Rat Default +10 damage Bonus
            if(weaponName.contains("Bone mace"))
            {
                predictedMaxHit = predictedMaxHit + 10;
            }

            // Check if predicted is better than current
            if (Math.floor(predictedMaxHit) > currentMaxHit)
            {
                nextPrayerBonus = (i * 0.01);
                break;
            }
        }

        // Predict Next Strength Bonus for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Melee Max Hit
            // Step 1: Calculate effective Strength
            int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
            double prayerBonus = getPrayerBonus(weaponAttackStyle);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidMeleeBonus(playerEquipment); // default 1;
            double soulStackBonus = getSoulStackBonus();
            double effectiveSoulStackLevel = Math.floor(strengthLevel * soulStackBonus);

            double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + effectiveSoulStackLevel + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the base damage
            double strengthBonus = getMeleeStrengthBonus(playerEquipment) + i; // default 0

            double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
            double flooredBaseDamage = Math.floor(baseDamage);

            // Step 3: Calculate the bonus damage
            List<Double> specialBonusMultipliers = getMeleeSpecialBonusMultiplier(playerEquipment); // default empty

            double predictedMaxHit = flooredBaseDamage;

            if(!specialBonusMultipliers.isEmpty())
            {
                for (double bonus: specialBonusMultipliers)
                {
                    predictedMaxHit += Math.floor(predictedMaxHit * bonus);
                }
            }

            // Osmumten's Fang Decrease
            String weaponName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
            if (weaponName.contains("Osmumten's fang"))
            {
                predictedMaxHit = predictedMaxHit * 0.85 + 1;
            }

            // Colossal Blade Base Increase
            if(weaponName.contains("Colossal blade")){
                predictedMaxHit = predictedMaxHit + 2;
            }

            // Rat Default +10 damage Bonus
            if(weaponName.contains("Bone mace"))
            {
                predictedMaxHit = predictedMaxHit + 10;
            }

            // Check if predicted is better than current
            if (Math.floor(predictedMaxHit) > currentMaxHit)
            {
                nextStrengthBonus = i;
                break;
            }
        }

        // Predict Next Strength Level for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Melee Max Hit
            // Step 1: Calculate effective Strength
            int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH) + i;
            double prayerBonus = getPrayerBonus(weaponAttackStyle);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidMeleeBonus(playerEquipment); // default 1;
            double soulStackBonus = getSoulStackBonus();
            double effectiveSoulStackLevel = Math.floor(strengthLevel * soulStackBonus);

            double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + effectiveSoulStackLevel + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the base damage
            double strengthBonus = getMeleeStrengthBonus(playerEquipment); // default 0

            double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
            double flooredBaseDamage = Math.floor(baseDamage);

            // Step 3: Calculate the bonus damage
            List<Double> specialBonusMultipliers = getMeleeSpecialBonusMultiplier(playerEquipment); // default empty

            double predictedMaxHit = flooredBaseDamage;

            if(!specialBonusMultipliers.isEmpty())
            {
                for (double bonus: specialBonusMultipliers)
                {
                    predictedMaxHit += Math.floor(predictedMaxHit * bonus);
                }
            }

            // Osmumten's Fang Decrease
            String weaponName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
            if (weaponName.contains("Osmumten's fang"))
            {
                predictedMaxHit = predictedMaxHit * 0.85 + 1;
            }

            // Colossal Blade Base Increase
            if(weaponName.contains("Colossal blade")){
                predictedMaxHit = predictedMaxHit + 2;
            }

            // Rat Default +10 damage Bonus
            if(weaponName.contains("Bone mace"))
            {
                predictedMaxHit = predictedMaxHit + 10;
            }

            // Check if predicted is better than current
            if (Math.floor(predictedMaxHit) > currentMaxHit)
            {
                nextStrengthLevel = i;
                break;
            }
        }

        List<Object> results = Arrays.asList("melee", nextStrengthLevel, nextStrengthBonus, nextPrayerBonus);

        // Complete
        return results;
    }

    private List<Object> predictNextRangeMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        int nextRangedLevel = 0;
        int nextRangeEquipmentBonus = 0;
        double nextPrayerBonus = 0;

        double currentMaxHit = Math.floor(calculateRangedMaxHit(playerEquipment, weaponAttackStyle, attackStyleID));

        // Predict Next Ranged Level for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Ranged Max Hit
            // Step 1: Calculate effective ranged Strength
            int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED) + i;
            double prayerBonus = getPrayerBonus(weaponAttackStyle);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidRangedBonus(playerEquipment); // default 1;

            double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the max hit
            double equipmentRangedStrength = getRangedStrengthBonus(playerEquipment);
            double gearBonus = getRangeGearBoost(playerEquipment);

            double predictedMaxHit = (0.5 + (((effectiveRangedStrength * (equipmentRangedStrength + 64))/640) * gearBonus) );

            // Step 3: Bonus damage from special attack and effects
            // Rat Default +10 damage Bonus
            String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
            if(weaponItemName.contains("Bone shortbow"))
            {
                predictedMaxHit = predictedMaxHit + 10;
            }

            // Tonalztics of Ralos (uncharged) max hit is 75% of normal
            if(weaponItemName.contains("Tonalztics of ralos"))
            {
                predictedMaxHit = Math.floor(predictedMaxHit * 0.75); // unknown if flooring here causes miscalcs
                if(!weaponItemName.contains("(uncharged)"))
                {
                    predictedMaxHit = predictedMaxHit*2;
                }
            }

            // Check if predicted is better than current
            if (Math.floor(predictedMaxHit) > currentMaxHit)
            {
                nextRangedLevel = i;
                break;
            }
        }

        // Predict Next Prayer Bonus for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Ranged Max Hit
            // Step 1: Calculate effective ranged Strength
            int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED);
            double prayerBonus = getPrayerBonus(weaponAttackStyle) + (i * 0.01);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidRangedBonus(playerEquipment); // default 1;

            double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the max hit
            double equipmentRangedStrength = getRangedStrengthBonus(playerEquipment);
            double gearBonus = getRangeGearBoost(playerEquipment);

            double predictedMaxHit = (0.5 + (((effectiveRangedStrength * (equipmentRangedStrength + 64))/640) * gearBonus) );

            // Step 3: Bonus damage from special attack and effects
            // Rat Default +10 damage Bonus
            String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
            if(weaponItemName.contains("Bone shortbow"))
            {
                predictedMaxHit = predictedMaxHit + 10;
            }

            // Tonalztics of Ralos (uncharged) max hit is 75% of normal
            if(weaponItemName.contains("Tonalztics of ralos"))
            {
                predictedMaxHit = Math.floor(predictedMaxHit * 0.75); // unknown if flooring here causes miscalcs
                if(!weaponItemName.contains("(uncharged)"))
                {
                    predictedMaxHit = predictedMaxHit*2;
                }
            }

            // Check if predicted is better than current
            if (Math.floor(predictedMaxHit) > currentMaxHit)
            {
                nextPrayerBonus = (i * 0.01);
                break;
            }
        }

        // Predict Next Ranged Equipment Strength Bonus for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Ranged Max Hit
            // Step 1: Calculate effective ranged Strength
            int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED);
            double prayerBonus = getPrayerBonus(weaponAttackStyle);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidRangedBonus(playerEquipment); // default 1;

            double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the max hit
            double equipmentRangedStrength = getRangedStrengthBonus(playerEquipment) + i;
            double gearBonus = getRangeGearBoost(playerEquipment);

            double predictedMaxHit = (0.5 + (((effectiveRangedStrength * (equipmentRangedStrength + 64))/640) * gearBonus) );

            // Step 3: Bonus damage from special attack and effects
            // Rat Default +10 damage Bonus
            String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
            if(weaponItemName.contains("Bone shortbow"))
            {
                predictedMaxHit = predictedMaxHit + 10;
            }

            // Tonalztics of Ralos (uncharged) max hit is 75% of normal
            if(weaponItemName.contains("Tonalztics of ralos"))
            {
                predictedMaxHit = Math.floor(predictedMaxHit * 0.75); // unknown if flooring here causes miscalcs
                if(!weaponItemName.contains("(uncharged)"))
                {
                    predictedMaxHit = predictedMaxHit*2;
                }
            }

            // Check if predicted is better than current
            if (Math.floor(predictedMaxHit) > currentMaxHit)
            {
                nextRangeEquipmentBonus = i;
                break;
            }
        }

        List<Object> results = Arrays.asList("ranged", nextRangedLevel, nextRangeEquipmentBonus, nextPrayerBonus);

        // Complete
        return results;
    }

    private List<Object> predictNextMageMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        int nextMagicLevel = 0;
        double nextMagicDamageBonus = 0;

        double currentMaxHit = Math.floor(calculateMagicMaxHit(playerEquipment, weaponAttackStyle));
        CombatSpell spell = getSpell();

        // Predict Next Magic Level for Next Max Hit - Only used for Charged Weapons and Magic Dart
        if(spell == null || (spell.getName().toLowerCase().contains("magic dart")))
        {
            for(int i = 1; i <= 20; i++)
            {
                // Calculate Magic Max Hit
                // Step 1: Find the base hit of the spell
                double spellBaseMaxHit = getSpellBaseHit(playerEquipment, weaponAttackStyle, (client.getBoostedSkillLevel(Skill.MAGIC) + i));

                // Step 2: Calculate the Magic Damage Bonus
                double magicDmgBonus = getMagicEquipmentBoost(playerEquipment) + (i * 0.01);

                double predictedMaxHit = (spellBaseMaxHit * magicDmgBonus);

                // Step 3: Calculate Type Bonuses
                // Tome Bonuses
                double correctTomeSpellBonus = getTomeSpellBonus(playerEquipment, weaponAttackStyle); // default 1
                predictedMaxHit = predictedMaxHit * correctTomeSpellBonus;

                // Check if predicted is better than current
                if (Math.floor(predictedMaxHit) > currentMaxHit)
                {
                    nextMagicLevel = i;
                    break;
                }
            }
        }

        // Predict Next Magic Damage Bonus needed for next Max Hit
        for(int i = 1; i <= 50; i++)
        {
            // Calculate Magic Max Hit
            // Step 1: Find the base hit of the spell
            double spellBaseMaxHit = getSpellBaseHit(playerEquipment, weaponAttackStyle, client.getBoostedSkillLevel(Skill.MAGIC));

            // Step 2: Calculate the Magic Damage Bonus
            double magicDmgBonus = getMagicEquipmentBoost(playerEquipment) + (i * 0.01);

            double predictedMaxHit = (spellBaseMaxHit * magicDmgBonus);

            // Step 3: Calculate Type Bonuses
            // Tome Bonuses
            double correctTomeSpellBonus = getTomeSpellBonus(playerEquipment, weaponAttackStyle); // default 1
            predictedMaxHit = predictedMaxHit * correctTomeSpellBonus;

            // Smoke Battlestaff Bonus
            String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
            if (weaponItemName.toLowerCase().contains("smoke battlestaff") || weaponItemName.toLowerCase().contains("smoke staff"))
            {
                if (spell != null && spell.getSpellbook().contains("standard")) {
                    double SmokeStandardSpellsBonus = predictedMaxHit * 0.1f;
                    predictedMaxHit = predictedMaxHit + SmokeStandardSpellsBonus;
                }
            }

            // Final step: Calculate and add spell type weakness Bonus
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

                            double typeBonusDamage = predictedMaxHit * ((double) bonusPercent / (double)100);
                            predictedMaxHit = predictedMaxHit + typeBonusDamage;
                        }
                    }
                }
            }

            // Check if predicted is better than current
            if (Math.floor(predictedMaxHit) > currentMaxHit)
            {
                nextMagicDamageBonus = (i * 0.01);
                break;
            }
        }

        List<Object> results = Arrays.asList("magic", nextMagicLevel, nextMagicDamageBonus);

        // Complete
        return results;
    }

    /**
     * Calculates a prediction of stat increases required for a new max hit.<br><br>
     * Predictions for Melee: Strength Level, Equipment Strength Bonus, and Prayer Bonus <br>
     * Predictions for Ranged: Ranged Level, Ranged Equipment Strength Bonus, and Prayer Bonus <br>
     * Predictions for Magic: Magic Level, and Magic Equipment Damage Bonus <br><br>
     *
     * @return List of predictions
     */
    public List<Object> predict(){
        int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
        int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

        // Get Current Attack Style
        AttackStyle[] weaponAttackStyles = WeaponType.getWeaponTypeStyles(client, weaponTypeID);
        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];
        if (attackStyle == null)
            return null;

        // Get Current Equipment
        Item[] playerEquipment = EquipmentItems.getCurrentlyEquipped(client);

        // Find what type to calculate
        if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE))
        {
            List<Object> meleeResults = this.predictNextMeleeMaxHit(playerEquipment, attackStyle, attackStyleID);

            // index: 0 = "melee", 1 = strength level, 2 = equipment strength bonus, 3 = prayer percent bonus
            return meleeResults;
        }
        else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
        {
            List<Object> rangedResults = this.predictNextRangeMaxHit(playerEquipment, attackStyle, attackStyleID);

            // index: 0 = "ranged", 1 = range level, 2 = range equipment strength bonus, 3 = prayer percent bonus
            return rangedResults;
        }
        else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
        {
            List<Object> mageResults = this.predictNextMageMaxHit(playerEquipment, attackStyle, attackStyleID);

            // index: 0 = "magic", 1 = magic level, 2 = mage equipment damage bonus
            return mageResults;
        }
        else
        {
            return null;
        }
    }
}
