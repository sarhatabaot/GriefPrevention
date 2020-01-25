/*
    GriefPrevention Server Plugin for Minecraft
    Copyright (C) 2012 Ryan Hamshire

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.ryanhamshire.GriefPrevention;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.aikar.commands.BukkitCommandManager;
import me.ryanhamshire.GriefPrevention.DataStore.NoTransferException;
import me.ryanhamshire.GriefPrevention.commands.*;
import me.ryanhamshire.GriefPrevention.config.Config;
import me.ryanhamshire.GriefPrevention.events.PreventBlockBreakEvent;
import me.ryanhamshire.GriefPrevention.events.SaveTrappedPlayerEvent;
import me.ryanhamshire.GriefPrevention.events.TrustChangedEvent;
import me.ryanhamshire.GriefPrevention.metrics.MetricsHandler;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.BanList.Type;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.jetbrains.annotations.NotNull;

public class GriefPrevention extends JavaPlugin {
	//for convenience, a reference to the instance of this plugin
	public static GriefPrevention instance;

	//for logging to the console and log file
	private static Logger log;

	//this handles data storage, like player and region data
	public DataStore dataStore;

	//this tracks item stacks expected to drop which will need protection
	ArrayList<PendingItemProtection> pendingItemWatchList = new ArrayList<>();

	//log entry manager for GP's custom log files
	CustomLogger customLogger;

	//configuration variables, loaded/saved from a config.yml

	//reference to the economy plugin, if economy integration is enabled
	public static Economy economy = null;

	//how far away to search from a tree trunk for its branch blocks
	public static final int TREE_RADIUS = 5;

	//how long to wait before deciding a player is staying online or staying offline, for notication messages
	public static final int NOTIFICATION_SECONDS = 20;

	//adds a server log entry
	public static synchronized void AddLogEntry(String entry, CustomLogEntryTypes customLogType, boolean excludeFromServerLogs) {
		if (customLogType != null && GriefPrevention.instance.customLogger != null) {
			GriefPrevention.instance.customLogger.AddEntry(entry, customLogType);
		}
		if (!excludeFromServerLogs) log.info(entry);
	}

	public static synchronized void AddLogEntry(String entry, CustomLogEntryTypes customLogType) {
		AddLogEntry(entry, customLogType, false);
	}

	public static synchronized void AddLogEntry(String entry) {
		AddLogEntry(entry, CustomLogEntryTypes.Debug);
	}

	private void registerCommands() {
		BukkitCommandManager commandManager = new BukkitCommandManager(this);
		commandManager.registerCommand(new AbandonAllClaimsCommand(this));
		commandManager.registerCommand(new AbandonClaimCommand(this));
		commandManager.registerCommand(new AbandonTopLevelClaimCommand(this));
		commandManager.registerCommand(new AccessTrustCommand(this));
		commandManager.registerCommand(new AdjustBonusClaimBlocksCommand(this));
		commandManager.registerCommand(new AdjustBonusClaimBlocksAllCommand(this));
		commandManager.registerCommand(new AdminClaimsListCommand(this));
		commandManager.registerCommand(new BasicClaimsCommand(this));
		commandManager.registerCommand(new BlockInfoCommand(this));
		commandManager.registerCommand(new ClaimBookCommand(this));
		commandManager.registerCommand(new ClaimCommand(this));
		commandManager.registerCommand(new ClaimExplosionsCommand(this));
		commandManager.registerCommand(new ClaimListCommand(this));
		commandManager.registerCommand(new ContainerTrustCommand(this));
		commandManager.registerCommand(new DeleteAllClaimsCommand(this));
		commandManager.registerCommand(new DeleteAllAdminClaimsCommand(this));
		commandManager.registerCommand(new DeleteClaimCommand(this));
		commandManager.registerCommand(new DeleteClaimsInWorldCommand(this));
		commandManager.registerCommand(new ExtendClaimCommand(this));
		commandManager.registerCommand(new GivePetCommand(this));
		commandManager.registerCommand(new IgnorePlayerCommand(this));
		commandManager.registerCommand(new IgnorePlayerListCommand(this));
		commandManager.registerCommand(new PermissionTrustCommand(this));
		commandManager.registerCommand(new ReloadCommand(this));
		commandManager.registerCommand(new RestoreNatureCommand(this));
		commandManager.registerCommand(new RestoreNatureAggressiveCommand(this));
		commandManager.registerCommand(new RestoreNatureFillCommand(this));
		commandManager.registerCommand(new RestrictSubClaimCommand(this));
		commandManager.registerCommand(new SeparatePlayersCommand(this));
		commandManager.registerCommand(new SetAccruedClaimBlocksCommand(this));
		commandManager.registerCommand(new SiegeCommand(this));
		commandManager.registerCommand(new SoftMuteCommand(this));
		commandManager.registerCommand(new SubDivideClaimsCommand(this));
		commandManager.registerCommand(new TransferClaimsCommand(this));
		commandManager.registerCommand(new TrappedCommand(this));
		commandManager.registerCommand(new TrustCommand(this));
		commandManager.registerCommand(new TrustListCommand(this));
		commandManager.registerCommand(new UnIgnorePlayerCommand(this));
		commandManager.registerCommand(new UnlockDropsCommand(this));
		commandManager.registerCommand(new UnlockItemsCommand(this));
		commandManager.registerCommand(new UnSeparateCommand(this));
		commandManager.registerCommand(new UnTrustCommand(this));
	}

	@Override
	public void onEnable() {
		instance = this;
		log = instance.getLogger();

		if(!new File(getDataFolder()+"/config.yml").exists())
			copyOldConfig();
		else saveDefaultConfig();

		Config.init(this);

		this.customLogger = new CustomLogger();

		AddLogEntry("Finished loading configuration.");

		registerCommands();
		//when datastore initializes, it loads player and claim data, and posts some stats to the log
		if (Config.databaseUrl.length() > 0) {
			try {
				DatabaseDataStore databaseStore = new DatabaseDataStore(Config.databaseUrl, Config.databaseUserName, Config.databasePassword);

				if (FlatFileDataStore.hasData()) {
					GriefPrevention.AddLogEntry("There appears to be some data on the hard drive.  Migrating those data to the database...");
					FlatFileDataStore flatFileStore = new FlatFileDataStore();
					this.dataStore = flatFileStore;
					flatFileStore.migrateData(databaseStore);
					GriefPrevention.AddLogEntry("Data migration process complete.");
				}

				this.dataStore = databaseStore;
			} catch (Exception e) {
				GriefPrevention.AddLogEntry("Because there was a problem with the database, GriefPrevention will not function properly.  Either update the database config settings resolve the issue, or delete those lines from your config.yml so that GriefPrevention can use the file system to store data.");
				e.printStackTrace();
				this.getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}

		//if not using the database because it's not configured or because there was a problem, use the file system to store data
		//this is the preferred method, as it's simpler than the database scenario
		if (this.dataStore == null) {
			File oldclaimdata = new File(getDataFolder(), "ClaimData");
			if (oldclaimdata.exists()) {
				if (!FlatFileDataStore.hasData()) {
					File claimdata = new File("plugins" + File.separator + "GriefPreventionData" + File.separator + "ClaimData");
					oldclaimdata.renameTo(claimdata);
					File oldplayerdata = new File(getDataFolder(), "PlayerData");
					File playerdata = new File("plugins" + File.separator + "GriefPreventionData" + File.separator + "PlayerData");
					oldplayerdata.renameTo(playerdata);
				}
			}
			try {
				this.dataStore = new FlatFileDataStore();
			} catch (Exception e) {
				GriefPrevention.AddLogEntry("Unable to initialize the file system data store.  Details:");
				GriefPrevention.AddLogEntry(e.getMessage());
				e.printStackTrace();
			}
		}

		String dataMode = (this.dataStore instanceof FlatFileDataStore) ? "(File Mode)" : "(Database Mode)";
		AddLogEntry("Finished loading data " + dataMode + ".");

		//unless claim block accrual is disabled, start the recurring per 10 minute event to give claim blocks to online players
		//20L ~ 1 second
		if (Config.config_claims_blocksAccruedPerHour_default > 0) {
			DeliverClaimBlocksTask task = new DeliverClaimBlocksTask(null, this);
			this.getServer().getScheduler().scheduleSyncRepeatingTask(this, task, 20L * 60 * 10, 20L * 60 * 10);
		}

		//start the recurring cleanup event for entities in creative worlds
		EntityCleanupTask task = new EntityCleanupTask(0);
		this.getServer().getScheduler().scheduleSyncDelayedTask(GriefPrevention.instance, task, 20L * 60 * 2);

		//start recurring cleanup scan for unused claims belonging to inactive players
		FindUnusedClaimsTask task2 = new FindUnusedClaimsTask();
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, task2, 20L * 60, 20L * Config.config_advanced_claim_expiration_check_rate);

		//register for events
		PluginManager pluginManager = this.getServer().getPluginManager();

		//player events
		PlayerEventHandler playerEventHandler = new PlayerEventHandler(this.dataStore, this);
		pluginManager.registerEvents(playerEventHandler, this);

		//block events
		BlockEventHandler blockEventHandler = new BlockEventHandler(this.dataStore);
		pluginManager.registerEvents(blockEventHandler, this);

		//entity events
		EntityEventHandler entityEventHandler = new EntityEventHandler(this.dataStore, this);
		pluginManager.registerEvents(entityEventHandler, this);

		//if economy is enabled
		if (Config.config_economy_claimBlocksPurchaseCost > 0 || Config.config_economy_claimBlocksSellValue > 0) {
			//try to load Vault
			GriefPrevention.AddLogEntry("GriefPrevention requires Vault for economy integration.");
			GriefPrevention.AddLogEntry("Attempting to load Vault...");
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			GriefPrevention.AddLogEntry("Vault loaded successfully!");

			//ask Vault to hook into an economy plugin
			GriefPrevention.AddLogEntry("Looking for a Vault-compatible economy plugin...");
			if (economyProvider != null) {
				GriefPrevention.economy = economyProvider.getProvider();

				//on success, display success message
				if (GriefPrevention.economy != null) {
					GriefPrevention.AddLogEntry("Hooked into economy: " + GriefPrevention.economy.getName() + ".");
					GriefPrevention.AddLogEntry("Ready to buy/sell claim blocks!");
				}

				//otherwise error message
				else {
					GriefPrevention.AddLogEntry("ERROR: Vault was unable to find a supported economy plugin.  Either install a Vault-compatible economy plugin, or set both of the economy config variables to zero.");
				}
			}

			//another error case
			else {
				GriefPrevention.AddLogEntry("ERROR: Vault was unable to find a supported economy plugin.  Either install a Vault-compatible economy plugin, or set both of the economy config variables to zero.");
			}
		}

		//cache offline players
		OfflinePlayer[] offlinePlayers = this.getServer().getOfflinePlayers();
		CacheOfflinePlayerNamesThread namesThread = new CacheOfflinePlayerNamesThread(offlinePlayers, this.playerNameToIDMap);
		namesThread.setPriority(Thread.MIN_PRIORITY);
		namesThread.start();

		//load ignore lists for any already-online players
		@SuppressWarnings("unchecked")
		Collection<Player> players = (Collection<Player>) GriefPrevention.instance.getServer().getOnlinePlayers();
		for (Player player : players) {
			new IgnoreLoaderThread(player.getUniqueId(), this.dataStore.getPlayerData(player.getUniqueId()).ignoredPlayers).start();
		}

		AddLogEntry("Boot finished.");

		try {
			new MetricsHandler(this, dataMode);
		} catch (Throwable ignored) {
		}
	}

	private ClaimsMode configStringToClaimsMode(String configSetting) {
		if (configSetting.equalsIgnoreCase("Survival")) {
			return ClaimsMode.Survival;
		} else if (configSetting.equalsIgnoreCase("Creative")) {
			return ClaimsMode.Creative;
		} else if (configSetting.equalsIgnoreCase("Disabled")) {
			return ClaimsMode.Disabled;
		} else if (configSetting.equalsIgnoreCase("SurvivalRequiringClaims")) {
			return ClaimsMode.SurvivalRequiringClaims;
		} else {
			return null;
		}
	}

	public void setIgnoreStatus(OfflinePlayer ignorer, OfflinePlayer ignoree, IgnoreMode mode) {
		PlayerData playerData = this.dataStore.getPlayerData(ignorer.getUniqueId());
		if (mode == IgnoreMode.NONE) {
			playerData.ignoredPlayers.remove(ignoree.getUniqueId());
		} else {
			playerData.ignoredPlayers.put(ignoree.getUniqueId(), mode == IgnoreMode.STANDARD ? false : true);
		}

		playerData.ignoreListChanged = true;
		if (!ignorer.isOnline()) {
			this.dataStore.savePlayerData(ignorer.getUniqueId(), playerData);
			this.dataStore.clearCachedPlayerData(ignorer.getUniqueId());
		}
	}

	public enum IgnoreMode {ADMIN, NONE, STANDARD}

	@Deprecated
	private String trustEntryToPlayerName(String entry) {
		if (entry.startsWith("[") || entry.equals("public")) {
			return entry;
		} else {
			return GriefPrevention.lookupPlayerName(entry);
		}
	}

	public static String getfriendlyLocationString(Location location) {
		return location.getWorld().getName() + ": x" + location.getBlockX() + ", z" + location.getBlockZ();
	}

	public boolean abandonClaimHandler(Player player, boolean deleteTopLevelClaim) {
		PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());

		//which claim is being abandoned?
		Claim claim = this.dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);
		if (claim == null) {
			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.AbandonClaimMissing);
		}

		//verify ownership
		else if (claim.allowEdit(player) != null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.NotYourClaim);
		}

		//warn if has children and we're not explicitly deleting a top level claim
		else if (claim.children.size() > 0 && !deleteTopLevelClaim) {
			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.DeleteTopLevelClaim);
			return true;
		} else {
			//delete it
			claim.removeSurfaceFluids(null);
			this.dataStore.deleteClaim(claim, true, false);

			//if in a creative mode world, restore the claim area
			if (GriefPrevention.instance.creativeRulesApply(claim.getLesserBoundaryCorner())) {
				GriefPrevention.AddLogEntry(player.getName() + " abandoned a claim @ " + GriefPrevention.getfriendlyLocationString(claim.getLesserBoundaryCorner()));
				GriefPrevention.sendMessage(player, TextMode.Warn, Messages.UnclaimCleanupWarning);
				GriefPrevention.instance.restoreClaim(claim, 20L * 60 * 2);
			}

			//adjust claim blocks when abandoning a top level claim
			if (Config.config_claims_abandonReturnRatio != 1.0D && claim.parent == null && claim.ownerID.equals(playerData.playerID)) {
				playerData.setAccruedClaimBlocks(playerData.getAccruedClaimBlocks() - (int) Math.ceil((claim.getArea() * (1 - Config.config_claims_abandonReturnRatio))));
			}

			//tell the player how many claim blocks he has left
			int remainingBlocks = playerData.getRemainingClaimBlocks();
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.AbandonSuccess, String.valueOf(remainingBlocks));

			//revert any current visualization
			Visualization.Revert(player);

			playerData.warnedAboutMajorDeletion = false;
		}

		return true;

	}

	//helper method keeps the trust commands consistent and eliminates duplicate code
	public void handleTrustCommand(Player player, ClaimPermission permissionLevel, String recipientName) {
		//determine which claim the player is standing in
		Claim claim = this.dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);

		//validate player or group argument
		String permission = null;
		OfflinePlayer otherPlayer = null;
		UUID recipientID = null;
		if (recipientName.startsWith("[") && recipientName.endsWith("]")) {
			permission = recipientName.substring(1, recipientName.length() - 1);
			if (permission == null || permission.isEmpty()) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.InvalidPermissionID);
				return;
			}
		} else if (recipientName.contains(".")) {
			permission = recipientName;
		} else {
			otherPlayer = this.resolvePlayerByName(recipientName);
			if (otherPlayer == null && !recipientName.equals("public") && !recipientName.equals("all")) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
				return;
			}

			if (otherPlayer != null) {
				recipientName = otherPlayer.getName();
				recipientID = otherPlayer.getUniqueId();
			} else {
				recipientName = "public";
			}
		}

		//determine which claims should be modified
		ArrayList<Claim> targetClaims = new ArrayList<Claim>();
		if (claim == null) {
			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
			for (int i = 0; i < playerData.getClaims().size(); i++) {
				targetClaims.add(playerData.getClaims().get(i));
			}
		} else {
			//check permission here
			if (claim.allowGrantPermission(player) != null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.NoPermissionTrust, claim.getOwnerName());
				return;
			}

			//see if the player has the level of permission he's trying to grant
			String errorMessage = null;

			//permission level null indicates granting permission trust
			if (permissionLevel == null) {
				errorMessage = claim.allowEdit(player);
				if (errorMessage != null) {
					errorMessage = "Only " + claim.getOwnerName() + " can grant /PermissionTrust here.";
				}
			}

			//otherwise just use the ClaimPermission enum values
			else {
				switch (permissionLevel) {
					case Access:
						errorMessage = claim.allowAccess(player);
						break;
					case Inventory:
						errorMessage = claim.allowContainers(player);
						break;
					default:
						errorMessage = claim.allowBuild(player, Material.AIR);
				}
			}

			//error message for trying to grant a permission the player doesn't have
			if (errorMessage != null) {
				GriefPrevention.sendMessage(player, TextMode.Err, Messages.CantGrantThatPermission);
				return;
			}

			targetClaims.add(claim);
		}

		//if we didn't determine which claims to modify, tell the player to be specific
		if (targetClaims.size() == 0) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.GrantPermissionNoClaim);
			return;
		}

		String identifierToAdd = recipientName;
		if (permission != null) {
			identifierToAdd = "[" + permission + "]";
		} else if (recipientID != null) {
			identifierToAdd = recipientID.toString();
		}

		//calling the event
		TrustChangedEvent event = new TrustChangedEvent(player, targetClaims, permissionLevel, true, identifierToAdd);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		//apply changes
		for (int i = 0; i < targetClaims.size(); i++) {
			Claim currentClaim = targetClaims.get(i);

			if (permissionLevel == null) {
				if (!currentClaim.managers.contains(identifierToAdd)) {
					currentClaim.managers.add(identifierToAdd);
				}
			} else {
				currentClaim.setPermission(identifierToAdd, permissionLevel);
			}
			this.dataStore.saveClaim(currentClaim);
		}

		//notify player
		if (recipientName.equals("public")) recipientName = this.dataStore.getMessage(Messages.CollectivePublic);
		String permissionDescription;
		if (permissionLevel == null) {
			permissionDescription = this.dataStore.getMessage(Messages.PermissionsPermission);
		} else if (permissionLevel == ClaimPermission.Build) {
			permissionDescription = this.dataStore.getMessage(Messages.BuildPermission);
		} else if (permissionLevel == ClaimPermission.Access) {
			permissionDescription = this.dataStore.getMessage(Messages.AccessPermission);
		} else //ClaimPermission.Inventory
		{
			permissionDescription = this.dataStore.getMessage(Messages.ContainersPermission);
		}

		String location;
		if (claim == null) {
			location = this.dataStore.getMessage(Messages.LocationAllClaims);
		} else {
			location = this.dataStore.getMessage(Messages.LocationCurrentClaim);
		}

		GriefPrevention.sendMessage(player, TextMode.Success, Messages.GrantPermissionConfirmation, recipientName, permissionDescription, location);
	}

	//helper method to resolve a player by name
	ConcurrentHashMap<String, UUID> playerNameToIDMap = new ConcurrentHashMap<String, UUID>();

	//thread to build the above cache
	private class CacheOfflinePlayerNamesThread extends Thread {
		private OfflinePlayer[] offlinePlayers;
		private ConcurrentHashMap<String, UUID> playerNameToIDMap;

		CacheOfflinePlayerNamesThread(OfflinePlayer[] offlinePlayers, ConcurrentHashMap<String, UUID> playerNameToIDMap) {
			this.offlinePlayers = offlinePlayers;
			this.playerNameToIDMap = playerNameToIDMap;
		}

		public void run() {
			long now = System.currentTimeMillis();
			final long millisecondsPerDay = 1000 * 60 * 60 * 24;
			for (OfflinePlayer player : offlinePlayers) {
				try {
					UUID playerID = player.getUniqueId();
					if (playerID == null) continue;
					long lastSeen = player.getLastPlayed();

					//if the player has been seen in the last 90 days, cache his name/UUID pair
					long diff = now - lastSeen;
					long daysDiff = diff / millisecondsPerDay;
					if (daysDiff <= Config.config_advanced_offlineplayer_cache_days) {
						String playerName = player.getName();
						if (playerName == null) continue;
						this.playerNameToIDMap.put(playerName, playerID);
						this.playerNameToIDMap.put(playerName.toLowerCase(), playerID);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public OfflinePlayer resolvePlayerByName(String name) {
		//try online players first
		Player targetPlayer = this.getServer().getPlayerExact(name);
		if (targetPlayer != null) return targetPlayer;

		targetPlayer = this.getServer().getPlayer(name);
		if (targetPlayer != null) return targetPlayer;

		UUID bestMatchID = null;

		//try exact match first
		bestMatchID = this.playerNameToIDMap.get(name);

		//if failed, try ignore case
		if (bestMatchID == null) {
			bestMatchID = this.playerNameToIDMap.get(name.toLowerCase());
		}
		if (bestMatchID == null) {
			return null;
		}

		return this.getServer().getOfflinePlayer(bestMatchID);
	}

	//helper method to resolve a player name from the player's UUID
	public static String lookupPlayerName(UUID playerID) {
		//parameter validation
		if (playerID == null) return "somebody";

		//check the cache
		OfflinePlayer player = GriefPrevention.instance.getServer().getOfflinePlayer(playerID);
		if (player.hasPlayedBefore() || player.isOnline()) {
			return player.getName();
		} else {
			return "someone(" + playerID.toString() + ")";
		}
	}

	//cache for player name lookups, to save searches of all offline players
	static void cacheUUIDNamePair(UUID playerID, String playerName) {
		//store the reverse mapping
		GriefPrevention.instance.playerNameToIDMap.put(playerName, playerID);
		GriefPrevention.instance.playerNameToIDMap.put(playerName.toLowerCase(), playerID);
	}

	//string overload for above helper
	public static String lookupPlayerName(String playerID) {
		UUID id;
		try {
			id = UUID.fromString(playerID);
		} catch (IllegalArgumentException ex) {
			GriefPrevention.AddLogEntry("Error: Tried to look up a local player name for invalid UUID: " + playerID);
			return "someone";
		}

		return lookupPlayerName(id);
	}

	@Override
	public void onDisable() {
		//save data for any online players
		@SuppressWarnings("unchecked")
		Collection<Player> players = (Collection<Player>) this.getServer().getOnlinePlayers();
		for (Player player : players) {
			UUID playerID = player.getUniqueId();
			PlayerData playerData = this.dataStore.getPlayerData(playerID);
			this.dataStore.savePlayerDataSync(playerID, playerData);
		}

		this.dataStore.close();

		//dump any remaining unwritten log entries
		this.customLogger.WriteEntries();

		AddLogEntry("GriefPrevention disabled.");
	}

	//called when a player spawns, applies protection for that player if necessary
	public void checkPvpProtectionNeeded(Player player) {
		//if anti spawn camping feature is not enabled, do nothing
		if (!Config.config_pvp_protectFreshSpawns) return;

		//if pvp is disabled, do nothing
		if (!pvpRulesApply(player.getWorld())) return;

		//if player is in creative mode, do nothing
		if (player.getGameMode() == GameMode.CREATIVE) return;

		//if the player has the damage any player permission enabled, do nothing
		if (player.hasPermission("griefprevention.nopvpimmunity")) return;

		//check inventory for well, anything
		if (GriefPrevention.isInventoryEmpty(player)) {
			//if empty, apply immunity
			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
			playerData.pvpImmune = true;

			//inform the player after he finishes respawning
			GriefPrevention.sendMessage(player, TextMode.Success, Messages.PvPImmunityStart, 5L);

			//start a task to re-check this player's inventory every minute until his immunity is gone
			PvPImmunityValidationTask task = new PvPImmunityValidationTask(player);
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, task, 1200L);
		}
	}

	static boolean isInventoryEmpty(Player player) {
		PlayerInventory inventory = player.getInventory();
		ItemStack[] armorStacks = inventory.getArmorContents();

		//check armor slots, stop if any items are found
		for (int i = 0; i < armorStacks.length; i++) {
			if (!(armorStacks[i] == null || armorStacks[i].getType() == Material.AIR)) return false;
		}

		//check other slots, stop if any items are found
		ItemStack[] generalStacks = inventory.getContents();
		for (int i = 0; i < generalStacks.length; i++) {
			if (!(generalStacks[i] == null || generalStacks[i].getType() == Material.AIR)) return false;
		}

		return true;
	}

	//checks whether players siege in a world
	public boolean siegeEnabledForWorld(World world) {
		return Config.config_siege_enabledWorlds.contains(world);
	}

	//moves a player from the claim he's in to a nearby wilderness location
	public Location ejectPlayer(Player player) {
		//look for a suitable location
		Location candidateLocation = player.getLocation();
		while (true) {
			Claim claim = null;
			claim = GriefPrevention.instance.dataStore.getClaimAt(candidateLocation, false, null);

			//if there's a claim here, keep looking
			if (claim != null) {
				candidateLocation = new Location(claim.lesserBoundaryCorner.getWorld(), claim.lesserBoundaryCorner.getBlockX() - 1, claim.lesserBoundaryCorner.getBlockY(), claim.lesserBoundaryCorner.getBlockZ() - 1);
				continue;
			}

			//otherwise find a safe place to teleport the player
			else {
				//find a safe height, a couple of blocks above the surface
				GuaranteeChunkLoaded(candidateLocation);
				Block highestBlock = candidateLocation.getWorld().getHighestBlockAt(candidateLocation.getBlockX(), candidateLocation.getBlockZ());
				Location destination = new Location(highestBlock.getWorld(), highestBlock.getX(), highestBlock.getY() + 2, highestBlock.getZ());
				player.teleport(destination);
				return destination;
			}
		}
	}

	//ensures a piece of the managed world is loaded into server memory
	//(generates the chunk if necessary)
	private static void GuaranteeChunkLoaded(Location location) {
		Chunk chunk = location.getChunk();
		while (!chunk.isLoaded() || !chunk.load(true)) ;
	}

	//sends a color-coded message to a player
	public static void sendMessage(Player player, ChatColor color, Messages messageID, String... args) {
		sendMessage(player, color, messageID, 0, args);
	}

	//sends a color-coded message to a player
	public static void sendMessage(Player player, ChatColor color, Messages messageID, long delayInTicks, String... args) {
		String message = GriefPrevention.instance.dataStore.getMessage(messageID, args);
		sendMessage(player, color, message, delayInTicks);
	}

	//sends a color-coded message to a player
	public static void sendMessage(Player player, ChatColor color, String message) {
		if (message == null || message.length() == 0) return;

		if (player == null) {
			GriefPrevention.AddLogEntry(color + message);
		} else {
			player.sendMessage(color + message);
		}
	}

	public static void sendMessage(Player player, ChatColor color, String message, long delayInTicks) {
		SendPlayerMessageTask task = new SendPlayerMessageTask(player, color, message);

		//Only schedule if there should be a delay. Otherwise, send the message right now, else the message will appear out of order.
		if (delayInTicks > 0) {
			GriefPrevention.instance.getServer().getScheduler().runTaskLater(GriefPrevention.instance, task, delayInTicks);
		} else {
			task.run();
		}
	}

	//checks whether players can create claims in a world
	public boolean claimsEnabledForWorld(World world) {
		ClaimsMode mode = Config.config_claims_worldModes.get(world);
		return mode != null && mode != ClaimsMode.Disabled;
	}

	//determines whether creative anti-grief rules apply at a location
	public boolean creativeRulesApply(Location location) {
		if (!Config.config_creativeWorldsExist) return false;

		return Config.config_claims_worldModes.get((location.getWorld())) == ClaimsMode.Creative;
	}

	public String allowBuild(Player player, Location location) {
		return this.allowBuild(player, location, location.getBlock().getType());
	}

	public String allowBuild(Player player, Location location, Material material) {
		if (!GriefPrevention.instance.claimsEnabledForWorld(location.getWorld())) return null;

		PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
		Claim claim = this.dataStore.getClaimAt(location, false, playerData.lastClaim);

		//exception: administrators in ignore claims mode
		if (playerData.ignoreClaims) return null;

		//wilderness rules
		if (claim == null) {
			//no building in the wilderness in creative mode
			if (this.creativeRulesApply(location) || Config.config_claims_worldModes.get(location.getWorld()) == ClaimsMode.SurvivalRequiringClaims) {
				//exception: when chest claims are enabled, players who have zero land claims and are placing a chest
				if (material != Material.CHEST || playerData.getClaims().size() > 0 || Config.config_claims_automaticClaimsForNewPlayersRadius == -1) {
					String reason = this.dataStore.getMessage(Messages.NoBuildOutsideClaims);
					if (player.hasPermission("griefprevention.ignoreclaims"))
						reason += "  " + this.dataStore.getMessage(Messages.IgnoreClaimsAdvertisement);
					reason += "  " + this.dataStore.getMessage(Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
					return reason;
				} else {
					return null;
				}
			}

			//but it's fine in survival mode
			else {
				return null;
			}
		}

		//if not in the wilderness, then apply claim rules (permissions, etc)
		else {
			//cache the claim for later reference
			playerData.lastClaim = claim;
			return claim.allowBuild(player, material);
		}
	}

	public String allowBreak(Player player, Block block, Location location) {
		return this.allowBreak(player, block, location, null);
	}

	public String allowBreak(Player player, Block block, Location location, BlockBreakEvent breakEvent) {
		if (!GriefPrevention.instance.claimsEnabledForWorld(location.getWorld())) return null;

		PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
		Claim claim = this.dataStore.getClaimAt(location, false, playerData.lastClaim);

		//exception: administrators in ignore claims mode
		if (playerData.ignoreClaims) return null;

		//wilderness rules
		if (claim == null) {
			//no building in the wilderness in creative mode
			if (this.creativeRulesApply(location) || Config.config_claims_worldModes.get(location.getWorld()) == ClaimsMode.SurvivalRequiringClaims) {
				String reason = this.dataStore.getMessage(Messages.NoBuildOutsideClaims);
				if (player.hasPermission("griefprevention.ignoreclaims"))
					reason += "  " + this.dataStore.getMessage(Messages.IgnoreClaimsAdvertisement);
				reason += "  " + this.dataStore.getMessage(Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
				return reason;
			}

			//but it's fine in survival mode
			else {
				return null;
			}
		} else {
			//cache the claim for later reference
			playerData.lastClaim = claim;

			//if not in the wilderness, then apply claim rules (permissions, etc)
			String cancel = claim.allowBreak(player, block.getType());
			if (cancel != null && breakEvent != null) {
				PreventBlockBreakEvent preventionEvent = new PreventBlockBreakEvent(breakEvent);
				Bukkit.getPluginManager().callEvent(preventionEvent);
				if (preventionEvent.isCancelled()) {
					cancel = null;
				}
			}

			return cancel;
		}
	}

	//restores nature in multiple chunks, as described by a claim instance
	//this restores all chunks which have ANY number of claim blocks from this claim in them
	//if the claim is still active (in the data store), then the claimed blocks will not be changed (only the area bordering the claim)
	public void restoreClaim(Claim claim, long delayInTicks) {
		//admin claims aren't automatically cleaned up when deleted or abandoned
		if (claim.isAdminClaim()) return;

		//it's too expensive to do this for huge claims
		if (claim.getArea() > 10000) return;

		ArrayList<Chunk> chunks = claim.getChunks();
		for (Chunk chunk : chunks) {
			this.restoreChunk(chunk, this.getSeaLevel(chunk.getWorld()) - 15, false, delayInTicks, null);
		}
	}

	@SuppressWarnings("deprecation")
	public void restoreChunk(Chunk chunk, int miny, boolean aggressiveMode, long delayInTicks, Player playerReceivingVisualization) {
		//build a snapshot of this chunk, including 1 block boundary outside of the chunk all the way around
		int maxHeight = chunk.getWorld().getMaxHeight();
		BlockSnapshot[][][] snapshots = new BlockSnapshot[18][maxHeight][18];
		Block startBlock = chunk.getBlock(0, 0, 0);
		Location startLocation = new Location(chunk.getWorld(), startBlock.getX() - 1, 0, startBlock.getZ() - 1);
		for (int x = 0; x < snapshots.length; x++) {
			for (int z = 0; z < snapshots[0][0].length; z++) {
				for (int y = 0; y < snapshots[0].length; y++) {
					Block block = chunk.getWorld().getBlockAt(startLocation.getBlockX() + x, startLocation.getBlockY() + y, startLocation.getBlockZ() + z);
					snapshots[x][y][z] = new BlockSnapshot(block.getLocation(), block.getType(), block.getBlockData());
				}
			}
		}

		//create task to process those data in another thread
		Location lesserBoundaryCorner = chunk.getBlock(0, 0, 0).getLocation();
		Location greaterBoundaryCorner = chunk.getBlock(15, 0, 15).getLocation();

		//create task
		//when done processing, this task will create a main thread task to actually update the world with processing results
		RestoreNatureProcessingTask task = new RestoreNatureProcessingTask(snapshots, miny, chunk.getWorld().getEnvironment(), lesserBoundaryCorner.getBlock().getBiome(), lesserBoundaryCorner, greaterBoundaryCorner, this.getSeaLevel(chunk.getWorld()), aggressiveMode, GriefPrevention.instance.creativeRulesApply(lesserBoundaryCorner), playerReceivingVisualization);
		GriefPrevention.instance.getServer().getScheduler().runTaskLaterAsynchronously(GriefPrevention.instance, task, delayInTicks);
	}

	private void parseMaterialListFromConfig(List<String> stringsToParse, MaterialCollection materialCollection) {
		materialCollection.clear();

		//for each string in the list
		for (int i = 0; i < stringsToParse.size(); i++) {
			//try to parse the string value into a material info
			MaterialInfo materialInfo = MaterialInfo.fromString(stringsToParse.get(i));

			//null value returned indicates an error parsing the string from the config file
			if (materialInfo == null) {
				//show error in log
				GriefPrevention.AddLogEntry("ERROR: Unable to read a material entry from the config file.  Please update your config.yml.");

				//update string, which will go out to config file to help user find the error entry
				if (!stringsToParse.get(i).contains("can't")) {
					stringsToParse.set(i, stringsToParse.get(i) + "     <-- can't understand this entry, see BukkitDev documentation");
				}
			}

			//otherwise store the valid entry in config data
			else {
				materialCollection.Add(materialInfo);
			}
		}
	}

	public int getSeaLevel(World world) {
		Integer overrideValue = Config.config_seaLevelOverride.get(world.getName());
		if (overrideValue == null || overrideValue == -1) {
			return world.getSeaLevel();
		} else {
			return overrideValue;
		}
	}

	public static Block getTargetNonAirBlock(Player player, int maxDistance) throws IllegalStateException {
		BlockIterator iterator = new BlockIterator(player.getLocation(), player.getEyeHeight(), maxDistance);
		Block result = player.getLocation().getBlock().getRelative(BlockFace.UP);
		while (iterator.hasNext()) {
			result = iterator.next();
			if (result.getType() != Material.AIR) return result;
		}

		return result;
	}

	public boolean containsBlockedIP(String message) {
		message = message.replace("\r\n", "");
		Pattern ipAddressPattern = Pattern.compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");
		Matcher matcher = ipAddressPattern.matcher(message);

		//if it looks like an IP address
		if (matcher.find()) {
			//and it's not in the list of allowed IP addresses
			if (!Config.config_spam_allowedIpAddresses.contains(matcher.group())) {
				return true;
			}
		}

		return false;
	}

	public void autoExtendClaim(Claim newClaim) {
		//auto-extend it downward to cover anything already built underground
		Location lesserCorner = newClaim.getLesserBoundaryCorner();
		Location greaterCorner = newClaim.getGreaterBoundaryCorner();
		World world = lesserCorner.getWorld();
		ArrayList<ChunkSnapshot> snapshots = new ArrayList<ChunkSnapshot>();
		for (int chunkx = lesserCorner.getBlockX() / 16; chunkx <= greaterCorner.getBlockX() / 16; chunkx++) {
			for (int chunkz = lesserCorner.getBlockZ() / 16; chunkz <= greaterCorner.getBlockZ() / 16; chunkz++) {
				if (world.isChunkLoaded(chunkx, chunkz)) {
					snapshots.add(world.getChunkAt(chunkx, chunkz).getChunkSnapshot(true, true, false));
				}
			}
		}

		Bukkit.getScheduler().runTaskAsynchronously(GriefPrevention.instance, new AutoExtendClaimTask(newClaim, snapshots, world.getEnvironment()));
	}

	public boolean pvpRulesApply(World world) {
		Boolean configSetting = Config.config_pvp_specifiedWorlds.get(world);
		if (configSetting != null) return configSetting;
		return world.getPVP();
	}

	public static boolean isNewToServer(Player player) {
		if (player.getStatistic(Statistic.PICKUP, Material.OAK_LOG) > 0 ||
				player.getStatistic(Statistic.PICKUP, Material.SPRUCE_LOG) > 0 ||
				player.getStatistic(Statistic.PICKUP, Material.BIRCH_LOG) > 0 ||
				player.getStatistic(Statistic.PICKUP, Material.JUNGLE_LOG) > 0 ||
				player.getStatistic(Statistic.PICKUP, Material.ACACIA_LOG) > 0 ||
				player.getStatistic(Statistic.PICKUP, Material.DARK_OAK_LOG) > 0) return false;

		PlayerData playerData = instance.dataStore.getPlayerData(player.getUniqueId());
		return playerData.getClaims().isEmpty();
	}

	public static void banPlayer(Player player, String reason, String source) {
		if (Config.config_ban_useCommand) {
			Bukkit.getServer().dispatchCommand(
					Bukkit.getConsoleSender(),
					Config.config_ban_commandFormat.replace("%name%", player.getName()).replace("%reason%", reason));
		} else {
			BanList bans = Bukkit.getServer().getBanList(Type.NAME);
			bans.addBan(player.getName(), reason, null, source);

			//kick
			if (player.isOnline()) {
				player.kickPlayer(reason);
			}
		}
	}

	public ItemStack getItemInHand(Player player, EquipmentSlot hand) {
		if (hand == EquipmentSlot.OFF_HAND) return player.getInventory().getItemInOffHand();
		return player.getInventory().getItemInMainHand();
	}

	public boolean claimIsPvPSafeZone(Claim claim) {
		if (claim.siegeData != null)
			return false;
		return claim.isAdminClaim() && claim.parent == null && Config.config_pvp_noCombatInAdminLandClaims ||
				claim.isAdminClaim() && claim.parent != null && Config.config_pvp_noCombatInAdminSubdivisions ||
				!claim.isAdminClaim() && Config.config_pvp_noCombatInPlayerLandClaims;
	}

	private void copyOldConfig() {
		final Path sourcePath = Paths.get("plugins/GriefPreventionData/config.yml");
		final Path newPath = Paths.get(getDataFolder()+"/config.yml");
		final File oldConfig = new File(sourcePath.toString());
		if(oldConfig.exists() && getConfig() != null){
			try {
				Files.copy(sourcePath, newPath);
			} catch (IOException e){
				getLogger().warning(e.getMessage());
				getLogger().warning("There was a problem copying the old config. Please copy manually.");
			}

		}
	}

	/*
    protected boolean isPlayerTrappedInPortal(Block block)
	{
		Material playerBlock = block.getType();
		if (playerBlock == Material.PORTAL)
			return true;
		//Most blocks you can "stand" inside but cannot pass through (isSolid) usually can be seen through (!isOccluding)
		//This can cause players to technically be considered not in a portal block, yet in reality is still stuck in the portal animation.
		if ((!playerBlock.isSolid() || playerBlock.isOccluding())) //If it is _not_ such a block,
		{
			//Check the block above
			playerBlock = block.getRelative(BlockFace.UP).getType();
			if ((!playerBlock.isSolid() || playerBlock.isOccluding()))
				return false; //player is not stuck
		}
		//Check if this block is also adjacent to a portal
		return block.getRelative(BlockFace.EAST).getType() == Material.PORTAL
				|| block.getRelative(BlockFace.WEST).getType() == Material.PORTAL
				|| block.getRelative(BlockFace.NORTH).getType() == Material.PORTAL
				|| block.getRelative(BlockFace.SOUTH).getType() == Material.PORTAL;
	}

	public void rescuePlayerTrappedInPortal(final Player player)
	{
		final Location oldLocation = player.getLocation();
		if (!isPlayerTrappedInPortal(oldLocation.getBlock()))
		{
			//Note that he 'escaped' the portal frame
			instance.portalReturnMap.remove(player.getUniqueId());
			instance.portalReturnTaskMap.remove(player.getUniqueId());
			return;
		}

		Location rescueLocation = portalReturnMap.get(player.getUniqueId());

		if (rescueLocation == null)
			return;

		//Temporarily store the old location, in case the player wishes to undo the rescue
		dataStore.getPlayerData(player.getUniqueId()).portalTrappedLocation = oldLocation;

		player.teleport(rescueLocation);
		sendMessage(player, TextMode.Info, Messages.RescuedFromPortalTrap);
		portalReturnMap.remove(player.getUniqueId());

		new BukkitRunnable()
		{
			public void run()
			{
				if (oldLocation == dataStore.getPlayerData(player.getUniqueId()).portalTrappedLocation)
					dataStore.getPlayerData(player.getUniqueId()).portalTrappedLocation = null;
			}
		}.runTaskLater(this, 600L);
	}
	*/

	//Track scheduled "rescues" so we can cancel them if the player happens to teleport elsewhere so we can cancel it.
	ConcurrentHashMap<UUID, BukkitTask> portalReturnTaskMap = new ConcurrentHashMap<UUID, BukkitTask>();

	public void startRescueTask(Player player, Location location) {
		//Schedule task to reset player's portal cooldown after 30 seconds (Maximum timeout time for client, in case their network is slow and taking forever to load chunks)
		BukkitTask task = new CheckForPortalTrapTask(player, this, location).runTaskLater(GriefPrevention.instance, 600L);

		//Cancel existing rescue task
		if (portalReturnTaskMap.containsKey(player.getUniqueId()))
			portalReturnTaskMap.put(player.getUniqueId(), task).cancel();
		else
			portalReturnTaskMap.put(player.getUniqueId(), task);
	}
}
