package net.shadowfacts.craftingslabs.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.*
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.shadowfacts.craftingslabs.multipart.PartFurnaceSlab
import net.shadowfacts.shadowmc.inventory.ContainerBase

/**
 * @author shadowfacts
 */
class ContainerFurnace(val furnace: IInventory, val world: World, pos: BlockPos): ContainerBase(pos) {

	private var cookTime = 0
	private var totalCookTime = 0
	private var furnaceBurnTime = 0
	private var currentItemBurnTime = 0

	constructor(playerInv: InventoryPlayer, furnace: IInventory, world: World, pos: BlockPos): this(furnace, world, pos) {
		addSlotToContainer(Slot(furnace, 0, 56, 17))
		addSlotToContainer(SlotFurnaceFuel(furnace, 1, 56, 53))
		addSlotToContainer(SlotFurnaceOutput(playerInv.player, furnace, 2, 116, 35))

		for (i in 0.until(3)) {
			for (j in 0.until(9)) {
				addSlotToContainer(Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
			}
		}

		for (i in 0.until(9)) {
			addSlotToContainer(Slot(playerInv, i, 8 + i * 18, 142))
		}
	}

	override fun addListener(listener: IContainerListener) {
		super.addListener(listener)
		listener.sendAllWindowProperties(this, furnace)
	}

	override fun detectAndSendChanges() {
		super.detectAndSendChanges()

		listeners.forEach {
			if (cookTime != furnace.getField(2)) {
				it.sendProgressBarUpdate(this, 2, furnace.getField(2))
			}
			if (furnaceBurnTime != furnace.getField(0)) {
				it.sendProgressBarUpdate(this, 0, furnace.getField(0))
			}
			if (currentItemBurnTime != furnace.getField(1)) {
				it.sendProgressBarUpdate(this, 1, furnace.getField(1))
			}
			if (totalCookTime != furnace.getField(3)) {
				it.sendProgressBarUpdate(this, 3, furnace.getField(3))
			}
		}

		cookTime = furnace.getField(2)
		furnaceBurnTime = furnace.getField(0)
		currentItemBurnTime = furnace.getField(1)
		totalCookTime = furnace.getField(3)
	}

	@SideOnly(Side.CLIENT)
	override fun updateProgressBar(id: Int, data: Int) {
		furnace.setField(id, data)
	}

	override fun canInteractWith(player: EntityPlayer): Boolean {
		return PartFurnaceSlab.getFurnaceSlab(world, pos, null) != null && super.canInteractWith(player)
	}

	override fun transferStackInSlot(playerIn: EntityPlayer, slotIndex: Int): ItemStack? {
		var itemstack: ItemStack? = null
		val slot = inventorySlots[slotIndex]

		if (slot != null && slot.hasStack) {
			val itemstack1 = slot.stack!!
			itemstack = itemstack1.copy()

			if (slotIndex == 2) {
				if (!mergeItemStack(itemstack1, 3, 39, true)) {
					return null
				}

				slot.onSlotChange(itemstack1, itemstack)
			} else if (slotIndex != 1 && slotIndex != 0) {
				if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null) {
					if (!mergeItemStack(itemstack1, 0, 1, false)) {
						return null
					}
				} else if (TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return null
					}
				} else if (slotIndex >= 3 && slotIndex < 30) {
					if (!mergeItemStack(itemstack1, 30, 39, false)) {
						return null
					}
				} else if (slotIndex >= 30 && slotIndex < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
					return null
				}
			} else if (!mergeItemStack(itemstack1, 3, 39, false)) {
				return null
			}

			if (itemstack1.stackSize === 0) {
				slot.putStack(null)
			} else {
				slot.onSlotChanged()
			}

			if (itemstack1.stackSize === itemstack!!.stackSize) {
				return null
			}

			slot.onPickupFromSlot(playerIn, itemstack1)
		}

		return itemstack
	}

}