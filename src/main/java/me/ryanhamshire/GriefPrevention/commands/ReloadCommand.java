package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gpreload")
public class ReloadCommand extends GPBaseCommand{
	public ReloadCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onReload(final CommandSender player){
		plugin.loadConfig();
		if(player != null) {
			GriefPrevention.sendMessage((Player) player, TextMode.Success, "Configuration updated.  If you have updated your Grief Prevention JAR, you still need to /reload or reboot your server.");
		}
		else {
			GriefPrevention.AddLogEntry("Configuration updated.  If you have updated your Grief Prevention JAR, you still need to /reload or reboot your server.");
		}
	}
}
