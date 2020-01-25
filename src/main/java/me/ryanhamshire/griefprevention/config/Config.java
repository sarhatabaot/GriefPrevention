package me.ryanhamshire.griefprevention.config;

import me.ryanhamshire.griefprevention.claim.ClaimsMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static me.ryanhamshire.griefprevention.GriefPrevention.addLogEntry;

/**
 * @author sarhatabaot
 */
public class Config {
	public static Map<World, ClaimsMode> config_claims_worldModes;
	public static boolean config_creativeWorldsExist;                     //note on whether there are any creative mode worlds, to save cpu cycles on a common hash lookup

	public static boolean config_claims_preventGlobalMonsterEggs; //whether monster eggs can be placed regardless of trust.
	public static boolean config_claims_preventTheft;                        //whether containers and crafting blocks are protectable
	public static boolean config_claims_protectCreatures;                    //whether claimed animals may be injured by players without permission
	public static boolean config_claims_protectHorses;                        //whether horses on a claim should be protected by that claim's rules
	public static boolean config_claims_protectDonkeys;                    //whether donkeys on a claim should be protected by that claim's rules
	public static boolean config_claims_protectLlamas;                        //whether llamas on a claim should be protected by that claim's rules
	public static boolean config_claims_preventButtonsSwitches;            //whether buttons and switches are protectable
	public static boolean config_claims_lockWoodenDoors;                    //whether wooden doors should be locked by default (require /accesstrust)
	public static boolean config_claims_lockTrapDoors;                        //whether trap doors should be locked by default (require /accesstrust)
	public static boolean config_claims_lockFenceGates;                    //whether fence gates should be locked by default (require /accesstrust)
	public static boolean config_claims_enderPearlsRequireAccessTrust;        //whether teleporting into a claim with a pearl requires access trust
	public static int config_claims_maxClaimsPerPlayer;                    //maximum number of claims per player
	public static boolean config_claims_respectWorldGuard;                 //whether claim creations requires WG build permission in creation area
	public static boolean config_claims_villagerTradingRequiresTrust;      //whether trading with a claimed villager requires permission

	public static int config_claims_initialBlocks;                            //the number of claim blocks a new player starts with
	public static double config_claims_abandonReturnRatio;                 //the portion of claim blocks returned to a player when a claim is abandoned
	public static int config_claims_blocksAccruedPerHour_default;            //how many additional blocks players get each hour of play (can be zero) without any special permissions
	public static int config_claims_maxAccruedBlocks_default;                //the limit on accrued blocks (over time) for players without any special permissions.  doesn't limit purchased or admin-gifted blocks
	public static int config_claims_accruedIdleThreshold;                    //how far (in blocks) a player must move in order to not be considered afk/idle when determining accrued claim blocks
	public static int config_claims_accruedIdlePercent;                    //how much percentage of claim block accruals should idle players get
	public static int config_claims_maxDepth;                                //limit on how deep claims can go
	public static int config_claims_expirationDays;                        //how many days of inactivity before a player loses his claims
	public static int config_claims_expirationExemptionTotalBlocks;        //total claim blocks amount which will exempt a player from claim expiration
	public static int config_claims_expirationExemptionBonusBlocks;        //bonus claim blocks amount which will exempt a player from claim expiration

	public static int config_claims_automaticClaimsForNewPlayersRadius;    //how big automatic new player claims (when they place a chest) should be.  0 to disable
	public static int config_claims_claimsExtendIntoGroundDistance;        //how far below the shoveled block a new claim will reach
	public static int config_claims_minWidth;                                //minimum width for non-admin claims
	public static int config_claims_minArea;                               //minimum area for non-admin claims

	public static int config_claims_chestClaimExpirationDays;                //number of days of inactivity before an automatic chest claim will be deleted
	public static int config_claims_unusedClaimExpirationDays;                //number of days of inactivity before an unused (nothing build) claim will be deleted
	public static boolean config_claims_survivalAutoNatureRestoration;        //whether survival claims will be automatically restored to nature when auto-deleted
	public static boolean config_claims_allowTrappedInAdminClaims;            //whether it should be allowed to use /trapped in adminclaims.

	public static Material config_claims_investigationTool;                //which material will be used to investigate claims with a right click
	public static Material config_claims_modificationTool;                    //which material will be used to create/resize claims with a right click

