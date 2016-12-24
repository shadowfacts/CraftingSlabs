package net.shadowfacts.craftingslabs.gui;

import net.minecraft.block.BlockSlab;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.shadowfacts.craftingslabs.container.ContainerCrafting;
import net.shadowfacts.craftingslabs.util.Utils;

/**
 * @author shadowfacts
 */
public class GUIHandler implements IGuiHandler {

	public static final int CRAFTING_TOP = 0;
	public static final int CRAFTING_BOTTOM = 1;
	public static final int FURNACE_TOP = 2;
	public static final int FURNACE_BOTTOM = 3;

	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (ID) {
			case CRAFTING_TOP:
				return new ContainerCrafting(pos, world, player.inventory, Utils.getTileCraftingSlab(world, pos, BlockSlab.EnumBlockHalf.TOP).orElseThrow(() -> new RuntimeException("No valid tile entity at position " + pos)));
			case CRAFTING_BOTTOM:
				return new ContainerCrafting(pos, world, player.inventory, Utils.getTileCraftingSlab(world, pos, BlockSlab.EnumBlockHalf.BOTTOM).orElseThrow(() -> new RuntimeException("No valid tile entity at position " + pos)));
			case FURNACE_TOP:
				return new ContainerFurnace(player.inventory, Utils.getTileFurnaceSlab(world, pos, BlockSlab.EnumBlockHalf.TOP).orElseThrow(() -> new RuntimeException("No valid tile entity at position " + pos)));
			case FURNACE_BOTTOM:
				return new ContainerFurnace(player.inventory, Utils.getTileFurnaceSlab(world, pos, BlockSlab.EnumBlockHalf.BOTTOM).orElseThrow(() -> new RuntimeException("No valid tile entity at position " + pos)));
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (ID) {
			case CRAFTING_TOP:
			case CRAFTING_BOTTOM:
				return new GUICrafting(getServerGuiElement(ID, player, world, x, y, z));
			case FURNACE_TOP:
				return new GuiFurnace(player.inventory, Utils.getTileFurnaceSlab(world, pos, BlockSlab.EnumBlockHalf.TOP).orElseThrow(() -> new RuntimeException("No valid tile entity at position " + pos)));
			case FURNACE_BOTTOM:
				return new GuiFurnace(player.inventory, Utils.getTileFurnaceSlab(world, pos, BlockSlab.EnumBlockHalf.BOTTOM).orElseThrow(() -> new RuntimeException("No valid tile entity at position " + pos)));
			default:
				return null;
		}
	}

}
