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

import net.runelite.api.ChatMessageType; // for debug
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.Skill;
import net.runelite.client.game.ItemManager;
import java.util.Arrays;
import java.util.List;

public class PredictNextMax extends MaxHit
{
    public static List<Object> predictNextMeleeMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        int nextStrengthLevel = 0;
        int nextStrengthBonus = 0;
        double nextPrayerBonus = 0;

        double currentMaxHit = calculateMeleeMaxHit(client, itemManager, playerEquipment, weaponAttackStyle, attackStyleID);

        // Predict Next Prayer Bonus for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Melee Max Hit
            // Step 1: Calculate effective Strength
            int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
            double prayerBonus = getPrayerBonus(client, weaponAttackStyle) + (i * 0.01);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidMeleeBonus(client, playerEquipment); // default 1;

            double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the base damage
            double strengthBonus = getMeleeStrengthBonus(client, itemManager, playerEquipment); // default 0

            double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
            double flooredBaseDamage = Math.floor(baseDamage);

            // Step 3: Calculate the bonus damage
            double specialBonusMultiplier = getMeleeSpecialBonusMultiplier(client, playerEquipment); // default 1

            double predictedMaxHit = (flooredBaseDamage * specialBonusMultiplier);

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
            double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidMeleeBonus(client, playerEquipment); // default 1;

            double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the base damage
            double strengthBonus = getMeleeStrengthBonus(client, itemManager, playerEquipment) + i; // default 0

            double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
            double flooredBaseDamage = Math.floor(baseDamage);

            // Step 3: Calculate the bonus damage
            double specialBonusMultiplier = getMeleeSpecialBonusMultiplier(client, playerEquipment); // default 1

            double predictedMaxHit = (flooredBaseDamage * specialBonusMultiplier);

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
            double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidMeleeBonus(client, playerEquipment); // default 1;

            double effectiveStrength = Math.floor((Math.floor(strengthLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the base damage
            double strengthBonus = getMeleeStrengthBonus(client, itemManager, playerEquipment); // default 0

            double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
            double flooredBaseDamage = Math.floor(baseDamage);

            // Step 3: Calculate the bonus damage
            double specialBonusMultiplier = getMeleeSpecialBonusMultiplier(client, playerEquipment); // default 1

            double predictedMaxHit = (flooredBaseDamage * specialBonusMultiplier);

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

    public static List<Object> predictNextRangeMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        int nextRangedLevel = 0;
        int nextRangeEquipmentBonus = 0;
        double nextPrayerBonus = 0;

        double currentMaxHit = calculateRangedMaxHit(client, itemManager, playerEquipment, weaponAttackStyle, attackStyleID);

        // Predict Next Ranged Level for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Ranged Max Hit
            // Step 1: Calculate effective ranged Strength
            int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED) + i;
            double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidRangedBonus(client, playerEquipment); // default 1;

            double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the max hit
            double equipmentRangedStrength = getRangedStrengthBonus(client, itemManager, playerEquipment);
            double gearBonus = getRangeGearBoost(client, playerEquipment);

            double predictedMaxHit = (0.5 + (((effectiveRangedStrength * (equipmentRangedStrength + 64))/640) * gearBonus) );

            // Step 3: Bonus damage from special attack and effects
            // Not used here.

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
            double prayerBonus = getPrayerBonus(client, weaponAttackStyle) + (i * 0.01);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidRangedBonus(client, playerEquipment); // default 1;

            double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the max hit
            double equipmentRangedStrength = getRangedStrengthBonus(client, itemManager, playerEquipment);
            double gearBonus = getRangeGearBoost(client, playerEquipment);

            double predictedMaxHit = (0.5 + (((effectiveRangedStrength * (equipmentRangedStrength + 64))/640) * gearBonus) );

            // Step 3: Bonus damage from special attack and effects
            // Not used here.

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
            double prayerBonus = getPrayerBonus(client, weaponAttackStyle);
            int styleBonus = getAttackStyleBonus(weaponAttackStyle, attackStyleID);
            double voidBonus = getVoidRangedBonus(client, playerEquipment); // default 1;

            double effectiveRangedStrength = Math.floor((Math.floor(rangedLevel * prayerBonus) + styleBonus + 8) * voidBonus);

            // Step 2: Calculate the max hit
            double equipmentRangedStrength = getRangedStrengthBonus(client, itemManager, playerEquipment) + i;
            double gearBonus = getRangeGearBoost(client, playerEquipment);

            double predictedMaxHit = (0.5 + (((effectiveRangedStrength * (equipmentRangedStrength + 64))/640) * gearBonus) );

            // Step 3: Bonus damage from special attack and effects
            // Not used here.

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

    public static List<Object> predictNextMageMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID)
    {
        int nextMagicLevel = 0;
        double nextMagicDamageBonus = 0;

        double currentMaxHit = calculateMagicMaxHit(client, itemManager, playerEquipment, weaponAttackStyle, attackStyleID);

        // Predict Next Magic Level for Next Max Hit
        for(int i = 1; i <= 20; i++)
        {
            // Calculate Magic Max Hit
            // Step 1: Find the base hit of the spell
            double spellBaseMaxHit = getSpellBaseHit(client, playerEquipment, weaponAttackStyle, (client.getBoostedSkillLevel(Skill.MAGIC) + i));

            // Step 2: Calculate the Magic Damage Bonus
            double magicDmgBonus = getMagicEquipmentBoost(client, itemManager, playerEquipment) + (i * 0.01);

            double predictedMaxHit = (spellBaseMaxHit * magicDmgBonus);

            // Step 3: Calculate Type Bonuses
            // Not used here.

            // Check if predicted is better than current
            if (Math.floor(predictedMaxHit) > currentMaxHit)
            {
                nextMagicLevel = i;
                break;
            }
        }

        // Predict Next Magic Damage Bonus needed for next Max Hit
        for(int i = 1; i <= 50; i++)
        {
            // Calculate Magic Max Hit
            // Step 1: Find the base hit of the spell
            double spellBaseMaxHit = getSpellBaseHit(client, playerEquipment, weaponAttackStyle, client.getBoostedSkillLevel(Skill.MAGIC));

            // Step 2: Calculate the Magic Damage Bonus
            double magicDmgBonus = getMagicEquipmentBoost(client, itemManager, playerEquipment) + (i * 0.01);

            double predictedMaxHit = (spellBaseMaxHit * magicDmgBonus);

            // Step 3: Calculate Type Bonuses
            // Not used here.

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
}
