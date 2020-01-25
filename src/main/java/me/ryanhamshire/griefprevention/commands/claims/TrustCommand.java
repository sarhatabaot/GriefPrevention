package me.ryanhamshire.griefprevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.ClaimPermission;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("trust")
@CommandPermission("griefprevention.claims")
public class TrustCommand extends GPBaseCommand {
	public TrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onTrust(final Player player, final String name) {
		plugin.handleTrustCommand(player, ClaimPermission.Build, name);
	}
}
