package net.shadowfacts.craftingslabs.multipart.crafting;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IPartConverter;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.block.BlockCraftingSlab;

import java.util.Collection;
import java.util.Collections;

/**
 * @author shadowfacts
 */
public class PartCraftingSlabConverter implements IPartConverter {

	@Override
	public Collection<Block> getConvertableBlocks() {
		return Collections.singleton(CraftingSlabs.blocks.craftingSlab);
	}

	@Override
	public Collection<? extends IMultipart> convertBlock(IBlockAccess world, BlockPos pos, boolean b) {
		PartCraftingSlab part = new PartCraftingSlab(world.getBlockState(pos).getValue(BlockCraftingSlab.HALF));
		return Collections.singleton(part);
	}

}
