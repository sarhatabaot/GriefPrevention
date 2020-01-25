package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
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
