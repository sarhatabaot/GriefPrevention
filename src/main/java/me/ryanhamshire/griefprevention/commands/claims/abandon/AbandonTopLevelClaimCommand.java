package me.ryanhamshire.griefprevention.commands.claims.abandon;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("abandontoplevelclaim")
@CommandPermission("griefprevention.claims")
public class AbandonTopLevelClaimCommand extends AAbandonCommand {

	public AbandonTopLevelClaimCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onAbandonTopLevelClaim(final Player player){
		abandonClaimHandler(player, true);
	}
}
