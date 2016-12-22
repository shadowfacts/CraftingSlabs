package net.shadowfacts.craftingslabs.util;

import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.BlockSlab;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.shadowfacts.craftingslabs.tileentity.TileEntityCraftingSlab;

import java.util.Optional;

/**
 * @author shadowfacts
 */
public class Utils {

	public static Optional<TileEntityCraftingSlab> getTileCraftingSlab(World world, BlockPos pos, BlockSlab.EnumBlockHalf half) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityCraftingSlab) {
			return Optional.of((TileEntityCraftingSlab)te);
		} else {
			if (Loader.isModLoaded("mcmultipart")) {
				return getPartTileCraftingSlab(world, pos, half);
			}
		}
		return Optional.empty();
	}

	@net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
	private static Optional<TileEntityCraftingSlab> getPartTileCraftingSlab(World world, BlockPos pos, BlockSlab.EnumBlockHalf half) {
		IPartSlot slot = half == BlockSlab.EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
		return MultipartHelper.getPartTile(world, pos, slot)
				.map(IMultipartTile::getTileEntity)
				.map(TileEntityCraftingSlab.class::cast);
	}

}
