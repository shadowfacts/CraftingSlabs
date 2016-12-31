package net.shadowfacts.craftingslabs.util

import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.shadowfacts.forgelin.extensions.forEach

/**
 * @author shadowfacts
 */
class InventoryCrafting(width: Int, height: Int): net.minecraft.inventory.InventoryCrafting(null, width, height) {

	private var stackList: Array<ItemStack?>
		get() = ObfuscationReflectionHelper.getPrivateValue<Any, InventoryCrafting>(net.minecraft.inventory.InventoryCrafting::class.java, this, "stackList", "field_70466_a") as Array<ItemStack?>
		set(value) = ObfuscationReflectionHelper.setPrivateValue<InventoryCrafting, Any>(net.minecraft.inventory.InventoryCrafting::class.java, this, value, "stackList", "field_70466_a")

	override fun decrStackSize(index: Int, count: Int): ItemStack? {
		val itemstack = ItemStackHelper.getAndSplit(stackList, index, count)
		return itemstack
	}

	override fun setInventorySlotContents(index: Int, stack: ItemStack?) {
		stackList[index] = stack
	}

	fun writeToNBT(tag: NBTTagCompound) {
		tag.setTag("Items", NBTTagList().apply {
			stackList.forEachIndexed { i, stack ->
				if (stack != null) {
					appendTag(NBTTagCompound().apply {
						setInteger("Slot", i)
						stack.writeToNBT(this)
					})
				}
			}
		})
	}

	fun readFromNBT(tag: NBTTagCompound) {
		val list = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND)
		list.forEach {
			if (it is NBTTagCompound) {
				stackList[it.getInteger("Slot")] = ItemStack.loadItemStackFromNBT(it)
			}
		}
	}

}
