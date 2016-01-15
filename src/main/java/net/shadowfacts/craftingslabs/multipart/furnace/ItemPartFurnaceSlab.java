package net.shadowfacts.craftingslabs.multipart.furnace;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.block.BlockSlab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class ItemPartFurnaceSlab extends ItemMultiPart {

	public ItemPartFurnaceSlab() {
		setUnlocalizedName("partFurnaceSlab");
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player) {
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

}
