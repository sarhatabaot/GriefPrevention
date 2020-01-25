package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.griefprevention.Claim;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.Messages;
import me.ryanhamshire.griefprevention.TextMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@CommandAlias("trustlist")
public class TrustListCommand extends GPBaseCommand {
	public TrustListCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onTrustList(final Player player) {
		Claim claim = plugin.dataStore.getClaimAt(player.getLocation(), true, null);

		//if no claim here, error message
		if (claim == null) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.TrustListNoClaim);
			return;
		}

		//if no permission to manage permissions, error message
		String errorMessage = claim.allowGrantPermission(player);
		if (errorMessage != null) {
			GriefPrevention.sendMessage(player, TextMode.Err, errorMessage);
			return;
		}

		//otherwise build a list of explicit permissions by permission level
		//and send that to the player
		ArrayList<String> builders = new ArrayList<>();
		ArrayList<String> containers = new ArrayList<>();
		ArrayList<String> accessors = new ArrayList<>();
		ArrayList<String> managers = new ArrayList<>();
		claim.getPermissions(builders, containers, accessors, managers);

		GriefPrevention.sendMessage(player, TextMode.Info, Messages.TrustListHeader);

		StringBuilder permissions = new StringBuilder();
		permissions.append(ChatColor.GOLD + ">");

		if (!managers.isEmpty()) {
			for (String manager : managers) permissions.append(trustEntryToPlayerName(manager)).append(" ");
		}

		player.sendMessage(permissions.toString());
		permissions = new StringBuilder();
		permissions.append(ChatColor.YELLOW + ">");

		if (!builders.isEmpty()) {
			for (String builder : builders) permissions.append(trustEntryToPlayerName(builder)).append(" ");
		}

		player.sendMessage(permissions.toString());
		permissions = new StringBuilder();
		permissions.append(ChatColor.GREEN + ">");

		if (!containers.isEmpty()) {
			for (String container : containers) permissions.append(trustEntryToPlayerName(container)).append(" ");
		}

		player.sendMessage(permissions.toString());
		permissions = new StringBuilder();
		permissions.append(ChatColor.BLUE + ">");

		if (!accessors.isEmpty()) {
			for (String accessor : accessors) permissions.append(trustEntryToPlayerName(accessor)).append(" ");
		}

		player.sendMessage(permissions.toString());

		player.sendMessage(
				ChatColor.GOLD + plugin.dataStore.getMessage(Messages.Manage) + " " +
						ChatColor.YELLOW + plugin.dataStore.getMessage(Messages.Build) + " " +
						ChatColor.GREEN + plugin.dataStore.getMessage(Messages.Containers) + " " +
						ChatColor.BLUE + plugin.dataStore.getMessage(Messages.Access));

		if (claim.getSubclaimRestrictions()) {
			GriefPrevention.sendMessage(player, TextMode.Err, Messages.HasSubclaimRestriction);
		}
	}

	private String trustEntryToPlayerName(String entry) {
		if (entry.startsWith("[") || entry.equals("public")) {
			return entry;
		} else {
			return GriefPrevention.lookupPlayerName(entry);
		}
	}
}
