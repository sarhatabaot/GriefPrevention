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

package me.ryanhamshire.griefprevention.siege;

import java.util.ArrayList;
import java.util.List;

import me.ryanhamshire.griefprevention.claim.Claim;
import org.bukkit.entity.Player;

//information about an ongoing siege
public class SiegeData {
	private Player defender;
	private Player attacker;
	private List<Claim> claims;
	private int checkupTaskID;

	public SiegeData(Player attacker, Player defender, Claim claim) {
		this.setDefender(defender);
		this.setAttacker(attacker);
		this.setClaims(new ArrayList<>());
		this.getClaims().add(claim);
	}

	public Player getDefender() {
		return defender;
	}

	public void setDefender(Player defender) {
		this.defender = defender;
	}

	public Player getAttacker() {
		return attacker;
	}

	public void setAttacker(Player attacker) {
		this.attacker = attacker;
	}

	public List<Claim> getClaims() {
		return claims;
	}

	public void setClaims(List<Claim> claims) {
		this.claims = claims;
	}

	public int getCheckupTaskID() {
		return checkupTaskID;
	}

	public void setCheckupTaskID(int checkupTaskID) {
		this.checkupTaskID = checkupTaskID;
	}
}
