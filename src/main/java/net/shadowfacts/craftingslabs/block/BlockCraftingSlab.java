package net.shadowfacts.craftingslabs.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
import net.shadowfacts.craftingslabs.property.PropertyDummy;
import net.shadowfacts.craftingslabs.tileentity.TileEntityCraftingSlab;

import javax.annotation.Nullable;

/**
 * @author shadowfacts
 */
public class BlockCraftingSlab extends BlockSlab {

	private static final PropertyDummy DUMMY = new PropertyDummy("dummy");

	public BlockCraftingSlab() {
		super(Material.WOOD);
		setRegistryName("crafting_slab");
		setUnlocalizedName(getRegistryName().toString());
		useNeighborBrightness = true;
		setCreativeTab(CreativeTabs.DECORATIONS);

		setDefaultState(blockState.getBaseState()
				.withProperty(DUMMY, PropertyDummy.Dummy.INSTANCE)
				.withProperty(HALF, EnumBlockHalf.BOTTOM));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		int id = state.getValue(HALF) == EnumBlockHalf.TOP ? GUIHandler.CRAFTING_TOP : GUIHandler.CRAFTING_BOTTOM;
		player.openGui(CraftingSlabs.instance, id, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntityCraftingSlab tile = (TileEntityCraftingSlab)world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, tile.inventory);
		super.onBlockHarvested(world, pos, state, player);
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

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityCraftingSlab();
	}

}
