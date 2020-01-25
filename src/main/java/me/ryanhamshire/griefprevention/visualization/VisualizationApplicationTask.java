/*
    GriefPrevention Server Plugin for Minecraft
    Copyright (C) 2012 Ryan Hamshire

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.ryanhamshire.griefprevention.visualization;

import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.PlayerData;
import org.bukkit.entity.Player;

//applies a visualization for a player by sending him block change packets
public class VisualizationApplicationTask implements Runnable {
	private Visualization visualization;
	private Player player;
	private PlayerData playerData;

	public VisualizationApplicationTask(Player player, PlayerData playerData, Visualization visualization) {
		this.visualization = visualization;
		this.playerData = playerData;
		this.player = player;
	}

	@Override
	public void run() {
		//for each element (=block) of the visualization
		for (int i = 0; i < visualization.elements.size(); i++) {
			VisualizationElement element = visualization.elements.get(i);

			//send the player a fake block change event
			if (!element.getLocation().getChunk().isLoaded()) continue;  //cheap distance check
			player.sendBlockChange(element.getLocation(), element.getVisualizedBlock());
		}

		//remember the visualization applied to this player for later (so it can be inexpensively reverted)
		playerData.currentVisualization = visualization;

		//schedule automatic visualization reversion in 60 seconds.
		GriefPrevention.instance.getServer().getScheduler().scheduleSyncDelayedTask(
				GriefPrevention.instance,
				new VisualizationReversionTask(player, playerData, visualization),
				20L * 60);  //60 seconds
	}
}
