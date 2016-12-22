package net.shadowfacts.craftingslabs.multipart;

import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.CraftingSlabs;

/**
 * @author shadowfacts
 */
public class PartCraftingSlab implements IMultipart {

	@Override
	public Block getBlock() {
		return CraftingSlabs.craftingSlab;
	}

	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		return state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
	}

	@Override
	public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
		return state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
	}

}
