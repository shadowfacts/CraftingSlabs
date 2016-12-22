package net.shadowfacts.craftingslabs;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.block.BlockCraftingSlab;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
import net.shadowfacts.craftingslabs.tileentity.TileEntityCraftingSlab;

/**
 * @author shadowfacts
 */
@Mod(modid = CraftingSlabs.MOD_ID, name = CraftingSlabs.NAME, version = CraftingSlabs.VERSION, dependencies = "required-before:mcmultipart;required-after:shadowmc;")
public class CraftingSlabs {

	public static final String MOD_ID = "craftingslabs";
	public static final String NAME = "Crafting Slabs";
	public static final String VERSION = "@VERSION@";

	@Mod.Instance
	public static CraftingSlabs instance;

//	Content
	public static BlockCraftingSlab craftingSlab = new BlockCraftingSlab();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.register(craftingSlab);
		GameRegistry.registerTileEntity(TileEntityCraftingSlab.class, MOD_ID + ":crafting_slab");
		GameRegistry.register(new ItemBlock(craftingSlab).setRegistryName(craftingSlab.getRegistryName()));

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());

	}

}
