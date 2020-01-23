package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.entity.Player;

@CommandAlias("subdivideclaims")
public class SubDivideClaimsCommand extends GPBaseCommand{
	public SubDivideClaimsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onSubDivideClaims(final Player player){
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		playerData.shovelMode = ShovelMode.Subdivide;
		playerData.claimSubdividing = null;
		GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SubdivisionMode);
		GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SubdivisionVideo2, DataStore.SUBDIVISION_VIDEO_URL);
	}
}
