package net.shadowfacts.craftingslabs.proxy;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.compat.craftingtweaks.CompatCraftingTweaks;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;
import net.shadowfacts.shadowmc.compat.CompatRegistrar;

/**
 * @author shadowfacts
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
//		TODO: config

		CraftingSlabs.blocks.register();
		CraftingSlabs.items.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(CraftingSlabs.instance, new GUIHandler());
		registerCompatModules();

		registerTileEntities();
	}

	public void init(FMLInitializationEvent event) {
		registerRecipes();
		registerInventoryModels();
	}

	protected void registerCompatModules() {
		CompatRegistrar.registerModule(CompatCraftingTweaks.class);
	}

	protected void registerInventoryModels() {

	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityFurnaceSlab.class, "furnaceSlab");
	}

	private void registerRecipes() {
		GameRegistry.addShapelessRecipe(new ItemStack(CraftingSlabs.blocks.craftingSlab), Blocks.crafting_table);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.crafting_table), CraftingSlabs.blocks.craftingSlab);

		GameRegistry.addShapelessRecipe(new ItemStack(CraftingSlabs.blocks.furnaceSlab), Blocks.furnace);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.furnace), CraftingSlabs.blocks.furnaceSlab);
	}

}
