package net.shadowfacts.craftingslabs;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.shadowfacts.craftingslabs.compat.ModCompat;

/**
 * @author shadowfacts
 */
@Mod(modid = CraftingSlabs.modId, name = CraftingSlabs.name)
public class CraftingSlabs {

	public static final String modId = "craftingslabs";
	public static final String name = "CraftingSlabs";

	@Mod.Instance(modId)
	public static CraftingSlabs instance;

//	Content
	public static BlockCraftingSlab craftingSlab;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		craftingSlab = new BlockCraftingSlab(false, Material.wood);

		GameRegistry.registerBlock(craftingSlab, "crafting_slab");

		GameRegistry.addShapelessRecipe(new ItemStack(craftingSlab), Blocks.crafting_table);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.crafting_table), craftingSlab);

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		ModCompat.registerModules();
		if (event.getSide() == Side.CLIENT) ModCompat.registerClientModules();

		ModCompat.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ModCompat.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ModCompat.postInit(event);
	}

}
