package net.shadowfacts.craftingslabs;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.block.BlockCraftingSlab;

/**
 * @author shadowfacts
 */
@Mod(modid = CraftingSlabs.MOD_ID, name = CraftingSlabs.NAME, version = CraftingSlabs.VERSION, dependencies = "required-before:mcmultipart;required-after:shadowmc;")
public class CraftingSlabs {

	public static final String MOD_ID = "craftingslabs";
	public static final String NAME = "Crafting Slabs";
	public static final String VERSION = "@VERSION@";

//	Content
	public static BlockCraftingSlab craftingSlab = new BlockCraftingSlab();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.register(craftingSlab);
		GameRegistry.register(new ItemBlock(craftingSlab).setRegistryName(craftingSlab.getRegistryName()));

	}

}
