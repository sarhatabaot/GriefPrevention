package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.config.Config;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("extendclaim")
public class ExtendClaimCommand extends GPBaseCommand {
	public ExtendClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onExtendClaim(final Player player, @Optional int optionalAmount) {
		//@CatchUnknown..
		if (getOrigArgs().length < 1) {
			//link to a video demo of land claiming, based on world type
			if (GriefPrevention.instance.creativeRulesApply(player.getLocation())) {
				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
			} else if (GriefPrevention.instance.claimsEnabledForWorld(player.getLocation().getWorld())) {
				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SurvivalBasicsVideo2, DataStore.SURVIVAL_VIDEO_URL);
			}
			return;
		}

		int amount;
		try {
			amount = Integer.parseInt(getOrigArgs()[0]);
		} catch (NumberFormatException e) {
			//link to a video demo of land claiming, based on world type
			if (GriefPrevention.instance.creativeRulesApply(player.getLocation())) {
				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
			} else if (GriefPrevention.instance.claimsEnabledForWorld(player.getLocation().getWorld())) {
				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SurvivalBasicsVideo2, DataStore.SURVIVAL_VIDEO_URL);
			}
			return;
		}

		//requires claim modification tool in hand
		if (player.getGameMode() != GameMode.CREATIVE && player.getItemInHand().getType() != Config.config_claims_modificationTool) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.MustHoldModificationToolForThat);
			return;
		}

		//must be standing in a land claim
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true, playerData.lastClaim);
		if (claim == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.StandInClaimToResize);
			return;
		}

		//must have permission to edit the land claim you're in
		String errorMessage = claim.allowEdit(player);
		if (errorMessage != null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.NotYourClaim);
			return;
		}

		//determine new corner coordinates
		org.bukkit.util.Vector direction = player.getLocation().getDirection();
		if (direction.getY() > .75) {
			GriefPrevention.sendMessage(player, TextMode.Info, Messages.ClaimsExtendToSky);
			return;
		}

		if (direction.getY() < -.75) {
			GriefPrevention.sendMessage(player, TextMode.Info, Messages.ClaimsAutoExtendDownward);
			return;
		}

		Location lc = claim.getLesserBoundaryCorner();
		Location gc = claim.getGreaterBoundaryCorner();
		int newx1 = lc.getBlockX();
		int newx2 = gc.getBlockX();
		int newy1 = lc.getBlockY();
		int newy2 = gc.getBlockY();
		int newz1 = lc.getBlockZ();
		int newz2 = gc.getBlockZ();

		//if changing Z only
		if (Math.abs(direction.getX()) < .3) {
			if (direction.getZ() > 0) {
				newz2 += amount;  //north
			} else {
				newz1 -= amount;  //south
			}
		}

		//if changing X only
		else if (Math.abs(direction.getZ()) < .3) {
			if (direction.getX() > 0) {
				newx2 += amount;  //east
			} else {
				newx1 -= amount;  //west
			}
		}

		//diagonals
		else {
			if (direction.getX() > 0) {
				newx2 += amount;
			} else {
				newx1 -= amount;
			}

			if (direction.getZ() > 0) {
				newz2 += amount;
			} else {
				newz1 -= amount;
			}
		}

		//attempt resize
		playerData.claimResizing = claim;
		plugin.dataStore.resizeClaimWithChecks(player, playerData, newx1, newx2, newy1, newy2, newz1, newz2);
		playerData.claimResizing = null;
	}
}
