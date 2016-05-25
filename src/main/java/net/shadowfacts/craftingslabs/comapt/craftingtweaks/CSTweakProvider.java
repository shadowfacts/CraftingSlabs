package net.shadowfacts.craftingslabs.comapt.craftingtweaks;

import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.blay09.mods.craftingtweaks.api.DefaultProviderV2;
import net.blay09.mods.craftingtweaks.api.TweakProvider;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.container.ContainerCrafting;
import net.shadowfacts.shadowmc.util.KeyboardHelper;

import java.util.List;

/**
 * @author shadowfacts
 */
public class CSTweakProvider implements TweakProvider<ContainerCrafting> {

	private DefaultProviderV2 provider = CraftingTweaksAPI.createDefaultProviderV2();

	@Override
	public String getModId() {
		return CraftingSlabs.modId;
	}

	@Override
	public boolean load() {
		return true;
	}

	@Override
	public void clearGrid(EntityPlayer player, ContainerCrafting container, int id, boolean forced) {
		provider.clearGrid(this, id, player, container, false, forced);
	}

	@Override
	public void rotateGrid(EntityPlayer player, ContainerCrafting container, int id, boolean counterClockwise) {
		provider.rotateGrid(this, id, player, container, counterClockwise);
	}

	@Override
	public void balanceGrid(EntityPlayer player, ContainerCrafting container, int id) {
		provider.balanceGrid(this, id, player, container);
	}

	@Override
	public void spreadGrid(EntityPlayer player, ContainerCrafting container, int id) {
		provider.spreadGrid(this, id, player, container);
	}

	@Override
	public boolean canTransferFrom(EntityPlayer player, ContainerCrafting container, int id, Slot slot) {
		return provider.canTransferFrom(player, container, slot);
	}

	@Override
	public boolean transferIntoGrid(EntityPlayer player, ContainerCrafting container, int id, Slot slot) {
		return provider.transferIntoGrid(this, id, player, container, slot);
	}

	@Override
	public ItemStack putIntoGrid(EntityPlayer player, ContainerCrafting container, int id, ItemStack itemStack, int index) {
		return provider.putIntoGrid(this, id, player, container, itemStack, index);
	}

	@Override
	public IInventory getCraftMatrix(EntityPlayer player, ContainerCrafting crafting, int i) {
		return crafting.matrix;
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
