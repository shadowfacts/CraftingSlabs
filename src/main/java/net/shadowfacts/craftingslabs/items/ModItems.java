package net.shadowfacts.craftingslabs.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameData;
import net.shadowfacts.craftingslabs.CraftingSlabs;

/**
 * @author shadowfacts
 */
public class ModItems {

	public Item craftingSlab;
	public Item furnaceSlab;

	public void register() {
		craftingSlab = GameData.getBlockItemMap().get(CraftingSlabs.blocks.craftingSlab);
		furnaceSlab = GameData.getBlockItemMap().get(CraftingSlabs.blocks.furnaceSlab);
	}

}
