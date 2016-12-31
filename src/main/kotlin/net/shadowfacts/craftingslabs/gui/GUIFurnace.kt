package net.shadowfacts.craftingslabs.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.shadowfacts.craftingslabs.container.ContainerFurnace
import net.shadowfacts.craftingslabs.multipart.PartFurnaceSlab

/**
 * @author shadowfacts
 */
class GUIFurnace(val playerInv: InventoryPlayer, val furnace: IInventory, world: World, pos: BlockPos): GuiContainer(ContainerFurnace(playerInv, furnace, world, pos)) {

	companion object {
		private val TEXTURE = ResourceLocation("textures/gui/container/furnace.png")
	}

	override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
		val s = I18n.format(furnace.displayName.unformattedText)
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 0x404040)
		fontRendererObj.drawString(playerInv.displayName.formattedText, 8, ySize - 96 + 2, 0x404040)
	}

	override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
		GlStateManager.color(1f, 1f, 1f, 1f)
		mc.textureManager.bindTexture(TEXTURE)
		val guiLeft = (width - xSize) / 2
		val guiTop = (height - ySize) / 2
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)

		if (PartFurnaceSlab.isBurning(furnace)) {
			val k = getBurnLeftScaled(13)
			drawTexturedModalRect(guiLeft + 56, guiTop + 36 + 12 - k, 176, 12 - k, 14, k + 1)
		}

		val l = getCookProgressScaled(24)
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, l + 1, 16)
	}

	private fun getCookProgressScaled(pixels: Int): Int {
		val i = furnace.getField(2)
		val j = furnace.getField(3)
		return if (j != 0 && i != 0) i * pixels / j else 0
	}

	private fun getBurnLeftScaled(pixels: Int): Int {
		var i = furnace.getField(1)
		if (i == 0) {
			i = 200
		}

		return furnace.getField(0) * pixels / i
	}

}