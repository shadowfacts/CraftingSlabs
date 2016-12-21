package net.shadowfacts.craftingslabs.block;

import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.property.PropertyDummy;

/**
 * @author shadowfacts
 */
public class BlockCraftingSlab extends BlockSlab implements IMultipart {

	private static final PropertyDummy DUMMY = new PropertyDummy("dummy");

	public BlockCraftingSlab() {
		super(Material.WOOD);
		setRegistryName("crafting_slab");
		setUnlocalizedName(getRegistryName().toString());
		useNeighborBrightness = true;
		setCreativeTab(CreativeTabs.DECORATIONS);

		setDefaultState(blockState.getBaseState().withProperty(DUMMY, PropertyDummy.Dummy.INSTANCE).withProperty(HALF, EnumBlockHalf.BOTTOM));
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return PropertyDummy.Dummy.INSTANCE;
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return getUnlocalizedName();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return DUMMY;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DUMMY, HALF);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(HALF).ordinal();
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(HALF, EnumBlockHalf.values()[meta]);
	}

	private EnumBlockHalf getHalfForPlacement(EnumFacing facing, float hitY) {
		return facing == EnumFacing.DOWN || hitY > 0.5 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(HALF, getHalfForPlacement(facing, hitY));
	}

	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		return getHalfForPlacement(facing, hitY) == EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
	}

	@Override
	public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
		return state.getValue(HALF) == EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
	}

}
