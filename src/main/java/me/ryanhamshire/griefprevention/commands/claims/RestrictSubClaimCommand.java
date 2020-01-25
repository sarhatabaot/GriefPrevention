package me.ryanhamshire.griefprevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("restrictsubclaim")
@CommandPermission("griefprevention.claims")
public class RestrictSubClaimCommand extends GPBaseCommand {
	public RestrictSubClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onRestrictSubClaim(final Player player) {
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true, playerData.lastClaim);
		if (claim == null || claim.parent == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.StandInSubclaim);
			return;
		}

		// If player has /ignoreclaims on, continue
		// If admin claim, fail if this user is not an admin
		// If not an admin claim, fail if this user is not the owner
		if (!playerData.ignoreClaims && (claim.isAdminClaim() ? !player.hasPermission("griefprevention.adminclaims") : !player.getUniqueId().equals(claim.parent.ownerID))) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.OnlyOwnersModifyClaims, claim.getOwnerName());
			return;
		}

		if (claim.getSubclaimRestrictions()) {
			claim.setSubclaimRestrictions(false);
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.SubclaimUnrestricted);
		} else {
			claim.setSubclaimRestrictions(true);
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.SubclaimRestricted);
		}
		plugin.dataStore.saveClaim(claim);
	}
}
