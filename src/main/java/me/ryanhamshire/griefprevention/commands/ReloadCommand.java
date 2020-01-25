package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.TextMode;
import me.ryanhamshire.griefprevention.config.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gpreload")
public class ReloadCommand extends GPBaseCommand{
	public ReloadCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onReload(final CommandSender player){
		Config.init(plugin);
		if(player != null) {
			GriefPrevention.sendMessage((Player) player, TextMode.Success, "Configuration updated.  If you have updated your Grief Prevention JAR, you still need to /reload or reboot your server.");
		}
		else {
			GriefPrevention.AddLogEntry("Configuration updated.  If you have updated your Grief Prevention JAR, you still need to /reload or reboot your server.");
		}
	}
}
