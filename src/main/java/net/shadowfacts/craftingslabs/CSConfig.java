package net.shadowfacts.craftingslabs;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.shadowmc.config.Config;
import net.shadowfacts.shadowmc.config.ConfigManager;
import net.shadowfacts.shadowmc.config.ConfigProperty;

/**
 * @author shadowfacts
 */
@Config(name = "CraftingSlabs")
public class CSConfig {

	@ConfigProperty(category = "multipart", comment = "Enable the multipart version of the crafting slab.\nRequires MCMultiPart.")
	public static boolean enableCraftingMultipart = true;

	@ConfigProperty(category = "multipart", comment = "Enable the multipart version of the furnace slab.\nRequires MCMultiPart.")
	public static boolean enableFurnaceMultipart = true;

	public static void initialize(FMLPreInitializationEvent event) {
		ConfigManager.instance.configDir = event.getModConfigurationDirectory();
		ConfigManager.instance.register(CraftingSlabs.modId, CSConfig.class, CraftingSlabs.modId);
		ConfigManager.instance.load(CraftingSlabs.modId);
	}

}
