package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import org.bukkit.entity.Player;

@CommandAlias("restorenatureaggressive")
public class RestoreNatureAggressiveCommand extends GPBaseCommand {
	public RestoreNatureAggressiveCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onRestoreNature(final Player player){
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		playerData.shovelMode = ShovelMode.RestoreNatureAggressive;
		GriefPrevention.sendMessage(player, TextMode.Warn, Messages.RestoreNatureAggressiveActivate);
	}
}
