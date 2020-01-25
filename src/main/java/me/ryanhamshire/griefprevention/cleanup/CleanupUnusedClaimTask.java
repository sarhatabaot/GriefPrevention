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

package me.ryanhamshire.griefprevention.cleanup;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import me.ryanhamshire.griefprevention.logging.CustomLogEntryTypes;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.PlayerData;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.ryanhamshire.griefprevention.events.ClaimExpirationEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CleanupUnusedClaimTask extends BukkitRunnable {
	private Claim claim;
	private PlayerData ownerData;
	private OfflinePlayer ownerInfo;

	public CleanupUnusedClaimTask(Claim claim, PlayerData ownerData, OfflinePlayer ownerInfo) {
		this.setClaim(claim);
		this.setOwnerData(ownerData);
		this.setOwnerInfo(ownerInfo);
	}

	private int determineDefaultClaimArea(){
		if (Config.config_claims_automaticClaimsForNewPlayersRadius >= 0) {
			return (int) Math.pow(Config.config_claims_automaticClaimsForNewPlayersRadius * 2 + 1, 2);
		}
		return 0;
	}

	private boolean isChestClaimExpired() {
		return (getClaim().getArea() <= determineDefaultClaimArea() && Config.config_claims_chestClaimExpirationDays > 0);
	}

	private boolean isNewPlayerExpired() {
		Calendar sevenDaysAgo = Calendar.getInstance();
		sevenDaysAgo.add(Calendar.DATE, -Config.config_claims_chestClaimExpirationDays);
		boolean newPlayerClaimsExpired = sevenDaysAgo.getTime().after(new Date(getOwnerInfo().getLastPlayed()));
		return newPlayerClaimsExpired && getOwnerData().getClaims().size() == 1;
	}

	@Override
	public void run() {
		//if this claim is a chest claim and those are set to expire
		if (isChestClaimExpired()) {
			//if the owner has been gone at least a week, and if he has ONLY the new player claim, it will be removed
			if (isNewPlayerExpired()) {
				if (expireEventCanceled())
					return;
				getClaim().removeSurfaceFluids(null);
				GriefPrevention.instance.dataStore.deleteClaim(getClaim(), true, true);

				//if configured to do so, restore the land to natural
				if (GriefPrevention.instance.creativeRulesApply(getClaim().getLesserBoundaryCorner()) || Config.config_claims_survivalAutoNatureRestoration) {
					GriefPrevention.instance.restoreClaim(getClaim(), 0);
				}

				GriefPrevention.addLogEntry(" " + getClaim().getOwnerName() + "'s new player claim expired.", CustomLogEntryTypes.AdminActivity);
			}
		}

		//if configured to always remove claims after some inactivity period without exceptions...
		else if (Config.config_claims_expirationDays > 0) {
			Calendar earliestPermissibleLastLogin = Calendar.getInstance();
			earliestPermissibleLastLogin.add(Calendar.DATE, -Config.config_claims_expirationDays);

			if (earliestPermissibleLastLogin.getTime().after(new Date(getOwnerInfo().getLastPlayed()))) {
				if (expireEventCanceled())
					return;
				//make a copy of this player's claim list
				Vector<Claim> claims = new Vector<>(getOwnerData().getClaims());

				//delete them
				GriefPrevention.instance.dataStore.deleteClaimsForPlayer(getClaim().ownerID, true);
				GriefPrevention.addLogEntry(" All of " + getClaim().getOwnerName() + "'s claims have expired.", CustomLogEntryTypes.AdminActivity);

				for (Claim value : claims) {
					//if configured to do so, restore the land to natural
					if (GriefPrevention.instance.creativeRulesApply(value.getLesserBoundaryCorner()) || Config.config_claims_survivalAutoNatureRestoration) {
						GriefPrevention.instance.restoreClaim(value, 0);
					}
				}
			}
		} else if (Config.config_claims_unusedClaimExpirationDays > 0 && GriefPrevention.instance.creativeRulesApply(getClaim().getLesserBoundaryCorner())) {
			//avoid scanning large claims and administrative claims
			if (getClaim().isAdminClaim() || getClaim().getWidth() > 25 || getClaim().getHeight() > 25) return;

			//otherwise scan the claim content
			int minInvestment = 400;

			long investmentScore = getClaim().getPlayerInvestmentScore();

			if (investmentScore < minInvestment) {
				//if the owner has been gone at least a week, and if he has ONLY the new player claim, it will be removed
				Calendar sevenDaysAgo = Calendar.getInstance();
				sevenDaysAgo.add(Calendar.DATE, -Config.config_claims_unusedClaimExpirationDays);
				boolean claimExpired = sevenDaysAgo.getTime().after(new Date(getOwnerInfo().getLastPlayed()));
				if (claimExpired) {
					if (expireEventCanceled())
						return;
					GriefPrevention.instance.dataStore.deleteClaim(getClaim(), true, true);
					GriefPrevention.addLogEntry("Removed " + getClaim().getOwnerName() + "'s unused claim @ " + GriefPrevention.getFriendlyLocationString(getClaim().getLesserBoundaryCorner()), CustomLogEntryTypes.AdminActivity);

					//restore the claim area to natural state
					GriefPrevention.instance.restoreClaim(getClaim(), 0);
				}
			}
		}
	}

	public boolean expireEventCanceled() {
		//see if any other plugins don't want this claim deleted
		ClaimExpirationEvent event = new ClaimExpirationEvent(this.getClaim());
		Bukkit.getPluginManager().callEvent(event);
		return event.isCancelled();
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public PlayerData getOwnerData() {
		return ownerData;
	}

	public void setOwnerData(PlayerData ownerData) {
		this.ownerData = ownerData;
	}

	public OfflinePlayer getOwnerInfo() {
		return ownerInfo;
	}

	public void setOwnerInfo(OfflinePlayer ownerInfo) {
		this.ownerInfo = ownerInfo;
	}
}
