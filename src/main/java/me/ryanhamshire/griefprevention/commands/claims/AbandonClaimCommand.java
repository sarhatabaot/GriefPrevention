package me.ryanhamshire.griefprevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
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
