package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.entity.Player;

@CommandAlias("basicclaims")
public class BasicClaimsCommand extends GPBaseCommand {
	public BasicClaimsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onBaseClaims(final Player player){
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		playerData.shovelMode = ShovelMode.Basic;
		playerData.claimSubdividing = null;
		GriefPrevention.sendMessage(player, TextMode.Success, Messages.BasicClaimsMode);
	}
}
