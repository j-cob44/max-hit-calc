package com.maxhitcalc;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.http.api.item.ItemStats;

// Custom Imports
import java.lang.Math;

@Slf4j
@PluginDescriptor(
	name = "Max Hit Calculator"
)
public class MaxHitCalcPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MaxHitCalcConfig config;

	@Inject
	private ItemManager itemManger;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

//	@Subscribe
//	public void onGameStateChanged(GameStateChanged gameStateChanged)
//	{
//		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
//		{
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Current Max hit: " + calculateMaxHit(), null);
//		}
//	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessageReceived){
		if(chatMessageReceived.getMessage().equals("!Check123")){
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Current Max hit: " + calculateMeleeMaxHit(), null);
		}
	}

	@Provides
	MaxHitCalcConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MaxHitCalcConfig.class);
	}

	// Get Prayer Bonus for Max Hit Calculation
	public double getPrayerBonus(){
		// Melee Prayers
		if(client.isPrayerActive((Prayer.BURST_OF_STRENGTH))) return 1.05;
		if(client.isPrayerActive((Prayer.SUPERHUMAN_STRENGTH))) return 1.1;
		if(client.isPrayerActive((Prayer.ULTIMATE_STRENGTH))) return 1.15;
		if(client.isPrayerActive((Prayer.CHIVALRY))) return 1.18;
		if(client.isPrayerActive((Prayer.PIETY))) return 1.23;

		return 1; // default
	}

	// Get Attack Style Bonus
	public int getMeleeAttackStyleBonus(){
		// Get IDs from equipped weapon
		int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
		int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

		// Get Current Attack Style
		WeaponType weaponType = WeaponType.getWeaponType(weaponTypeID);
		AttackStyle[] weaponAttackStyles = weaponType.getAttackStyles();

		AttackStyle attackStyle = weaponAttackStyles[attackStyleID];

		// Return attack style bonus
		// Melee bonuses
		if (attackStyle.getName().equalsIgnoreCase("Aggressive")) return 3;
		if(attackStyle.getName().equalsIgnoreCase("Controlled")) return 1;

		return 0; // default
	}

	// Get Melee Strength Bonus from Weapon and armor
	public double getMeleeStrengthBonus(){
		double strengthBonus = 0;

		// Get Player Equipment
		Item[] playerItems = client.getItemContainer(InventoryID.EQUIPMENT).getItems();

		// Get Melee Strength Bonus of each equipped Item
		for (Item equipmentItem: playerItems) {
			if(equipmentItem.getId() != -1){
				int equipmentID = equipmentItem.getId();
				int equipmentStrengthStat = itemManger.getItemStats(equipmentID, false).getEquipment().getStr();

				strengthBonus += (double)equipmentStrengthStat;
			}
		}

		return strengthBonus;
	}

	public boolean getVoidMeleeBonus(){
		// Get Player Equipment
		Item[] playerItems = client.getItemContainer(InventoryID.EQUIPMENT).getItems();

		// Check for set bonus
		if (!client.getItemDefinition(playerItems[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Void melee")) return false; // if not void melee helm
		if (!client.getItemDefinition(playerItems[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Void")
				&& !client.getItemDefinition(playerItems[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("void")) return false; // if not void top and not elite top
		if (!client.getItemDefinition(playerItems[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Void")
				&& !client.getItemDefinition(playerItems[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("void")) return false; // if not void robe and not elite robe
		if (!client.getItemDefinition(playerItems[EquipmentInventorySlot.GLOVES.getSlotIdx()].getId()).getName().contains("Void")) return false; // if not void gloves

		// Void Set Complete, grant bonus
		return true;
	}

	// Passive Melee Set effects
	public double getMeleeSpecialBonusMultiplier(){
		double specialBonus = 1; // Initialize Variable

		// Get Player Equipment
		Item[] playerItems = client.getItemContainer(InventoryID.EQUIPMENT).getItems();

		// Debug
		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Head Slot: " + client.getItemDefinition(playerItems[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName(), null);
		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Body Slot: " + client.getItemDefinition(playerItems[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName(), null);
		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Legs Slot: " + client.getItemDefinition(playerItems[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName(), null);
		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Weapon Slot: " + client.getItemDefinition(playerItems[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName(), null);

		// Dharok's Set Check
		if (client.getItemDefinition(playerItems[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Dharok's")) {
			if(client.getItemDefinition(playerItems[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Dharok's")){
				if(client.getItemDefinition(playerItems[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Dharok's")){
					if(client.getItemDefinition(playerItems[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("Dharok's")){
						// Passed Check, Dharok's Set Equipped, Apply Effect
						double baseHP = client.getRealSkillLevel(Skill.HITPOINTS);
						double currentHP = client.getBoostedSkillLevel(Skill.HITPOINTS);
						specialBonus = (1 + (((baseHP - currentHP) / 100) * (baseHP / 100)));
					}
				}
			}
		}

		// Berserker Necklace and Obisidian Melee Check
		if(client.getItemDefinition(playerItems[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("ket")) {
			if(client.getItemDefinition(playerItems[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName().contains("Berserker")){
				specialBonus += 0.2;
			}
		}

		// Obsidian Set Check
		if (client.getItemDefinition(playerItems[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName().contains("Obsidian")) {
			if(client.getItemDefinition(playerItems[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName().contains("Obsidian")){
				if(client.getItemDefinition(playerItems[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName().contains("Obsidian")){
					if(client.getItemDefinition(playerItems[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName().contains("ket")){
						specialBonus += 0.1;
					}
				}
			}
		}

		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Special Bonus: " + specialBonus, null);
		return specialBonus;
	}

	// Calculate Melee Max Hit
	public double calculateMeleeMaxHit(){
		// Calculate Melee Max Hit
		// Step 1: Calculate effective Strength
		int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
		double prayerBonus = getPrayerBonus();
		int styleBonus = getMeleeAttackStyleBonus();
		boolean voidBonus = getVoidMeleeBonus(); // default false;

		double effectiveStrength = Math.floor((Math.floor(Math.floor(strengthLevel) * prayerBonus) + styleBonus + 8) * (voidBonus?1.1:1));

		// Step 2: Calculate the base damage
		double strengthBonus = getMeleeStrengthBonus(); // default 0

		double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
		double flooredBaseDamage = Math.floor(baseDamage);

		// Step 3: Calculate the bonus damage
		double specialBonusMultiplier = getMeleeSpecialBonusMultiplier(); // default 1

		double maxHit = Math.floor(flooredBaseDamage * specialBonusMultiplier);

		// Complete
		return maxHit;
	}
}
