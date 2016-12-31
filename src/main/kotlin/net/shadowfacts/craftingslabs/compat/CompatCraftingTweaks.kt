package net.shadowfacts.craftingslabs.compat

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.shadowfacts.craftingslabs.container.ContainerCrafting
import net.shadowfacts.shadowmc.compat.Compat

/**
 * @author shadowfacts
 */
@Compat("craftingtweaks")
object CompatCraftingTweaks {

	@JvmStatic
	@Compat.Init
	fun init(event: FMLInitializationEvent) {
		FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", NBTTagCompound().apply {
			setString("ContainerClass", ContainerCrafting::class.qualifiedName)
			setInteger("GridSlotNumber", 1)
			setInteger("GridSize", 9)
			setString("AlignToGrid", "left")
		})
	}

}