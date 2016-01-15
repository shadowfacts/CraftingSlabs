package net.shadowfacts.craftingslabs.container;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.util.MiscUtils;

/**
 * @author shadowfacts
 */
public class ContainerCrafting extends Container {

	public InventoryCrafting matrix = new InventoryCrafting(this, 3, 3);
	public IInventory result = new InventoryCraftResult();

	private World world;
	private BlockPos pos;

	public ContainerCrafting(InventoryPlayer playerInv, World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
		addSlotToContainer(new SlotCrafting(playerInv.player, matrix, result, 0, 124, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				addSlotToContainer(new Slot(matrix, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}

		onCraftMatrixChanged(matrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		result.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(matrix, world));
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		if (!world.isRemote) {
			for (int i = 0; i < 9; ++i) {
				ItemStack stack = matrix.removeStackFromSlot(i);
				if (stack != null) {
					player.dropPlayerItemWithRandomChoice(stack, false);
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return MiscUtils.isCraftingSlab(world, pos) && player.getDistanceSq((double) pos.getX() + .5d, (double) pos.getY() + .5d, (double) pos.getZ() + .5d) <= 64;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemStack = null;
		Slot slot = inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();

			if (slotIndex == 0) {
				if (!mergeItemStack(itemStack1, 10, 46, true)) {
					return null;
				}

				slot.onSlotChange(itemStack1, itemStack);
			} else if (slotIndex >= 10 && slotIndex < 37) {
				if (!mergeItemStack(itemStack1, 37, 46, false)) {
					return null;
				}
			} else if (slotIndex >= 37 && slotIndex < 46) {
				if (!mergeItemStack(itemStack1, 10, 37, false)) {
					return null;
				}
			} else if (!mergeItemStack(itemStack1, 10, 46, false)) {
				return null;
			}

			if (itemStack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemStack1.stackSize == itemStack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemStack1);
		}

		return itemStack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_) {
		return super.canMergeSlot(stack, p_94530_2_);
	}

}
