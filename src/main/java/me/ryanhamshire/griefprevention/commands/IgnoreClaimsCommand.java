package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.PlayerData;
import me.ryanhamshire.griefprevention.TextMode;
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
