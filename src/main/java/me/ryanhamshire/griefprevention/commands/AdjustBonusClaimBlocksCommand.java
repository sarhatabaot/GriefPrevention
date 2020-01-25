package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("adjustbonusclaimblocks")
public class AdjustBonusClaimBlocksCommand extends GPBaseCommand {
	public AdjustBonusClaimBlocksCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onAdjust(final Player player, final String name, final int amount) {
		//parse the adjustment amount

		//if granting blocks to all players with a specific permission
		if (getOrigArgs()[0].startsWith("[") && getOrigArgs()[0].endsWith("]")) {
			String permissionIdentifier = getOrigArgs()[0].substring(1, getOrigArgs()[0].length() - 1);
			int newTotal = plugin.dataStore.adjustGroupBonusBlocks(permissionIdentifier, amount);

			GriefPrevention.sendMessage(player, TextMode.Success, Messages.AdjustGroupBlocksSuccess, permissionIdentifier, String.valueOf(amount), String.valueOf(newTotal));
			if (player != null)
				GriefPrevention.addLogEntry(player.getName() + " adjusted " + permissionIdentifier + "'s bonus claim blocks by " + amount + ".");

			return;
		}

		//otherwise, find the specified player
		OfflinePlayer targetPlayer;
		try {
			UUID playerID = UUID.fromString(name);
			targetPlayer = plugin.getServer().getOfflinePlayer(playerID);

		} catch (IllegalArgumentException e) {
			targetPlayer = plugin.resolvePlayerByName(name);
		}

		if (targetPlayer == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		//give blocks to player
		PlayerData playerData = plugin.dataStore.getPlayerData(targetPlayer.getUniqueId());
		playerData.setBonusClaimBlocks(playerData.getBonusClaimBlocks() + amount);
		plugin.dataStore.savePlayerData(targetPlayer.getUniqueId(), playerData);

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.AdjustBlocksSuccess, targetPlayer.getName(), String.valueOf(amount), String.valueOf(playerData.getBonusClaimBlocks()));
		if (player != null)
			GriefPrevention.addLogEntry(player.getName() + " adjusted " + targetPlayer.getName() + "'s bonus claim blocks by " + amount + ".", CustomLogEntryTypes.AdminActivity);

	}
}
