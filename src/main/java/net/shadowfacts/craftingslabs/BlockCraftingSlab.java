package net.shadowfacts.craftingslabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class BlockCraftingSlab extends BlockSlab {

	public BlockCraftingSlab(boolean p_i45410_1_, Material material) {
		super(p_i45410_1_, material);
		setHarvestLevel("axe", 0);
		setHardness(3f);
		setBlockName("craftingslab");
		setStepSound(soundTypeWood);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return Blocks.crafting_table.getIcon(side, meta);
	}

	@Override
	public String func_150002_b(int metadata) {
		return "";
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) player.openGui(CraftingSlabs.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(CraftingSlabs.craftingSlab);
	}
}
