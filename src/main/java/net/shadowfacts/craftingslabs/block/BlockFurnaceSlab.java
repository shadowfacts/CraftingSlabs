package net.shadowfacts.craftingslabs.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;

import java.util.Random;

/**
 * @author shadowfacts
 */
public class BlockFurnaceSlab extends BlockSlab implements ITileEntityProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private static final PropertyBool VARIANT = PropertyBool.create("variant");
	public static final PropertyBool BURNING = PropertyBool.create("burning");

	private static boolean keepInventory;

	public BlockFurnaceSlab() {
		super(Material.rock);
		setUnlocalizedName("furnaceSlab");
		useNeighborBrightness = true;
		setCreativeTab(CreativeTabs.tabDecorations);

		setDefaultState(blockState.getBaseState()
						.withProperty(VARIANT, false)
						.withProperty(HALF, EnumBlockHalf.BOTTOM)
						.withProperty(FACING, EnumFacing.NORTH)
						.withProperty(BURNING, false));
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (state.getValue(BURNING)) {
			EnumFacing facing = state.getValue(FACING);
			double d0 = (double)pos.getX() + 0.5D;
			double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			double d2 = (double)pos.getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;

			if (state.getValue(HALF) == EnumBlockHalf.TOP) d1 += .5d;

			switch (facing) {
				case WEST:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case EAST:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case NORTH:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					break;
				case SOUTH:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getValue(BURNING) ? 13 : 0;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, getDirection(pos, placer)));
	}

	private EnumFacing getDirection(BlockPos pos, EntityLivingBase entity) {
		return EnumFacing.getFacingFromVector(
				(float)(entity.posX - pos.getX()),
				0,
				(float)(entity.posZ - pos.getZ()));
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return getUnlocalizedName();
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return VARIANT;
	}

	@Override
	public Object getVariant(ItemStack stack) {
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumBlockHalf half = EnumBlockHalf.values()[meta & 1];
		EnumFacing facing = EnumFacing.HORIZONTALS[(meta >> 2) & 4];
		boolean burning = ((meta >> 1) & 1) == 1;
		return getDefaultState()
				.withProperty(VARIANT, false)
				.withProperty(HALF, half)
				.withProperty(FACING, facing)
				.withProperty(BURNING, burning);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(FACING).getHorizontalIndex() << 2) | ((state.getValue(BURNING) ? 0 : 1) << 1) | state.getValue(HALF).ordinal();
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, VARIANT, HALF, FACING, BURNING);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return CraftingSlabs.items.furnaceSlab;
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		return CraftingSlabs.items.furnaceSlab;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		player.openGui(CraftingSlabs.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity te = world.getTileEntity(pos);

			if (te instanceof TileEntityFurnaceSlab) {
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityFurnaceSlab)te);
				world.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFurnaceSlab();
	}

	public static void setState(boolean active, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		TileEntity te = world.getTileEntity(pos);

		keepInventory = true;

		if (active) {
			world.setBlockState(pos, CraftingSlabs.blocks.furnaceSlab.getDefaultState()
					.withProperty(FACING, state.getValue(FACING))
					.withProperty(HALF, state.getValue(HALF))
					.withProperty(BURNING, true));
			world.setBlockState(pos, CraftingSlabs.blocks.furnaceSlab.getDefaultState()
					.withProperty(FACING, state.getValue(FACING))
					.withProperty(HALF, state.getValue(HALF))
					.withProperty(BURNING, true));
		} else {
			world.setBlockState(pos, CraftingSlabs.blocks.furnaceSlab.getDefaultState()
					.withProperty(FACING, state.getValue(FACING))
					.withProperty(HALF, state.getValue(HALF))
					.withProperty(BURNING, false));
			world.setBlockState(pos, CraftingSlabs.blocks.furnaceSlab.getDefaultState()
					.withProperty(FACING, state.getValue(FACING))
					.withProperty(HALF, state.getValue(HALF))
					.withProperty(BURNING, true));
		}

		keepInventory = false;
		if (te != null) {
			te.validate();
			world.setTileEntity(pos, te);
		}
	}
}
