package me.ryanhamshire.griefprevention.commands.claims.trust;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.claim.ClaimPermission;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("trust")
@CommandPermission("griefprevention.claims")
public class TrustCommand extends ATrustCommand {
	public TrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onTrust(final Player player, final String name) {
		handleTrustCommand(player, ClaimPermission.BUILD, name);
	}
}
