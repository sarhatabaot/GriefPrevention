package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.PlayerData;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("unignoreplayer")
public class UnIgnorePlayerCommand extends GPBaseCommand {
	public UnIgnorePlayerCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onUnIgnorePlayer(final Player player, final String playerName){

		//validate target player
		OfflinePlayer targetPlayer = plugin.resolvePlayerByName(playerName);
		if(targetPlayer == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		boolean ignoreStatus = playerData.ignoredPlayers.get(targetPlayer.getUniqueId());
		if(ignoreStatus) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.NotIgnoringPlayer);
			return;
		}

		plugin.setIgnoreStatus(player, targetPlayer, GriefPrevention.IgnoreMode.NONE);

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.UnIgnoreConfirmation);
	}
}
