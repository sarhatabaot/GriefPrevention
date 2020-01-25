package me.ryanhamshire.griefprevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import me.ryanhamshire.griefprevention.events.TrustChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("untrust")
@CommandPermission("griefprevention.claims")
public class UnTrustCommand extends GPBaseCommand {
	public UnTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onUnTrust(final Player player, final String name) {

		//determine which claim the player is standing in
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true, null);

		//bracket any permissions
		String[] copyOriginArgs = null;
		if (getOrigArgs()[0].contains(".") && !getOrigArgs()[0].startsWith("[") && !getOrigArgs()[0].endsWith("]")) {
			copyOriginArgs[0] = "[" + getOrigArgs()[0] + "]";
		}

		//determine whether a single player or clearing permissions entirely
		boolean clearPermissions = false;
		OfflinePlayer otherPlayer = null;
		if (copyOriginArgs[0].equals("all")) {
			if (claim == null || claim.allowEdit(player) == null) {
				clearPermissions = true;
			} else {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.ClearPermsOwnerOnly);
				return;
			}
		} else {
			//validate player argument or group argument
			if (!copyOriginArgs[0].startsWith("[") || !copyOriginArgs[0].endsWith("]")) {
				otherPlayer = plugin.resolvePlayerByName(name);
				if (!clearPermissions && otherPlayer == null && !copyOriginArgs[0].equals("public")) {
					GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
					return;
				}

				//correct to proper casing
				if (otherPlayer != null)
					copyOriginArgs[0] = otherPlayer.getName();
			}
		}

		//if no claim here, apply changes to all his claims
		if (claim == null) {
			PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());

			String idToDrop = copyOriginArgs[0];
			if (otherPlayer != null) {
				idToDrop = otherPlayer.getUniqueId().toString();
			}

			//calling event
			TrustChangedEvent event = new TrustChangedEvent(player, playerData.getClaims(), null, false, idToDrop);
			Bukkit.getPluginManager().callEvent(event);

			if (event.isCancelled()) {
				return;
			}

			//dropping permissions
			for (int i = 0; i < playerData.getClaims().size(); i++) {
				claim = playerData.getClaims().get(i);

				//if untrusting "all" drop all permissions
				if (clearPermissions) {
					claim.clearPermissions();
				}

				//otherwise drop individual permissions
				else {
					claim.dropPermission(idToDrop);
					claim.managers.remove(idToDrop);
				}

				//save changes
				plugin.dataStore.saveClaim(claim);
			}

			//beautify for output
			if (copyOriginArgs[0].equals("public")) {
				copyOriginArgs[0] = "the public";
			}

			//confirmation message
			if (!clearPermissions) {
				GriefPrevention.sendMessage(player, TextMode.Success, Messages.UntrustIndividualAllClaims, copyOriginArgs[0]);
			} else {
				GriefPrevention.sendMessage(player, TextMode.Success, Messages.UntrustEveryoneAllClaims);
			}
		}

		//otherwise, apply changes to only this claim
		else if (claim.allowGrantPermission(player) != null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.NoPermissionTrust, claim.getOwnerName());
		} else {
			//if clearing all
			if (clearPermissions) {
				//requires owner
				if (claim.allowEdit(player) != null) {
					GriefPrevention.sendMessage(player, TextMode.Err, Messages.UntrustAllOwnerOnly);
					return;
				}

				//calling the event
				TrustChangedEvent event = new TrustChangedEvent(player, claim, null, false, copyOriginArgs[0]);
				Bukkit.getPluginManager().callEvent(event);

				if (event.isCancelled()) {
					return;
				}

				claim.clearPermissions();
				GriefPrevention.sendMessage(player, TextMode.Success, Messages.ClearPermissionsOneClaim);
			}

			//otherwise individual permission drop
			else {
				String idToDrop = copyOriginArgs[0];
				if (otherPlayer != null) {
					idToDrop = otherPlayer.getUniqueId().toString();
				}
				boolean targetIsManager = claim.managers.contains(idToDrop);
				if (targetIsManager && claim.allowEdit(player) != null)  //only claim owners can untrust managers
				{
					GriefPrevention.sendMessage(player, TextMode.Err, Messages.ManagersDontUntrustManagers, claim.getOwnerName());
					return;
				} else {
					//calling the event
					TrustChangedEvent event = new TrustChangedEvent(player, claim, null, false, idToDrop);
					Bukkit.getPluginManager().callEvent(event);

					if (event.isCancelled()) {
						return;
					}

					claim.dropPermission(idToDrop);
					claim.managers.remove(idToDrop);

					//beautify for output
					if (copyOriginArgs[0].equals("public")) {
						copyOriginArgs[0] = "the public";
					}

					GriefPrevention.sendMessage(player, TextMode.Success, Messages.UntrustIndividualSingleClaim, copyOriginArgs[0]);
				}
			}

			//save changes
			plugin.dataStore.saveClaim(claim);
		}

	}
}
