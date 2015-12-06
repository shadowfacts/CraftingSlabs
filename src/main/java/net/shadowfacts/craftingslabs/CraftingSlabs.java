package net.shadowfacts.craftingslabs;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.craftingslabs.compat.ModCompat;
import net.shadowfacts.craftingslabs.proxy.CommonProxy;

/**
 * @author shadowfacts
 */
@Mod(modid = CraftingSlabs.modId, name = CraftingSlabs.name)
public class CraftingSlabs {

	public static final String modId = "craftingslabs";
	public static final String name = "CraftingSlabs";

	@Mod.Instance(modId)
	public static CraftingSlabs instance;

	@SidedProxy(serverSide = "net.shadowfacts.craftingslabs.proxy.CommonProxy", clientSide = "net.shadowfacts.craftingslabs.proxy.ClientProxy")
	public static CommonProxy proxy;

//	Content
	public static BlockCraftingSlab craftingSlab;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		craftingSlab = new BlockCraftingSlab();
		GameRegistry.registerBlock(craftingSlab, ItemBlockCraftingSlab.class, "craftingslab", craftingSlab, craftingSlab, false);

		proxy.preInit(event);

		ModCompat.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		ModCompat.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ModCompat.postInit(event);
	}

}
