package net.shadowfacts.craftingslabs.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.shadowfacts.craftingslabs.container.ContainerCrafting;

/**
 * @author shadowfacts
 */
public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (GUIs.values()[ID]) {
			case CRAFTING:
				return new ContainerCrafting(player.inventory, world, new BlockPos(x, y, z));
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (GUIs.values()[ID]) {
			case CRAFTING:
				return new GUICrafting(player.inventory, world, new BlockPos(x, y, z));
			default:
				return null;
		}
	}

}
