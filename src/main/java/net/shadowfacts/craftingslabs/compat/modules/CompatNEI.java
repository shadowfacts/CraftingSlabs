package net.shadowfacts.craftingslabs.compat.modules;

//import codechicken.nei.api.API;
//import codechicken.nei.recipe.DefaultOverlayHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.craftingslabs.compat.Compat;
import net.shadowfacts.craftingslabs.gui.GuiCrafting;

/**
 * @author shadowfacts
 */
@Compat("NotEnoughItems")
public class CompatNEI {

	@Compat.PreInit
	public static void preInit(FMLPreInitializationEvent event) {
//		API.registerGuiOverlay(GuiCrafting.class, "crafting");
//		API.registerGuiOverlayHandler(GuiCrafting.class, new DefaultOverlayHandler(), "crafting");
	}

}