	public static List<String> config_claims_commandsRequiringAccessTrust; //the list of slash commands requiring access trust when in a claim
	public static boolean config_claims_supplyPlayerManual;                //whether to give new players a book with land claim help in it
	public static int config_claims_manualDeliveryDelaySeconds;            //how long to wait before giving a book to a new player

	public static boolean config_claims_firespreads;                        //whether fire will spread in claims
	public static boolean config_claims_firedamages;                        //whether fire will damage in claims

	public static boolean config_claims_lecternReadingRequiresAccessTrust;                    //reading lecterns requires access trust

	public static List<World> config_siege_enabledWorlds;                //whether or not /siege is enabled on this server
	public static List<Material> config_siege_blocks;                    //which blocks will be breakable in siege mode
	public static int config_siege_doorsOpenSeconds;  // how before claim is re-secured after siege win
	public static int config_siege_cooldownEndInMinutes;
	public static boolean config_spam_enabled;                                //whether or not to monitor for spam
	public static int config_spam_loginCooldownSeconds;                    //how long players must wait between logins.  combats login spam.
	public static int config_spam_loginLogoutNotificationsPerMinute;        //how many login/logout notifications to show per minute (global, not per player)
	public static List<String> config_spam_monitorSlashCommands;    //the list of slash commands monitored for spam
	public static boolean config_spam_banOffenders;                        //whether or not to ban spammers automatically
	public static String config_spam_banMessage;                            //message to show an automatically banned player
	public static String config_spam_warningMessage;                        //message to show a player who is close to spam level
	public static String config_spam_allowedIpAddresses;                    //IP addresses which will not be censored
	public static int config_spam_deathMessageCooldownSeconds;                //cooldown period for death messages (per player) in seconds
	public static int config_spam_logoutMessageDelaySeconds;               //delay before a logout message will be shown (only if the player stays offline that long)

	public static Map<World, Boolean> config_pvp_specifiedWorlds;                //list of worlds where pvp anti-grief rules apply, according to the config file
	public static boolean config_pvp_protectFreshSpawns;                    //whether to make newly spawned players immune until they pick up an item
	public static boolean config_pvp_punishLogout;                            //whether to kill players who log out during PvP combat
	public static int config_pvp_combatTimeoutSeconds;                        //how long combat is considered to continue after the most recent damage
	public static boolean config_pvp_allowCombatItemDrop;                    //whether a player can drop items during combat to hide them
	public static List<String> config_pvp_blockedCommands;            //list of commands which may not be used during pvp combat
	public static boolean config_pvp_noCombatInPlayerLandClaims;            //whether players may fight in player-owned land claims
	public static boolean config_pvp_noCombatInAdminLandClaims;            //whether players may fight in admin-owned land claims
	public static boolean config_pvp_noCombatInAdminSubdivisions;          //whether players may fight in subdivisions of admin-owned land claims
	public static boolean config_pvp_allowLavaNearPlayers;                 //whether players may dump lava near other players in pvp worlds
	public static boolean config_pvp_allowLavaNearPlayers_NonPvp;            //whather this applies in non-PVP rules worlds <ArchdukeLiamus>
	public static boolean config_pvp_allowFireNearPlayers;                 //whether players may start flint/steel fires near other players in pvp worlds
	public static boolean config_pvp_allowFireNearPlayers_NonPvp;            //whether this applies in non-PVP rules worlds <ArchdukeLiamus>
	public static boolean config_pvp_protectPets;                          //whether players may damage pets outside of land claims in pvp worlds

	public static boolean config_lockDeathDropsInPvpWorlds;                //whether players' dropped on death items are protected in pvp worlds
	public static boolean config_lockDeathDropsInNonPvpWorlds;             //whether players' dropped on death items are protected in non-pvp worlds

	public static double config_economy_claimBlocksPurchaseCost;            //cost to purchase a claim block.  set to zero to disable purchase.
	public static double config_economy_claimBlocksSellValue;                //return on a sold claim block.  set to zero to disable sale.

