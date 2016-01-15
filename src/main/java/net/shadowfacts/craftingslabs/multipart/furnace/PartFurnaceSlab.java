package net.shadowfacts.craftingslabs.multipart.furnace;

import lombok.Getter;
import lombok.Setter;
import mcmultipart.MCMultiPartMod;
import mcmultipart.client.multipart.IRandomDisplayTickPart;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowfacts.craftingslabs.CraftingSlabs;
import net.shadowfacts.craftingslabs.gui.GUIs;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * @author shadowfacts
 */
public class PartFurnaceSlab extends Multipart implements IRandomDisplayTickPart, ISlottedPart, ITickable, IInventory {

	public static final PropertyEnum<BlockSlab.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockSlab.EnumBlockHalf.class);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private static final PropertyBool BURNING = PropertyBool.create("burning");

	private ItemStack[] inventory = new ItemStack[3];
	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	@Getter @Setter
	private EnumFacing facing;
	@Getter @Setter
	private boolean burning;
	@Getter @Setter
	private BlockSlab.EnumBlockHalf half = BlockSlab.EnumBlockHalf.BOTTOM;


	public PartFurnaceSlab() {
	}

	public PartFurnaceSlab(BlockSlab.EnumBlockHalf half, EnumFacing facing) {
		this.half = half;
		this.facing = facing;
	}

	private AxisAlignedBB getBoundingBox() {
		if (half == BlockSlab.EnumBlockHalf.BOTTOM) {
			return AxisAlignedBB.fromBounds(0, 0, 0, 1, .5, 1);
		} else {
			return AxisAlignedBB.fromBounds(0, .5, 0, 1, 1, 1);
		}
	}

	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list) {
		list.add(getBoundingBox());
	}

	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
		AxisAlignedBB box = getBoundingBox();
		if (box.intersectsWith(mask)) {
			list.add(box);
		}
	}

	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {
		return new ItemStack(CraftingSlabs.items.partFurnaceSlab);
	}

	@Override
	public List<ItemStack> getDrops() {
		return Arrays.asList(new ItemStack(CraftingSlabs.items.partFurnaceSlab));
	}

	@Override
	public Material getMaterial() {
		return Material.rock;
	}

	@Override
	public boolean onActivated(EntityPlayer player, ItemStack stack, PartMOP hit) {
		player.openGui(CraftingSlabs.instance, GUIs.FURNACE.ordinal(), player.worldObj, hit.getBlockPos().getX(), hit.getBlockPos().getY(), hit.getBlockPos().getZ());
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setBoolean("Half", half == BlockSlab.EnumBlockHalf.BOTTOM);
		tag.setInteger("Facing", facing.ordinal());
		tag.setBoolean("Burning", burning);

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
	public void readFromNBT(NBTTagCompound tag) {
		half = tag.getBoolean("Half") ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP;
		facing = EnumFacing.values()[tag.getInteger("Facing")];
		burning = tag.getBoolean("Burning");

		NBTTagList list = tag.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound itemTag = list.getCompoundTagAt(i);
			int slot = itemTag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		furnaceBurnTime = tag.getShort("BurnTime");
		cookTime = tag.getShort("CookTime");
		totalCookTime = tag.getShort("CookTimeTotal");
		currentItemBurnTime = TileEntityFurnace.getItemBurnTime(inventory[1]);
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		try {
			readFromNBT(buf.readNBTTagCompoundFromBuffer());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		buf.writeNBTTagCompoundToBuffer(tag);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(MCMultiPartMod.multipart, HALF, FACING, BURNING);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state) {
		return state
				.withProperty(HALF, half)
				.withProperty(FACING, facing)
				.withProperty(BURNING, burning);
	}

	@Override
	public String getModelPath() {
		return "craftingslabs:partFurnaceSlab";
	}

	@Override
	public EnumSet<PartSlot> getSlotMask() {
		switch (half) {
			case BOTTOM:
				return EnumSet.of(PartSlot.DOWN);
			case TOP:
				return EnumSet.of(PartSlot.UP);
		}
		return EnumSet.noneOf(PartSlot.class);
	}

	@Override
	public float getHardness(PartMOP hit) {
		return .3f;
	}

	@Override
	public boolean isToolEffective(String type, int level) {
		return type.equals("pickaxe");
	}

	@Override
	public void randomDisplayTick(Random random) {
		if (burning) {
			double d0 = (double)getPos().getX() + 0.5D;
			double d1 = (double)getPos().getY() + random.nextDouble() * 6.0D / 16.0D;
			double d2 = (double)getPos().getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = random.nextDouble() * 0.6D - 0.3D;

			if (half == BlockSlab.EnumBlockHalf.TOP) d1 += .5d;

			switch (facing) {
				case WEST:
					getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case EAST:
					getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case NORTH:
					getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					break;
				case SOUTH:
					getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
					getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void update() {
		boolean flag = isBurning();
		boolean flag1 = false;

		if (isBurning()) {
			--furnaceBurnTime;
		}

		if (!getWorld().isRemote) {
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
				burning = isBurning();
				sendUpdatePacket(true);
			}
		}

		if (flag1) {
			markDirty();
		}
	}

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
		return player.getDistanceSq((double) getPos().getX() + .5d, (double) getPos().getY() + .5d, (double) getPos().getZ() + .5d) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot != 2 && (slot != 1 || (TileEntityFurnace.isItemFuel(stack)) || SlotFurnaceFuel.isBucket(stack));
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

	@Override
	public void markDirty() {
		super.markDirty();
	}

	public static PartFurnaceSlab getFurnaceSlab(World world, BlockPos pos, PartSlot slot) {
		IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
		if (container == null) {
			return null;
		}

		if (slot != null) {
			ISlottedPart part = container.getPartInSlot(slot);
			if (part instanceof PartFurnaceSlab) {
				return ((PartFurnaceSlab)part);
			}
		} else {
			ISlottedPart down = container.getPartInSlot(PartSlot.DOWN);
			if (down instanceof PartFurnaceSlab) {
				return (PartFurnaceSlab)down;
			} else {
				ISlottedPart up = container.getPartInSlot(PartSlot.UP);
				if (up instanceof PartFurnaceSlab) {
					return (PartFurnaceSlab)up;
				}
			}
		}

		return null;
	}
}
