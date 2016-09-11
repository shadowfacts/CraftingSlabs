package net.shadowfacts.craftingslabs.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.shadowfacts.craftingslabs.container.ContainerCrafting

/**
 * @author shadowfacts
 */
class GUICrafting(playerInv: InventoryPlayer, world: World, pos: BlockPos) : GuiContainer(ContainerCrafting(playerInv, world, pos)) {

	companion object {
		private val TEXTURE = ResourceLocation("textures/gui/container/crafting_table.png")
	}

	override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
		fontRendererObj.drawString(I18n.format("container.crafting"), 28, 6, 0x404040)
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 94, 0x404040)
	}

	override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
		GlStateManager.color(1f, 1f, 1f, 1f)
		mc.textureManager.bindTexture(TEXTURE)
		val guiLeft = (width - xSize) / 2
		val guiTop = (height - ySize) / 2
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
	}

}