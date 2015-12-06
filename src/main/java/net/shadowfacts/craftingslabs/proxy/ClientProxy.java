package net.shadowfacts.craftingslabs.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.compat.ModCompat;

/**
 * @author shadowfacts
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ModCompat.registerClientModules();
	}

	@Override
	protected void registerInventoryModels() {
		registerInventoryModel(Item.getItemFromBlock(CraftingSlabs.craftingSlab), 0, "craftingslab");
	}

	private static void registerInventoryModel(Item item, int meta, String id) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(item, meta, new ModelResourceLocation(CraftingSlabs.modId + ":" + id, "inventory"));
	}
}
