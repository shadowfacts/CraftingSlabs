package net.shadowfacts.craftingslabs.gui

import mcmultipart.multipart.PartSlot
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.shadowfacts.craftingslabs.container.ContainerCrafting
import net.shadowfacts.craftingslabs.container.ContainerFurnace
import net.shadowfacts.craftingslabs.multipart.PartFurnaceSlab

/**
 * @author shadowfacts
 */
object GUIHandler : IGuiHandler {

	override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		return when (id) {
			// crafting
			0 -> GUICrafting(player.inventory, world, pos)
			// furnace bottom
			1 -> GUIFurnace(player.inventory, PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.DOWN)!!, world, pos)
			// furnace top
			2 -> GUIFurnace(player.inventory, PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.UP)!!, world, pos)
			else -> null
		}
	}

	override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val pos = BlockPos(x, y, z)
		return when (id) {
			// crafting
			0 -> ContainerCrafting(player.inventory, world, pos)
			// furnace bottom
			1 -> ContainerFurnace(player.inventory, PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.DOWN)!!, world, pos)
			// furnace top
			2 -> ContainerFurnace(player.inventory, PartFurnaceSlab.getFurnaceSlab(world, pos, PartSlot.UP)!!, world, pos)
			else -> null
		}
	}

}