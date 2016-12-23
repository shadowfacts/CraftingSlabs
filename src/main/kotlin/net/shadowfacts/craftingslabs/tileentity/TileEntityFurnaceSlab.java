package net.shadowfacts.craftingslabs.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.network.PacketRequestUpdateFurnaceSlab;
import net.shadowfacts.craftingslabs.network.PacketUpdateFurnaceSlab;
import net.shadowfacts.shadowmc.capability.CapHolder;
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity;

/**
 * @author shadowfacts
 */
public class TileEntityFurnaceSlab extends BaseTileEntity implements ITickable, ISidedInventory {

	private static final int[] SLOTS_TOP = new int[] {0};
	private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
	private static final int[] SLOTS_SIDES = new int[] {1};

	@CapHolder(capabilities = IItemHandler.class, sides = EnumFacing.UP)
	private IItemHandler wrapperUp = new SidedInvWrapper(this, EnumFacing.UP);
	@CapHolder(capabilities = IItemHandler.class, sides = EnumFacing.DOWN)
	private IItemHandler wrapperDown = new SidedInvWrapper(this, EnumFacing.DOWN);
	@CapHolder(capabilities = IItemHandler.class, sides = {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST})
	private IItemHandler wrapperSide = new SidedInvWrapper(this, EnumFacing.NORTH);

	private NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);

	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	@Override
	public void onLoad() {
		if (world.isRemote) {
			PacketRequestUpdateFurnaceSlab msg = new PacketRequestUpdateFurnaceSlab(this);
			CraftingSlabs.network.sendToServer(msg);
		}
	}

	@Override
	public int getSizeInventory() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return inventory.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		return ItemStackHelper.getAndSplit(inventory, slot, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		return ItemStackHelper.getAndRemove(inventory, slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		ItemStack current = inventory.get(slot);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(current) && ItemStack.areItemStackTagsEqual(stack, current);
		inventory.set(slot, stack);

		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
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
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(getName());
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("BurnTime", furnaceBurnTime);
		tag.setInteger("CookTime", cookTime);
		tag.setInteger("CookTimeTotal", totalCookTime);
		ItemStackHelper.saveAllItems(tag, inventory);
		return super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(tag, inventory);
		furnaceBurnTime = tag.getInteger("BurnTime");
		cookTime = tag.getInteger("CookTime");
		totalCookTime = tag.getInteger("CookTimeTotal");
		currentItemBurnTime = TileEntityFurnace.getItemBurnTime(inventory.get(1));
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}

	@Override
	public void update() {
		boolean flag = isBurning();
		boolean flag1 = false;

		if (isBurning()) {
			furnaceBurnTime--;
		}

		if (!world.isRemote) {
			ItemStack itemStack = inventory.get(1);

			if (isBurning() || !itemStack.isEmpty() && !inventory.get(0).isEmpty()) {
				if (!isBurning() && canSmelt()) {
					furnaceBurnTime = TileEntityFurnace.getItemBurnTime(itemStack);
					currentItemBurnTime = furnaceBurnTime;

					if (isBurning()) {
						flag1 = true;

						if (!itemStack.isEmpty()) {
							Item item = itemStack.getItem();
							itemStack.shrink(1);

							if (itemStack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemStack);
								inventory.set(1, item1);
							}
						}
					}
				}

				if (isBurning() && canSmelt()) {
					cookTime++;

					if (cookTime == totalCookTime) {
						cookTime = 0;
						totalCookTime = getCookTime(inventory.get(0));
						smeltItem();
						flag1 = true;
					}
				} else {
					cookTime = 0;
				}
			} else if (!isBurning() && cookTime > 0) {
				cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime);
			}

			if (flag != isBurning()) {
				flag1 = true;
				PacketUpdateFurnaceSlab msg = new PacketUpdateFurnaceSlab(this);
				NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
				CraftingSlabs.network.sendToAllAround(msg, target);
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
		if (inventory.get(0).isEmpty()) {
			return false;
		} else {
			ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(inventory.get(0));

			if (stack.isEmpty()) {
				return false;
			} else {
				ItemStack stack1 = inventory.get(2);
				if (stack1.isEmpty()) return true;
				if (!stack1.isItemEqual(stack)) return false;
				int result = stack1.getCount() + stack.getCount();
				return result <= getInventoryStackLimit() && result <= stack1.getMaxStackSize();
			}
		}
	}

	public void smeltItem() {
		if (canSmelt()) {
			ItemStack stack = inventory.get(0);
			ItemStack stack1 = FurnaceRecipes.instance().getSmeltingResult(stack);
			ItemStack stack2 = inventory.get(2);

			if (stack2.isEmpty()) {
				inventory.set(2, stack1.copy());
			} else if (stack2.getItem() == stack1.getItem()) {
				stack2.grow(stack1.getCount());
			}

			if (stack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && stack.getMetadata() == 1 && !inventory.get(1).isEmpty() && inventory.get(1).getItem() == Items.BUCKET) {
				inventory.set(1, new ItemStack(Items.WATER_BUCKET));
			}

			stack.shrink(1);
		}
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot == 2) {
			return false;
		} else if (slot != 1) {
			return true;
		} else {
			ItemStack itemStack = inventory.get(1);
			return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemStack.getItem() != Items.BUCKET;
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && slot == 1) {
			Item item = stack.getItem();

			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
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
				furnaceBurnTime = value;
				break;
			case 1:
				currentItemBurnTime = value;
				break;
			case 2:
				cookTime = value;
				break;
			case 3:
				totalCookTime = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

}