	public static boolean config_blockClaimExplosions;                     //whether explosions may destroy claimed blocks
	public static boolean config_blockSurfaceCreeperExplosions;            //whether creeper explosions near or above the surface destroy blocks
	public static boolean config_blockSurfaceOtherExplosions;                //whether non-creeper explosions near or above the surface destroy blocks
	public static boolean config_blockSkyTrees;                            //whether players can build trees on platforms in the sky

	public static boolean config_fireSpreads;                                //whether fire spreads outside of claims
	public static boolean config_fireDestroys;                                //whether fire destroys blocks outside of claims

	public static boolean config_whisperNotifications;                    //whether whispered messages will broadcast to administrators in game
	public static boolean config_signNotifications;                        //whether sign content will broadcast to administrators in game
	public static List<String> config_eavesdrop_whisperCommands;        //list of whisper commands to eavesdrop on

	public static boolean config_smartBan;                                    //whether to ban accounts which very likely owned by a banned player

	public static boolean config_endermenMoveBlocks;                        //whether or not endermen may move blocks around
	public static boolean config_claims_ravagersBreakBlocks;                //whether or not ravagers may break blocks in claims
	public static boolean config_silverfishBreakBlocks;                    //whether silverfish may break blocks
	public static boolean config_creaturesTrampleCrops;                    //whether or not non-player entities may trample crops
	public static boolean config_rabbitsEatCrops;                          //whether or not rabbits may eat crops
	public static boolean config_zombiesBreakDoors;                        //whether or not hard-mode zombies may break down wooden doors

	public static int config_ipLimit;                                      //how many players can share an IP address

	public static boolean config_trollFilterEnabled;                       //whether to auto-mute new players who use banned words right after joining

	public static Map<String, Integer> config_seaLevelOverride;        //override for sea level, because bukkit doesn't report the right value for all situations

	public static boolean config_limitTreeGrowth;                          //whether trees should be prevented from growing into a claim from outside
	public static boolean config_checkPistonMovement;                      //whether to check piston movement
	public static boolean config_pistonsInClaimsOnly;                      //whether pistons are limited to only move blocks located within the piston's land claim

	public static boolean config_advanced_fixNegativeClaimblockAmounts;    //whether to attempt to fix negative claim block amounts (some addons cause/assume players can go into negative amounts)
	public static int config_advanced_claim_expiration_check_rate;            //How often GP should check for expired claims, amount in seconds
	public static int config_advanced_offlineplayer_cache_days;            //Cache players who have logged in within the last x number of days

	//custom log settings
	public static int config_logs_daysToKeep;
	public static boolean config_logs_socialEnabled;
	public static boolean config_logs_suspiciousEnabled;
	public static boolean config_logs_adminEnabled;
	public static boolean config_logs_debugEnabled;
	public static boolean config_logs_mutedChatEnabled;

	//ban management plugin interop settings
	public static boolean config_ban_useCommand;
	public static String config_ban_commandFormat;

	public static String databaseUrl;
	public static String databaseUserName;
	public static String databasePassword;

