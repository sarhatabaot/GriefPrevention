package me.ryanhamshire.griefprevention.commands.claims.abandon;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.commands.GPBaseCommand;
import me.ryanhamshire.griefprevention.config.Config;
import me.ryanhamshire.griefprevention.visualization.Visualization;
import org.bukkit.entity.Player;

@CommandAlias("abandonallclaims")
@CommandPermission("griefprevention.claims")
public class AbandonAllClaimsCommand extends GPBaseCommand {
	public AbandonAllClaimsCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onAbandonAllClaims(final Player player){
		//count claims
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		int originalClaimCount = playerData.getClaims().size();

		//check count
		if(originalClaimCount == 0)
		{
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.YouHaveNoClaims);
			return;
		}

		if (Config.config_claims_abandonReturnRatio != 1.0D) {
			//adjust claim blocks
			for(Claim claim : playerData.getClaims())
			{
				playerData.setAccruedClaimBlocks(playerData.getAccruedClaimBlocks() - (int)Math.ceil((claim.getArea() * (1 - Config.config_claims_abandonReturnRatio))));
			}
		}


		//delete them
		plugin.dataStore.deleteClaimsForPlayer(player.getUniqueId(), false);

		//inform the player
		int remainingBlocks = playerData.getRemainingClaimBlocks();
		GriefPrevention.sendMessage(player, TextMode.Success, Messages.SuccessfulAbandon, String.valueOf(remainingBlocks));

		//revert any current visualization
		Visualization.revert(player);
	}
}
