package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;

@CommandAlias("accesstrust")
public class AccessTrustCommand extends GPBaseCommand {

	public AccessTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onAccessTrust(final Player player, final String name){
		plugin.handleTrustCommand(player, ClaimPermission.Access, name);
	}
}
