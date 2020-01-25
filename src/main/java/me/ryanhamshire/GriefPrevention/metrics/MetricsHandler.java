package me.ryanhamshire.GriefPrevention.metrics;

import me.ryanhamshire.GriefPrevention.ClaimsMode;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.config.Config;
import org.bukkit.World;

import java.util.concurrent.Callable;

/**
 * Created on 9/22/2018.
 *
 * @author RoboMWM
 */
public class MetricsHandler
{
    private Metrics metrics;
    public MetricsHandler(GriefPrevention plugin, String dataMode)
    {
        metrics = new Metrics(plugin);

        try
        {
            addSimplePie("custom_build", plugin.getDescription().getVersion().equals("15.2.2"));
            addSimplePie("bukkit_impl", plugin.getServer().getVersion().split("-")[1]);
        }
        catch (Throwable ignored){}

        //enums and etc. would be amazing.

        addSimplePie("lock_death_drops_pvp", Config.config_lockDeathDropsInPvpWorlds);
        addSimplePie("lock_death_drops_nonpvp", Config.config_lockDeathDropsInNonPvpWorlds);

        //PvP - only send PvP configs for those who use them
        boolean pvpApplies = false;
        for (World world : plugin.getServer().getWorlds())
        {
            if (plugin.pvpRulesApply(world))
            {
                addSimplePie("no_pvp_in_player_claims", Config.config_pvp_noCombatInPlayerLandClaims);
                addSimplePie("protect_pets_pvp", Config.config_pvp_protectPets);
                addSimplePie("protect_fresh_spawns_pvp", Config.config_pvp_protectFreshSpawns);
                pvpApplies = true;
                break;
            }
        }

        addSimplePie("uses_pvp", pvpApplies);

        //spam
        addSimplePie("uses_spam", Config.config_spam_enabled);
        if (Config.config_spam_enabled)
        {
            addSimplePie("ban_spam_offenders", Config.config_spam_banOffenders);
            addSimplePie("use_ban_command", Config.config_ban_useCommand);
        }

        //Used for claims?
        boolean claimsEnabled = false;
        for (ClaimsMode mode : Config.config_claims_worldModes.values())
        {
            if (mode != ClaimsMode.Disabled)
            {
                claimsEnabled = true;
                break;
            }
        }

        addSimplePie("uses_claims", claimsEnabled);

        //Don't send any claim/nature-related configs if claim protections aren't used at all
        if (!claimsEnabled)
            return;

        //How many people want vanilla fire behavior?
        addSimplePie("fire_spreads", Config.config_fireSpreads);
        addSimplePie("fire_destroys", Config.config_fireDestroys);

        //Everything that is wooden should be accessible by default?
        addSimplePie("lock_wooden_doors", Config.config_claims_lockWoodenDoors);
        addSimplePie("lock_fence_gates", Config.config_claims_lockFenceGates);
        addSimplePie("lock_trapdoors", Config.config_claims_lockTrapDoors);

        addSimplePie("protect_horses", Config.config_claims_protectHorses);
        addSimplePie("protect_donkeys", Config.config_claims_protectDonkeys);
        addSimplePie("protect_llamas", Config.config_claims_protectLlamas);

        addSimplePie("prevent_buttons_switches", Config.config_claims_preventButtonsSwitches);
        addSimplePie("villager_trading_requires_trust", Config.config_claims_villagerTradingRequiresTrust);

        //CPU-intensive options
        addSimplePie("survival_nature_restoration", Config.config_claims_survivalAutoNatureRestoration);
        addSimplePie("block_sky_trees", Config.config_blockSkyTrees);
        addSimplePie("limit_tree_growth", Config.config_limitTreeGrowth);

        addSimplePie("pistons_only_work_in_claims", Config.config_pistonsInClaimsOnly);
        addSimplePie("creatures_trample_crops", Config.config_creaturesTrampleCrops);

        addSimplePie("claim_tool", Config.config_claims_modificationTool.name());
        addSimplePie("claim_inspect_tool", Config.config_claims_investigationTool.name());

        addSimplePie("block_surface_creeper_explosions", Config.config_blockSurfaceCreeperExplosions);
        addSimplePie("block_surface_other_explosions", Config.config_blockSurfaceOtherExplosions);
        addSimplePie("endermen_move_blocks", Config.config_endermenMoveBlocks);

        addSimplePie("storage_mode", dataMode);

        //siege
        addSimplePie("uses_siege", !Config.config_siege_enabledWorlds.isEmpty());
    }

    private void addSimplePie(String id, boolean value)
    {
        addSimplePie(id, Boolean.toString(value));
    }

    private void addSimplePie(String id, String value)
    {
        metrics.addCustomChart(new Metrics.SimplePie(id, new Callable<String>()
        {
            @Override
            public String call() throws Exception
            {
                return value;
            }
        }));
    }
}
