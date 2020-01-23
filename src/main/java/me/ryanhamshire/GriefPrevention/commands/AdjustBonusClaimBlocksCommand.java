package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("adjustbonusclaimblocks")
public class AdjustBonusClaimBlocksCommand extends GPBaseCommand {
	public AdjustBonusClaimBlocksCommand(final GriefPrevention plugin) {
		super(plugin);
	}


	public void onAdjust(final Player player, final String name, final int amount) {
		//parse the adjustment amount
		int adjustment = amount;

		//if granting blocks to all players with a specific permission
		if (args[0].startsWith("[") && args[0].endsWith("]")) {
			String permissionIdentifier = args[0].substring(1, args[0].length() - 1);
			int newTotal = plugin.dataStore.adjustGroupBonusBlocks(permissionIdentifier, adjustment);

			GriefPrevention.sendMessage(player, TextMode.Success, Messages.AdjustGroupBlocksSuccess, permissionIdentifier, String.valueOf(adjustment), String.valueOf(newTotal));
			if (player != null)
				GriefPrevention.AddLogEntry(player.getName() + " adjusted " + permissionIdentifier + "'s bonus claim blocks by " + adjustment + ".");

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
		playerData.setBonusClaimBlocks(playerData.getBonusClaimBlocks() + adjustment);
		plugin.dataStore.savePlayerData(targetPlayer.getUniqueId(), playerData);

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.AdjustBlocksSuccess, targetPlayer.getName(), String.valueOf(adjustment), String.valueOf(playerData.getBonusClaimBlocks()));
		if (player != null)
			GriefPrevention.AddLogEntry(player.getName() + " adjusted " + targetPlayer.getName() + "'s bonus claim blocks by " + adjustment + ".", CustomLogEntryTypes.AdminActivity);

	}
}
