package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.Optional;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.PlayerData;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UnlockDropsCommand extends GPBaseCommand {
	public UnlockDropsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	public void onUnlockDrops(final Player player, @Optional String name){
		PlayerData playerData;

		if (player.hasPermission("griefprevention.unlockothersdrops") && getOrigArgs().length == 1)
		{
			Player otherPlayer = Bukkit.getPlayer(name);
			if (otherPlayer == null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
				return;
			}

			playerData = plugin.dataStore.getPlayerData(otherPlayer.getUniqueId());
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.DropUnlockOthersConfirmation, otherPlayer.getName());
		}
		else
		{
			playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.DropUnlockConfirmation);
		}

		playerData.dropsAreUnlocked = true;

	}
}
