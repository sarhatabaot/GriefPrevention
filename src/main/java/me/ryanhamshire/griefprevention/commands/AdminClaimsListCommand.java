package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.TextMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("adminclaimslist")
public class AdminClaimsListCommand extends GPBaseCommand {
	public AdminClaimsListCommand(final GriefPrevention plugin) {
		super(plugin);
	}
	@Default
	public void onAdminClaimsList(final Player player){
		//find admin claims
		List<Claim> claims = new ArrayList<>();
		for(Claim claim : plugin.dataStore.claims)
		{
			if(claim.ownerID == null)  //admin claim
			{
				claims.add(claim);
			}
		}
		if(!claims.isEmpty())
		{
			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.ClaimsListHeader);
			for (Claim claim : claims) {
				GriefPrevention.sendMessage(player, TextMode.Instr, GriefPrevention.getFriendlyLocationString(claim.getLesserBoundaryCorner()));
			}
		}

	}

}
