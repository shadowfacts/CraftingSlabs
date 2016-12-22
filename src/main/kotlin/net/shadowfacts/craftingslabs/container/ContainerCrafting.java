package net.shadowfacts.craftingslabs.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.tileentity.TileEntityCraftingSlab;
import net.shadowfacts.craftingslabs.util.InventoryCrafting;
import net.shadowfacts.shadowmc.inventory.ContainerBase;

/**
 * @author shadowfacts
 */
public class ContainerCrafting extends ContainerBase {

	private IInventory result = new InventoryCraftResult();
	private InventoryCrafting matrix;
	private World world;

	public ContainerCrafting(BlockPos pos, World world, InventoryPlayer playerInv, TileEntityCraftingSlab craftingSlab) {
		super(pos);
		this.matrix = craftingSlab.inventory;
		this.world = world;

		addSlotToContainer(new SlotCrafting(playerInv.player, matrix, result, 0, 124, 35) {
			@Override
			public void onSlotChanged() {
				updateCraftingResult();
			}
		});

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new Slot(matrix, j + i * 3, 30 + j * 18, 17 + i * 18) {
					@Override
					public void onSlotChanged() {
						updateCraftingResult();
						craftingSlab.markDirty();
					}
				});
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}

		updateCraftingResult();
	}

	private void updateCraftingResult() {
		result.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(matrix, world));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				itemstack1.getItem().onCreated(itemstack1, world, player);

				if (!mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!mergeItemStack(itemstack1, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 37 && index < 46) {
				if (!mergeItemStack(itemstack1, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(player, itemstack1);

			if (index == 0) {
				player.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

}
