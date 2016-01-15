package net.shadowfacts.craftingslabs.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.items.ItemBlockCraftingSlab;

/**
 * @author shadowfacts
 */
public class ModBlocks {

	public BlockCraftingSlab craftingSlab;

	public void register() {
		craftingSlab = new BlockCraftingSlab();
		GameRegistry.registerBlock(craftingSlab, ItemBlockCraftingSlab.class, "craftingSlab", craftingSlab, craftingSlab);
	}

	private static <T extends Block> T register(T block, String name) {
		GameRegistry.registerBlock(block, name);
		return block;
	}

}
