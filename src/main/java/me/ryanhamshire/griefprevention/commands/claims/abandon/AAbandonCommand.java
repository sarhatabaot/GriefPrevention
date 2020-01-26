package me.ryanhamshire.griefprevention.commands.claims.abandon;

import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.PlayerData;
import me.ryanhamshire.griefprevention.TextMode;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import me.ryanhamshire.griefprevention.config.Config;
import me.ryanhamshire.griefprevention.visualization.Visualization;
import org.bukkit.entity.Player;

public class AAbandonCommand extends GPBaseCommand {
	public AAbandonCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	protected boolean abandonClaimHandler(final Player player, final boolean deleteTopLevelClaim) {
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());

		//which claim is being abandoned?
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);
		if (claim == null) {
			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.AbandonClaimMissing);
		}

		//verify ownership
		else if (claim.allowEdit(player) != null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.NotYourClaim);
		}

		//warn if has children and we're not explicitly deleting a top level claim
		else if (claim.children.size() > 0 && !deleteTopLevelClaim) {
			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.DeleteTopLevelClaim);
			return true;
		}

		//delete it
		claim.removeSurfaceFluids(null);
		plugin.dataStore.deleteClaim(claim, true, false);

		//if in a creative mode world, restore the claim area
		if (GriefPrevention.instance.creativeRulesApply(claim.getLesserBoundaryCorner())) {
			GriefPrevention.addLogEntry(player.getName() + " abandoned a claim @ " + GriefPrevention.getFriendlyLocationString(claim.getLesserBoundaryCorner()));
			GriefPrevention.sendMessage(player, TextMode.Warn, Messages.UnclaimCleanupWarning);
			GriefPrevention.instance.restoreClaim(claim, 20L * 60 * 2);
		}

		//adjust claim blocks when abandoning a top level claim
		if (Config.config_claims_abandonReturnRatio != 1.0D && claim.parent == null && claim.ownerID.equals(playerData.playerID)) {
			playerData.setAccruedClaimBlocks(playerData.getAccruedClaimBlocks() - (int) Math.ceil((claim.getArea() * (1 - Config.config_claims_abandonReturnRatio))));
		}

		//tell the player how many claim blocks he has left
		int remainingBlocks = playerData.getRemainingClaimBlocks();
		GriefPrevention.sendMessage(player, TextMode.Success, Messages.AbandonSuccess, String.valueOf(remainingBlocks));

		//revert any current visualization
		Visualization.revert(player);

		playerData.warnedAboutMajorDeletion = false;
		return true;

	}

}
