package net.shadowfacts.craftingslabs;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.shadowfacts.craftingslabs.block.ModBlocks;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
import net.shadowfacts.craftingslabs.items.ModItems;
import net.shadowfacts.craftingslabs.multipart.crafting.PartCraftingSlab;
import net.shadowfacts.craftingslabs.multipart.crafting.PartCraftingSlabConverter;
import net.shadowfacts.craftingslabs.multipart.crafting.PartCraftingSlabReverseConverter;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlab;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlabConverter;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlabReverseConverter;
import net.shadowfacts.craftingslabs.proxy.CommonProxy;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;
import net.shadowfacts.shadowmc.compat.CompatManager;

/**
 * @author shadowfacts
 */
@Mod(modid = CraftingSlabs.modId, name = CraftingSlabs.name, version = CraftingSlabs.version, dependencies = "required-after:shadowmc;after:mcmultipart@[1.1.1,)", acceptedMinecraftVersions = "[1.9]")
public class CraftingSlabs {

	public static final String modId = "CraftingSlabs";
	public static final String name = modId;
	public static final String version = "2.2.0";

	@Mod.Instance(modId)
	public static CraftingSlabs instance;

	@SidedProxy(serverSide = "net.shadowfacts.craftingslabs.proxy.CommonProxy", clientSide = "net.shadowfacts.craftingslabs.proxy.ClientProxy")
	public static CommonProxy proxy;

	public static ModBlocks blocks = new ModBlocks();
	public static ModItems items = new ModItems();

	public CompatManager compatManager = new CompatManager(modId);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		CraftingSlabs.blocks.register();
		CraftingSlabs.items.register();

		if (Loader.isModLoaded("mcmultipart")) {
			initMultiparts();
		}

		NetworkRegistry.INSTANCE.registerGuiHandler(CraftingSlabs.instance, new GUIHandler());
		proxy.registerCompatModules();

		registerTileEntities();

		proxy.registerInventoryModels();

		compatManager.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		registerRecipes();

		compatManager.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		compatManager.postInit(event);
	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityFurnaceSlab.class, "furnaceSlab");
	}

	private void registerRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftingSlabs.items.craftingSlab), "SS", "SS", 'S', "slabWood"));

		GameRegistry.addShapedRecipe(new ItemStack(CraftingSlabs.items.furnaceSlab), "SS", "SS", 'S', new ItemStack(Blocks.STONE_SLAB, 1, 3));
	}

	@Optional.Method(modid = "mcmultipart")
	private static void initMultiparts() {
		MultipartRegistry.registerPart(PartCraftingSlab.class, "partCraftingSlab");
		MultipartRegistry.registerPart(PartFurnaceSlab.class, "partFurnaceSlab");

		MultipartRegistry.registerPartConverter(new PartCraftingSlabConverter());
		MultipartRegistry.registerReversePartConverter(new PartCraftingSlabReverseConverter());

		MultipartRegistry.registerPartConverter(new PartFurnaceSlabConverter());
		MultipartRegistry.registerReversePartConverter(new PartFurnaceSlabReverseConverter());
	}

}
