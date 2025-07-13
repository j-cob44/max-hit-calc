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
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.ClientToolbar;

import java.awt.image.BufferedImage;

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

	@Inject
	private ConfigManager configManager;

	// UI Panels
	@Inject
	private ClientToolbar clientToolbar;
	private MaxHitCalcPanel panel;
	private NavigationButton navButton;

	//
	private MaxHit maxHits;
	private MaxSpec maxSpecs;
	private MaxAgainstType maxAgainstTypes;
	private MaxSpecAgainstType maxSpecsAgainstTypes;

	// Public Max Hit variables, calculated when Equipment changes
	public int maxHit = 0;
	public int maxSpec = 0;
	public int maxVsType = 0;
	public int maxSpecVsType = 0;

	// Variable to check custom "gamestate"
	private boolean gameReady; // false before logged-in screen, true once logged-in screen closes, reset on logout

	// Variables for Currently interacting NPC
	public String selectedNPCName;
	public int selectedNPCExpiryTime = Integer.MAX_VALUE;
	boolean npcSelectedByPanel = false;
	boolean npcResetByPanel = false;

	// Vars for Calculations
	public int NPCSize = 1;
	boolean npcSizeSettingChanged = false;
	boolean markOfDarknessActive = false;

	public BlowpipeDartType selectedDartType = BlowpipeDartType.MITHRIL;
	boolean dartSettingChanged = false;




	@Subscribe
	public void onChatMessage(ChatMessage chatMessageReceived)
	{
		String message = chatMessageReceived.getMessage().toLowerCase();

		// Mark of darkness workaround since it doesn't seem to change a varbit
		if(message.contains("you have placed a mark of darkness upon yourself."))
		{
			markOfDarknessActive = true;
			calculateMaxes();
		}
		if(message.contains("your mark of darkness has faded away."))
		{
			markOfDarknessActive = false;
			calculateMaxes();
		}

		// DEBUG CHAT COMMANDS
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
	}

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

		maxHits = new MaxHit(this, config, itemManager, client);
		maxSpecs = new MaxSpec(this, config, itemManager, client);
		maxAgainstTypes = new MaxAgainstType(this, config, itemManager, client);
		maxSpecsAgainstTypes = new MaxSpecAgainstType(this, config, itemManager, client);


		// UI Startup
		panel = injector.getInstance(MaxHitCalcPanel.class);
		panel.init(this, config);

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Max Hit Calc")
				.icon(icon)
				.priority(7)
				.panel(panel)
				.build();

		if(config.showPanel()) {
			clientToolbar.addNavigation(navButton);
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(pluginOverlay);

		panel.deinit();
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
	}

	// On Widget Closed, check for when login screen is closed
	@Subscribe
	public void onWidgetClosed(WidgetClosed widget)
	{
		if (widget.getGroupId() == InterfaceID.WELCOME_SCREEN) // "Click to play" screen interface id = 378
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
		if(event.getContainerId() == InventoryID.WORN) // Equipment Container ID
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
		if (event.getVarpId() == VarPlayerID.PRAYER0)
		{
			calculateMaxes();
		}

		// On attack style changed
		if (event.getVarpId() == VarPlayerID.COM_MODE) // Varplayer: Attack Style
		{
			calculateMaxes();
		}

		// On selected Spell changed
		if (event.getVarbitId() == VarbitID.AUTOCAST_SPELL)
		{
			calculateMaxes();
		}

		// On soul stack changed
		if (event.getVarpId() == VarPlayerID.SOULREAPER_STACKS) // Varplayer: Soul Stack
		{
			calculateMaxes();
		}

		// On Charge Spell Buff Start/End
		if(event.getVarpId() == VarPlayerID.MAGEARENA_CHARGE) // Varplayer: Charge God Spell
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

				// Update on panel if updated in settings
				if(event.getKey().equals("blowpipeDartType"))
				{
					panel.configDartSwitched();
				}
			}

			// Show or hide the side panel
			if(config.showPanel()) {
				clientToolbar.addNavigation(navButton);
			} else {
				clientToolbar.removeNavigation(navButton);
			}
		}
	}

	// Get Selected NPC from interaction
	@Subscribe
	public void onInteractingChanged(InteractingChanged interaction)
	{
		// Verify interaction is between user and npc
		if(interaction.getSource() != null)
		{
			// Verify source == local player
			String localPlayerName = client.getLocalPlayer().getName();
			String sourceName = interaction.getSource().getName();

			if(localPlayerName.equals(sourceName))
			{
				if(interaction.getTarget() != null)
				{
					NPC rawNPC = (NPC)interaction.getTarget();

					if(rawNPC != null)
					{
						// Do nothing for combat dummy or bankers, etc
						if(rawNPC.getCombatLevel() == 0) return; // Can't fight it, don't calc it

						if(config.timeToWaitBeforeResettingSelectedNPC() > 0)
						{
							selectedNPCExpiryTime = client.getTickCount() + (int)((config.timeToWaitBeforeResettingSelectedNPC() * 60)/0.6);
						}

						// Get necessary vars: name and size
						selectedNPCName = rawNPC.getComposition().getName();
						NPCSize = Math.max(1, rawNPC.getComposition().getSize()); // enforce to 1, incase of error

						panel.setNPCviaPlugin();

						calculateMaxes();
					}
				}
			}
		}
	}

	// After certain amount of ticks, clear clickedNPC
	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		// If NPC is selected, wait for time to expire to deselect it
		if(selectedNPCName != null)
		{
			if (selectedNPCExpiryTime < client.getTickCount() && config.timeToWaitBeforeResettingSelectedNPC() != 0)
			{
				selectedNPCName = null;
				NPCSize = 1;
				selectedNPCExpiryTime = Integer.MAX_VALUE; // Set higher than tick count
				panel.resetNPCviaPanel();
				calculateMaxes();
			}
		}

		// Check flags set by panel
		// Update tick after panel settings are changed
		if(npcSizeSettingChanged)
		{
			calculateMaxes();
			npcSizeSettingChanged = false;
		}

		if(dartSettingChanged)
		{
			calculateMaxes();
			configManager.setConfiguration("MaxHitCalc", "blowpipeDartType", selectedDartType);
			dartSettingChanged = false;
		}

		if(npcSelectedByPanel)
		{
			calculateMaxes();
			npcSelectedByPanel = false;
		}

		if (npcResetByPanel)
		{
			calculateMaxes();
			npcResetByPanel = false;
		}
	}

	// Calculates all panel max hits.
	public void calculateMaxes()
	{
		// Calculate Normal Max Hits
		maxHit = (int)Math.floor(maxHits.calculate(false));

		// Calculate Special Attack Max Hit
		maxSpec = (int)Math.floor(maxSpecs.calculate());
		if(config.displayMultiHitWeaponsAsOneHit())
		{
			int multiHitSpec = maxSpecs.getSpecMultiHit(maxSpec);
			if(multiHitSpec != 0)
			{
				maxSpec = multiHitSpec;
			}
		}

		// Calculate Max Hit vs Types of NPCs
		maxVsType = (int)Math.floor(maxAgainstTypes.calculate());


		// Calculate Special Attack Max Hit vs Types of NPCs
		maxSpecVsType = (int)Math.floor(maxSpecsAgainstTypes.calculate());
		if(config.displayMultiHitWeaponsAsOneHit())
		{
			int multiHitSpec = maxSpecs.getSpecMultiHit(maxSpecVsType);
			if(multiHitSpec != 0)
			{
				maxSpecVsType = multiHitSpec;
			}
		}
	}
}
