package net.shadowfacts.craftingslabs.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.*
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.shadowfacts.craftingslabs.multipart.PartCraftingSlab
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerCrafting(val world: World, pos: BlockPos) : ContainerBase(pos) {

	val matrix = InventoryCrafting(this, 3, 3)
	val result = InventoryCraftResult()

	constructor(playerInv: InventoryPlayer, world: World, pos: BlockPos) : this(world, pos) {
		addSlotToContainer(SlotCrafting(playerInv.player, matrix, result, 0, 124, 35))

		for (i in 0.until(3)) {
			for (j in 0.until(3)) {
				addSlotToContainer(Slot(matrix, j + i * 3, 30 + j * 18, 17 + i * 18))
			}
		}

		for (i in 0.until(3)) {
			for (j in 0.until(9)) {
				addSlotToContainer(Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
			}
		}

		for (i in 0.until(9)) {
			addSlotToContainer(Slot(playerInv, i, 8 + i * 18, 142))
		}

		onCraftMatrixChanged(matrix)
	}

	override fun onCraftMatrixChanged(inventory: IInventory) {
		result.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(matrix, world))
	}

	override fun onContainerClosed(player: EntityPlayer) {
		super.onContainerClosed(player)

		if (!world.isRemote) {
			for (i in 0.until(9)) {
				val stack = matrix.removeStackFromSlot(i)
				if (stack != null) {
					player.dropItem(stack, false)
				}
			}
		}
	}

	override fun canInteractWith(player: EntityPlayer?): Boolean {
		return PartCraftingSlab.getCraftingSlab(world, pos, null) != null && super.canInteractWith(player)
	}

	override fun transferStackInSlot(player: EntityPlayer, slotIndex: Int): ItemStack? {
		var itemStack: ItemStack? = null
		val slot = inventorySlots[slotIndex]

		if (slot != null && slot.hasStack) {
			val itemStack1 = slot.stack!!
			itemStack = itemStack1.copy()

			if (slotIndex == 0) {
				if (!mergeItemStack(itemStack1, 10, 46, true)) {
					return null
				}

				slot.onSlotChange(itemStack1, itemStack)
			} else if (slotIndex >= 10 && slotIndex < 37) {
				if (!mergeItemStack(itemStack1, 37, 46, false)) {
					return null
				}
			} else if (slotIndex >= 37 && slotIndex < 46) {
				if (!mergeItemStack(itemStack1, 10, 37, false)) {
					return null
				}
			} else if (!mergeItemStack(itemStack1, 10, 46, false)) {
				return null
			}

			if (itemStack1.stackSize === 0) {
				slot.putStack(null)
			} else {
				slot.onSlotChanged()
			}

			if (itemStack1.stackSize === itemStack!!.stackSize) {
				return null
			}

			slot.onPickupFromSlot(player, itemStack1)
		}

		return itemStack
	}

}