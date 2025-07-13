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

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Skill;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.game.ItemManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains functions for calculating max hit vs specific types of npc.
 */
public class MaxAgainstType extends MaxHit {
    MaxAgainstType(MaxHitCalcPlugin plugin, MaxHitCalcConfig config, ItemManager itemManager, Client client)
    {
        super(plugin, config, itemManager, client);
    }

    protected List<Double> getTypeBonus(AttackStyle attackStyle, Item[] playerEquipment)
    {
        List<Double> typeBonusToApply = new ArrayList<>();

        // Get Required Items
        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        String amuletItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.AMULET);
        String headItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.HEAD);
        String bodyItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.BODY);
        String legsItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.LEGS);

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
            if(weaponItemName.contains("Silverlight"))
            {
                typeBonusToApply.add(1.6) ;
            }
            else if(weaponItemName.contains("Darklight"))
            {
                typeBonusToApply.add(1.6); // same bonus as silverlight
            }

            // Shades, added 22 March 2006
            if(weaponItemName.contains("Gadderhammer"))
            {
                typeBonusToApply.add(1.25);
            }

            // Demonbane, added 9 June 2016
            if(weaponItemName.contains("Arclight"))
            {
                typeBonusToApply.add(1.7); // different from silverlight and darklight
            }
            else if(weaponItemName.contains("Emberlight"))
            {
                typeBonusToApply.add(1.7); //same as Arclight, but does not degrade
            }

            // Leaf-bladed Battleaxe vs Turoths and Kurasks, 15 September 2016
            if(weaponItemName.contains("Leaf-bladed battleaxe"))
            {
                typeBonusToApply.add(1.175);
            }

            // Dragonbane, added 5 January 2017
            if(weaponItemName.contains("Dragon hunter"))
            {
                typeBonusToApply.add(1.2); // same as dragon hunter crossbow boost which was added first
            }

            // Vampyre, added 24 May 2018
            if(weaponItemName.contains("Ivandis flail"))
            {
                typeBonusToApply.add(1.2);
            }

            // Wilderness, added 26 July 2018
            if(weaponItemName.contains("Viggora's"))
            {
                if(!weaponItemName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Wilderness, added 26 July 2018
            if(weaponItemName.contains("Ursine chainmace"))
            {
                if(!weaponItemName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Inquisitor's Crush Bonus, added 6 February 2020, set effect removed 25 September 2024
            // Now, wearing the Mace is the only requirement for the inquisitor's crush bonus
            if (weaponItemName.contains("Inquisitor"))
            {
                // If on crush style, which for the mace is all styles except controlled
                if (attackStyle != AttackStyle.CONTROLLED)
                {
                    typeBonusToApply.add(1.025); // +2.5% for the mace

                    // +2.5% crush dmg bonus for each armor piece if wearing the mace
                    if (headItemName.contains("Inquisitor's"))
                    {
                        typeBonusToApply.add(1.025);
                    }

                    if (bodyItemName.contains("Inquisitor's"))
                    {
                        typeBonusToApply.add(1.025);
                    }

                    if (legsItemName.contains("Inquisitor's"))
                    {
                        typeBonusToApply.add(1.025);
                    }
                }
            }

            // Vampyre, added 4 June 2020
            if(weaponItemName.contains("Blisterwood flail"))
            {
                typeBonusToApply.add(1.25);
            }

            // Golem bonus, added 14 April 2021, updated on 28 April 2021
            if(weaponItemName.contains("Barronite mace"))
            {
                typeBonusToApply.add(1.15);
            }

            // Kalphite, acording to Mod Ash, added with Partisan, 27 April 2022
            if(weaponItemName.contains("Keris"))
            {
                typeBonusToApply.add(1.33);
            }

            // Demonbane, added 10 July 2024
            if(weaponItemName.contains("Burning claws"))
            {
                typeBonusToApply.add(1.05) ;
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
            if(weaponItemName.contains("Dragon hunter"))
            {
                typeBonusToApply.add(1.25);
            }

            // Wilderness, added 26 July 2018
            if(weaponItemName.contains("Craw's bow"))
            {
                if(!weaponItemName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Wilderness, added 26 July 2018
            if(weaponItemName.contains("Webweaver bow"))
            {
                if(!weaponItemName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Demonbane, added 10 July 2024
            if(weaponItemName.contains("Scorching bow"))
            {
                typeBonusToApply.add(1.3);
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

            // Dragonbane, added 5 January 2017 ; OR potentially "new" type since value is different from original, added 25 September 2024
            if(weaponItemName.contains("Dragon hunter"))
            {
                typeBonusToApply.add(1.4);
            }

            // Wilderness, added 26 July 2018
            if(weaponItemName.contains("Thammaron's sceptre"))
            {
                if(!weaponItemName.contains("(u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }

            // Wilderness, added 26 July 2018
            if(weaponItemName.contains("Accursed sceptre"))
            {
                if(!weaponItemName.contains("u)"))
                {
                    typeBonusToApply.add(1.5);
                }
            }
        }

        return typeBonusToApply; // List of Modifiers
    }

    // Needed in Magic for Slayer Staff (e)
    protected double getSpellBaseHit(Item[] playerEquipment, AttackStyle weaponAttackStyle)
    {
        double basehit = 0;
        double magicLevel = client.getBoostedSkillLevel(Skill.MAGIC);

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
        // Warped sceptre
        else if(weaponItemName.contains("Warped sceptre"))
        {
            // Current Wiki Value
            basehit = Math.floor(((8*magicLevel)+96)/37);
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
        else if(weaponItemName.contains("Bone staff"))
        {
            basehit = Math.floor(magicLevel/3) + 5;
        }
        // Autocasted Spell
        else
        {
            // Check if casting without spell selected
            int selectedSpellId = client.getVarbitValue(VarbitID.AUTOCAST_SPELL); // Varbit 276 is Selected Autocasted Spell
            if (selectedSpellId == 0)
            {
                // no spell selected
                return -1; // error
            }

            CombatSpell selectedSpell = CombatSpell.getSpellbyVarbitValue(selectedSpellId);

            // Debug
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "VsType: Selected Spell ID: " + selectedSpellId, null);
            //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "VsType: Selected Spell: " + selectedSpell, null);

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

                // Purging Staff Boost to Demonbane spells
                if(weaponItemName.contains("Purging staff"))
                {
                    if((selectedSpell == CombatSpell.INFERIOR_DEMONBANE) || (selectedSpell == CombatSpell.SUPERIOR_DEMONBANE) || (selectedSpell == CombatSpell.DARK_DEMONBANE))
                    {
                        basehit = (selectedSpell.getBaseDamage() * 2); // Demonbane spells are doubled
                    }
                }

                // God Spell Cases with Charge
                if((selectedSpell == CombatSpell.FLAMES_OF_ZAMORAK) || (selectedSpell == CombatSpell.CLAWS_OF_GUTHIX) || (selectedSpell == CombatSpell.SARADOMIN_STRIKE))
                {
                    if (client.getVarpValue(VarPlayerID.MAGEARENA_CHARGE) > 0) // Varplayer: Charge God Spell
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

    protected double calculateMagicMaxHit(Item[] playerEquipment, AttackStyle weaponAttackStyle)
    {
        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        double spellBaseMaxHit = getSpellBaseHit(playerEquipment, weaponAttackStyle);

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = getMagicEquipmentBoost(playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Step 3: Calculate Bonuses
        // Tome Bonuses
        double correctTomeSpellBonus = getTomeSpellBonus(playerEquipment, weaponAttackStyle); // default 1
        maxDamage = maxDamage * correctTomeSpellBonus;

        CombatSpell spell = getSpell();

        // Twinflame Staff Double Hit bonus
        String weaponItemName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        if (weaponItemName.toLowerCase().contains("twinflame staff"))
        {
            if (spell != null && spell.getSpellbook().contains("standard")) {

                if(!spell.getName().toLowerCase().contains("strike") && !spell.getName().toLowerCase().contains("surge"))
                {
                    double bonusHit = Math.floor(maxDamage) * 0.4;
                    maxDamage = Math.floor(maxDamage) + Math.floor(bonusHit);
                }
            }
        }

        return maxDamage;
    }

    private double calculateTypeMaxHit()
    {
        int attackStyleID = client.getVarpValue(VarPlayerID.COM_MODE); // Varplayer: Attack Style
        int weaponTypeID = client.getVarbitValue(VarbitID.COMBAT_WEAPON_CATEGORY);  // Varbit: Equipped Weapon Type

        // Get Current Attack Style
        AttackStyle[] weaponAttackStyles = WeaponType.getWeaponTypeStyles(client, weaponTypeID);
        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];

        // Get Current Equipment
        Item[] playerEquipment;
        if (client.getItemContainer(InventoryID.WORN) != null ) // Equipment container ID = 94
        {
            playerEquipment = client.getItemContainer(InventoryID.WORN).getItems();
        }
        else
        {
            playerEquipment = null;
        }

        // Find what type to calculate
        if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE))
        {
            return calculateMeleeMaxHit(playerEquipment, attackStyle, attackStyleID, false);
        }
        else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
        {
            return calculateRangedMaxHit(playerEquipment, attackStyle, attackStyleID);
        }
        else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
        {
            return calculateMagicMaxHit(playerEquipment, attackStyle);
        }
        else
        {
            return -1;
        }
    }

    /**
     * Calculates Max Hit Vs Types
     *
     * @return Max Hit vs Types as Double
     */
    public double calculate()
    {
        // Get Current Equipment
        Item[] playerEquipment = EquipmentItems.getCurrentlyEquipped(client);
        int attackStyleID = client.getVarpValue(VarPlayerID.COM_MODE); // Varplayer: Attack Style
        int weaponTypeID = client.getVarbitValue(VarbitID.COMBAT_WEAPON_CATEGORY);  // Varbit: Equipped Weapon Type

        // Get Current Attack Style
        AttackStyle[] weaponAttackStyles = WeaponType.getWeaponTypeStyles(client, weaponTypeID);
        AttackStyle attackStyle = weaponAttackStyles[attackStyleID];
        if (attackStyle == null)
            return 0;

        // Get Type modifier
        List<Double> typeModifiersList = this.getTypeBonus(attackStyle, playerEquipment);

        // Debug Modifiers
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Type Modifiers: " + typeModifiersList.toString(), null);

        // Get Max hit
        MaxHit normalMaxCalc = new MaxHit(plugin, config, itemManager, client);
        double maxHit = normalMaxCalc.calculate(false); // Normal Max
        double maxHitVsType = Math.floor(this.calculateTypeMaxHit()); // Vs Type Max

        String weaponName = EquipmentItems.getItemNameInGivenSetSlot(client, playerEquipment, EquipmentInventorySlot.WEAPON);
        CombatSpell spell = getSpell();

        // Remove bonuses that are constant from normal max hit in order to calculate max vs type.
        // Remove default + 2 colossal blade bonus
        if(weaponName.contains("Colossal blade"))
        {
            maxHitVsType = maxHitVsType - 2; // remove default bonus
        }

        // remove default +10 rat bonus
        if(weaponName.contains("Bone mace") || weaponName.contains("Bone shortbow"))
        {
            maxHitVsType = maxHitVsType - 10;
        }

        // Iterate through modifiers, flooring after multiplying
        if(!typeModifiersList.isEmpty())
        {
            for (double modifier: typeModifiersList)
            {
                maxHitVsType = Math.floor(maxHitVsType * modifier);
            }
        }

        // Re Add Bonuses that do not scale
        // Re-add Colossal Blade Increase, factoring in other modifiers.
        if(weaponName.contains("Colossal blade"))
        {
            int sizeBonus = (2 * Math.min(plugin.NPCSize, 5));

            maxHitVsType = maxHitVsType + sizeBonus;
        }

        // Re-add Rat +10 Damage Bonus
        if(weaponName.contains("Bone mace") || weaponName.contains("Bone shortbow"))
        {
            maxHitVsType = maxHitVsType + 10;
        }

        // Mark of darkness bonuses
        if(spell != null)
        {
            if(plugin.markOfDarknessActive)
            {
                if(spell.getName().toLowerCase().contains("demonbane"))
                {
                    if(weaponName.toLowerCase().contains("purging staff"))
                    {
                        double bonusDmg = Math.floor(maxHitVsType) * 0.5;
                        maxHitVsType = Math.floor(maxHitVsType) + Math.floor(bonusDmg);
                    }
                    else
                    {
                        double bonusDmg = Math.floor(maxHitVsType) * 0.25;
                        maxHitVsType = Math.floor(maxHitVsType) + Math.floor(bonusDmg);
                    }
                }
            }
        }

        // Final step: Calculate and add spell type weakness Bonus
        if (spell != null && spell.hasType())
        {
            // Get original base hit to add elemental bonus
            double spellBaseHit = 0;
            double magicLevel = client.getBoostedSkillLevel(Skill.MAGIC);

            if (spell.getTier() == 0)
            {
                // NO TIER,
                spellBaseHit = spell.getBaseDamage();
            }
            else {
                // GET TIER, Get highest tier in level
                int spellTier = spell.getTier();
                String spellbook = spell.getSpellbook();

                CombatSpell[] spellsInTier = CombatSpell.getSpellsOfTier(spellTier, spellbook);

                for (CombatSpell itSpell : spellsInTier) {
                    if (magicLevel >= spell.getReqLevel()) {
                        if (spellBaseHit <= itSpell.getBaseDamage()) {
                            // new highest found
                            spellBaseHit = itSpell.getBaseDamage();
                        }
                    }
                }
            }

            if (plugin.selectedNPCName != null)
            {
                NPCTypeWeakness weaknessBonus = NPCTypeWeakness.findWeaknessByName(plugin.selectedNPCName);
                if (weaknessBonus != null)
                {
                    if(spell.getSpellType() == weaknessBonus.getElementalWeakness())
                    {
                        int bonusPercent = weaknessBonus.getWeaknessPercent();

                        double typeBonusDamage = spellBaseHit * ((double) bonusPercent / (double)100);
                        maxHitVsType = maxHitVsType + typeBonusDamage;
                    }
                }
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
