package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.CustomLogEntryTypes;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;


@CommandAlias("deleteclaimsinworld")
public class DeleteClaimsInWorldCommand extends GPBaseCommand {
	public DeleteClaimsInWorldCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onDelete(final ConsoleCommandSender consoleCommandSender, final String worldName) {

		//try to find the specified world
		World world = Bukkit.getServer().getWorld(worldName);
		if(world == null) {
			GriefPrevention.sendMessage(consoleCommandSender, TextMode.Err, Messages.WorldNotFound);
			return;
		}

		//delete all claims in that world
		plugin.dataStore.deleteClaimsInWorld(world, true);
		GriefPrevention.AddLogEntry("Deleted all claims in world: " + world.getName() + ".", CustomLogEntryTypes.AdminActivity);
	}
}
