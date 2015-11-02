package net.shadowfacts.craftingslabs.compat.modules.craftingtweaks;

import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.blay09.mods.craftingtweaks.api.DefaultProvider;
import net.blay09.mods.craftingtweaks.api.TweakProvider;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.shadowfacts.craftingslabs.ContainerCrafting;
import net.shadowfacts.craftingslabs.CraftingSlabs;

import java.util.List;

/**
 * @author shadowfacts
 */
public class CSTweakProvider implements TweakProvider {

	private DefaultProvider defaultProvider = CraftingTweaksAPI.createDefaultProvider();

	@Override
	public boolean load() {
		return true;
	}

	@Override
	public void clearGrid(EntityPlayer player, Container container, int i) {
		defaultProvider.clearGrid(player, container, ((ContainerCrafting)container).matrix);
	}

	@Override
	public void rotateGrid(EntityPlayer player, Container container, int i) {
		defaultProvider.rotateGrid(player, container, ((ContainerCrafting)container).matrix);
	}

	@Override
	public void balanceGrid(EntityPlayer player, Container container, int i) {
		defaultProvider.balanceGrid(player, container, ((ContainerCrafting)container).matrix);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui(GuiContainer gui, List list) {
		int x = gui.width / 2 - 80;
		list.add(CraftingTweaksAPI.createRotateButton(0, x, gui.height / 2 - 66));
		list.add(CraftingTweaksAPI.createBalanceButton(0, x, gui.height / 2 - 48));
		list.add(CraftingTweaksAPI.createClearButton(0, x, gui.height / 2 - 30));
	}

	@Override
	public boolean areHotkeysEnabled(EntityPlayer entityPlayer, Container container) {
		return true;
	}

	@Override
	public String getModId() {
		return CraftingSlabs.modId;
	}

}
