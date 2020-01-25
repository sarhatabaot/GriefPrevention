package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.entity.Player;

@CommandAlias("restorenaturefill")
public class RestoreNatureFillCommand extends GPBaseCommand {
	public RestoreNatureFillCommand(final GriefPrevention plugin) {
		super(plugin);
	}


	@Default
	public void onRestoreNature(final Player player, @Optional int optionalRadius){
		//change shovel mode
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		playerData.shovelMode = ShovelMode.RestoreNatureFill;

		//set radius based on arguments
		playerData.fillRadius = 2;
		if(getOrigArgs().length > 0)
		{
			try
			{
				playerData.fillRadius = Integer.parseInt(getOrigArgs()[0]);
			}
			catch(Exception exception){ }
		}

		if(playerData.fillRadius < 0) playerData.fillRadius = 2;

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.FillModeActive, String.valueOf(playerData.fillRadius));
	}
}
