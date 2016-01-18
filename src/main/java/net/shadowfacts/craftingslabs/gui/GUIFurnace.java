package net.shadowfacts.craftingslabs.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.shadowfacts.craftingslabs.container.ContainerFurnace;
import net.shadowfacts.craftingslabs.multipart.furnace.PartFurnaceSlab;
import net.shadowfacts.craftingslabs.tileentity.TileEntityFurnaceSlab;

/**
 * @author shadowfacts
 */
public class GUIFurnace extends GuiContainer {

	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");

	private final InventoryPlayer playerInv;
	private IInventory furnace;

	public GUIFurnace(InventoryPlayer playerInv, IInventory furnace, World world, BlockPos pos) {
		super(new ContainerFurnace(playerInv, furnace, world, pos));
		this.playerInv = playerInv;
		this.furnace = furnace;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = furnace.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

		if (TileEntityFurnaceSlab.isBurning(furnace)) {
			int k = getBurnLeftScaled(13);
			drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int l = getCookProgressScaled(24);
		drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}

	private int getCookProgressScaled(int pixels) {
		int i = furnace.getField(2);
		int j = furnace.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private int getBurnLeftScaled(int pixels) {
		int i = furnace.getField(1);
		if (i == 0); {
			i = 200;
		}

		return furnace.getField(0) * pixels / i;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if (Loader.isModLoaded("mcmultipart") && furnace instanceof PartFurnaceSlab) {
			((PartFurnaceSlab)furnace).playerUsingGUI = null;
		}
	}
}
