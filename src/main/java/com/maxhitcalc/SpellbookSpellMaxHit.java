/* SpellbookSpellMaxHit.java
 * Calculates Max Hit for magic when given a specific spell.
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

public class SpellbookSpellMaxHit extends MaxHit
{
    public static double getSpellBaseHit(Client client, Item[] playerEquipment, int magicLevel, CombatSpell spell)
    {
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

        // God Spells Cases
        if((spell == CombatSpell.FLAMES_OF_ZAMORAK) || (spell == CombatSpell.CLAWS_OF_GUTHIX) || (spell == CombatSpell.SARADOMIN_STRIKE))
        {
            if (client.getVarpValue(VarPlayer.CHARGE_GOD_SPELL) > 0)
            {
                if(spell == CombatSpell.CLAWS_OF_GUTHIX &&
                        (capeItemName.toLowerCase().contains("guthix cape") ||  capeItemName.toLowerCase().contains("guthix max cape")))
                {
                    return 30;
                }
                else if(spell == CombatSpell.FLAMES_OF_ZAMORAK &&
                        (capeItemName.toLowerCase().contains("zamorak cape") || capeItemName.toLowerCase().contains("zamorak max cape")))
                {
                    return 30;
                }
                else if(spell == CombatSpell.SARADOMIN_STRIKE &&
                        (capeItemName.toLowerCase().contains("saradomin cape") || capeItemName.toLowerCase().contains("saradomin max cape")))
                {
                    return 30;
                }
                else
                {
                    return 20;
                }
            }
            else
            {
                return 20;
            }
        }
        // Magic Dart Case
        else if(spell == CombatSpell.MAGIC_DART)
        {
            double magicDartDamage = Math.floor(magicLevel * ((double)1/10)) + 10;

            return magicDartDamage;
        }
        else
        {
            return spell.getBaseDamage();
        }
    }

    public static double getTomeSpellBonus(Client client, Item[] playerEquipment, CombatSpell spell)
    {
        String shieldItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.SHIELD.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()] != null)
        {
            shieldItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()].getId()).getName();
        }

        if (spell.getName().toLowerCase().contains("fire"))
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

        if (spell.getName().toLowerCase().contains("water"))
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

        return 1;
    }

    public static double calculateMagicMaxHit(Client client, ItemManager itemManager, Item[] playerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, CombatSpell spell)
    {
        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        double spellBaseMaxHit = getSpellBaseHit(client, playerEquipment, client.getBoostedSkillLevel(Skill.MAGIC), spell);

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = getMagicEquipmentBoost(client, itemManager, playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Get Tome Bonuses
        double correctTomeSpellBonus = getTomeSpellBonus(client, playerEquipment, spell); // default 1
        maxDamage = maxDamage * correctTomeSpellBonus;

        // Step 3: Calculate Type Bonuses
        // Not used here.

        return maxDamage;
    }
}
