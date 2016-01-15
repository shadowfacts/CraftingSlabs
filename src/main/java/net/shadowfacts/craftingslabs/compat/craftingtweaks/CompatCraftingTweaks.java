package net.shadowfacts.craftingslabs.compat.craftingtweaks;

import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.shadowfacts.craftingslabs.container.ContainerCrafting;
import net.shadowfacts.shadowmc.compat.Compat;

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
