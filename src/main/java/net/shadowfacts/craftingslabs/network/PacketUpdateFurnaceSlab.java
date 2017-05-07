package net.shadowfacts.craftingslabs.network;

import net.minecraft.block.BlockSlab;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.shadowfacts.craftingslabs.block.BlockFurnaceSlab;
import net.shadowfacts.craftingslabs.util.Utils;
import net.shadowfacts.shadowmc.ShadowMC;
import net.shadowfacts.shadowmc.network.PacketBase;

/**
 * @author shadowfacts
 */
public class PacketUpdateFurnaceSlab extends PacketBase<PacketUpdateFurnaceSlab, IMessage> {

	private BlockPos pos;
	private BlockSlab.EnumBlockHalf half;
	private NBTTagCompound tag;

	static {
		addHandlers(BlockSlab.EnumBlockHalf.class, buf -> buf.readBoolean() ? BlockSlab.EnumBlockHalf.TOP : BlockSlab.EnumBlockHalf.BOTTOM, (half, buf) -> buf.writeBoolean(half == BlockSlab.EnumBlockHalf.TOP));
	}

	public PacketUpdateFurnaceSlab() {
	}

	public PacketUpdateFurnaceSlab(TileEntity tile) {
		this.pos = tile.getPos();
		this.half = Utils.getStateFurnaceSlab(tile.getWorld(), tile.getPos()).get().getValue(BlockFurnaceSlab.HALF);

		this.tag = tile.writeToNBT(new NBTTagCompound());
	}

	@Override
	public IMessage onMessage(PacketUpdateFurnaceSlab message, MessageContext ctx) {
		Utils.getTileFurnaceSlab(ShadowMC.proxy.getClientWorld(), message.pos, message.half).orElseThrow(() -> new RuntimeException("No valid tile entity at " + message.pos)).readFromNBT(message.tag);
		ShadowMC.proxy.getClientWorld().markBlockRangeForRenderUpdate(message.pos, message.pos);
		return null;
	}

}
