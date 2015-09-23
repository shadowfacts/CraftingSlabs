package net.shadowfacts.craftingslabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.shadowfacts.shadowmc.util.StringHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author shadowfacts
 */
@SideOnly(Side.CLIENT)
public class GuiCrafting extends GuiContainer {

	private static final ResourceLocation bg = new ResourceLocation("textures/gui/container/crafting_table.png");

	public GuiCrafting(InventoryPlayer inv, World world) {
		super(new ContainerCrafting(inv, world));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(StringHelper.localize("container.crafting"), 28, 6, 4210752);
		fontRendererObj.drawString(StringHelper.localize("container.inventory"), 8, ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f1, int i1, int i2) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(bg);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}
