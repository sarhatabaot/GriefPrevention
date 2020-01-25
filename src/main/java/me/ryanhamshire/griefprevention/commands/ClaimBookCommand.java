package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.TextMode;
import me.ryanhamshire.griefprevention.WelcomeTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("claimbook")
public class ClaimBookCommand extends GPBaseCommand {
	public ClaimBookCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onClaimBook(final Player player, final String playerName) {
		//try to find the specified player
		Player otherPlayer = Bukkit.getServer().getPlayer(playerName);
		if (otherPlayer == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		new WelcomeTask(otherPlayer).run();
	}
}
