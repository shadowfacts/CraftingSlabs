package net.shadowfacts.craftingslabs.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * @author shadowfacts
 */
public class GUICrafting extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");

	public GUICrafting(Container container) {
		super(container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString(I18n.format("container.crafting"), 26, 6, 0x404040);
		fontRendererObj.drawString(I18n.format("container.crafting"), 8, ySize - 94, 0x404040);
	}
}
