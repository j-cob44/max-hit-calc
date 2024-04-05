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
import net.runelite.api.events.*;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

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
	private ClientThread clientThread;

	@Inject
	private MaxHitCalcConfig config;

	@Inject
	private ItemManager itemManager;

	// Public Max Hit variables, calculated when Equipment changes
	public int maxHit = 0;
	public int maxSpec = 0;
	public int maxVsType = 0;
	public int maxSpecVsType = 0;

	// Variable to check custom "gamestate"
	private boolean gameReady; // false before logged-in screen, true once logged-in screen closes, reset on logout

//	DEBUG
//	@Subscribe
//	public void onChatMessage(ChatMessage chatMessageReceived)
//	{
//		if(chatMessageReceived.getMessage().equals("!Allstyles"))
//		{
//			System.out.println("================S===============");
//			int[] weaponStyles = client.getEnum(EnumID.WEAPON_STYLES).getIntVals();
//
//			for(int i = 0; i < weaponStyles.length; i++)
//			{
//				System.out.println("i: " + i + "; WS: " + weaponStyles[i]);
//			}
//			System.out.println("================E===============");
//		}
//
//		if(chatMessageReceived.getMessage().equals("!Getstyle"))
//		{
//			int attackStyleID = client.getVarpValue(VarPlayer.ATTACK_STYLE);
//			int weaponTypeID = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
//
//			System.out.println("=================S==================");
//			System.out.println("WTID: " + weaponTypeID);
//			System.out.println("ASID: " + attackStyleID);
//			System.out.println("=================E==================");
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

		// Check if plugin started while game is running
		if (client.getGameState().equals(GameState.LOGGED_IN))
		{
			gameReady = true; // Set true if game is logged in and ready
		}
		else
		{
			gameReady = false; // Set false on normal runelite boot
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(pluginOverlay);
	}

	// On Widget Closed, check for when login screen is closed
	@Subscribe
	public void onWidgetClosed(WidgetClosed widget)
	{

		if (widget.getGroupId() == InterfaceID.LOGIN_CLICK_TO_PLAY_SCREEN)
		{
			gameReady = true; // Set as soon as user closes welcome screen
			calculateMaxes();
		}
	}

	// Un-ready when logged out.
	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		// On return to login screen, gameReady = false
		if (event.getGameState().equals(GameState.LOGIN_SCREEN))
		{
			gameReady = false;
		}
	}

	// OnItemContainerChanged, waiting for equipment container
	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		// On Item Equip/de-equip
		if(event.getContainerId() == InventoryID.EQUIPMENT.getId())
		{
			calculateMaxes();
		}
	}

	// OnVarbitChanged, waiting for change in prayer, attack style, or selected spell
	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if(!gameReady)
		{
			// Fix for potential out-of-order startup problems (logging in without welcome screen)
			if (client.getGameState() == GameState.LOGGED_IN)
			{
				gameReady = true;
			}
			else
			{
				return; // do nothing, before you can see the game
			}
		}

//		System.out.println("Varplayer: " + event.getVarpId());
//		System.out.println("Varbit: " + event.getVarbitId());
//		System.out.println("Varbit value: " + event.getValue());

		// On prayer changed: 83 = normal prayerbook
		if (event.getVarpId() == 83)
		{
			calculateMaxes();
		}

		// On attack style changed
		if (event.getVarpId() == VarPlayer.ATTACK_STYLE)
		{
			calculateMaxes();
		}

		// On selected Spell changed
		if (event.getVarbitId() == 276)
		{
			calculateMaxes();
		}

		// On soul stack changed
		if (event.getVarpId() == 3784) // Should be replaced with VarPlayer.SOUL_STACK when implemented. See this PR for more info: https://github.com/runelite/runelite/pull/17390
		{
			calculateMaxes();
		}

		// On Charge Spell Buff Start/End
		if(event.getVarpId() == VarPlayer.CHARGE_GOD_SPELL)
		{
			calculateMaxes();
		}
	}

	// OnStatChanged, waiting for skill changes, boosted or levelled
	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		// On Strength Changed
		if(event.getSkill() == Skill.STRENGTH)
		{
			calculateMaxes();
		}
		// On Ranged Changed
		if(event.getSkill() == Skill.RANGED)
		{
			calculateMaxes();
		}
		// On Magic Changed
		if(event.getSkill() == Skill.MAGIC)
		{
			calculateMaxes();
		}
		// On HP Changed
		if(event.getSkill() == Skill.HITPOINTS)
		{
			calculateMaxes();
		}
	}

	// On config Changed, run calculations
	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		// Only update for this plugin!
		if(event.getGroup().contains("MaxHitCalc"))
		{
			if(gameReady)
			{
				clientThread.invoke(this::calculateMaxes);
			}
		}
	}

	// Calculates all panel max hits.
	public void calculateMaxes()
	{
		maxHit = (int)Math.floor(MaxHit.calculate(client, itemManager, config, false));

		maxSpec = (int)Math.floor(MaxSpec.calculate(client, itemManager, config));
		if(config.displayMultiHitWeaponsAsOneHit())
		{
			int multiHitSpec = MaxSpec.getSpecMultiHit(client, maxSpec);
			if(multiHitSpec != 0)
			{
				maxSpec = multiHitSpec;
			}
		}

		maxVsType = (int)Math.floor(MaxAgainstType.calculate(client, itemManager, config));

		maxSpecVsType = (int)Math.floor(MaxSpecAgainstType.calculate(client, itemManager, config));
		if(config.displayMultiHitWeaponsAsOneHit())
		{
			int multiHitSpec = MaxSpec.getSpecMultiHit(client, maxSpecVsType);
			if(multiHitSpec != 0)
			{
				maxSpecVsType = multiHitSpec;
			}
		}
	}
}
