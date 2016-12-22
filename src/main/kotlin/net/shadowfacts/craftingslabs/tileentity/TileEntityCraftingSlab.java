package net.shadowfacts.craftingslabs.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.shadowfacts.craftingslabs.util.InventoryCrafting;
import net.shadowfacts.shadowmc.capability.CapHolder;
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity;

/**
 * @author shadowfacts
 */
public class TileEntityCraftingSlab extends BaseTileEntity {

	public InventoryCrafting inventory = new InventoryCrafting(3, 3);
	@CapHolder(capabilities = IItemHandler.class)
	private InvWrapper wrapper = new InvWrapper(inventory);

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		inventory.writeToNBT(tag);
		return super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		inventory.readFromNBT(tag);
		super.readFromNBT(tag);
	}

}
