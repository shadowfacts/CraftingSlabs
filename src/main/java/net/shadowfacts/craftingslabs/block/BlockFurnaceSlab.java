package net.shadowfacts.craftingslabs.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
import net.shadowfacts.craftingslabs.property.PropertyDummy;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;
import net.shadowfacts.craftingslabs.util.Utils;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author shadowfacts
 */
public class BlockFurnaceSlab extends BlockSlab {

	private static final PropertyDummy DUMMY = new PropertyDummy("dummy");
	private static final PropertyBool BURNING = PropertyBool.create("burning");
	private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockFurnaceSlab() {
		super(Material.ROCK);
		setRegistryName("furnace_slab");
		setUnlocalizedName(getRegistryName().toString());
		useNeighborBrightness = true;
		setCreativeTab(CreativeTabs.DECORATIONS);

		setDefaultState(blockState.getBaseState()
				.withProperty(DUMMY, PropertyDummy.Dummy.INSTANCE)
				.withProperty(HALF, EnumBlockHalf.BOTTOM)
				.withProperty(BURNING, false)
				.withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		int id = state.getValue(HALF) == EnumBlockHalf.TOP ? GUIHandler.FURNACE_TOP : GUIHandler.FURNACE_BOTTOM;
		player.openGui(CraftingSlabs.MOD_ID, id, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntityFurnaceSlab tile = (TileEntityFurnaceSlab)world.getTileEntity(pos);
			InventoryHelper.dropInventoryItems(world, pos, tile);
		}
		super.onBlockHarvested(world, pos, state, player);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (((TileEntityFurnaceSlab)world.getTileEntity(pos)).isBurning()) {
			EnumFacing enumfacing = state.getValue(FACING);
			double d0 = (double)pos.getX() + 0.5D;
			double y = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			if (state.getValue(HALF) == EnumBlockHalf.TOP) y += 0.5;
			double d2 = (double)pos.getZ() + 0.5D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;

			if (rand.nextDouble() < 0.1D)
			{
				world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			switch (enumfacing)
			{
				case WEST:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, y, d2 + d4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, y, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case EAST:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, y, d2 + d4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, y, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case NORTH:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, y, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, y, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
					break;
				case SOUTH:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, y, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, y, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	@Deprecated
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
		return Container.calcRedstone(world.getTileEntity(pos));
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
		return new BlockStateContainer(this, DUMMY, HALF, BURNING, FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(FACING).ordinal();
		if (state.getValue(HALF) == EnumBlockHalf.TOP) {
			meta |= 0b1000;
		}
		return meta;
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.VALUES[meta & 0b0111];
		if (!EnumFacing.Plane.HORIZONTAL.apply(facing)) facing = EnumFacing.NORTH;
		boolean top = meta >> 3 == 1;
		return getDefaultState()
				.withProperty(FACING, facing)
				.withProperty(HALF, top ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
	}

	@Override
	@Deprecated
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntityFurnaceSlab tile = Utils.getTileFurnaceSlab(world, pos, state.getValue(HALF)).orElseThrow(() -> new RuntimeException("No valid tile entity at position " + pos));
		return state.withProperty(BURNING, tile.isBurning());
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityFurnaceSlab();
	}

}
