package net.shadowfacts.craftingslabs.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowfacts.craftingslabs.block.BlockFurnaceSlab;

import static net.minecraft.tileentity.TileEntityFurnace.getItemBurnTime;


/**
 * @author shadowfacts
 */
public class TileEntityFurnaceSlab extends TileEntity implements ITickable, ISidedInventory {

	private static final int[] slotsTop = new int[] {0};
	private static final int[] slotsBottom = new int[] {2, 1};
	private static final int[] slotsSides = new int[] {1};

	private ItemStack[] inventory = new ItemStack[3];

	private int furnaceBurnTime;

	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		if (inventory[slot] != null) {
			if (inventory[slot].stackSize <= count) {
				ItemStack stack = inventory[slot];
				inventory[slot] = null;
				return stack;
			} else {
				ItemStack stack = inventory[slot].splitStack(count);

				if (inventory[slot].stackSize <= 0) {
					inventory[slot] = null;
				}

				return stack;
			}
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if (inventory[slot] != null) {
			ItemStack stack = inventory[slot];
			inventory[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		boolean flag = stack != null && stack.isItemEqual(inventory[slot]) && ItemStack.areItemStackTagsEqual(stack, inventory[slot]);
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		if (slot == 0 && !flag) {
			totalCookTime = getCookTime(stack);
			cookTime = 0;
			markDirty();
		}
	}

	@Override
	public String getName() {
		return "container.furnace";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentTranslation(getName());
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagList list = tag.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound itemTag = list.getCompoundTagAt(i);
			int j = itemTag.getByte("Slot");
			if (j >= 0 && j < inventory.length) {
				inventory[j] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		furnaceBurnTime = tag.getShort("BurnTime");
		cookTime = tag.getShort("CookTime");
		totalCookTime = tag.getShort("CookTimeTotal");
		currentItemBurnTime = TileEntityFurnace.getItemBurnTime(inventory[1]);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setShort("BurnTime", (short)furnaceBurnTime);
		tag.setShort("CookTime", (short)cookTime);
		tag.setShort("CookTimeTotal", (short)totalCookTime);

		NBTTagList list = new NBTTagList();

		for (int i = 0; i < inventory.length; ++i) {
			if (inventory[i] != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				inventory[i].writeToNBT(itemTag);
				list.appendTag(itemTag);
			}
		}

		tag.setTag("Items", list);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	@Override
	public void update() {
		boolean flag = isBurning();
		boolean flag1 = false;

		if (isBurning()) {
			--furnaceBurnTime;
		}

		if (!worldObj.isRemote) {
			if (isBurning() || inventory[1] != null && inventory[0] != null) {
				if (!isBurning() && canSmelt()) {
					currentItemBurnTime = furnaceBurnTime = TileEntityFurnace.getItemBurnTime(inventory[1]);

					if (isBurning()) {
						flag1 = true;

						if (inventory[1] != null) {
							--inventory[1].stackSize;

							if (inventory[1].stackSize <= 0) {
								inventory[1] = inventory[1].getItem().getContainerItem(inventory[1]);
							}
						}
					}
				}

				if (isBurning() && canSmelt()) {
					++cookTime;
					if (cookTime == totalCookTime) {
						cookTime = 0;
						totalCookTime = getCookTime(inventory[0]);
						smeltItem();
						flag1 = true;
					}
				} else {
					cookTime = 0;
				}
			} else if (!isBurning() && cookTime > 0) {
				cookTime = MathHelper.clamp_int(cookTime - 2, 0, totalCookTime);
			}

			if (flag != isBurning()) {
				flag1 = true;
				BlockFurnaceSlab.setState(isBurning(), worldObj, pos);
			}
		}

		if (flag1) {
			markDirty();
		}
	}

	public int getCookTime(ItemStack stack) {
		return 200;
	}

	private boolean canSmelt() {
		if (inventory[0] == null) {
			return false;
		} else {
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult(inventory[0]);
			if (result == null) {
				return false;
			}
			if (inventory[2] == null) {
				return true;
			}
			if (!inventory[2].isItemEqual(result)) {
				return false;
			}
			int resultCount = inventory[2].stackSize + result.stackSize;
			return resultCount <= getInventoryStackLimit() && resultCount <= inventory[2].getMaxStackSize();
		}
	}

	public void smeltItem() {
		if (canSmelt()) {
			ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(inventory[0]);
			if (inventory[2] == null) {
				inventory[2] = stack.copy();
			} else if (inventory[2].getItem() == stack.getItem()) {
				inventory[2].stackSize += stack.stackSize;
			}

			if (inventory[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && inventory[0].getMetadata() == 1 && inventory[1] != null && inventory[1].getItem() == Items.bucket) {
				inventory[1] = new ItemStack(Items.water_bucket);
			}

			--inventory[0].stackSize;

			if (inventory[0].stackSize <= 0) {
				inventory[0] = null;
			}
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq((double) pos.getX() + .5d, (double) pos.getY() + .5d, (double) pos.getZ() + .5d) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index != 2 && (index != 1 || (TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack)));
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
		return isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		if (side == EnumFacing.DOWN && slot == 1) {
			Item item = stack.getItem();
			if (item != Items.water_bucket && item != Items.bucket) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getField(int id) {
		switch (id) {
			case 0:
				return furnaceBurnTime;
			case 1:
				return currentItemBurnTime;
			case 2:
				return cookTime;
			case 3:
				return totalCookTime;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
			case 0:
				this.furnaceBurnTime = value;
				break;
			case 1:
				this.currentItemBurnTime = value;
				break;
			case 2:
				cookTime = value;
				break;
			case 3:
				totalCookTime = value;
				break;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inventory.length; ++i) {
			inventory[i] = null;
		}
	}
}
