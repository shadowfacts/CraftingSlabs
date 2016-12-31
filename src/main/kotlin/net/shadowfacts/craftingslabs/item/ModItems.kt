package net.shadowfacts.craftingslabs.item

import net.shadowfacts.shadowmc.item.ModItems

/**
 * @author shadowfacts
 */
object ModItems : ModItems() {

	val craftingSlab = ItemCraftingSlab()
	val furnaceSlab = ItemFurnaceSlab()

	override fun init() {
		register(craftingSlab)
		register(furnaceSlab)
	}

}