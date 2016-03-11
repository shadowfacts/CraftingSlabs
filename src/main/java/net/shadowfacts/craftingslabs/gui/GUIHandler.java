package net.shadowfacts.craftingslabs.gui;

import mcmultipart.multipart.PartSlot;
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
		if (ID == 0) { // furnace bottom
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity te = world.getTileEntity(pos);
			IInventory inv = null;
			if (te instanceof TileEntityFurnaceSlab) {
				inv = (TileEntityFurnaceSlab)te;
			} else if (Loader.isModLoaded("mcmultipart")) {
				PartFurnaceSlab partFurnace = PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.DOWN);
				if (partFurnace != null) {
					inv = partFurnace;
				}
			}

			return new ContainerFurnace(player.inventory, inv, world, pos);
		} else if (ID == 1) { // furnace top
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity te = world.getTileEntity(pos);
			IInventory inv = null;
			if (te instanceof TileEntityFurnaceSlab) {
				inv = (TileEntityFurnaceSlab)te;
			} else if (Loader.isModLoaded("mcmultipart")) {
				PartFurnaceSlab partFurnace = PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.UP);
				if (partFurnace != null) {
					inv = partFurnace;
				}
			}

			return new ContainerFurnace(player.inventory, inv, world, pos);
		} else if (ID == 2) { // crafting
			return new ContainerCrafting(player.inventory, world, new BlockPos(x, y, z));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) { // furnace bottom
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity te = world.getTileEntity(pos);
			IInventory inv = null;
			if (te instanceof TileEntityFurnaceSlab) {
				inv = (TileEntityFurnaceSlab)te;
			} else if (Loader.isModLoaded("mcmultipart")) {
				PartFurnaceSlab partFurnace = PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.DOWN);
				if (partFurnace != null) {
					inv = partFurnace;
				}
			}

			return new GUIFurnace(player.inventory, inv, world, pos);
		} else if (ID == 1) { // furnace top
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity te = world.getTileEntity(pos);
			IInventory inv = null;
			if (te instanceof TileEntityFurnaceSlab) {
				inv = (TileEntityFurnaceSlab)te;
			} else if (Loader.isModLoaded("mcmultipart")) {
				PartFurnaceSlab partFurnace = PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.UP);
				if (partFurnace != null) {
					inv = partFurnace;
				}
			}

			return new GUIFurnace(player.inventory, inv, world, pos);
		} else if (ID == 2) { // crafting
			return new GUICrafting(player.inventory, world, new BlockPos(x, y, z));
		}
		return null;
	}

}
