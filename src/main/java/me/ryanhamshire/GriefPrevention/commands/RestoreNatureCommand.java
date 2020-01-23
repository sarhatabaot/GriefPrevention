package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.entity.Player;

@CommandAlias("restorenature")
public class RestoreNatureCommand extends GPBaseCommand{
	public RestoreNatureCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onRestoreNature(final Player player){
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		playerData.shovelMode = ShovelMode.RestoreNature;
		GriefPrevention.sendMessage(player, TextMode.Instr, Messages.RestoreNatureActivate);
	}
}
