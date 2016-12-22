package net.shadowfacts.craftingslabs.multipart;

import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.tileentity.TileEntity;
import net.shadowfacts.craftingslabs.tileentity.TileEntityCraftingSlab;

/**
 * @author shadowfacts
 */
public class PartTileCraftingSlab implements IMultipartTile {

	private TileEntityCraftingSlab tile;

	public PartTileCraftingSlab(TileEntityCraftingSlab tile) {
		this.tile = tile;
	}

	@Override
	public TileEntity getTileEntity() {
		return tile;
	}

}
