package net.shadowfacts.craftingslabs.proxy;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.compat.craftingtweaks.CompatCraftingTweaks;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
import net.shadowfacts.craftingslabs.multipart.crafting.PartCraftingSlab;
import net.shadowfacts.craftingslabs.multipart.crafting.PartCraftingSlabConverter;
import net.shadowfacts.craftingslabs.multipart.crafting.PartCraftingSlabReverseConverter;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlab;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlabConverter;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlabReverseConverter;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;

/**
 * @author shadowfacts
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		CraftingSlabs.blocks.register();
		CraftingSlabs.items.register();

		if (Loader.isModLoaded("mcmultipart")) {
			initMultiparts();
		}

		NetworkRegistry.INSTANCE.registerGuiHandler(CraftingSlabs.instance, new GUIHandler());
		registerCompatModules();

		registerTileEntities();
	}

	public void init(FMLInitializationEvent event) {
		registerRecipes();
		registerInventoryModels();
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	protected void registerCompatModules() {
		CraftingSlabs.instance.compatManager.registerModule(CompatCraftingTweaks.class);
	}

	protected void registerInventoryModels() {

	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityFurnaceSlab.class, "furnaceSlab");
	}

	private void registerRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftingSlabs.items.craftingSlab), "SS", "SS", 'S', "slabWood"));

		GameRegistry.addShapedRecipe(new ItemStack(CraftingSlabs.items.furnaceSlab), "SS", "SS", 'S', new ItemStack(Blocks.stone_slab, 1, 3));
	}

	@Optional.Method(modid = "mcmultipart")
	public static void initMultiparts() {
		MultipartRegistry.registerPart(PartCraftingSlab.class, "craftingSlab");
		MultipartRegistry.registerPart(PartFurnaceSlab.class, "furnaceSlab");

		MultipartRegistry.registerPartConverter(new PartCraftingSlabConverter());
		MultipartRegistry.registerReversePartConverter(new PartCraftingSlabReverseConverter());

		MultipartRegistry.registerPartConverter(new PartFurnaceSlabConverter());
		MultipartRegistry.registerReversePartConverter(new PartFurnaceSlabReverseConverter());
	}

	public World getClientWorld() {
		return null;
	}
}
