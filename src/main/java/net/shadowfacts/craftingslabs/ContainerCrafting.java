package net.shadowfacts.craftingslabs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class ContainerCrafting extends Container {

	public InventoryCrafting matrix;
	public IInventory result;
	private World world;

	public ContainerCrafting(InventoryPlayer inventoryPlayer, World world) {
		matrix = new InventoryCrafting(this, 3, 3);
		result = new InventoryCraftResult();
		this.world = world;

		this.addSlotToContainer(new SlotCrafting(inventoryPlayer.player, matrix, result, 0, 124, 35));

		for (int i = 0; i < 3; i++) {
			for (int l = 0; l < 3; l++) {
				this.addSlotToContainer(new Slot(matrix, l + i * 3, 30 + l * 18, 17 + i * 18));
			}
		}

		for (int column = 0; column < 3; column++) {
			for (int row = 0; row < 9; row++) {
				this.addSlotToContainer(new Slot(inventoryPlayer, row + column * 9 + 9, 8 + row * 18, 84 + column * 18));
			}
		}

		for (int column = 0; column < 9; column++) {
			this.addSlotToContainer(new Slot(inventoryPlayer, column, 8 + column * 18, 142));
		}

		onCraftMatrixChanged(matrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		result.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(matrix, world));
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
//		if (world.isRemote)
//		{
//			return;
//		}
		if (!world.isRemote) {
			for (int i = 0; i < 9; i++) {
				ItemStack itemstack = matrix.getStackInSlot(i);
				if (itemstack != null) {
					player.entityDropItem(itemstack, 0);
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(slotNum);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (slotNum == 0) {
				if (!mergeItemStack(itemstack1, 10, 46, true)) {
					return null;
				}
			}
			else if (slotNum >= 10 && slotNum < 37) {
				if (!mergeItemStack(itemstack1, 37, 46, false)) {
					return null;
				}
			}
			else if (slotNum >= 37 && slotNum < 46) {
				if (!mergeItemStack(itemstack1, 10, 37, false)) {
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, 10, 46, false)) {
				return null;
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			}
			else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onPickupFromSlot(player, itemstack1);
			}
			else {
				return null;
			}
		}
		return itemstack;
	}
}
