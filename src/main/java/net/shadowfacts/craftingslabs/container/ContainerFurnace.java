package net.shadowfacts.craftingslabs.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author shadowfacts
 */
public class ContainerFurnace extends Container {

	private final IInventory furnace;
	private int cookTime;
	private int totalCookTime;
	private int furnaceBurnTime;
	private int currentItemBurnTime;

	public ContainerFurnace(InventoryPlayer playerInv, IInventory furnace) {
		this.furnace = furnace;
		addSlotToContainer(new Slot(furnace, 0, 56, 17));
		addSlotToContainer(new SlotFurnaceFuel(furnace, 1, 56, 53));
		addSlotToContainer(new SlotFurnaceOutput(playerInv.player, furnace, 2, 116, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void onCraftGuiOpened(ICrafting listener) {
		super.onCraftGuiOpened(listener);
		listener.sendAllWindowProperties(this, furnace);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (this.cookTime != this.furnace.getField(2))
			{
				icrafting.sendProgressBarUpdate(this, 2, this.furnace.getField(2));
			}

			if (this.furnaceBurnTime != this.furnace.getField(0))
			{
				icrafting.sendProgressBarUpdate(this, 0, this.furnace.getField(0));
			}

			if (this.currentItemBurnTime != this.furnace.getField(1))
			{
				icrafting.sendProgressBarUpdate(this, 1, this.furnace.getField(1));
			}

			if (this.totalCookTime != this.furnace.getField(3))
			{
				icrafting.sendProgressBarUpdate(this, 3, this.furnace.getField(3));
			}
		}

		this.cookTime = this.furnace.getField(2);
		this.furnaceBurnTime = this.furnace.getField(0);
		this.currentItemBurnTime = this.furnace.getField(1);
		this.totalCookTime = this.furnace.getField(3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		furnace.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return furnace.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotIndex == 2) {
				if (!mergeItemStack(itemstack1, 3, 39, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (slotIndex != 1 && slotIndex != 0) {
				if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null) {
					if (!mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}
				} else if (TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return null;
					}
				} else if (slotIndex >= 3 && slotIndex < 30) {
					if (!mergeItemStack(itemstack1, 30, 39, false)) {
						return null;
					}
				} else if (slotIndex >= 30 && slotIndex < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
					return null;
				}
			} else if (!mergeItemStack(itemstack1, 3, 39, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
	}
}
