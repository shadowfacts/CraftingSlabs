package net.shadowfacts.craftingslabs.items;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.CraftingSlabs;

/**
 * @author shadowfacts
 */
public class ModItems {

	public ItemCraftingSlab craftingSlab;
	public ItemFurnaceSlab furnaceSlab;

	public void register() {
		craftingSlab = GameRegistry.register(new ItemCraftingSlab(CraftingSlabs.blocks.craftingSlab));
		furnaceSlab = GameRegistry.register(new ItemFurnaceSlab(CraftingSlabs.blocks.furnaceSlab));
	}

}
