package net.shadowfacts.craftingslabs.items;

import mcmultipart.multipart.IMultipart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.shadowfacts.craftingslabs.block.BlockFurnaceSlab;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlab;

/**
 * @author shadowfacts
 */
public class ItemFurnaceSlab extends ItemCraftingSlabBase {

	public ItemFurnaceSlab(Block block, BlockFurnaceSlab furnaceSlab) {
		super(block, furnaceSlab);
		setUnlocalizedName("furnaceSlab");
	}

	@Override
	@Optional.Method(modid = "mcmultipart")
	protected IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player) {
		BlockSlab.EnumBlockHalf half;
		switch (side) {
			case DOWN:
				half = BlockSlab.EnumBlockHalf.BOTTOM;
				break;
			case UP:
				half = BlockSlab.EnumBlockHalf.TOP;
				break;
			default:
				half = hit.yCoord > .5d ? BlockSlab.EnumBlockHalf.TOP : BlockSlab.EnumBlockHalf.BOTTOM;
		}
		return new PartFurnaceSlab(half, getDirection(pos, player));
	}

	private EnumFacing getDirection(BlockPos pos, EntityLivingBase entity) {
		return EnumFacing.getFacingFromVector(
				(float)(entity.posX - pos.getX()),
				0,
				(float)(entity.posZ - pos.getZ()));
	}

	@Override
	protected Block.SoundType getPlacementSound(ItemStack stack) {
		return Blocks.furnace.stepSound;
	}
}
