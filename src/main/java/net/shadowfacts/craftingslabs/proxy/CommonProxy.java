package net.shadowfacts.craftingslabs.proxy;

import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.comapt.craftingtweaks.CompatCraftingTweaks;

/**
 * @author shadowfacts
 */
public class CommonProxy {

	public void registerCompatModules() {
		CraftingSlabs.instance.compatManager.registerModule(CompatCraftingTweaks.class);
	}

	public void registerInventoryModels() {

	}



	public World getClientWorld() {
		return null;
	}

}
