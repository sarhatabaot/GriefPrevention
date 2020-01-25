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

package me.ryanhamshire.GriefPrevention.visualization;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

//represents a "fake" block sent to a player as part of a visualization
public class VisualizationElement {
	private Location location;
	private BlockData visualizedBlock;
	private BlockData realBlock;

	public VisualizationElement(Location location, BlockData visualizedBlock, BlockData realBlock) {
		this.setLocation(location);
		this.setVisualizedBlock(visualizedBlock);
		this.setRealBlock(realBlock);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public BlockData getVisualizedBlock() {
		return visualizedBlock;
	}

	public void setVisualizedBlock(BlockData visualizedBlock) {
		this.visualizedBlock = visualizedBlock;
	}

	public BlockData getRealBlock() {
		return realBlock;
	}

	public void setRealBlock(BlockData realBlock) {
		this.realBlock = realBlock;
	}
}
