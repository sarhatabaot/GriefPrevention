package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.visualization.Visualization;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("deleteallclaims")
public class DeleteAllClaimsCommand extends GPBaseCommand {
	public DeleteAllClaimsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	public void onDeleteAllClaims(final Player player, final String playerName) {
		OfflinePlayer otherPlayer = plugin.resolvePlayerByName(playerName);
		if (otherPlayer == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		//delete all that player's claims
		plugin.dataStore.deleteClaimsForPlayer(otherPlayer.getUniqueId(), true);

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.DeleteAllSuccess, otherPlayer.getName());
		if (player != null) {
			GriefPrevention.AddLogEntry(player.getName() + " deleted all claims belonging to " + otherPlayer.getName() + ".", CustomLogEntryTypes.AdminActivity);
			//revert any current visualization
			Visualization.revert(player);
		}

	}
}
