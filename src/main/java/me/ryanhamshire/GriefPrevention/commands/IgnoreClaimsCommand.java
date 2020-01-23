package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.PlayerData;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.entity.Player;

@CommandAlias("ignoreclaims")
public class IgnoreClaimsCommand extends GPBaseCommand {

	public IgnoreClaimsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onToggle(final Player player) {
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());

		playerData.ignoreClaims = !playerData.ignoreClaims;

		//toggle ignore claims mode on or off
		if (!playerData.ignoreClaims) {
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.RespectingClaims);
		} else {
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.IgnoringClaims);
		}

	}
}
