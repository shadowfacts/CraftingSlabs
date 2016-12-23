package net.shadowfacts.craftingslabs;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowfacts.craftingslabs.block.BlockCraftingSlab;
import net.shadowfacts.craftingslabs.block.BlockFurnaceSlab;
import net.shadowfacts.craftingslabs.gui.GUIHandler;
import net.shadowfacts.craftingslabs.network.PacketUpdateFurnaceSlab;
import net.shadowfacts.craftingslabs.tileentity.TileEntityCraftingSlab;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;

/**
 * @author shadowfacts
 */
@Mod(modid = CraftingSlabs.MOD_ID, name = CraftingSlabs.NAME, version = CraftingSlabs.VERSION, dependencies = "required-before:mcmultipart;required-after:shadowmc;")
public class CraftingSlabs {

	public static final String MOD_ID = "craftingslabs";
	public static final String NAME = "Crafting Slabs";
	public static final String VERSION = "@VERSION@";

	@Mod.Instance
	public static CraftingSlabs instance;

	public static SimpleNetworkWrapper network;

//	Content
	public static BlockCraftingSlab craftingSlab = new BlockCraftingSlab();
	public static BlockFurnaceSlab furnaceSlab = new BlockFurnaceSlab();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.register(craftingSlab);
		GameRegistry.registerTileEntity(TileEntityCraftingSlab.class, MOD_ID + ":crafting_slab");
		GameRegistry.register(new ItemBlock(craftingSlab).setRegistryName(craftingSlab.getRegistryName()));

		GameRegistry.register(furnaceSlab);
		GameRegistry.registerTileEntity(TileEntityFurnaceSlab.class, MOD_ID + ":furnace_slab");
		GameRegistry.register(new ItemBlock(furnaceSlab).setRegistryName(furnaceSlab.getRegistryName()));

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
		network.registerMessage(PacketUpdateFurnaceSlab.class, PacketUpdateFurnaceSlab.class, 0, Side.CLIENT);
	}

	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(craftingSlab), 0, new ModelResourceLocation(new ResourceLocation(MOD_ID, "crafting_slab"), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(furnaceSlab), 0, new ModelResourceLocation(new ResourceLocation(MOD_ID, "furnace_slab"), "inventory"));
	}

}
