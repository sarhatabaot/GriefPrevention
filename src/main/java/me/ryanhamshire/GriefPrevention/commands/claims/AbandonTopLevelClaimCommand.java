package me.ryanhamshire.GriefPrevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("abandontoplevelclaim")
@CommandPermission("griefprevention.claims")
public class AbandonTopLevelClaimCommand extends GPBaseCommand {

	public AbandonTopLevelClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onAbandonTopLevelClaim(final Player player){
		plugin.abandonClaimHandler(player, true);
	}
}
