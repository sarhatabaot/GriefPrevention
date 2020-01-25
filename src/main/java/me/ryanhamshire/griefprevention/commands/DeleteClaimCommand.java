package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.config.Config;
import me.ryanhamshire.griefprevention.logging.CustomLogEntryTypes;
import me.ryanhamshire.griefprevention.visualization.Visualization;
import org.bukkit.entity.Player;

@CommandAlias("deleteclaim")
public class DeleteClaimCommand extends GPBaseCommand {
	public DeleteClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onDeleteClaim(final Player player) {
		//determine which claim the player is standing in
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);

		if (claim == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.DeleteClaimMissing);
			return;
		}

		//deleting an admin claim additionally requires the adminclaims permission
		if (!claim.isAdminClaim() || player.hasPermission("griefprevention.adminclaims")) {
			PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
			if (claim.children.size() > 0 && !playerData.warnedAboutMajorDeletion) {
				GriefPrevention.sendMessage(player, TextMode.Warn, Messages.DeletionSubdivisionWarning);
				playerData.warnedAboutMajorDeletion = true;
			} else {
				claim.removeSurfaceFluids(null);
				plugin.dataStore.deleteClaim(claim, true, true);

				//if in a creative mode world, /restorenature the claim
				if (GriefPrevention.instance.creativeRulesApply(claim.getLesserBoundaryCorner()) || Config.config_claims_survivalAutoNatureRestoration) {
					GriefPrevention.instance.restoreClaim(claim, 0);
				}

				GriefPrevention.sendMessage(player, TextMode.Success, Messages.DeleteSuccess);
				GriefPrevention.addLogEntry(player.getName() + " deleted " + claim.getOwnerName() + "'s claim at " + GriefPrevention.getFriendlyLocationString(claim.getLesserBoundaryCorner()), CustomLogEntryTypes.ADMIN_ACTIVITY);

				//revert any current visualization
				Visualization.revert(player);

				playerData.warnedAboutMajorDeletion = false;
			}
		} else {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.CantDeleteAdminClaim);
		}

	}
}
