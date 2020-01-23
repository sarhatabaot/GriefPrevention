package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;

@CommandAlias("abandonclaim")
public class AbandonClaimCommand extends GPBaseCommand {
	public AbandonClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	public void onAbandonClaim(final Player player){
		plugin.abandonClaimHandler(player, false);
	}
}
