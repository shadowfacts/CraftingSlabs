package net.shadowfacts.craftingslabs.proxy;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.CSConfig;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.compat.craftingtweaks.CompatCraftingTweaks;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
import net.shadowfacts.craftingslabs.multipart.crafting.PartCraftingSlab;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlab;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;
import net.shadowfacts.shadowmc.compat.CompatRegistrar;

/**
 * @author shadowfacts
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		CSConfig.initialize(event);

		CraftingSlabs.blocks.register();
		CraftingSlabs.items.register();

		if (Loader.isModLoaded("mcmultipart")) {
			registerMultiParts();
		}

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
		if (CSConfig.enableCraftingMultipart && Loader.isModLoaded("mcmultipart")) {
			GameRegistry.addShapelessRecipe(new ItemStack(CraftingSlabs.items.partCraftingSlab), CraftingSlabs.blocks.craftingSlab);
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.crafting_table), CraftingSlabs.items.partCraftingSlab);
		} else {
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.crafting_table), CraftingSlabs.blocks.craftingSlab);
		}

		GameRegistry.addShapelessRecipe(new ItemStack(CraftingSlabs.blocks.furnaceSlab), Blocks.furnace);
		if (CSConfig.enableFurnaceMultipart && Loader.isModLoaded("mcmultipart")) {
			GameRegistry.addShapelessRecipe(new ItemStack(CraftingSlabs.items.partFurnaceSlab), CraftingSlabs.blocks.furnaceSlab);
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.furnace), CraftingSlabs.items.partFurnaceSlab);
		} else {
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.furnace), CraftingSlabs.blocks.furnaceSlab);
		}
	}

	private void registerMultiParts() {
		MultipartRegistry.registerPart(PartCraftingSlab.class, "partCraftingSlab");
		MultipartRegistry.registerPart(PartFurnaceSlab.class, "partFurnaceSlab");
	}

	public World getClientWorld() {
		return null;
	}

}
