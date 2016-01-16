package net.shadowfacts.craftingslabs.multipart.crafting;

import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.IPartConverter;
import net.minecraftforge.fml.common.Optional;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.block.BlockCraftingSlab;

/**
 * @author shadowfacts
 */
public class PartCraftingSlabReverseConverter implements IPartConverter.IReversePartConverter {

	@Override
	public boolean convertToBlock(IMultipartContainer container) {
		PartCraftingSlab part = PartCraftingSlab.getCraftingSlab(container.getWorldIn(), container.getPosIn(), null);
		if (container.getParts().size() == 1 && part != null) {
			container.getWorldIn().setBlockState(container.getPosIn(), CraftingSlabs.blocks.craftingSlab.getDefaultState().withProperty(BlockCraftingSlab.HALF, part.getHalf()));
			return true;
		}
		return false;
	}

}
