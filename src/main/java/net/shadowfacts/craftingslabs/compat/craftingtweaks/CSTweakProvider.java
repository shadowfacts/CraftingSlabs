package net.shadowfacts.craftingslabs.compat.craftingtweaks;

import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.blay09.mods.craftingtweaks.api.DefaultProvider;
import net.blay09.mods.craftingtweaks.api.TweakProvider;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.container.ContainerCrafting;

import java.util.List;

/**
 * @author shadowfacts
 */
public class CSTweakProvider implements TweakProvider {

	private DefaultProvider defaultProvider = CraftingTweaksAPI.createDefaultProvider();

	@Override
	public String getModId() {
		return CraftingSlabs.modId;
	}

	@Override
	public boolean load() {
		return true;
	}

	@Override
	public void clearGrid(EntityPlayer entityPlayer, Container container, int i) {
		defaultProvider.clearGrid(entityPlayer, container, ((ContainerCrafting)container).matrix);
	}

	@Override
	public void rotateGrid(EntityPlayer entityPlayer, Container container, int i) {
		defaultProvider.rotateGrid(entityPlayer, container, ((ContainerCrafting)container).matrix);
	}

	@Override
	public void balanceGrid(EntityPlayer entityPlayer, Container container, int i) {
		defaultProvider.balanceGrid(entityPlayer, container, ((ContainerCrafting)container).matrix);
	}

	@Override
	public boolean canTransferFrom(EntityPlayer entityPlayer, Container container, int i, Slot slot) {
		return defaultProvider.canTransferFrom(entityPlayer, container, i, slot);
	}

	@Override
	public boolean transferIntoGrid(EntityPlayer entityPlayer, Container container, int i, Slot slot) {
		return defaultProvider.transferIntoGrid(entityPlayer, container, ((ContainerCrafting)container).matrix, slot);
	}

	@Override
	public ItemStack putIntoGrid(EntityPlayer entityPlayer, Container container, int i, ItemStack itemStack, int i1) {
		return defaultProvider.putIntoGrid(entityPlayer, container, ((ContainerCrafting)container).matrix, itemStack, i1);
	}

	@Override
	public IInventory getCraftMatrix(EntityPlayer entityPlayer, Container container, int i) {
		return ((ContainerCrafting)container).matrix;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initGui(GuiContainer gui, List<GuiButton> list) {
		int x = gui.width / 2 - 80;
		list.add(CraftingTweaksAPI.createRotateButton(0, x, gui.height / 2 - 66));
		list.add(CraftingTweaksAPI.createBalanceButton(0, x, gui.height / 2 - 48));
		list.add(CraftingTweaksAPI.createClearButton(0, x, gui.height / 2 - 30));
	}

}
