package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import me.ryanhamshire.griefprevention.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("siege")
public class SiegeCommand extends GPBaseCommand{
	public SiegeCommand(final GriefPrevention plugin) {
		super(plugin);
	}


	private Player getDefender(final PlayerData attackerData, final String defenderName) {
		Player defender = null;
		if(defenderName != null){
			return Bukkit.getPlayer(defenderName);
		}
		if(attackerData.lastPvpPlayer.length() > 0)
			return Bukkit.getPlayer(attackerData.lastPvpPlayer);
		return null;
	}

	@Default
	public void onSiege(final Player attacker,@Optional final String defenderName){
		//error message for when siege mode is disabled
		if(!plugin.siegeEnabledForWorld(attacker.getWorld())) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.NonSiegeWorld);
			return;
		}


		//can't start a siege when you're already involved in one
		PlayerData attackerData = plugin.dataStore.getPlayerData(attacker.getUniqueId());
		if(attackerData.siegeData != null) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.AlreadySieging);
			return;
		}

		//can't start a siege when you're protected from pvp combat
		if(attackerData.pvpImmune) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.CantFightWhileImmune);
			return;
		}

		//if a player name was specified, use that
		Player defender = getDefender(attackerData, defenderName);
		if(defender == null) return;

		// First off, you cannot siege yourself, that's just
		// silly:
		if (attacker.getName().equals(defender.getName())) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.NoSiegeYourself);
			return;
		}

		//victim must not have the permission which makes him immune to siege
		if(defender.hasPermission("griefprevention.siegeimmune")) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.SiegeImmune);
			return;
		}

		//victim must not be under siege already
		PlayerData defenderData = plugin.dataStore.getPlayerData(defender.getUniqueId());
		if(defenderData.siegeData != null) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.AlreadyUnderSiegePlayer);
			return;
		}

		//victim must not be pvp immune
		if(defenderData.pvpImmune) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.NoSiegeDefenseless);
			return;
		}

		Claim defenderClaim = plugin.dataStore.getClaimAt(defender.getLocation(), false, null);

		//defender must have some level of permission there to be protected
		if(defenderClaim == null || defenderClaim.allowAccess(defender) != null) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.NotSiegableThere);
			return;
		}

		//attacker must be close to the claim he wants to siege
		if(!defenderClaim.isNear(attacker.getLocation(), 25)) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.SiegeTooFarAway);
			return;
		}

		//claim can't be under siege already
		if(defenderClaim.siegeData != null) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.AlreadyUnderSiegeArea);
			return;
		}

		//can't siege admin claims
		if(defenderClaim.isAdminClaim()) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.NoSiegeAdminClaim);
			return;
		}

		//can't be on cooldown
		if(plugin.dataStore.onCooldown(attacker, defender, defenderClaim)) {
			GriefPrevention.sendMessage(attacker, TextMode.Err, Messages.SiegeOnCooldown);
			return;
		}

		//start the siege
		plugin.dataStore.startSiege(attacker, defender, defenderClaim);

		//confirmation message for attacker, warning message for defender
		GriefPrevention.sendMessage(defender, TextMode.Warn, Messages.SiegeAlert, attacker.getName());
		GriefPrevention.sendMessage(attacker, TextMode.Success, Messages.SiegeConfirmed, defender.getName());
	}
}
