package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.config.Config;
import me.ryanhamshire.griefprevention.datastore.DataStore;
import me.ryanhamshire.griefprevention.visualization.Visualization;
import me.ryanhamshire.griefprevention.visualization.VisualizationType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("claim")
public class ClaimCommand extends GPBaseCommand {

	public ClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	private boolean isClaimLimit(final Player player, final PlayerData playerData) {
		return Config.config_claims_maxClaimsPerPlayer > 0 &&
				!player.hasPermission("griefprevention.overrideclaimcountlimit") &&
				playerData.getClaims().size() >= Config.config_claims_maxClaimsPerPlayer;
	}

	@Default
	public void onClaim(final Player player, @Optional int optionalRadius /*i.e. specified radius*/) {
		if (!GriefPrevention.instance.claimsEnabledForWorld(player.getWorld())) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.ClaimsDisabledWorld);
			return;
		}

		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		//if he's at the claim count per player limit already and doesn't have permission to bypass, display an error message
		if (isClaimLimit(player, playerData)) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.ClaimCreationFailedOverClaimCountLimit);
			return;
		}

		//default is chest claim radius, unless -1
		int radius = Config.config_claims_automaticClaimsForNewPlayersRadius;
		if (radius < 0) radius = (int) Math.ceil(Math.sqrt(Config.config_claims_minArea) / 2);

		//if player has any claims, respect claim minimum size setting
		if (playerData.getClaims().size() > 0) {
			//if player has exactly one land claim, this requires the claim modification tool to be in hand (or creative mode player)
			if (playerData.getClaims().size() == 1 && player.getGameMode() != GameMode.CREATIVE && player.getItemInHand().getType() != Config.config_claims_modificationTool) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.MustHoldModificationToolForThat);
				return;
			}

			radius = (int) Math.ceil(Math.sqrt(Config.config_claims_minArea) / 2);
		}

		//allow for specifying the radius
		if (getOrigArgs().length > 0) {
			if (playerData.getClaims().size() < 2 && player.getGameMode() != GameMode.CREATIVE && player.getItemInHand().getType() != Config.config_claims_modificationTool) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.RadiusRequiresGoldenShovel);
				return;
			}

			int specifiedRadius;
			try {
				specifiedRadius = Integer.parseInt(getOrigArgs()[0]);
			} catch (NumberFormatException e) {
				return;
			}

			if (specifiedRadius < radius) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.MinimumRadius, String.valueOf(radius));
				return;
			} else {
				radius = specifiedRadius;
			}
		}

		if (radius < 0) radius = 0;

		Location lc = player.getLocation().add(-radius, 0, -radius);
		Location gc = player.getLocation().add(radius, 0, radius);

		//player must have sufficient unused claim blocks
		int area = Math.abs((gc.getBlockX() - lc.getBlockX() + 1) * (gc.getBlockZ() - lc.getBlockZ() + 1));
		int remaining = playerData.getRemainingClaimBlocks();
		if (remaining < area) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.CreateClaimInsufficientBlocks, String.valueOf(area - remaining));
			GriefPrevention.instance.dataStore.tryAdvertiseAdminAlternatives(player);
			return;
		}

		CreateClaimResult result = plugin.dataStore.createClaim(lc.getWorld(),
				lc.getBlockX(), gc.getBlockX(),
				lc.getBlockY() - Config.config_claims_claimsExtendIntoGroundDistance - 1,
				gc.getWorld().getHighestBlockYAt(gc) - Config.config_claims_claimsExtendIntoGroundDistance - 1,
				lc.getBlockZ(), gc.getBlockZ(),
				player.getUniqueId(), null, null, player);
		if (!result.succeeded) {
			if (result.claim != null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.CreateClaimFailOverlapShort);

				Visualization visualization = Visualization.fromClaim(result.claim, player.getEyeLocation().getBlockY(), VisualizationType.ERROR_CLAIM, player.getLocation());
				Visualization.apply(player, visualization);
			} else {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.CreateClaimFailOverlapRegion);
			}
		} else {
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.CreateClaimSuccess);

			//link to a video demo of land claiming, based on world type
			if (GriefPrevention.instance.creativeRulesApply(player.getLocation())) {
				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
			} else if (GriefPrevention.instance.claimsEnabledForWorld(player.getLocation().getWorld())) {
				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SurvivalBasicsVideo2, DataStore.SURVIVAL_VIDEO_URL);
			}
			Visualization visualization = Visualization.fromClaim(result.claim, player.getEyeLocation().getBlockY(), VisualizationType.CLAIM, player.getLocation());
			Visualization.apply(player, visualization);
			playerData.claimResizing = null;
			playerData.lastShovelLocation = null;

			plugin.autoExtendClaim(result.claim);
		}

	}
}
