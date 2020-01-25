package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setaccruedclaimblocks")
public class SetAccruedClaimBlocksCommand extends GPBaseCommand {
	public SetAccruedClaimBlocksCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onSet(final CommandSender player, final String name, final int amount){
		//find the specified player
		OfflinePlayer targetPlayer = plugin.resolvePlayerByName(name);
		if(targetPlayer == null) {
			GriefPrevention.sendMessage((Player) player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		//set player's blocks
		PlayerData playerData = plugin.dataStore.getPlayerData(targetPlayer.getUniqueId());
		playerData.setAccruedClaimBlocks(amount);
		plugin.dataStore.savePlayerData(targetPlayer.getUniqueId(), playerData);

		GriefPrevention.sendMessage((Player) player, TextMode.Success, Messages.SetClaimBlocksSuccess);
		if(player != null) GriefPrevention.AddLogEntry(player.getName() + " set " + targetPlayer.getName() + "'s accrued claim blocks to " + amount + ".", CustomLogEntryTypes.AdminActivity);

	}
}
