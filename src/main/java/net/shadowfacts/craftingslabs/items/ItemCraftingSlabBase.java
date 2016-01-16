package net.shadowfacts.craftingslabs.items;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

/**
 * @author shadowfacts
 */
public abstract class ItemCraftingSlabBase extends ItemBlock {

	private BlockSlab slab;

	public ItemCraftingSlabBase(Block block, BlockSlab slab) {
		super(block);
		this.slab = slab;
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (stack.stackSize == 0) {
			return false;
		} else if (!player.canPlayerEdit(pos, side, stack)) {
			return false;
		} else {
			if (Loader.isModLoaded("mcmultipart")) {
				return placeMultipart(stack, player, world, pos, side, hitX, hitY, hitZ);
			} else {
				return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
			}
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
		BlockPos blockpos1 = pos;
		IProperty iproperty = this.slab.getVariantProperty();
		Object object = this.slab.getVariant(stack);

		pos = pos.offset(side);
		IBlockState iblockstate1 = worldIn.getBlockState(pos);
		return iblockstate1.getBlock() == this.slab && object == iblockstate1.getValue(iproperty) || super.canPlaceBlockOnSide(worldIn, blockpos1, side, player, stack);

	}

	private boolean placeMultipart(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		Vec3 hit = new Vec3(hitX, hitY, hitZ);
		double depth = (hit.xCoord * 2d - 1d) * (double)side.getFrontOffsetX() + (hit.yCoord * 2d - 1d) * (double)side.getFrontOffsetY() + (hit.zCoord * 2d - 1d) * (double)side.getFrontOffsetZ();
		if (depth < 1d && placeMultipart(world, pos, side, hit, stack, player)) {
			return true;
		} else {
			return placeMultipart(world, pos.offset(side), side.getOpposite(), hit, stack, player);
		}
	}

	private boolean placeMultipart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player) {
		IMultipart part = createPart(world, pos, side, hit, stack, player);
		if (MultipartHelper.canAddPart(world, pos, part)) {
			if (!world.isRemote) {
				MultipartHelper.addPart(world, pos, part);
			}

			--stack.stackSize;
			Block.SoundType sound = getPlacementSound(stack);
			if (sound != null) {
				world.playSoundEffect((double)pos.getX() + .5d, (double)pos.getY() + .5d, (double)pos.getZ() + .5d, sound.getPlaceSound(), sound.getVolume(), sound.getFrequency());
			}

			return true;
		}
		return false;
	}

	@Optional.Method(modid = "mcmultipart")
	protected abstract IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player);

	protected Block.SoundType getPlacementSound(ItemStack stack) {
		return Block.soundTypeGrass;
	}

}
