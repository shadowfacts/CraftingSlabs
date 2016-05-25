package net.shadowfacts.craftingslabs.block;

import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author shadowfacts
 */
public class ModBlocks {

	public BlockCraftingSlab craftingSlab;
	public BlockFurnaceSlab furnaceSlab;

	public void register() {
		craftingSlab = GameRegistry.register(new BlockCraftingSlab());

		furnaceSlab = GameRegistry.register(new BlockFurnaceSlab());
	}

}
