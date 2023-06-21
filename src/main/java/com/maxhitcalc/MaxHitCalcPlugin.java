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
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
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

//	DEBUG
//	@Subscribe
//	public void onChatMessage(ChatMessage chatMessageReceived)
//	{
//		if(chatMessageReceived.getMessage().equals("!Checkmax"))
//		{
//
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

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOGGED_IN) {
			onLogIn();
		}
	}

	// Run Thread on login which waits for first game tick
	private void onLogIn()
	{
		clientThread.invokeLater(() ->
		{
			int tick = client.getTickCount();

			// If not first tick, retry
			if(tick < 1)
			{
				return false; // Reschedule task
			}

			calculateMaxes();
			return true; // Do not reschedule
		});
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

	// OnVarbitChanged, waiting for change in prayer
	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// On prayer change
		if (event.getVarpId() == 83)
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
	}

	// Calculates all panel max hits.
	public void calculateMaxes()
	{
		maxHit = (int)Math.floor(MaxHit.calculate(client, itemManager, config));

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
