package net.shadowfacts.craftingslabs.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.shadowfacts.shadowmc.network.PacketBase;

/**
 * @author shadowfacts
 */
public class PacketRequestUpdateFurnaceSlab extends PacketBase<PacketRequestUpdateFurnaceSlab, PacketUpdateFurnaceSlab> {

	private int dimension;
	private BlockPos pos;

	public PacketRequestUpdateFurnaceSlab(TileEntity tile) {
		dimension = tile.getWorld().provider.getDimension();
		pos = tile.getPos();
	}

	public PacketRequestUpdateFurnaceSlab() {

	}

	@Override
	public PacketUpdateFurnaceSlab onMessage(PacketRequestUpdateFurnaceSlab message, MessageContext ctx) {
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(message.dimension);
		TileEntity tile = world.getTileEntity(message.pos);
		if (tile != null) {
			return new PacketUpdateFurnaceSlab(tile);
		} else {
			return null;
		}
	}

}