	public static void init(final JavaPlugin plugin){
		final FileConfiguration config = plugin.getConfig();
		//read configuration settings (note defaults)

		//get (deprecated node) claims world names from the config file
		List<World> worlds = plugin.getServer().getWorlds();
		List<String> deprecated_claimsEnabledWorldNames = config.getStringList("GriefPrevention.Claims.Worlds");

		//validate that list
		for (int i = 0; i < deprecated_claimsEnabledWorldNames.size(); i++) {
			String worldName = deprecated_claimsEnabledWorldNames.get(i);
			World world = plugin.getServer().getWorld(worldName);
			if (world == null) {
				deprecated_claimsEnabledWorldNames.remove(i--);
			}
		}

		//get (deprecated node) creative world names from the config file
		List<String> deprecated_creativeClaimsEnabledWorldNames = config.getStringList("GriefPrevention.Claims.CreativeRulesWorlds");

		//validate that list
		for (int i = 0; i < deprecated_creativeClaimsEnabledWorldNames.size(); i++) {
			String worldName = deprecated_creativeClaimsEnabledWorldNames.get(i);
			World world = plugin.getServer().getWorld(worldName);
			if (world == null) {
				deprecated_claimsEnabledWorldNames.remove(i--);
			}
		}

		//get (deprecated) pvp fire placement proximity note and use it if it exists (in the new config format it will be overwritten later).
		config_pvp_allowFireNearPlayers = config.getBoolean("GriefPrevention.PvP.AllowFlintAndSteelNearOtherPlayers", false);
		//get (deprecated) pvp lava dump proximity note and use it if it exists (in the new config format it will be overwritten later).
		config_pvp_allowLavaNearPlayers = config.getBoolean("GriefPrevention.PvP.AllowLavaDumpingNearOtherPlayers", false);

		//decide claim mode for each world
		config_claims_worldModes = new ConcurrentHashMap<>();
		config_creativeWorldsExist = false;

		config_claims_preventGlobalMonsterEggs = config.getBoolean("GriefPrevention.Claims.PreventGlobalMonsterEggs", true);
		config_claims_preventTheft = config.getBoolean("GriefPrevention.Claims.PreventTheft", true);
		config_claims_protectCreatures = config.getBoolean("GriefPrevention.Claims.ProtectCreatures", true);
		config_claims_protectHorses = config.getBoolean("GriefPrevention.Claims.ProtectHorses", true);
		config_claims_protectDonkeys = config.getBoolean("GriefPrevention.Claims.ProtectDonkeys", true);
		config_claims_protectLlamas = config.getBoolean("GriefPrevention.Claims.ProtectLlamas", true);
		config_claims_preventButtonsSwitches = config.getBoolean("GriefPrevention.Claims.PreventButtonsSwitches", true);
		config_claims_lockWoodenDoors = config.getBoolean("GriefPrevention.Claims.LockWoodenDoors", false);
		config_claims_lockTrapDoors = config.getBoolean("GriefPrevention.Claims.LockTrapDoors", false);
		config_claims_lockFenceGates = config.getBoolean("GriefPrevention.Claims.LockFenceGates", true);
		config_claims_enderPearlsRequireAccessTrust = config.getBoolean("GriefPrevention.Claims.EnderPearlsRequireAccessTrust", true);
		config_claims_initialBlocks = config.getInt("GriefPrevention.Claims.InitialBlocks", 100);
		config_claims_blocksAccruedPerHour_default = config.getInt("GriefPrevention.Claims.BlocksAccruedPerHour", 100);
		config_claims_blocksAccruedPerHour_default = config.getInt("GriefPrevention.Claims.Claim Blocks Accrued Per Hour.Default", config_claims_blocksAccruedPerHour_default);
		config_claims_maxAccruedBlocks_default = config.getInt("GriefPrevention.Claims.MaxAccruedBlocks", 2000);
		config_claims_maxAccruedBlocks_default = config.getInt("GriefPrevention.Claims.Max Accrued Claim Blocks.Default", config_claims_maxAccruedBlocks_default);
		config_claims_accruedIdleThreshold = config.getInt("GriefPrevention.Claims.AccruedIdleThreshold", 0);
		config_claims_accruedIdleThreshold = config.getInt("GriefPrevention.Claims.Accrued Idle Threshold", config_claims_accruedIdleThreshold);
		config_claims_accruedIdlePercent = config.getInt("GriefPrevention.Claims.AccruedIdlePercent", 0);
		config_claims_abandonReturnRatio = config.getDouble("GriefPrevention.Claims.AbandonReturnRatio", 1.0D);
		config_claims_automaticClaimsForNewPlayersRadius = config.getInt("GriefPrevention.Claims.AutomaticNewPlayerClaimsRadius", 4);
		config_claims_claimsExtendIntoGroundDistance = Math.abs(config.getInt("GriefPrevention.Claims.ExtendIntoGroundDistance", 5));
		config_claims_minWidth = config.getInt("GriefPrevention.Claims.MinimumWidth", 5);
		config_claims_minArea = config.getInt("GriefPrevention.Claims.MinimumArea", 100);
		config_claims_maxDepth = config.getInt("GriefPrevention.Claims.MaximumDepth", 0);
		config_claims_chestClaimExpirationDays = config.getInt("GriefPrevention.Claims.Expiration.ChestClaimDays", 7);
		config_claims_unusedClaimExpirationDays = config.getInt("GriefPrevention.Claims.Expiration.UnusedClaimDays", 14);
		config_claims_expirationDays = config.getInt("GriefPrevention.Claims.Expiration.AllClaims.DaysInactive", 60);
		config_claims_expirationExemptionTotalBlocks = config.getInt("GriefPrevention.Claims.Expiration.AllClaims.ExceptWhenOwnerHasTotalClaimBlocks", 10000);
		config_claims_expirationExemptionBonusBlocks = config.getInt("GriefPrevention.Claims.Expiration.AllClaims.ExceptWhenOwnerHasBonusClaimBlocks", 5000);
		config_claims_survivalAutoNatureRestoration = config.getBoolean("GriefPrevention.Claims.Expiration.AutomaticNatureRestoration.SurvivalWorlds", false);
		config_claims_allowTrappedInAdminClaims = config.getBoolean("GriefPrevention.Claims.AllowTrappedInAdminClaims", false);

		config_claims_maxClaimsPerPlayer = config.getInt("GriefPrevention.Claims.MaximumNumberOfClaimsPerPlayer", 0);
		config_claims_respectWorldGuard = config.getBoolean("GriefPrevention.Claims.CreationRequiresWorldGuardBuildPermission", true);
		config_claims_villagerTradingRequiresTrust = config.getBoolean("GriefPrevention.Claims.VillagerTradingRequiresPermission", true);
		String accessTrustSlashCommands = config.getString("GriefPrevention.Claims.CommandsRequiringAccessTrust", "/sethome");
		config_claims_supplyPlayerManual = config.getBoolean("GriefPrevention.Claims.DeliverManuals", true);
		config_claims_manualDeliveryDelaySeconds = config.getInt("GriefPrevention.Claims.ManualDeliveryDelaySeconds", 30);
		config_claims_ravagersBreakBlocks = config.getBoolean("GriefPrevention.Claims.RavagersBreakBlocks", true);

		config_claims_firespreads = config.getBoolean("GriefPrevention.Claims.FireSpreadsInClaims", false);
		config_claims_firedamages = config.getBoolean("GriefPrevention.Claims.FireDamagesInClaims", false);
		config_claims_lecternReadingRequiresAccessTrust = config.getBoolean("GriefPrevention.Claims.LecternReadingRequiresAccessTrust", true);

		config_spam_enabled = config.getBoolean("GriefPrevention.Spam.Enabled", true);
		config_spam_loginCooldownSeconds = config.getInt("GriefPrevention.Spam.LoginCooldownSeconds", 60);
		config_spam_loginLogoutNotificationsPerMinute = config.getInt("GriefPrevention.Spam.LoginLogoutNotificationsPerMinute", 5);
		config_spam_warningMessage = config.getString("GriefPrevention.Spam.WarningMessage", "Please reduce your noise level.  Spammers will be banned.");
		config_spam_allowedIpAddresses = config.getString("GriefPrevention.Spam.AllowedIpAddresses", "1.2.3.4; 5.6.7.8");
		config_spam_banOffenders = config.getBoolean("GriefPrevention.Spam.BanOffenders", true);
		config_spam_banMessage = config.getString("GriefPrevention.Spam.BanMessage", "Banned for spam.");
		String slashCommandsToMonitor = config.getString("GriefPrevention.Spam.MonitorSlashCommands", "/me;/global;/local");
		slashCommandsToMonitor = config.getString("GriefPrevention.Spam.ChatSlashCommands", slashCommandsToMonitor);
		config_spam_deathMessageCooldownSeconds = config.getInt("GriefPrevention.Spam.DeathMessageCooldownSeconds", 120);
		config_spam_logoutMessageDelaySeconds = config.getInt("GriefPrevention.Spam.Logout Message Delay In Seconds", 0);

		config_pvp_protectFreshSpawns = config.getBoolean("GriefPrevention.PvP.ProtectFreshSpawns", true);
		config_pvp_punishLogout = config.getBoolean("GriefPrevention.PvP.PunishLogout", true);
		config_pvp_combatTimeoutSeconds = config.getInt("GriefPrevention.PvP.CombatTimeoutSeconds", 15);
		config_pvp_allowCombatItemDrop = config.getBoolean("GriefPrevention.PvP.AllowCombatItemDrop", false);
		String bannedPvPCommandsList = config.getString("GriefPrevention.PvP.BlockedSlashCommands", "/home;/vanish;/spawn;/tpa");

		config_economy_claimBlocksPurchaseCost = config.getDouble("GriefPrevention.Economy.ClaimBlocksPurchaseCost", 0);
		config_economy_claimBlocksSellValue = config.getDouble("GriefPrevention.Economy.ClaimBlocksSellValue", 0);

		config_lockDeathDropsInPvpWorlds = config.getBoolean("GriefPrevention.ProtectItemsDroppedOnDeath.PvPWorlds", false);
		config_lockDeathDropsInNonPvpWorlds = config.getBoolean("GriefPrevention.ProtectItemsDroppedOnDeath.NonPvPWorlds", true);

		config_blockClaimExplosions = config.getBoolean("GriefPrevention.BlockLandClaimExplosions", true);
		config_blockSurfaceCreeperExplosions = config.getBoolean("GriefPrevention.BlockSurfaceCreeperExplosions", true);
		config_blockSurfaceOtherExplosions = config.getBoolean("GriefPrevention.BlockSurfaceOtherExplosions", true);
		config_blockSkyTrees = config.getBoolean("GriefPrevention.LimitSkyTrees", true);
		config_limitTreeGrowth = config.getBoolean("GriefPrevention.LimitTreeGrowth", false);
		config_checkPistonMovement = config.getBoolean("GriefPrevention.CheckPistonMovement", true);
		config_pistonsInClaimsOnly = config.getBoolean("GriefPrevention.LimitPistonsToLandClaims", true);

		config_fireSpreads = config.getBoolean("GriefPrevention.FireSpreads", false);
		config_fireDestroys = config.getBoolean("GriefPrevention.FireDestroys", false);

		config_whisperNotifications = config.getBoolean("GriefPrevention.AdminsGetWhispers", true);
		config_signNotifications = config.getBoolean("GriefPrevention.AdminsGetSignNotifications", true);
		String whisperCommandsToMonitor = config.getString("GriefPrevention.WhisperCommands", "/tell;/pm;/r;/whisper;/msg");
		whisperCommandsToMonitor = config.getString("GriefPrevention.Spam.WhisperSlashCommands", whisperCommandsToMonitor);

		config_smartBan = config.getBoolean("GriefPrevention.SmartBan", true);
		config_trollFilterEnabled = config.getBoolean("GriefPrevention.Mute New Players Using Banned Words", true);
		config_ipLimit = config.getInt("GriefPrevention.MaxPlayersPerIpAddress", 3);

		config_endermenMoveBlocks = config.getBoolean("GriefPrevention.EndermenMoveBlocks", false);
		config_silverfishBreakBlocks = config.getBoolean("GriefPrevention.SilverfishBreakBlocks", false);
		config_creaturesTrampleCrops = config.getBoolean("GriefPrevention.CreaturesTrampleCrops", false);
		config_rabbitsEatCrops = config.getBoolean("GriefPrevention.RabbitsEatCrops", true);
		config_zombiesBreakDoors = config.getBoolean("GriefPrevention.HardModeZombiesBreakDoors", false);
		config_ban_useCommand = config.getBoolean("GriefPrevention.UseBanCommand", false);
		config_ban_commandFormat = config.getString("GriefPrevention.BanCommandPattern", "ban %name% %reason%");

		//default for claim investigation tool
		String investigationToolMaterialName = Material.STICK.name();

		//get investigation tool from config
		investigationToolMaterialName = config.getString("GriefPrevention.Claims.InvestigationTool", investigationToolMaterialName);

		//validate investigation tool
		config_claims_investigationTool = Material.getMaterial(investigationToolMaterialName);
		if (config_claims_investigationTool == null) {
			addLogEntry("ERROR: Material " + investigationToolMaterialName + " not found.  Defaulting to the stick.  Please update your config.yml.");
			config_claims_investigationTool = Material.STICK;
		}

		//default for claim creation/modification tool
		String modificationToolMaterialName = Material.GOLDEN_SHOVEL.name();

		//get modification tool from config
		modificationToolMaterialName = config.getString("GriefPrevention.Claims.ModificationTool", modificationToolMaterialName);

		//validate modification tool
		config_claims_modificationTool = Material.getMaterial(modificationToolMaterialName);
		if (config_claims_modificationTool == null) {
			addLogEntry("ERROR: Material " + modificationToolMaterialName + " not found.  Defaulting to the golden shovel.  Please update your config.yml.");
			config_claims_modificationTool = Material.GOLDEN_SHOVEL;
		}

		//default for siege worlds list
		ArrayList<String> defaultSiegeWorldNames = new ArrayList<>();

		//get siege world names from the config file
		List<String> siegeEnabledWorldNames = config.getStringList("GriefPrevention.Siege.Worlds");

		//validate that list
		config_siege_enabledWorlds = new ArrayList<>();
		for (String worldName : siegeEnabledWorldNames) {
			World world = plugin.getServer().getWorld(worldName);
			if (world == null) {
				addLogEntry("Error: Siege Configuration: There's no world named \"" + worldName + "\".  Please update your config.yml.");
			} else {
				config_siege_enabledWorlds.add(world);
			}
		}

		//default siege blocks
		config_siege_blocks = new ArrayList<>();
		config_siege_blocks.add(Material.DIRT);
		config_siege_blocks.add(Material.GRASS_BLOCK);
		config_siege_blocks.add(Material.GRASS);
		config_siege_blocks.add(Material.FERN);
		config_siege_blocks.add(Material.DEAD_BUSH);
		config_siege_blocks.add(Material.COBBLESTONE);
		config_siege_blocks.add(Material.GRAVEL);
		config_siege_blocks.add(Material.SAND);
		config_siege_blocks.add(Material.GLASS);
		config_siege_blocks.add(Material.GLASS_PANE);
		config_siege_blocks.add(Material.OAK_PLANKS);
		config_siege_blocks.add(Material.SPRUCE_PLANKS);
		config_siege_blocks.add(Material.BIRCH_PLANKS);
		config_siege_blocks.add(Material.JUNGLE_PLANKS);
		config_siege_blocks.add(Material.ACACIA_PLANKS);
		config_siege_blocks.add(Material.DARK_OAK_PLANKS);
		config_siege_blocks.add(Material.WHITE_WOOL);
		config_siege_blocks.add(Material.ORANGE_WOOL);
		config_siege_blocks.add(Material.MAGENTA_WOOL);
		config_siege_blocks.add(Material.LIGHT_BLUE_WOOL);
		config_siege_blocks.add(Material.YELLOW_WOOL);
		config_siege_blocks.add(Material.LIME_WOOL);
		config_siege_blocks.add(Material.PINK_WOOL);
		config_siege_blocks.add(Material.GRAY_WOOL);
		config_siege_blocks.add(Material.LIGHT_GRAY_WOOL);
		config_siege_blocks.add(Material.CYAN_WOOL);
		config_siege_blocks.add(Material.PURPLE_WOOL);
		config_siege_blocks.add(Material.BLUE_WOOL);
		config_siege_blocks.add(Material.BROWN_WOOL);
		config_siege_blocks.add(Material.GREEN_WOOL);
		config_siege_blocks.add(Material.RED_WOOL);
		config_siege_blocks.add(Material.BLACK_WOOL);
		config_siege_blocks.add(Material.SNOW);

		//build a default config entry
		ArrayList<String> defaultBreakableBlocksList = new ArrayList<>();
		for (Material config_siege_block : config_siege_blocks) {
			defaultBreakableBlocksList.add(config_siege_block.name());
		}

		//try to load the list from the config file
		List<String> breakableBlocksList = config.getStringList("GriefPrevention.Siege.BreakableBlocks");

		//if it fails, use default list instead
		if (breakableBlocksList.size() == 0) {
			breakableBlocksList = defaultBreakableBlocksList;
		}

		//parse the list of siege-breakable blocks
		config_siege_blocks = new ArrayList<>();
		for (String blockName : breakableBlocksList) {
			Material material = Material.getMaterial(blockName);
			if (material == null) {
				addLogEntry("Siege Configuration: Material not found: " + blockName + ".");
			} else {
				config_siege_blocks.add(material);
			}
		}

		config_siege_doorsOpenSeconds = config.getInt("GriefPrevention.Siege.DoorsOpenDelayInSeconds", 5 * 60);
		config_siege_cooldownEndInMinutes = config.getInt("GriefPrevention.Siege.CooldownEndInMinutes", 60);
		config_pvp_noCombatInPlayerLandClaims = config.getBoolean("GriefPrevention.PvP.ProtectPlayersInLandClaims.PlayerOwnedClaims", config_siege_enabledWorlds.size() == 0);
		config_pvp_noCombatInAdminLandClaims = config.getBoolean("GriefPrevention.PvP.ProtectPlayersInLandClaims.AdministrativeClaims", config_siege_enabledWorlds.size() == 0);
		config_pvp_noCombatInAdminSubdivisions = config.getBoolean("GriefPrevention.PvP.ProtectPlayersInLandClaims.AdministrativeSubdivisions", config_siege_enabledWorlds.size() == 0);
		config_pvp_allowLavaNearPlayers = config.getBoolean("GriefPrevention.PvP.AllowLavaDumpingNearOtherPlayers.PvPWorlds", true);
		config_pvp_allowLavaNearPlayers_NonPvp = config.getBoolean("GriefPrevention.PvP.AllowLavaDumpingNearOtherPlayers.NonPvPWorlds", false);
		config_pvp_allowFireNearPlayers = config.getBoolean("GriefPrevention.PvP.AllowFlintAndSteelNearOtherPlayers.PvPWorlds", true);
		config_pvp_allowFireNearPlayers_NonPvp = config.getBoolean("GriefPrevention.PvP.AllowFlintAndSteelNearOtherPlayers.NonPvPWorlds", false);
		config_pvp_protectPets = config.getBoolean("GriefPrevention.PvP.ProtectPetsOutsideLandClaims", false);

		//optional database settings
		databaseUrl = config.getString("GriefPrevention.Database.URL", "");
		databaseUserName = config.getString("GriefPrevention.Database.UserName", "");
		databasePassword = config.getString("GriefPrevention.Database.Password", "");

		config_advanced_fixNegativeClaimblockAmounts = config.getBoolean("GriefPrevention.Advanced.fixNegativeClaimblockAmounts", true);
		config_advanced_claim_expiration_check_rate = config.getInt("GriefPrevention.Advanced.ClaimExpirationCheckRate", 60);
		config_advanced_offlineplayer_cache_days = config.getInt("GriefPrevention.Advanced.OfflinePlayer_cache_days", 90);

		//custom logger settings
		config_logs_daysToKeep = config.getInt("GriefPrevention.Abridged Logs.Days To Keep", 7);
		config_logs_socialEnabled = config.getBoolean("GriefPrevention.Abridged Logs.Included Entry Types.Social Activity", true);
		config_logs_suspiciousEnabled = config.getBoolean("GriefPrevention.Abridged Logs.Included Entry Types.Suspicious Activity", true);
		config_logs_adminEnabled = config.getBoolean("GriefPrevention.Abridged Logs.Included Entry Types.Administrative Activity", false);
		config_logs_debugEnabled = config.getBoolean("GriefPrevention.Abridged Logs.Included Entry Types.Debug", false);
		config_logs_mutedChatEnabled = config.getBoolean("GriefPrevention.Abridged Logs.Included Entry Types.Muted Chat Messages", false);

		//try to parse the list of commands requiring access trust in land claims
		config_claims_commandsRequiringAccessTrust = new ArrayList<>();
		String[] commands = accessTrustSlashCommands.split(";");
		for (final String command : commands) {
			if (!command.isEmpty()) {
				config_claims_commandsRequiringAccessTrust.add(command.trim().toLowerCase());
			}
		}

		//try to parse the list of commands which should be monitored for spam
		config_spam_monitorSlashCommands = new ArrayList<>();
		commands = slashCommandsToMonitor.split(";");
		for (final String command : commands) {
			config_spam_monitorSlashCommands.add(command.trim().toLowerCase());
		}

		//try to parse the list of commands which should be included in eavesdropping
		config_eavesdrop_whisperCommands = new ArrayList<>();
		commands = whisperCommandsToMonitor.split(";");
		for (final String command : commands) {
			config_eavesdrop_whisperCommands.add(command.trim().toLowerCase());
		}

		//try to parse the list of commands which should be banned during pvp combat
		config_pvp_blockedCommands = new ArrayList<>();
		commands = bannedPvPCommandsList.split(";");
		for (final String command : commands) {
			config_pvp_blockedCommands.add(command.trim().toLowerCase());
		}
	}
}
