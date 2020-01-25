package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.IgnoreMode;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.TextMode;
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
		plugin.setIgnoreStatus(player, targetPlayer, IgnoreMode.STANDARD);
		GriefPrevention.sendMessage(player, TextMode.Success, Messages.IgnoreConfirmation);
	}
}
