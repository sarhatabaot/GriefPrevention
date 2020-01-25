package me.ryanhamshire.griefprevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
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
