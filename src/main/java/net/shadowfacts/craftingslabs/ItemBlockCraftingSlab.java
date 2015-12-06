package net.shadowfacts.craftingslabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class ItemBlockCraftingSlab extends ItemBlock {

	private final BlockSlab singleSlab;
	private final BlockSlab doubleSlab;

	public ItemBlockCraftingSlab(Block block, BlockCraftingSlab singleSlab, BlockCraftingSlab doubleSlab, Boolean stacked) {
		super(block);
		this.singleSlab = singleSlab;
		this.doubleSlab = doubleSlab;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (stack.stackSize == 0) {
			return false;
		} else if (!player.canPlayerEdit(pos.offset(side), side, stack)) {
			return false;
		} else {
			Object object = this.singleSlab.getVariant(stack);

			return this.tryPlace(stack, world, pos.offset(side), object) || super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
		BlockPos blockpos1 = pos;
		IProperty iproperty = this.singleSlab.getVariantProperty();
		Object object = this.singleSlab.getVariant(stack);

		pos = pos.offset(side);
		IBlockState iblockstate1 = worldIn.getBlockState(pos);
		return iblockstate1.getBlock() == this.singleSlab && object == iblockstate1.getValue(iproperty) || super.canPlaceBlockOnSide(worldIn, blockpos1, side, player, stack);
	}

	private boolean tryPlace(ItemStack stack, World world, BlockPos pos, Object variantInStack)
	{
//		IBlockState iblockstate = world.getBlockState(pos);
//
//		if (iblockstate.getBlock() == this.singleSlab)
//		{
//			Comparable comparable = iblockstate.getValue(this.singleSlab.getVariantProperty());
//
//			if (comparable == variantInStack)
//			{
//				IBlockState iblockstate1 = this.doubleSlab.getDefaultState().withProperty(this.singleSlab.getVariantProperty(), comparable);
//
//				if (world.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(world, pos, iblockstate1)) && world.setBlockState(pos, iblockstate1, 3))
//				{
//					world.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
//					--stack.stackSize;
//				}
//
//				return true;
//			}
//		}

		return false;
	}

}
