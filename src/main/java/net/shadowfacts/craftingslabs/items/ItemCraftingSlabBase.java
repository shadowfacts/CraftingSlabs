package net.shadowfacts.craftingslabs.items;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

/**
 * @author shadowfacts
 */
public abstract class ItemCraftingSlabBase extends ItemBlock {

	private BlockSlab slab;

	public ItemCraftingSlabBase(BlockSlab slab) {
		super(slab);
		this.slab = slab;
		setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (stack.stackSize == 0) {
			return EnumActionResult.FAIL;
		} else if (!player.canPlayerEdit(pos, side, stack)) {
			return EnumActionResult.FAIL;
		} else {
			if (Loader.isModLoaded("mcmultipart")) {
				return placeMultipart(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
			} else {
				return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
			}
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
		BlockPos pos2 = pos.offset(side);
		IBlockState state = worldIn.getBlockState(pos2);
		return state.getBlock() == this.slab || super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);

	}

	private EnumActionResult placeMultipart(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		Vec3d hit = new Vec3d(hitX, hitY, hitZ);
		double depth = (hit.xCoord * 2d - 1d) * (double)side.getFrontOffsetX() + (hit.yCoord * 2d - 1d) * (double)side.getFrontOffsetY() + (hit.zCoord * 2d - 1d) * (double)side.getFrontOffsetZ();
		if (depth < 1d && placeMultipart(world, pos, hand, side, hit, stack, player) == EnumActionResult.SUCCESS) {
			return EnumActionResult.SUCCESS;
		} else {
			return placeMultipart(world, pos.offset(side), hand, side.getOpposite(), hit, stack, player);
		}
	}

	private EnumActionResult placeMultipart(World world, BlockPos pos, EnumHand hand, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
		IMultipart part = createPart(world, pos, side, hit, stack, player);
		if (MultipartHelper.canAddPart(world, pos, part)) {
			if (!world.isRemote) {
				MultipartHelper.addPart(world, pos, part);
			}

			--stack.stackSize;
			SoundType sound = getPlacementSound(stack);
			if (sound != null) {
				world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, sound.volume, sound.pitch);
			}

			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Optional.Method(modid = "mcmultipart")
	protected abstract IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player);

	protected SoundType getPlacementSound(ItemStack stack) {
		return SoundType.GROUND;
	}

}
