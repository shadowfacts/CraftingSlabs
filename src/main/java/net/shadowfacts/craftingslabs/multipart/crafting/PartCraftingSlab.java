package net.shadowfacts.craftingslabs.multipart.crafting;

import lombok.Getter;
import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.shadowfacts.craftingslabs.CraftingSlabs;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * @author shadowfacts
 */
public class PartCraftingSlab extends Multipart implements ISlottedPart {

	public static final PropertyEnum<BlockSlab.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockSlab.EnumBlockHalf.class);

	@Getter
	private EnumBlockHalf half = EnumBlockHalf.BOTTOM;


	public PartCraftingSlab() {
	}

	public PartCraftingSlab(EnumBlockHalf half) {
		this.half = half;
	}

	private AxisAlignedBB getBoundingBox() {
		if (half == EnumBlockHalf.BOTTOM) {
			return new AxisAlignedBB(0, 0, 0, 1, .5, 1);
		} else {
			return new AxisAlignedBB(0, .5, 0, 1, 1, 1);
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
		return new ItemStack(CraftingSlabs.items.craftingSlab);
	}

	@Override
	public List<ItemStack> getDrops() {
		return Arrays.asList(new ItemStack(CraftingSlabs.items.craftingSlab));
	}

	@Override
	public Material getMaterial() {
		return Material.WOOD;
	}

	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
		player.openGui(CraftingSlabs.instance, 2, player.worldObj, hit.getBlockPos().getX(), hit.getBlockPos().getY(), hit.getBlockPos().getZ());
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setBoolean("Half", half == EnumBlockHalf.BOTTOM);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		half = tag.getBoolean("Half") ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP;
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		half = buf.readBoolean() ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP;
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		buf.writeBoolean(half == EnumBlockHalf.BOTTOM);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(MCMultiPartMod.multipart, HALF);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state) {
		return state.withProperty(HALF, half);
	}

	@Override
	public ResourceLocation getModelPath() {
		return new ResourceLocation("craftingslabs", "partCraftingSlab");
	}

	public void setHalf(EnumBlockHalf half) {
		this.half = half;
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
		return 0.3f;
	}

	@Override
	public boolean isToolEffective(String type, int level) {
		return type.equals("axe");
	}

	public static PartCraftingSlab getCraftingSlab(World world, BlockPos pos, PartSlot slot) {
		IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
		if (container == null) {
			return null;
		}

		if (slot != null) {
			ISlottedPart part = container.getPartInSlot(slot);
			if (part instanceof PartCraftingSlab) {
				return ((PartCraftingSlab)part);
			}
		} else {
			ISlottedPart down = container.getPartInSlot(PartSlot.DOWN);
			if (down instanceof PartCraftingSlab) {
				return (PartCraftingSlab)down;
			} else {
				ISlottedPart up = container.getPartInSlot(PartSlot.UP);
				if (up instanceof PartCraftingSlab) {
					return (PartCraftingSlab)up;
				}
			}
		}

		return null;
	}

}
