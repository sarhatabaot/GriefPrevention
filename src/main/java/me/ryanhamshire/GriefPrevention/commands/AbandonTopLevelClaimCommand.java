package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;

@CommandAlias("abandontoplevelclaim")
public class AbandonTopLevelClaimCommand extends GPBaseCommand {

	public AbandonTopLevelClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	public void onAbandonTopLevelClaim(final Player player){
		plugin.abandonClaimHandler(player, true);
	}
}
