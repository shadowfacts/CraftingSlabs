package net.shadowfacts.craftingslabs.multipart;

import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.tileentity.TileEntity;

/**
 * @author shadowfacts
 */
public class PartTile implements IMultipartTile {

	private TileEntity tile;

	public PartTile(TileEntity tile) {
		this.tile = tile;
	}

	@Override
	public TileEntity getTileEntity() {
		return tile;
	}

}
