/* MaxHitCalcConfig.java
 * Contains all config items for the plugin.
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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("MaxHitCalc")
public interface MaxHitCalcConfig extends Config
{
	// Show Max Hit
	@ConfigItem(
			keyName = "showMaxHit",
			name = "Display Max Hit",
			description = "Shows your Max Hit with equipped item.",
			position = 1
	)
	default boolean showMaxHit()
	{
		return true;
	}

	// Show Max Spec
	@ConfigItem(
		keyName = "showSpec",
		name = "Display Max Spec",
		description = "Shows your Max Spec with equipped items.",
		position = 1
	)
	default boolean showSpec()
	{
		return true;
	}

	// Show Max Hit vs Type
	@ConfigItem(
			keyName = "showType",
			name = "Display Max Hit vs Type",
			description = "Shows your Max Hit vs current Type bonus.",
			position = 2
	)
	default boolean showType()
	{
		return true;
	}

	// Show Max Spec vs Type
	@ConfigItem(
			keyName = "showSpecVsType",
			name = "Display Max Spec vs Type",
			description = "Shows your Max Spec vs current Type bonus.",
			position = 3
	)
	default boolean showSpecVsType()
	{
		return true;
	}

	// Show Predicted Requirements for next Max Hit
	@ConfigItem(
			keyName = "showPredictionTooltip",
			name = "Tooltip for Predicted Next Max Hit",
			description = "Hover over the panel for a prediction of requirements for next max hit.",
			position = 4
	)
	default boolean showPredictionTooltip()
	{
		return true;
	}


	// Show Predicted Max Hit when hovering over item in inventory
	@ConfigItem(
			keyName = "showInventoryTooltip",
			name = "Tooltip on Inventory Item",
			description = "Hover over an item in your inventory to see it's max hit impact when equipped.",
			position = 5
	)
	default boolean showInventoryTooltip()
	{
		return true;
	}
}
