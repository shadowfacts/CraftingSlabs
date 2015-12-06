package net.shadowfacts.craftingslabs.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
		this.addSlotToContainer(new SlotCrafting(playerInv.player, matrix, result, 0, 124, 35));

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
				ItemStack stack = matrix.getStackInSlotOnClosing(i);

				if (stack != null) {
					player.dropPlayerItemWithRandomChoice(stack, false);
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return world.getBlockState(pos).getBlock() == Blocks.crafting_table && player.getDistanceSq((double) pos.getX() + .5d, (double) pos.getY() + .5d, (double) pos.getZ() + .5d) <= 64d;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = null;
		Slot slot = (Slot)inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (index == 0) {
				if (!mergeItemStack(stack1, 10, 46, true)) {
					return null;
				}
				slot.onSlotChange(stack1, stack);
			} else if (index >= 10 && index < 37) {
				if (!mergeItemStack(stack1, 37, 46, false)) {
					return null;
				}
			} else if (index >= 37 && index < 46) {
				if (!mergeItemStack(stack1, 10, 37, false)) {
					return null;
				}
			} else if (!mergeItemStack(stack1, 10, 46, false)) {
				return null;
			}


			if (stack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (stack1.stackSize == stack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, stack1);
		}

		return stack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slot) {
		return slot.inventory != result && super.canMergeSlot(stack, slot);
	}
}
