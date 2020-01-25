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

package me.ryanhamshire.griefprevention;

import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

//asynchronously loads player data without caching it in the datastore, then
//passes those data to a claim cleanup task which might decide to delete a claim for inactivity

public class CleanupUnusedClaimPreTask extends BukkitRunnable {
	private UUID ownerID = null;

	public CleanupUnusedClaimPreTask(UUID uuid) {
		this.ownerID = uuid;
	}

	@Override
	public void run() {
		//get the data
		PlayerData ownerData = GriefPrevention.instance.dataStore.getPlayerDataFromStorage(ownerID);
		OfflinePlayer ownerInfo = Bukkit.getServer().getOfflinePlayer(ownerID);

		//expiration code uses last logout timestamp to decide whether to expire claims
		//don't expire claims for online players
		if (ownerInfo.isOnline()) return;
		if (ownerInfo.getLastPlayed() <= 0) return;

		GriefPrevention.addLogEntry("Looking for expired claims.  Checking data for " + ownerID.toString(), CustomLogEntryTypes.Debug, true);

		//skip claims belonging to exempted players based on block totals in config
		int bonusBlocks = ownerData.getBonusClaimBlocks();
		if (bonusBlocks >= Config.config_claims_expirationExemptionBonusBlocks || bonusBlocks + ownerData.getAccruedClaimBlocks() >= Config.config_claims_expirationExemptionTotalBlocks) {
			GriefPrevention.addLogEntry("Player exempt from claim expiration based on claim block counts vs. config file settings.", CustomLogEntryTypes.Debug, true);
			return;
		}

		Claim claimToExpire = null;

		for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
			if (ownerID.equals(claim.ownerID)) {
				claimToExpire = claim;
				break;
			}
		}

		if (claimToExpire == null) {
			GriefPrevention.addLogEntry("Unable to find a claim to expire for " + ownerID.toString(), CustomLogEntryTypes.Debug, false);
			return;
		}

		//pass it back to the main server thread, where it's safe to delete a claim if needed
		new CleanupUnusedClaimTask(claimToExpire, ownerData, ownerInfo).runTaskLater(GriefPrevention.instance, 1L);
	}
}