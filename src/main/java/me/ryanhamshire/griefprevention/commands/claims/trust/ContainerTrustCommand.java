package me.ryanhamshire.griefprevention.commands.claims.trust;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.claim.ClaimPermission;
import me.ryanhamshire.griefprevention.GriefPrevention;
import org.bukkit.entity.Player;

@CommandAlias("containertrust")
@CommandPermission("griefprevention.claims")
public class ContainerTrustCommand extends ATrustCommand {
	public ContainerTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}
	@Default
	public void onContainerTrust(final Player player, final String name){
		handleTrustCommand(player, ClaimPermission.INVENTORY, name);
	}
}
