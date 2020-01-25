package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.PlayerData;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("unlockitems")
public class UnlockItemsCommand extends GPBaseCommand {
	public UnlockItemsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onUnlockItems(final Player player, @Optional final String playerName) {
		PlayerData playerData;

		if (player.hasPermission("griefprevention.unlockothersdrops") && playerName != null) {
			Player otherPlayer = Bukkit.getPlayer(playerName);
			if (otherPlayer == null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
				return;
			}

			playerData = plugin.dataStore.getPlayerData(otherPlayer.getUniqueId());
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.DropUnlockOthersConfirmation, otherPlayer.getName());
		} else {
			playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.DropUnlockConfirmation);
		}

		playerData.dropsAreUnlocked = true;
	}
}
