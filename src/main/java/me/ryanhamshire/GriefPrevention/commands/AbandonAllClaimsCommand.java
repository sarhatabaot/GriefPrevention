package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.*;
import me.ryanhamshire.GriefPrevention.config.Config;
import org.bukkit.entity.Player;

/**
 * @author sarhatabaot
 */
@CommandAlias("abandonallclaims")
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
		Visualization.Revert(player);
	}
}
