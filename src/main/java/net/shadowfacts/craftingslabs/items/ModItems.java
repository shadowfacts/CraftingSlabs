package net.shadowfacts.craftingslabs.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.CSConfig;
import net.shadowfacts.craftingslabs.multipart.crafting.ItemPartCraftingSlab;
import net.shadowfacts.craftingslabs.multipart.furnace.ItemPartFurnaceSlab;

/**
 * @author shadowfacts
 */
public class ModItems {

	public Item partCraftingSlab; // null if disabled
	public Item partFurnaceSlab; // null if disabled

	public void register() {
		if (Loader.isModLoaded("mcmultipart")) {
			if (CSConfig.enableCraftingMultipart) {
				partCraftingSlab = register(new ItemPartCraftingSlab(), "partCraftingSlab");
			}
			if (CSConfig.enableFurnaceMultipart) {
				partFurnaceSlab = register(new ItemPartFurnaceSlab(), "partFurnaceSlab");
			}
		}
	}

	private <T extends Item> T register(T item, String name) {
		GameRegistry.registerItem(item, name);
		return item;
	}

}
