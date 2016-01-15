package net.shadowfacts.craftingslabs.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.compat.craftingtweaks.CompatCraftingTweaks;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
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
	}

	public void init(FMLInitializationEvent event) {
		registerInventoryModels();
	}

	protected void registerCompatModules() {
		CompatRegistrar.registerModule(CompatCraftingTweaks.class);
	}

	protected void registerInventoryModels() {

	}

}
