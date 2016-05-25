package net.shadowfacts.craftingslabs.multipart.furnace;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IPartConverter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.block.BlockFurnaceSlab;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;

import java.util.Collection;
import java.util.Collections;

/**
 * @author shadowfacts
 */
public class PartFurnaceSlabConverter implements IPartConverter {

	@Override
	public Collection<Block> getConvertableBlocks() {
		return Collections.singleton(CraftingSlabs.blocks.furnaceSlab);
	}

	@Override
	public Collection<? extends IMultipart> convertBlock(IBlockAccess world, BlockPos pos, boolean simulate) {
		IBlockState state = world.getBlockState(pos);
		PartFurnaceSlab part = new PartFurnaceSlab(state.getValue(BlockFurnaceSlab.HALF), state.getValue(BlockFurnaceSlab.FACING));
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityFurnaceSlab) {
			TileEntityFurnaceSlab furnace = (TileEntityFurnaceSlab)te;
			for (int i = 0; i < furnace.getSizeInventory(); i++) {
				ItemStack furnaceStack = furnace.getStackInSlot(i);
				if (furnaceStack != null) {
					part.setInventorySlotContents(i, furnaceStack.copy());
					if (!simulate) furnace.setInventorySlotContents(i, null);
				}
			}
			part.setBurning(furnace.isBurning());
			part.furnaceBurnTime = furnace.furnaceBurnTime;
			part.currentItemBurnTime = furnace.currentItemBurnTime;
			part.cookTime = furnace.cookTime;
			part.totalCookTime = furnace.totalCookTime;
		}
		return Collections.singleton(part);
	}

}
