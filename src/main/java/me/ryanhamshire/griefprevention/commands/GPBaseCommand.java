package me.ryanhamshire.griefprevention.commands;

import co.aikar.commands.BaseCommand;
import me.ryanhamshire.griefprevention.GriefPrevention;

/**
 * @author sarhatabaot
 */
public class GPBaseCommand extends BaseCommand {
	protected GriefPrevention plugin;

	public GPBaseCommand(final GriefPrevention plugin) {
		this.plugin = plugin;
	}
}
