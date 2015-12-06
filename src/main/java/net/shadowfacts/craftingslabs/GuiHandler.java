package net.shadowfacts.craftingslabs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.shadowfacts.craftingslabs.gui.ContainerCrafting;
import net.shadowfacts.craftingslabs.gui.GuiCrafting;

/**
 * @author shadowfacts
 */
public class GuiHandler implements IGuiHandler {

	public static final int CRAFTING = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) return new ContainerCrafting(player.inventory, world, new BlockPos(x, y, z));

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) return new GuiCrafting(player.inventory, world, new BlockPos(x, y, z));

		return null;
	}

}
