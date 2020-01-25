package me.ryanhamshire.GriefPrevention.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("gpblockinfo")
public class BlockInfoCommand extends GPBaseCommand {
	public BlockInfoCommand(final GriefPrevention plugin) {
		super(plugin);
	}

	@Default
	public void onBlockInfo(final Player player){
		ItemStack inHand = player.getInventory().getItemInMainHand();
		player.sendMessage("In Hand: " + String.format("%s(dValue:%s)", inHand.getType().name(), inHand.getData().getData()));

		Block inWorld = GriefPrevention.getTargetNonAirBlock(player, 300);
		player.sendMessage("In World: " + String.format("%s(dValue:%s)", inWorld.getType().name(), inWorld.getData()));
	}
}
