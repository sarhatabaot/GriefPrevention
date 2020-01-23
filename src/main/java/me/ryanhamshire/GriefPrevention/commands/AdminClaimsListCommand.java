package me.ryanhamshire.GriefPrevention.commands;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AdminClaimsListCommand extends GPBaseCommand {
	public AdminClaimsListCommand(final GriefPrevention plugin) {
		super(plugin);
	}

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
				GriefPrevention.sendMessage(player, TextMode.Instr, GriefPrevention.getfriendlyLocationString(claim.getLesserBoundaryCorner()));
			}
		}

	}

}
