package net.shadowfacts.craftingslabs.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author shadowfacts
 */
@SideOnly(Side.CLIENT)
public class GuiCrafting extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/crafting_table.png");

	public GuiCrafting(InventoryPlayer playerInv, World world) {
		this(playerInv, world, BlockPos.ORIGIN);
	}

	public GuiCrafting(InventoryPlayer playerInv, World world, BlockPos pos) {
		super(new ContainerCrafting(playerInv, world, pos));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString(I18n.format("container.crafting"), 28, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
