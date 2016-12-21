package net.shadowfacts.craftingslabs

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.shadowfacts.craftingslabs.block.BlockCraftingSlab

/**
 * @author shadowfacts
 */
@Mod(modid = MOD_ID, name = NAME, version = VERSION, dependencies = "required-after:mcmultipart;required-after:shadowmc;", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object CraftingSlabs {

//	Content
	val craftingSlab = BlockCraftingSlab()

	@Mod.EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		GameRegistry.register(craftingSlab)
	}

}