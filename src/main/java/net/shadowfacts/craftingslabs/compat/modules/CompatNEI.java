package net.shadowfacts.craftingslabs.compat.modules;

import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.craftingslabs.GuiCrafting;
import net.shadowfacts.craftingslabs.compat.Compat;

/**
 * @author shadowfacts
 */
@Compat("NotEnoughItems")
public class CompatNEI {

	@Compat.PreInit
	public static void preInit(FMLPreInitializationEvent event) {
		API.registerGuiOverlay(GuiCrafting.class, "crafting");
		API.registerGuiOverlayHandler(GuiCrafting.class, new DefaultOverlayHandler(), "crafting");
	}

}
