package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.logging.CustomLogEntryTypes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("adjustbonusclaimblocksall")
public class AdjustBonusClaimBlocksAllCommand extends GPBaseCommand {
	public AdjustBonusClaimBlocksAllCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	public void onAdjust(final CommandSender sender, final String name, final int amount){
		StringBuilder builder = new StringBuilder();
		for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			UUID playerID = onlinePlayer.getUniqueId();
			PlayerData playerData = plugin.dataStore.getPlayerData(playerID);
			playerData.setBonusClaimBlocks(playerData.getBonusClaimBlocks() + amount);
			plugin.dataStore.savePlayerData(playerID, playerData);
			builder.append(onlinePlayer.getName()).append(" ");
		}

		GriefPrevention.sendMessage((Player) sender, TextMode.Success, Messages.AdjustBlocksAllSuccess, String.valueOf(amount));
		GriefPrevention.addLogEntry("Adjusted all " + Bukkit.getOnlinePlayers().size() + "players' bonus claim blocks by " + amount + ".  " + builder.toString(), CustomLogEntryTypes.ADMIN_ACTIVITY);
	}
}
