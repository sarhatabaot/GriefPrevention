package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.IgnoreMode;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.TextMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("unseparate")
public class UnSeparateCommand extends GPBaseCommand {

	public UnSeparateCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onUnSeparate(final Player player, final String name1, final String name2) {
		//validate target players
		OfflinePlayer targetPlayer = plugin.resolvePlayerByName(name1);
		if (targetPlayer == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		OfflinePlayer targetPlayer2 = plugin.resolvePlayerByName(name2);
		if (targetPlayer2 == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		plugin.setIgnoreStatus(targetPlayer, targetPlayer2, IgnoreMode.NONE);
		plugin.setIgnoreStatus(targetPlayer2, targetPlayer, IgnoreMode.NONE);

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.UnSeparateConfirmation);

	}
}
