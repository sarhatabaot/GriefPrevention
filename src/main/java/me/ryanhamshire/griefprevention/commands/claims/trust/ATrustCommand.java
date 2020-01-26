package me.ryanhamshire.griefprevention.commands.claims.trust;


import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.PlayerData;
import me.ryanhamshire.griefprevention.TextMode;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.claim.ClaimPermission;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import me.ryanhamshire.griefprevention.events.TrustChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class ATrustCommand extends GPBaseCommand {
	public ATrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}


	private boolean isPermissionFormat(final String string) {
		return string.startsWith("[") && string.endsWith("]");
	}

	private String checkPermission(final String recipientName) {
		if (isPermissionFormat(recipientName))
			return recipientName.substring(1, recipientName.length() - 1);
		if (recipientName.contains("."))
			return recipientName;
		return null;
	}

	private ArrayList<Claim> getTargetClaims(final Claim claim, final Player player, final ClaimPermission permissionLevel){
		ArrayList<Claim> targetClaims = new ArrayList<>();
		if (claim == null) {
			PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
			targetClaims.addAll(playerData.getClaims());
		} else {
			//check permission here
			if (claim.allowGrantPermission(player) != null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.NoPermissionTrust, claim.getOwnerName());
				return null;
			}

			//see if the player has the level of permission he's trying to grant
			String errorMessage = null;

			//permission level null indicates granting permission trust
			if (permissionLevel == null) {
				errorMessage = claim.allowEdit(player);
				if (errorMessage != null) {
					errorMessage = "Only " + claim.getOwnerName() + " can grant /PermissionTrust here.";
				}
			}

			//otherwise just use the ClaimPermission enum values
			else {
				switch (permissionLevel) {
					case ACCESS:
						errorMessage = claim.allowAccess(player);
						break;
					case INVENTORY:
						errorMessage = claim.allowContainers(player);
						break;
					default:
						errorMessage = claim.allowBuild(player, Material.AIR);
				}
			}

			//error message for trying to grant a permission the player doesn't have
			if (errorMessage != null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.CantGrantThatPermission);
				return null;
			}

			targetClaims.add(claim);
		}
		return targetClaims;
	}

	public void handleTrustCommand(Player player, ClaimPermission permissionLevel, String recipientName) {
		//determine which claim the player is standing in
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true, null);

		//validate player or group argument
		String permission = checkPermission(recipientName);

		if (permission == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.InvalidPermissionID);
			return;
		}

		OfflinePlayer otherPlayer = plugin.resolvePlayerByName(recipientName);
		if (otherPlayer == null && !recipientName.equals("public") && !recipientName.equals("all")) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		if (otherPlayer != null) {
			recipientName = otherPlayer.getName();
		} else {
			recipientName = "public";
		}


		//determine which claims should be modified
		ArrayList<Claim> targetClaims = getTargetClaims(claim, player, permissionLevel);
		if (targetClaims == null) return;

		//if we didn't determine which claims to modify, tell the player to be specific
		if (targetClaims.isEmpty()) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.GrantPermissionNoClaim);
			return;
		}

		String identifierToAdd = recipientName;
		if (permission != null) {
			identifierToAdd = "[" + permission + "]";
		} else if (otherPlayer != null) {
			identifierToAdd = otherPlayer.getUniqueId().toString();
		}

		//calling the event
		TrustChangedEvent event = new TrustChangedEvent(player, targetClaims, permissionLevel, true, identifierToAdd);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		applyClaimChanges(targetClaims, permissionLevel, identifierToAdd);
		notifyPlayer(player, claim, recipientName, permissionLevel);
	}

	private void applyClaimChanges(final List<Claim> targetClaims, final ClaimPermission permissionLevel, final String identifierToAdd) {
		for (Claim currentClaim : targetClaims) {
			if (permissionLevel == null) {
				if (!currentClaim.managers.contains(identifierToAdd)) {
					currentClaim.managers.add(identifierToAdd);
				}
			} else {
				currentClaim.setPermission(identifierToAdd, permissionLevel);
			}
			plugin.dataStore.saveClaim(currentClaim);
		}
	}

	private void notifyPlayer(final Player player, final Claim claim, final String recipientName, final ClaimPermission permissionLevel) {
		String newName = recipientName;
		if (recipientName.equals("public"))
			newName = plugin.dataStore.getMessage(Messages.CollectivePublic);

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.GrantPermissionConfirmation, newName, getPermissionDescription(permissionLevel), getLocationString(claim));
	}

	private String getPermissionDescription(final ClaimPermission permissionLevel) {
		if (permissionLevel == null) {
			return plugin.dataStore.getMessage(Messages.PermissionsPermission);
		}
		if (permissionLevel == ClaimPermission.BUILD) {
			return plugin.dataStore.getMessage(Messages.BuildPermission);
		}
		if (permissionLevel == ClaimPermission.ACCESS) {
			return plugin.dataStore.getMessage(Messages.AccessPermission);
		}

		return plugin.dataStore.getMessage(Messages.ContainersPermission);
	}

	private String getLocationString(final Claim claim) {
		if (claim == null) {
			return plugin.dataStore.getMessage(Messages.LocationAllClaims);
		} else {
			return plugin.dataStore.getMessage(Messages.LocationCurrentClaim);
		}
	}
}
