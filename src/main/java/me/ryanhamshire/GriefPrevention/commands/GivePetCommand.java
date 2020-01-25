package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.PlayerData;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("givepet")
public class GivePetCommand extends GPBaseCommand {
	public GivePetCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onGivePet(final Player player, final String playerName){
		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());

		//special case: cancellation
		if(playerName.equalsIgnoreCase("cancel")) {
			playerData.petGiveawayRecipient = null;
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.PetTransferCancellation);
			return;
		}

		//find the specified player
		OfflinePlayer targetPlayer = plugin.resolvePlayerByName(playerName);
		if(targetPlayer == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
			return;
		}

		//remember the player's ID for later pet transfer
		playerData.petGiveawayRecipient = targetPlayer;

		//send instructions
		GriefPrevention.sendMessage(player, TextMode.Instr, Messages.ReadyToTransferPet);

	}
}
