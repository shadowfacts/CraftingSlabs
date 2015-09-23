package net.shadowfacts.craftingslabs;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class GuiHandler implements IGuiHandler {

	public static final int CRAFTING = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) return new ContainerCrafting(player.inventory, world);

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) return new GuiCrafting(player.inventory, world);

		return null;
	}

}
