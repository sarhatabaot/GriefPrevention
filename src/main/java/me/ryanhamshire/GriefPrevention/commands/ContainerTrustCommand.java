package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;

@CommandAlias("containertrust")
public class ContainerTrustCommand extends GPBaseCommand {
	public ContainerTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}
	@Default
	public void onContainerTrust(final Player player, final String name){
		plugin.handleTrustCommand(player, ClaimPermission.Inventory, name);
	}
}
