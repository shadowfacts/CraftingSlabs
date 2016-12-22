package net.shadowfacts.craftingslabs.util;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * @author shadowfacts
 */
public class InventoryCrafting extends net.minecraft.inventory.InventoryCrafting {

	public InventoryCrafting(int width, int height) {
		super(null, width, height);
	}

	private NonNullList<ItemStack> getStackList() {
		return ObfuscationReflectionHelper.getPrivateValue(net.minecraft.inventory.InventoryCrafting.class, this, "stackList", "field_70466_a");
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(getStackList(), index, count);
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		getStackList().set(index, stack);
	}

	public void writeToNBT(NBTTagCompound tag) {
		ItemStackHelper.saveAllItems(tag, getStackList());
	}

	public void readFromNBT(NBTTagCompound tag) {
		ItemStackHelper.loadAllItems(tag, getStackList());
	}

}
