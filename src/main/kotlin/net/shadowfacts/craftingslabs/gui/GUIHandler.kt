package net.shadowfacts.craftingslabs.gui

import mcmultipart.multipart.PartSlot
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.shadowfacts.craftingslabs.container.ContainerCrafting
import net.shadowfacts.craftingslabs.container.ContainerFurnace
import net.shadowfacts.craftingslabs.multipart.PartCraftingSlab
import net.shadowfacts.craftingslabs.multipart.PartFurnaceSlab

/**
 * @author shadowfacts
 */
object GUIHandler: IGuiHandler {

	val CRAFTING_BOTTOM = 0
	val CRAFTING_TOP = 1
	val FURNACE_BOTTOM = 2
	val FURNACE_TOP = 3

	override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		return when (id) {
			CRAFTING_BOTTOM -> GUICrafting(player.inventory, PartCraftingSlab.getCraftingSlab(world, pos, PartSlot.DOWN)!!, world, pos)
			CRAFTING_TOP -> GUICrafting(player.inventory, PartCraftingSlab.getCraftingSlab(world, pos, PartSlot.UP)!!, world, pos)
			FURNACE_BOTTOM -> GUIFurnace(player.inventory, PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.DOWN)!!, world, pos)
			FURNACE_TOP -> GUIFurnace(player.inventory, PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.UP)!!, world, pos)
			else -> null
		}
	}

	override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		return when (id) {
			CRAFTING_BOTTOM -> ContainerCrafting(player.inventory, PartCraftingSlab.getCraftingSlab(world, pos, PartSlot.DOWN)!!, world, pos)
			CRAFTING_TOP -> ContainerCrafting(player.inventory, PartCraftingSlab.getCraftingSlab(world, pos, PartSlot.UP)!!, world, pos)
			FURNACE_BOTTOM -> ContainerFurnace(player.inventory, PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.DOWN)!!, world, pos)
			FURNACE_TOP -> ContainerFurnace(player.inventory, PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.UP)!!, world, pos)
			else -> null
		}
	}

}