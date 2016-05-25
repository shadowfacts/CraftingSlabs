package net.shadowfacts.craftingslabs.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.shadowfacts.craftingslabs.CraftingSlabs;

/**
 * @author shadowfacts
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerInventoryModels() {
		registerInvModel(CraftingSlabs.blocks.craftingSlab, 0, "craftingSlab");
		registerInvModel(CraftingSlabs.blocks.furnaceSlab, 0, "furnaceSlab");
		registerInvModel(CraftingSlabs.items.craftingSlab, 0, "craftingSlab");
		registerInvModel(CraftingSlabs.items.furnaceSlab, 0, "furnaceSlab");
	}

	private static void registerInvModel(Block block, int meta, String id) {
		registerInvModel(Item.getItemFromBlock(block), meta, id);
	}

	private static void registerInvModel(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(CraftingSlabs.modId + ":" + id, "inventory"));
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
}
