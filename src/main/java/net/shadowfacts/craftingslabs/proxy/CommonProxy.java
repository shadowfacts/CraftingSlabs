package net.shadowfacts.craftingslabs.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.GuiHandler;
import net.shadowfacts.craftingslabs.compat.ModCompat;

/**
 * @author shadowfacts
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(CraftingSlabs.instance, new GuiHandler());
		ModCompat.registerModules();
	}

	public void init(FMLInitializationEvent event) {
		registerInventoryModels();
	}

	protected void registerInventoryModels() {}

}
