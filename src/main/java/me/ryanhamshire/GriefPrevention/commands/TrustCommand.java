package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;

@CommandAlias("trust")
public class TrustCommand extends GPBaseCommand {
	public TrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onTrust(final Player player, final String name) {
		plugin.handleTrustCommand(player, ClaimPermission.Build, name);
	}
}
