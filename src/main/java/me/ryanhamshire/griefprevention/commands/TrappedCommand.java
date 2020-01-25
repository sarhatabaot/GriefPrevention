package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.*;
import me.ryanhamshire.griefprevention.claim.Claim;
import me.ryanhamshire.griefprevention.config.Config;
import me.ryanhamshire.griefprevention.events.SaveTrappedPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandAlias("trapped")
public class TrappedCommand extends GPBaseCommand {
	public TrappedCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onTrapped(final Player player){
		//FEATURE: empower players who get "stuck" in an area where they don't have permission to build to save themselves

		PlayerData playerData = plugin.dataStore.getPlayerData(player.getUniqueId());
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), false, playerData.lastClaim);

		//if another /trapped is pending, ignore this slash command
		if(playerData.pendingTrapped) {
			return;
		}

		//if the player isn't in a claim or has permission to build, tell him to man up
		if(claim == null || claim.allowBuild(player, Material.AIR) == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.NotTrappedHere);
			return;
		}

		//rescue destination may be set by GPFlags or other plugin, ask to find out
		SaveTrappedPlayerEvent event = new SaveTrappedPlayerEvent(claim);
		Bukkit.getPluginManager().callEvent(event);

		//if the player is in the nether or end, he's screwed (there's no way to programmatically find a safe place for him)
		if(player.getWorld().getEnvironment() != World.Environment.NORMAL && event.getDestination() == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.TrappedWontWorkHere);
			return;
		}

		//if the player is in an administrative claim and AllowTrappedInAdminClaims is false, he should contact an admin
		if(!Config.config_claims_allowTrappedInAdminClaims && claim.isAdminClaim() && event.getDestination() == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.TrappedWontWorkHere);
			return;
		}
		//send instructions
		GriefPrevention.sendMessage(player, TextMode.Instr, Messages.RescuePending);

		//create a task to rescue this player in a little while
		PlayerRescueTask task = new PlayerRescueTask(player, player.getLocation(), event.getDestination());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, 200L);  //20L ~ 1 second
	}
}
