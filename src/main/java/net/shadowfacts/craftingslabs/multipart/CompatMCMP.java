package net.shadowfacts.craftingslabs.multipart;

import mcmultipart.api.addon.IMCMPAddon;
import mcmultipart.api.addon.IWrappedBlock;
import mcmultipart.api.addon.MCMPAddon;
import mcmultipart.api.multipart.IMultipartRegistry;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.CraftingSlabs;

/**
 * @author shadowfacts
 */
@MCMPAddon
public class CompatMCMP implements IMCMPAddon {

	@Override
	public void preInit(IMultipartRegistry registry) {
		registry.registerPartWrapper(CraftingSlabs.craftingSlab, new Part(CraftingSlabs.craftingSlab));
		IWrappedBlock craftingSlab = registry.registerStackWrapper(Item.getItemFromBlock(CraftingSlabs.craftingSlab), stack -> true, CraftingSlabs.craftingSlab);
		craftingSlab.setPlacementInfo(this::getSlabState);

		registry.registerPartWrapper(CraftingSlabs.furnaceSlab, new Part(CraftingSlabs.furnaceSlab));
		IWrappedBlock furnaceSlab = registry.registerStackWrapper(Item.getItemFromBlock(CraftingSlabs.furnaceSlab), stack -> true, CraftingSlabs.furnaceSlab);
		furnaceSlab.setPlacementInfo(this::getSlabState);
	}

	private IBlockState getSlabState(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand, IBlockState state) {
		if (facing.getAxis() == EnumFacing.Axis.Y
				&& Math.abs(hitX * facing.getFrontOffsetX() + hitY * facing.getFrontOffsetY() + hitZ * facing.getFrontOffsetZ()) == 0.5) {
			return state.cycleProperty(BlockSlab.HALF);
		}
		return state;
	}

}
