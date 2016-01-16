package net.shadowfacts.craftingslabs.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.items.ItemCraftingSlab;
import net.shadowfacts.craftingslabs.items.ItemFurnaceSlab;

/**
 * @author shadowfacts
 */
public class ModBlocks {

	public BlockCraftingSlab craftingSlab;
	public BlockFurnaceSlab furnaceSlab;

	public void register() {
		craftingSlab = new BlockCraftingSlab();
		GameRegistry.registerBlock(craftingSlab, ItemCraftingSlab.class, "craftingSlab", craftingSlab);
		furnaceSlab = new BlockFurnaceSlab();
		GameRegistry.registerBlock(furnaceSlab, ItemFurnaceSlab.class, "furnaceSlab", furnaceSlab);
	}

	private static <T extends Block> T register(T block, String name) {
		GameRegistry.registerBlock(block, name);
		return block;
	}

}
