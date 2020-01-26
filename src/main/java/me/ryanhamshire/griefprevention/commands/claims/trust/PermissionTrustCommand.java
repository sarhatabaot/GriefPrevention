package me.ryanhamshire.griefprevention.commands.claims.trust;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("permissionalias|pt")
@CommandPermission("griefprevention.claims")
public class PermissionTrustCommand extends ATrustCommand {
	public PermissionTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onPermissionTrust(final Player player, final String name){
		handleTrustCommand(player, null, name);
	}
}
