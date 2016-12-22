package net.shadowfacts.craftingslabs.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.shadowfacts.craftingslabs.property.PropertyDummy;

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

}
