package net.shadowfacts.craftingslabs.util;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.multipart.crafting.PartCraftingSlab;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlab;

/**
 * @author shadowfacts
 */
public class MiscUtils {

	public static boolean hasPartCraftingSlab(World world, BlockPos pos) {
		if (Loader.isModLoaded("mcmultipart")) {
			return PartCraftingSlab.getCraftingSlab(world, pos, null) != null;
		}
		return false;
	}

	public static boolean isCraftingSlab(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == CraftingSlabs.blocks.craftingSlab || hasPartCraftingSlab(world, pos);
	}

	public static boolean hasPartFurnaceSlab(World world, BlockPos pos) {
		if (Loader.isModLoaded("mcmultipart")) {
			return PartFurnaceSlab.getFurnaceSlab(world, pos, null) != null;
		}
		return false;
	}

	public static boolean isFurnaceSlab(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == CraftingSlabs.blocks.furnaceSlab || hasPartFurnaceSlab(world, pos);
	}

}
