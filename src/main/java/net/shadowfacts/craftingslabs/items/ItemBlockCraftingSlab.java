package net.shadowfacts.craftingslabs.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.block.BlockCraftingSlab;

/**
 * @author shadowfacts
 */
public class ItemBlockCraftingSlab extends ItemBlock {

	private final BlockSlab singleSlab;
	private final BlockSlab doubleSlab;

	public ItemBlockCraftingSlab(Block block, BlockCraftingSlab singleSlab, BlockCraftingSlab doubleSlab) {
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
			return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
		IBlockState state = world.getBlockState(pos.offset(side));
		return state.getBlock() == singleSlab || super.canPlaceBlockOnSide(world, pos, side, player, stack);
	}
}
