package net.shadowfacts.craftingslabs.util;

import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.tileentity.TileEntityCraftingSlab;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;

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

	public static Optional<TileEntityFurnaceSlab> getTileFurnaceSlab(IBlockAccess world, BlockPos pos, BlockSlab.EnumBlockHalf half) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityFurnaceSlab) {
			return Optional.of((TileEntityFurnaceSlab)te);
		} else {
			if (Loader.isModLoaded("mcmultipart")) {
				return getPartTileFurnaceSlab(world, pos, half);
			}
		}
		return Optional.empty();
	}

	@net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
	private static Optional<TileEntityFurnaceSlab> getPartTileFurnaceSlab(IBlockAccess world, BlockPos pos, BlockSlab.EnumBlockHalf half) {
		IPartSlot slot = half == BlockSlab.EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
		return MultipartHelper.getPartTile(world, pos, slot)
				.map(IMultipartTile::getTileEntity)
				.map(TileEntityFurnaceSlab.class::cast);
	}

	public static Optional<IBlockState> getStateFurnaceSlab(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == CraftingSlabs.furnaceSlab) {
			return Optional.of(state);
		} else {
			if (Loader.isModLoaded("mcmultipart")) {
				return getPartStateFurnaceSlab(world, pos);
			}
		}
		return Optional.empty();
	}

	@net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
	private static Optional<IBlockState> getPartStateFurnaceSlab(IBlockAccess world, BlockPos pos) {
		Optional<IBlockState> down = MultipartHelper.getPartState(world, pos, EnumFaceSlot.DOWN);
		if (down.isPresent() && down.get().getBlock() == CraftingSlabs.furnaceSlab) {
			return down;
		} else {
			Optional<IBlockState> up = MultipartHelper.getPartState(world, pos, EnumFaceSlot.UP);
			if (up.isPresent() && up.get().getBlock() == CraftingSlabs.furnaceSlab) {
				return up;
			}
		}
		return Optional.empty();
	}

}
