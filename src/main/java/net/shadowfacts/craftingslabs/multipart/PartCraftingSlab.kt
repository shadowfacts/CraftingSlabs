package net.shadowfacts.craftingslabs.multipart

import mcmultipart.MCMultiPartMod
import mcmultipart.multipart.ISlottedPart
import mcmultipart.multipart.Multipart
import mcmultipart.multipart.MultipartHelper
import mcmultipart.multipart.PartSlot
import mcmultipart.raytrace.PartMOP
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.PacketBuffer
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.shadowfacts.craftingslabs.CraftingSlabs
import net.shadowfacts.craftingslabs.MODID
import java.util.*

/**
 * @author shadowfacts
 */
class PartCraftingSlab(var half: BlockSlab.EnumBlockHalf) : Multipart(), ISlottedPart {

	companion object {
		val HALF: PropertyEnum<BlockSlab.EnumBlockHalf> = PropertyEnum.create("half", BlockSlab.EnumBlockHalf::class.java)

		fun getCraftingSlab(world: World, pos: BlockPos, slot: PartSlot?): PartCraftingSlab? {
			val container = MultipartHelper.getPartContainer(world, pos) ?: return null

			if (slot != null) {
				val part = container.getPartInSlot(slot)
				if (part is PartCraftingSlab) {
					return part
				}
			} else {
				val down = container.getPartInSlot(PartSlot.DOWN)
				if (down is PartCraftingSlab) {
					return down
				} else {
					val up = container.getPartInSlot(PartSlot.UP)
					if (up is PartCraftingSlab) {
						return up
					}
				}
			}

			return null
		}
	}

	constructor() : this(BlockSlab.EnumBlockHalf.BOTTOM)

	private fun getBoundingBox(): AxisAlignedBB {
		when (half) {
			BlockSlab.EnumBlockHalf.BOTTOM -> return AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)
			BlockSlab.EnumBlockHalf.TOP -> return AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0)
		}
	}

	override fun addSelectionBoxes(list: MutableList<AxisAlignedBB>) {
		list.add(getBoundingBox())
	}

	override fun addCollisionBoxes(mask: AxisAlignedBB, list: MutableList<AxisAlignedBB>, collidingEntity: Entity?) {
		val box = getBoundingBox()
		if (box.intersectsWith(mask)) {
			list.add(box)
		}
	}

	override fun getPickBlock(player: EntityPlayer?, hit: PartMOP?): ItemStack {
		return ItemStack(CraftingSlabs.items.craftingSlab)
	}

	override fun getDrops(): MutableList<ItemStack>? {
		return mutableListOf(ItemStack(CraftingSlabs.items.craftingSlab))
	}

	override fun getMaterial(): Material {
		return Material.WOOD
	}

	override fun onActivated(player: EntityPlayer, hand: EnumHand, heldItem: ItemStack?, hit: PartMOP): Boolean {
		player.openGui(CraftingSlabs, 0, player.worldObj, hit.blockPos.x, hit.blockPos.y, hit.blockPos.z)
		return true
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setBoolean("half", half == BlockSlab.EnumBlockHalf.BOTTOM)
		return tag
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		half = if (tag.getBoolean("half")) BlockSlab.EnumBlockHalf.BOTTOM else BlockSlab.EnumBlockHalf.TOP
	}

	override fun writeUpdatePacket(buf: PacketBuffer) {
		buf.writeBoolean(half == BlockSlab.EnumBlockHalf.BOTTOM)
	}

	override fun readUpdatePacket(buf: PacketBuffer) {
		half = if (buf.readBoolean()) BlockSlab.EnumBlockHalf.BOTTOM else BlockSlab.EnumBlockHalf.TOP
	}

	override fun createBlockState(): BlockStateContainer {
		return BlockStateContainer(MCMultiPartMod.multipart, HALF)
	}

	override fun getExtendedState(state: IBlockState): IBlockState {
		return state.withProperty(HALF, half)
	}

	override fun getModelPath(): ResourceLocation {
		return ResourceLocation(MODID, "partCraftingSlab")
	}

	override fun getSlotMask(): EnumSet<PartSlot> {
		when (half) {
			BlockSlab.EnumBlockHalf.BOTTOM -> return EnumSet.of(PartSlot.DOWN)
			BlockSlab.EnumBlockHalf.TOP -> return EnumSet.of(PartSlot.UP)
		}
	}

	override fun getHardness(hit: PartMOP?): Float {
		return 0.3f
	}

	override fun isToolEffective(type: String, level: Int): Boolean {
		return type == "axe"
	}

}