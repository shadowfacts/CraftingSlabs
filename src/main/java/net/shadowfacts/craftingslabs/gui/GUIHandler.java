package net.shadowfacts.craftingslabs.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.shadowfacts.craftingslabs.container.ContainerCrafting;
import net.shadowfacts.craftingslabs.container.ContainerFurnace;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlab;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;

/**
 * @author shadowfacts
 */
public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (GUIs.values()[ID]) {
			case CRAFTING:
				return new ContainerCrafting(player.inventory, world, new BlockPos(x, y, z));
			case FURNACE:
				BlockPos pos = new BlockPos(x, y, z);
				IInventory furnace = null;
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TileEntityFurnaceSlab) {
					furnace = (TileEntityFurnaceSlab)te;
				} else if (Loader.isModLoaded("mcmultipart")) {
					furnace = PartFurnaceSlab.getFurnaceSlab(world, pos, null);
				}
				return new ContainerFurnace(player.inventory, furnace, world, pos);
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (GUIs.values()[ID]) {
			case CRAFTING:
				return new GUICrafting(player.inventory, world, new BlockPos(x, y, z));
			case FURNACE:
				BlockPos pos = new BlockPos(x, y, z);
				IInventory furnace = null;
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TileEntityFurnaceSlab) {
					furnace = (TileEntityFurnaceSlab)te;
				} else if (Loader.isModLoaded("mcmultipart")) {
					furnace = PartFurnaceSlab.getFurnaceSlab(world, pos, null);
				}
					return new GUIFurnace(player.inventory, furnace, world, pos);
			default:
				return null;
		}
	}

}