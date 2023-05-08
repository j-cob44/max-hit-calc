package com.maxhitcalc;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

// Custom Imports
import com.maxhitcalc.MaxHit;

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
	private ItemManager itemManager;

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
		if(chatMessageReceived.getMessage().equals("!Checkmax")){
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Current Max hit: " + calculateMaxHit(), null);
		}
	}

	@Provides
	MaxHitCalcConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MaxHitCalcConfig.class);
	}

	// Calculate Normal Max Hit
	public double calculateMaxHit(){
		int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
		int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);

		// Get Current Attack Style
		WeaponType weaponType = WeaponType.getWeaponType(weaponTypeID);
		AttackStyle[] weaponAttackStyles = weaponType.getAttackStyles();

		AttackStyle attackStyle = weaponAttackStyles[attackStyleID];

		// Get Current Equipment
		Item[] playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();

		// Find what type to calculate
		if(attackStyle.equals(AttackStyle.ACCURATE) || attackStyle.equals(AttackStyle.AGGRESSIVE) || attackStyle.equals(AttackStyle.CONTROLLED) || attackStyle.equals(AttackStyle.DEFENSIVE)){
			return MaxHit.calculateMeleeMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
		} else if (attackStyle.equals(AttackStyle.RANGING) || attackStyle.equals(AttackStyle.LONGRANGE)) {
			return MaxHit.calculateRangedMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
		} else if ((attackStyle.equals(AttackStyle.CASTING)  || attackStyle.equals(AttackStyle.DEFENSIVE_CASTING))){
			return MaxHit.calculateMagicMaxHit(client, itemManager, playerEquipment, attackStyle, attackStyleID);
		} else {
			return -1;
		}
	}
}
