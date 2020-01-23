package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;

@CommandAlias("permissionalias")
public class PermissionTrustCommand extends GPBaseCommand {
	public PermissionTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onPermissionTrust(final Player player, final String name){
		plugin.handleTrustCommand(player, null, name);
	}
}
