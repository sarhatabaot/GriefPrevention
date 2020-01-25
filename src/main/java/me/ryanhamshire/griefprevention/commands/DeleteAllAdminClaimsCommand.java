package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.visualization.Visualization;
import org.bukkit.entity.Player;

@CommandAlias("deletealladminclaims")
@CommandPermission("griefprevention.deleteclaims")
public class DeleteAllAdminClaimsCommand extends GPBaseCommand{
	public DeleteAllAdminClaimsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onDeleteAllClaims(final Player player){
		//delete all admin claims
		plugin.dataStore.deleteClaimsForPlayer(null, true);  //null for owner id indicates an administrative claim

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.AllAdminDeleted);
		if(player != null) {
			GriefPrevention.addLogEntry(player.getName() + " deleted all administrative claims.", CustomLogEntryTypes.AdminActivity);
			//revert any current visualization
			Visualization.revert(player);
		}

	}
}
