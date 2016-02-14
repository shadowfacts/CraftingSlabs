package net.shadowfacts.craftingslabs;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.shadowfacts.craftingslabs.block.ModBlocks;
import net.shadowfacts.craftingslabs.items.ModItems;
import net.shadowfacts.craftingslabs.proxy.CommonProxy;
import net.shadowfacts.shadowmc.compat.CompatManager;

/**
 * @author shadowfacts
 */
@Mod(modid = CraftingSlabs.modId, name = CraftingSlabs.name, version = CraftingSlabs.version, dependencies = CraftingSlabs.dependencies, acceptedMinecraftVersions = "[1.8.8,1.8.9]")
public class CraftingSlabs {

	public static final String modId = "CraftingSlabs";
	public static final String name = modId;
	public static final String version = "2.0.0";
	public static final String dependencies = "required-after:shadowmc;after:mcmultipart@[1.0.5,)";

	@Mod.Instance(modId)
	public static CraftingSlabs instance;

	@SidedProxy(serverSide = "net.shadowfacts.craftingslabs.proxy.CommonProxy", clientSide = "net.shadowfacts.craftingslabs.proxy.ClientProxy")
	public static CommonProxy proxy;

	public static ModBlocks blocks = new ModBlocks();
	public static ModItems items = new ModItems();

	public CompatManager compatManager = new CompatManager(modId);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		compatManager.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		compatManager.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		compatManager.postInit(event);
	}

}
