package net.shadowfacts.craftingslabs.compat.modules.craftingtweaks;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.shadowfacts.craftingslabs.ContainerCrafting;
import net.shadowfacts.craftingslabs.compat.Compat;

/**
 * @author shadowfacts
 */
@Compat("craftingtweaks")
public class CompatCraftingTweaks {

	@Compat.PostInit
	public static void postInit(FMLPostInitializationEvent event) {
		CraftingTweaksAPI.registerProvider(ContainerCrafting.class, new CSTweakProvider());
	}

}
