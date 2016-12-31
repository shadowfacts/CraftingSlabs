package net.shadowfacts.craftingslabs.compat

import mezz.jei.api.BlankModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import net.minecraft.item.ItemStack
import net.shadowfacts.craftingslabs.CraftingSlabs
import net.shadowfacts.craftingslabs.container.ContainerCrafting
import net.shadowfacts.craftingslabs.container.ContainerFurnace
import net.shadowfacts.craftingslabs.gui.GUICrafting
import net.shadowfacts.craftingslabs.gui.GUIFurnace

/**
 * @author shadowfacts
 */
@JEIPlugin
class CompatJEI: BlankModPlugin() {

	override fun register(registry: IModRegistry) {
		registry.recipeTransferRegistry.addRecipeTransferHandler(ContainerCrafting::class.java, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36)
		registry.recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnace::class.java, VanillaRecipeCategoryUid.SMELTING, 0, 1, 1, 36)

		registry.addRecipeClickArea(GUICrafting::class.java, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING)
		registry.addRecipeClickArea(GUIFurnace::class.java, 78, 32, 28, 23, VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL)

		registry.addRecipeCategoryCraftingItem(ItemStack(CraftingSlabs.items.craftingSlab), VanillaRecipeCategoryUid.CRAFTING)
		registry.addRecipeCategoryCraftingItem(ItemStack(CraftingSlabs.items.furnaceSlab), VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL)

	}

}