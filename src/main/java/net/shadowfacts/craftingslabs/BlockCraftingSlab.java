package net.shadowfacts.craftingslabs;

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

import java.util.Random;

/**
 * @author shadowfacts
 */
public class BlockCraftingSlab extends BlockSlab {

	private static final PropertyBool VARIANT = PropertyBool.create("variant");

	public BlockCraftingSlab() {
		super(Material.wood);
		setUnlocalizedName("craftingslab");
		useNeighborBrightness = true;
		setCreativeTab(CreativeTabs.tabDecorations);

		IBlockState state = blockState.getBaseState();
		state = state.withProperty(VARIANT, false);
		state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
		setDefaultState(state);
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return getUnlocalizedName();
	}

	@Override
	public Object getVariant(ItemStack stack) {
		return false;
	}

	@Override
	public IProperty getVariantProperty() {
		return VARIANT;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState();
		state = state.withProperty(VARIANT, false);
		EnumBlockHalf value = (meta & 8) != 0 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
		state = state.withProperty(HALF, value);
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumBlockHalf value = (EnumBlockHalf)state.getValue(HALF);
		if (value == EnumBlockHalf.TOP) {
			return 8;
		}
		return 0;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		return Item.getItemFromBlock(this);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, VARIANT, HALF);
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) player.openGui(CraftingSlabs.instance, GuiHandler.CRAFTING, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}


}
