package net.shadowfacts.craftingslabs.multipart.crafting;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.Multipart;
import net.minecraft.block.BlockSlab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;

/**
 * Don't use this unless you know what you're doing.
 *
 * @author shadowfacts
 */
public class ItemPartCraftingSlab extends ItemMultiPart {


	public ItemPartCraftingSlab() {
		setUnlocalizedName("partCraftingSlab");
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player) {
		BlockSlab.EnumBlockHalf half;
		switch (side) {
			case DOWN:
				half = BlockSlab.EnumBlockHalf.BOTTOM;
				break;
			case UP:
				half = BlockSlab.EnumBlockHalf.TOP;
				break;
			default:
				half = hit.yCoord > .5d ? BlockSlab.EnumBlockHalf.TOP : BlockSlab.EnumBlockHalf.BOTTOM;
		}
		return new PartCraftingSlab(half);
	}

}
