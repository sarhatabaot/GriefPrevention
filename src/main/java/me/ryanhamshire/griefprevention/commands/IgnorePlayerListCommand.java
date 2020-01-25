package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.PlayerData;
import me.ryanhamshire.griefprevention.TextMode;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@CommandAlias("ignoreplayerlist")
public class IgnorePlayerListCommand extends GPBaseCommand {
	public IgnorePlayerListCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onList(final Player player) {
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<UUID, Boolean> entry : playerData.ignoredPlayers.entrySet()) {
			if (entry.getValue() != null) {
				//if not an admin ignore, add it to the list
				if (!entry.getValue()) {
					builder.append(GriefPrevention.lookupPlayerName(entry.getKey()));
					builder.append(" ");
				}
			}
		}

		String list = builder.toString().trim();
		if (list.isEmpty()) {
			GriefPrevention.sendMessage(player, TextMode.Info, Messages.NotIgnoringAnyone);
		} else {
			GriefPrevention.sendMessage(player, TextMode.Info, list);
		}

	}
}
