package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Vector;

@CommandAlias("claimslist|claimlist")
public class ClaimListCommand extends GPBaseCommand {
	public ClaimListCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onClaimList(final Player player, @Optional String name) {
		//player whose claims will be listed
		OfflinePlayer otherPlayer = null;

		//if another player isn't specified, assume current player
		if (getOrigArgs().length < 1) {
			if (player != null)
				otherPlayer = player;
		}

		//otherwise if no permission to delve into another player's claims data
		else if (player != null && !player.hasPermission("griefprevention.claimslistother")) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.ClaimsListNoPermission);
			return;
		}

		//otherwise try to find the specified player
		else {
			otherPlayer = plugin.resolvePlayerByName(name);
			if (otherPlayer == null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
				return;
			}
		}

		//load the target player's data
		PlayerData playerData = plugin.dataStore.getPlayerData(otherPlayer.getUniqueId());
		Vector<Claim> claims = playerData.getClaims();
		GriefPrevention.sendMessage(player, TextMode.Instr, Messages.StartBlockMath,
				String.valueOf(playerData.getAccruedClaimBlocks()),
				String.valueOf((playerData.getBonusClaimBlocks() + plugin.dataStore.getGroupBonusBlocks(otherPlayer.getUniqueId()))),
				String.valueOf((playerData.getAccruedClaimBlocks() + playerData.getBonusClaimBlocks() + plugin.dataStore.getGroupBonusBlocks(otherPlayer.getUniqueId()))));
		if (!claims.isEmpty()) {
			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.ClaimsListHeader);
			for (int i = 0; i < playerData.getClaims().size(); i++) {
				Claim claim = playerData.getClaims().get(i);
				GriefPrevention.sendMessage(player, TextMode.Instr, GriefPrevention.getfriendlyLocationString(claim.getLesserBoundaryCorner()) + plugin.dataStore.getMessage(Messages.ContinueBlockMath, String.valueOf(claim.getArea())));
			}

			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.EndBlockMath, String.valueOf(playerData.getRemainingClaimBlocks()));
		}

		//drop the data we just loaded, if the player isn't online
		if (!otherPlayer.isOnline())
			plugin.dataStore.clearCachedPlayerData(otherPlayer.getUniqueId());

	}
}
