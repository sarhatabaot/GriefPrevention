package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.TextMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@CommandAlias("claimexplosions")
public class ClaimExplosionsCommand extends GPBaseCommand {
	public ClaimExplosionsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onClaimExplosions(final Player player) {
		//determine which claim the player is standing in
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true, null);

		if (claim == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.DeleteClaimMissing);
		} else {
			String noBuildReason = claim.allowBuild(player, Material.STONE);
			if (noBuildReason != null) {
				GriefPrevention.sendMessage(player, TextMode.Err, noBuildReason);
				return;
			}

			if (claim.areExplosivesAllowed) {
				claim.areExplosivesAllowed = false;
				GriefPrevention.sendMessage(player, TextMode.Success, Messages.ExplosivesDisabled);
			} else {
				claim.areExplosivesAllowed = true;
				GriefPrevention.sendMessage(player, TextMode.Success, Messages.ExplosivesEnabled);
			}
		}
	}
}
