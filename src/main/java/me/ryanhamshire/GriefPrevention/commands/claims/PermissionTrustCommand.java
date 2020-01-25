package me.ryanhamshire.GriefPrevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("permissionalias|pt")
@CommandPermission("griefprevention.claims")
public class PermissionTrustCommand extends GPBaseCommand {
	public PermissionTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onPermissionTrust(final Player player, final String name){
		plugin.handleTrustCommand(player, null, name);
	}
}
