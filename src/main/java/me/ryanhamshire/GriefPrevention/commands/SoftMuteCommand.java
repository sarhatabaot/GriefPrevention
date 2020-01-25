package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.CustomLogEntryTypes;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("softmute")
public class SoftMuteCommand extends GPBaseCommand {
	public SoftMuteCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onSoftMute(final Player player, final String playerName) {

		//find the specified player
		OfflinePlayer targetPlayer = plugin.resolvePlayerByName(playerName);
		if (targetPlayer == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		//toggle mute for player
		boolean isMuted = plugin.dataStore.toggleSoftMute(targetPlayer.getUniqueId());
		if (isMuted) {
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.SoftMuted, targetPlayer.getName());
			String executorName = "console";
			if (player != null) {
				executorName = player.getName();
			}

			GriefPrevention.AddLogEntry(executorName + " muted " + targetPlayer.getName() + ".", CustomLogEntryTypes.AdminActivity, true);
		} else {
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.UnSoftMuted, targetPlayer.getName());
		}


	}
}
