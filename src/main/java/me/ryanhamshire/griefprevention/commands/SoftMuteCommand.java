package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.CustomLogEntryTypes;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.TextMode;
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

			GriefPrevention.addLogEntry(executorName + " muted " + targetPlayer.getName() + ".", CustomLogEntryTypes.AdminActivity, true);
		} else {
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.UnSoftMuted, targetPlayer.getName());
		}


	}
}
