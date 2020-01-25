package me.ryanhamshire.griefprevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.claim.ClaimPermission;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("containertrust")
@CommandPermission("griefprevention.claims")
public class ContainerTrustCommand extends GPBaseCommand {
	public ContainerTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}
	@Default
	public void onContainerTrust(final Player player, final String name){
		plugin.handleTrustCommand(player, ClaimPermission.INVENTORY, name);
	}
}
