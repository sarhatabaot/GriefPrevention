package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.entity.Player;

@CommandAlias("deletealladminclaims")
@CommandPermission("griefprevention.deleteclaims")
public class DeleteAllAdminClaimsCommand extends GPBaseCommand{
	public DeleteAllAdminClaimsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	public void onDeleteAllClaims(final Player player){
		//delete all admin claims
		plugin.dataStore.deleteClaimsForPlayer(null, true);  //null for owner id indicates an administrative claim

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.AllAdminDeleted);
		if(player != null) {
			GriefPrevention.AddLogEntry(player.getName() + " deleted all administrative claims.", CustomLogEntryTypes.AdminActivity);
			//revert any current visualization
			Visualization.Revert(player);
		}

	}
}
