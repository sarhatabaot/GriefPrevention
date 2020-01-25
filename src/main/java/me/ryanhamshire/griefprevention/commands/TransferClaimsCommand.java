package me.ryanhamshire.griefprevention.commands;


import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.logging.CustomLogEntryTypes;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("transerclaim")
public class TransferClaimsCommand extends GPBaseCommand {
	public TransferClaimsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onTransfer(final Player player, final String name) {
		//which claim is the user in?
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true, null);
		if (claim == null) {
			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.TransferClaimMissing);
			return;
		}

		//check additional permission for admin claims
		if (claim.isAdminClaim() && !player.hasPermission("griefprevention.adminclaims")) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.TransferClaimPermission);
			return;
		}

		UUID newOwnerID = null;  //no argument = make an admin claim
		String ownerName = "admin";

		if (getOrigArgs().length > 0) {
			OfflinePlayer targetPlayer = plugin.resolvePlayerByName(name);
			if (targetPlayer == null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
				return;
			}
			newOwnerID = targetPlayer.getUniqueId();
			ownerName = targetPlayer.getName();
		}

		//change ownership
		try {
			plugin.dataStore.changeClaimOwner(claim, newOwnerID);
		} catch (DataStore.NoTransferException e) {
			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.TransferTopLevel);
			return;
		}

		//confirm
		GriefPrevention.sendMessage(player, TextMode.Success, Messages.TransferSuccess);
		GriefPrevention.addLogEntry(player.getName() + " transferred a claim at " + GriefPrevention.getFriendlyLocationString(claim.getLesserBoundaryCorner()) + " to " + ownerName + ".", CustomLogEntryTypes.ADMIN_ACTIVITY);

	}
}
