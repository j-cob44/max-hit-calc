/* MaxHitCalcPlugin.java
 * Main program code for Max Hit Calc RuneLite plugin.
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

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage; // for debug
import net.runelite.client.eventbus.Subscribe; // for debug
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Max Hit Calculator",
	description = "Calculates Max Hit stats for the current equipment setup.",
	tags = "max hit, combat, stats, helpful, melee, ranged, magic"
)
public class MaxHitCalcPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private MaxHitCalcOverlay pluginOverlay;

	@Inject
	private Client client;

	@Inject
	private MaxHitCalcConfig config;

	@Inject
	private ItemManager itemManager;

//	DEBUG
//	@Subscribe
//	public void onChatMessage(ChatMessage chatMessageReceived)
//	{
//		if(chatMessageReceived.getMessage().equals("!Checkmax"))
//		{
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Current Max Hit: " + Math.floor(calculateMaxHit()), null);
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Current Max Spec Hit: " + calculateMaxSpec(), null);
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Current Max Against Type: " + calculateMaxAgainstType(), null);
//		}
//	}

	@Provides
	MaxHitCalcConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MaxHitCalcConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(pluginOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(pluginOverlay);
	}

	// Calculate Normal Max Hit
	public double calculateMaxHit()
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
			return MaxHit.calculateMeleeMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
		}
		else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
		{
			return MaxHit.calculateRangedMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID, config.blowpipeDartType());
		}
		else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
		{
			return MaxHit.calculateMagicMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
		}
		else
		{
			return -1;
		}
	}

	// Calculate Max Spec Hit
	public double calculateMaxSpec()
	{
		// Get Current Equipment
		Item[] playerEquipment;
		if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
		{
			playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
		}
		else {
			return 0;
		}

		String weaponName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName();

		// Get Spec modifier
		double specialAttackWeapon = MaxSpec.getSpecWeaponStat(client, weaponName, playerEquipment);

		// Debug
		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Spec Modifier: " + specialAttackWeapon, null);

		if(specialAttackWeapon != 0)
		{
			// Get Max hit then calculate Spec
			double maxHit = calculateMaxHit();
			double maxSpecHit = maxHit * specialAttackWeapon;

			return maxSpecHit;
		}

		return 0; // No spec attack on weapon
	}

	// Calculate Max Hit against Type bonus
	public double calculateMaxAgainstType()
	{
		// Get Current Equipment
		Item[] playerEquipment;
		if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
		{
			playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
		}
		else {
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
		double maxHit = calculateMaxHit(); // Normal Max
		double maxHitVsType = Math.floor(MaxAgainstType.calculateMaxHit(client, itemManager, config)); // Vs Type Max

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

	public double calculateMaxSpecAgainstType(){
		// Get Current Equipment
		Item[] playerEquipment;
		if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
		{
			playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
		}
		else {
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

		if(!typeModifiersList.isEmpty())
		{
			// Get Max hit then calculate Spec
			double maxSpec = calculateMaxSpec();

			if(maxSpec != 0)
			{
				double maxSpecVsTypeHit = Math.floor(maxSpec);
				// Iterate through modifiers, flooring after multiplying
				for (double modifier: typeModifiersList)
				{
					maxSpecVsTypeHit = Math.floor(maxSpecVsTypeHit * modifier);
				}

				return maxSpecVsTypeHit;
			}
		}

		return 0; // No Type Bonus
	}

	public List<Object> predictNextMaxHit(){
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
			List<Object> meleeResults = PredictNextMax.predictNextMeleeMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);

			// index: 0 = "melee", 1 = strength level, 2 = equipment strength bonus, 3 = prayer percent bonus
			return meleeResults;
		}
		else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
		{
			List<Object> rangedResults = PredictNextMax.predictNextRangeMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID, config.blowpipeDartType());

			// index: 0 = "ranged", 1 = range level, 2 = range equipment strength bonus, 3 = prayer percent bonus
			return rangedResults;
		}
		else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
		{
			List<Object> mageResults = PredictNextMax.predictNextMageMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);

			// index: 0 = "magic", 1 = magic level, 2 = mage equipment damage bonus
			return mageResults;
		}
		else
		{
			return null;
		}
	}

	// Calculate Max Hit for an inventory item
	public double calculateMaxHitFromInventory(int slotID, int itemID)
	{
		// Initialize Variables
		int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
		int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
		AttackStyle attackStyle = null;

		// Get Current Equipment
		Item[] playerEquipment;
		if (client.getItemContainer(InventoryID.EQUIPMENT) != null )
		{
			playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
		}
		else {
			playerEquipment = null;
		}

		// Determine if Attack Style is correct
		if(slotID == 3)
		{
			// IS A WEAPON
			attackStyle = InventoryItemMaxHit.determineAttackStyle(client, itemID);
		}
		else
		{
			// Get Current Attack Style
			WeaponType weaponType = WeaponType.getWeaponType(weaponTypeID);
			AttackStyle[] weaponAttackStyles = weaponType.getAttackStyles();

			attackStyle = weaponAttackStyles[attackStyleID];
		}

		// Get corrected slot ID if player is not fully equipped
		//slotID = InventoryItemMaxHit.getCorrectedSlotID(client, slotID);

		// Find what type to calculate
		if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE))
		{
			return InventoryItemMaxHit.calculateMeleeMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID, slotID, itemID);
		}
		else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE))
		{
			return InventoryItemMaxHit.calculateRangedMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID, slotID, itemID, config.blowpipeDartType());
		}
		else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING)))
		{
			double magicMaxHit = InventoryItemMaxHit.calculateMagicMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID, slotID, itemID);

			// If -1, error skip
			if (magicMaxHit > -1){
				return magicMaxHit;
			}
		}

		return -1;

	}
}
