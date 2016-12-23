package net.shadowfacts.craftingslabs.multipart;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * @author shadowfacts
 */
public class PartTileTickable extends PartTile implements ITickable {

	private ITickable tickable;

	public PartTileTickable(TileEntity tile, ITickable tickable) {
		super(tile);
		this.tickable = tickable;
	}

	@Override
	public void update() {
		tickable.update();
	}
}
