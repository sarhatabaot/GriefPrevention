package me.ryanhamshire.griefprevention.commands.claims.trust;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.claim.ClaimPermission;
import me.ryanhamshire.griefprevention.GriefPrevention;
import org.bukkit.entity.Player;

@CommandAlias("accesstrust")
@CommandPermission("griefprevention.claims")
public class AccessTrustCommand extends ATrustCommand {

	public AccessTrustCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onAccessTrust(final Player player, final String name){
		handleTrustCommand(player, ClaimPermission.ACCESS, name);
	}
}
