package net.shadowfacts.craftingslabs.multipart.furnace;

import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.IReversePartConverter;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.block.BlockFurnaceSlab;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;

/**
 * @author shadowfacts
 */
public class PartFurnaceSlabReverseConverter implements IReversePartConverter {

	@Override
	public boolean convertToBlock(IMultipartContainer container) {
		PartFurnaceSlab part = PartFurnaceSlab.getFurnaceSlab(container.getWorldIn(), container.getPosIn(), null);
		if (container.getParts().size() == 1 && part != null) {
			container.getWorldIn().setBlockState(container.getPosIn(), CraftingSlabs.blocks.furnaceSlab.getDefaultState()
			.withProperty(BlockFurnaceSlab.HALF, part.getHalf())
			.withProperty(BlockFurnaceSlab.FACING, part.getFacing())
			.withProperty(BlockFurnaceSlab.BURNING, part.isBurning()));

			TileEntity te = container.getWorldIn().getTileEntity(container.getPosIn());
			if (te instanceof TileEntityFurnaceSlab) {
				TileEntityFurnaceSlab furnace = (TileEntityFurnaceSlab)te;
				furnace.furnaceBurnTime = part.furnaceBurnTime;
				furnace.currentItemBurnTime = part.currentItemBurnTime;
				furnace.cookTime = part.cookTime;
				furnace.totalCookTime = part.totalCookTime;

				for (int i = 0; i < part.getSizeInventory(); i++) {
					ItemStack partStack = part.getStackInSlot(i);
					if (partStack != null) {
						furnace.setInventorySlotContents(i, partStack.copy());
						part.setInventorySlotContents(i, null);
					}
				}
			}

			return true;
		}
		return false;
	}

}
