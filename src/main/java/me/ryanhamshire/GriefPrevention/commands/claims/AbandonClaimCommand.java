package me.ryanhamshire.GriefPrevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("abandonclaim")
@CommandPermission("griefprevention.claims")
public class AbandonClaimCommand extends GPBaseCommand {
	public AbandonClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onAbandonClaim(final Player player){
		plugin.abandonClaimHandler(player, false);
	}
}
