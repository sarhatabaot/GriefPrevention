package me.ryanhamshire.griefprevention.commands.claims;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.ClaimPermission;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import org.bukkit.entity.Player;

@CommandAlias("accesstrust")
@CommandPermission("griefprevention.claims")
public class AccessTrustCommand extends GPBaseCommand {

	public AccessTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onAccessTrust(final Player player, final String name){
		plugin.handleTrustCommand(player, ClaimPermission.Access, name);
	}
}
