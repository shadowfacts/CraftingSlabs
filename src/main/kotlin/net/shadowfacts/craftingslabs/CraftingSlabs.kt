package net.shadowfacts.craftingslabs

import mcmultipart.multipart.MultipartRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.shadowfacts.craftingslabs.gui.GUIHandler
import net.shadowfacts.craftingslabs.item.ModItems
import net.shadowfacts.craftingslabs.multipart.PartCraftingSlab
import net.shadowfacts.craftingslabs.multipart.PartFurnaceSlab
import net.shadowfacts.shadowmc.compat.CompatManager

/**
 * @author shadowfacts
 */
@Mod(modid = MODID, version = VERSION, name = NAME, dependencies = "required-after:shadowmc;", acceptedMinecraftVersions = "[1.10.2]", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object CraftingSlabs {

	private val compat = CompatManager(MODID)

	val items = ModItems

	@Mod.EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		MultipartRegistry.registerPart(PartCraftingSlab::class.java, "partCraftingSlab")
		MultipartRegistry.registerPart(PartFurnaceSlab::class.java, "partFurnaceSlab")
		items.init()

		NetworkRegistry.INSTANCE.registerGuiHandler(CraftingSlabs, GUIHandler)

		compat.preInit(event)
	}

	@Mod.EventHandler
	fun init(event: FMLInitializationEvent) {
		compat.init(event)
	}

	@Mod.EventHandler
	fun postInit(event: FMLPostInitializationEvent) {
		compat.postInit(event)
	}

}