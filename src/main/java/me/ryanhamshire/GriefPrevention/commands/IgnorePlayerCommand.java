package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("ignoreplayer")
public class IgnorePlayerCommand extends GPBaseCommand{
	public IgnorePlayerCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onIgnorePlayer(final Player player, final String playerName){
		//validate target player
		OfflinePlayer targetPlayer = plugin.resolvePlayerByName(playerName);
		if(targetPlayer == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}
		plugin.setIgnoreStatus(player, targetPlayer, GriefPrevention.IgnoreMode.STANDARD);
		GriefPrevention.sendMessage(player, TextMode.Success, Messages.IgnoreConfirmation);
	}
}
