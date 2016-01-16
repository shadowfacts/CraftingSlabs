package net.shadowfacts.craftingslabs.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.gui.GUIs;

import java.util.Random;

/**
 * @author shadowfacts
 */
public class BlockCraftingSlab extends BlockSlab {

	private static final PropertyBool VARIANT = PropertyBool.create("variant");

	public BlockCraftingSlab() {
		super(Material.wood);
		setUnlocalizedName("craftingSlab");
		useNeighborBrightness = true;
		setCreativeTab(CreativeTabs.tabDecorations);

		setDefaultState(blockState.getBaseState()
						.withProperty(VARIANT, false)
						.withProperty(HALF, EnumBlockHalf.BOTTOM));
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
		EnumBlockHalf half = (meta & 8) != 0 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
		return getDefaultState()
				.withProperty(VARIANT, false)
				.withProperty(HALF, half);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumBlockHalf half = state.getValue(HALF);
		return half == EnumBlockHalf.TOP ? 8 : 0;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, VARIANT, HALF);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return CraftingSlabs.items.craftingSlab;
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		return CraftingSlabs.items.craftingSlab;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		player.openGui(CraftingSlabs.instance, GUIs.CRAFTING.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
