package net.shadowfacts.craftingslabs.multipart

import mcmultipart.MCMultiPartMod
import mcmultipart.multipart.ISlottedPart
import mcmultipart.multipart.Multipart
import mcmultipart.multipart.MultipartHelper
import mcmultipart.multipart.PartSlot
import mcmultipart.raytrace.PartMOP
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.SlotFurnaceFuel
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.shadowfacts.craftingslabs.CraftingSlabs
import net.shadowfacts.craftingslabs.MODID
import net.shadowfacts.craftingslabs.gui.GUIHandler
import java.util.*

/**
 * @author shadowfacts
 */
class PartFurnaceSlab(var half: BlockSlab.EnumBlockHalf, var facing: EnumFacing): Multipart(), ISlottedPart, ITickable, IInventory {

	companion object {
		val HALF: PropertyEnum<BlockSlab.EnumBlockHalf> = PropertyEnum.create("half", BlockSlab.EnumBlockHalf::class.java)
		val FACING: PropertyDirection = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL)
		private val BURNING: PropertyBool = PropertyBool.create("burning")

		fun isBurning(inventory: IInventory): Boolean {
			return inventory.getField(0) > 0
		}

		fun getFurnaceSlab(world: World, pos: BlockPos, slot: PartSlot?): PartFurnaceSlab? {
			val container = MultipartHelper.getPartContainer(world, pos) ?: return null

			if (slot != null) {
				val part = container.getPartInSlot(slot)
				if (part is PartFurnaceSlab) {
					return part
				}
			} else {
				val down = container.getPartInSlot(PartSlot.DOWN)
				if (down is PartFurnaceSlab) {
					return down
				} else {
					val up = container.getPartInSlot(PartSlot.UP)
					if (up is PartFurnaceSlab) {
						return up
					}
				}
			}
			return null
		}
	}

	private val inventory: Array<ItemStack?> = arrayOfNulls(3)
	var furnaceBurnTime = 0
	var currentItemBurnTime = 0
	var cookTime = 0
	var totalCookTime = 0

	var burning = false

	constructor(): this(BlockSlab.EnumBlockHalf.BOTTOM, EnumFacing.NORTH)

	private fun getBoundingBox(): AxisAlignedBB {
		return when (half) {
			BlockSlab.EnumBlockHalf.BOTTOM -> AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)
			BlockSlab.EnumBlockHalf.TOP -> AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0)
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
		return ItemStack(CraftingSlabs.items.furnaceSlab)
	}

	override fun getDrops(): MutableList<ItemStack>? {
		val drops: MutableList<ItemStack> = mutableListOf()
		drops.add(ItemStack(CraftingSlabs.items.furnaceSlab))
		inventory.filter { it != null }.forEach { drops.add(it!!) }
		return drops
	}

	override fun getMaterial(): Material {
		return Material.ROCK
	}

	override fun onActivated(player: EntityPlayer, hand: EnumHand, heldItem: ItemStack?, hit: PartMOP): Boolean {
		val id = when (half) {
			BlockSlab.EnumBlockHalf.BOTTOM -> GUIHandler.FURNACE_BOTTOM
			BlockSlab.EnumBlockHalf.TOP -> GUIHandler.FURNACE_TOP
		}
		player.openGui(CraftingSlabs, id, player.world, hit.blockPos.x, hit.blockPos.y, hit.blockPos.z)
		return true
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
		tag.setBoolean("Half", half == BlockSlab.EnumBlockHalf.BOTTOM)
		tag.setInteger("Facing", facing.ordinal)
		tag.setBoolean("Burning", burning)

		tag.setShort("BurnTime", furnaceBurnTime.toShort())
		tag.setShort("CookTime", cookTime.toShort())
		tag.setShort("CookTimeTotal", totalCookTime.toShort())

		val list = NBTTagList()
		for (i in 0.until(inventory.size)) {
			val stack = inventory[i]
			if (stack != null) {
				val itemTag = NBTTagCompound()
				itemTag.setByte("Slot", i.toByte())
				stack.writeToNBT(itemTag)
				list.appendTag(itemTag)
			}
		}

		tag.setTag("Items", list)

		return tag
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		half = if (tag.getBoolean("Half")) BlockSlab.EnumBlockHalf.BOTTOM else BlockSlab.EnumBlockHalf.TOP
		facing = EnumFacing.values()[tag.getInteger("Facing")]
		burning = tag.getBoolean("Burning")

		val list = tag.getTagList("Items", 10)
		for (i in 0.until(list.tagCount())) {
			val itemTag = list.getCompoundTagAt(i)
			val slot = itemTag.getByte("Slot").toInt()
			if (slot >= 0 && slot < inventory.size) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag)
			}
		}

		furnaceBurnTime = tag.getShort("BurnTime").toInt()
		cookTime = tag.getShort("CookTime").toInt()
		totalCookTime = tag.getShort("CookTimeTotal").toInt()
		currentItemBurnTime = TileEntityFurnace.getItemBurnTime(inventory[1])
	}

	override fun writeUpdatePacket(buf: PacketBuffer) {
		val tag = writeToNBT(NBTTagCompound())
		buf.writeCompoundTag(tag)
		buf.writeBoolean(burning)
	}

	override fun readUpdatePacket(buf: PacketBuffer) {
		readFromNBT(buf.readCompoundTag()!!)
		burning = buf.readBoolean()
	}

	override fun createBlockState(): BlockStateContainer {
		return BlockStateContainer(MCMultiPartMod.multipart, HALF, FACING, BURNING)
	}

	override fun getActualState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState {
		return state
				.withProperty(HALF, half)
				.withProperty(FACING, facing)
				.withProperty(BURNING, burning)
	}

	override fun getModelPath(): ResourceLocation {
		return ResourceLocation(MODID, "partFurnaceSlab")
	}

	override fun getSlotMask(): EnumSet<PartSlot> {
		return when (half) {
			BlockSlab.EnumBlockHalf.BOTTOM -> EnumSet.of(PartSlot.DOWN)
			BlockSlab.EnumBlockHalf.TOP -> EnumSet.of(PartSlot.UP)
		}
	}

	override fun getHardness(hit: PartMOP?): Float {
		return 0.5f
	}

	override fun isToolEffective(type: String, level: Int): Boolean {
		return type == "pickaxe"
	}

	override fun randomDisplayTick(rand: Random) {
		if (burning) {
			val d0 = pos.x.toDouble()+ 0.5
			var d1 = pos.y.toDouble() + rand.nextDouble() * 6.0 / 16.0
			val d2 = pos.z.toDouble() + 0.5
			val d3 = 0.52
			val d4 = rand.nextDouble() * 0.6 - 0.3

			if (half === BlockSlab.EnumBlockHalf.TOP) d1 += 0.5

			when (facing) {
				EnumFacing.WEST -> {
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0, 0.0, 0.0)
					world.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0, 0.0, 0.0)
				}
				EnumFacing.EAST -> {
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0, 0.0, 0.0)
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0, 0.0, 0.0)
				}
				EnumFacing.NORTH -> {
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0, 0.0, 0.0)
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0, 0.0, 0.0)
				}
				EnumFacing.SOUTH -> {
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0, 0.0, 0.0)
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0, 0.0, 0.0)
				}
			}

		}
	}

	override fun getLightValue(): Int {
		return if (burning) 13 else 0
	}

	override fun update() {
		val prevBurning = isBurning()
		var flag1 = false

		if (isBurning()) {
			--furnaceBurnTime
		}

		if (!world.isRemote) {
			if (isBurning() || inventory[1] != null && inventory[0] != null) {
				if (!isBurning() && canSmelt()) {
					currentItemBurnTime = TileEntityFurnace.getItemBurnTime(inventory[1])
					furnaceBurnTime = currentItemBurnTime

					if (isBurning()) {
						flag1 = true

						if (inventory[1] != null) {
							--inventory[1]!!.stackSize

							if (inventory[1]!!.stackSize <= 0) {
								inventory[1] = inventory[1]!!.item.getContainerItem(inventory[1]!!)
							}
						}
					}
				}

				if (isBurning() && canSmelt()) {
					++cookTime;
					if (cookTime == totalCookTime) {
						cookTime = 0
						totalCookTime = getCookTime(inventory[0]!!)
						smeltItem()
						flag1 = true
					}
				} else {
					cookTime = 0
				}
			} else if (!isBurning() && cookTime > 0) {
				cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime)
			}

			if (prevBurning != isBurning()) {
				flag1 = true
				burning = isBurning()
				sendUpdatePacket(true)
			}
		}

		if (flag1) {
			markDirty()
		}
	}

	override fun getSizeInventory(): Int {
		return inventory.size
	}

	override fun getStackInSlot(slot: Int): ItemStack? {
		return inventory[slot]
	}

	override fun decrStackSize(slot: Int, count: Int): ItemStack? {
		if (inventory[slot] != null) {
			if (inventory[slot]!!.stackSize <= count) {
				val stack = inventory[slot]
				inventory[slot] = null
				return stack
			} else {
				val stack = inventory[slot]!!.splitStack(count)

				if (inventory[slot]!!.stackSize <= 0) {
					inventory[slot] = null
				}

				return stack
			}
		}

		return null
	}

	override fun removeStackFromSlot(slot: Int): ItemStack? {
		if (inventory[slot] != null) {
			val stack = inventory[slot]
			inventory[slot] = null
			return stack
		}
		return null
	}

	override fun setInventorySlotContents(slot: Int, stack: ItemStack?) {
		val flag = stack != null && stack.isItemEqual(inventory[slot]) && ItemStack.areItemStackTagsEqual(stack, inventory[slot])
		inventory[slot] = stack
		if (stack != null && stack.stackSize > inventoryStackLimit) {
			stack.stackSize = inventoryStackLimit
		}

		if (slot == 0 && !flag) {
			totalCookTime = getCookTime(stack)
			cookTime = 0
			markDirty()
		}
	}

	override fun getName(): String {
		return "container.furnace"
	}

	override fun hasCustomName(): Boolean {
		return false
	}

	override fun getDisplayName(): ITextComponent {
		return TextComponentTranslation(name)
	}

	override fun getInventoryStackLimit(): Int {
		return 64
	}

	fun isBurning(): Boolean {
		return furnaceBurnTime > 0
	}

	fun getCookTime(stack: ItemStack?): Int {
		return 200
	}

	private fun canSmelt(): Boolean {
		if (inventory[0] == null) {
			return false
		} else {
			val result = FurnaceRecipes.instance().getSmeltingResult(inventory[0])
			if (result == null) {
				return false
			}
			if (inventory[2] == null) {
				return true
			}
			if (!inventory[2]!!.isItemEqual(result)) {
				return false
			}
			val resultCount = inventory[2]!!.stackSize + result.stackSize
			return resultCount <= inventoryStackLimit && resultCount <= inventory[2]!!.maxStackSize
		}
	}

	fun smeltItem() {
		if (canSmelt()) {
			val stack = FurnaceRecipes.instance().getSmeltingResult(inventory[0])!!
			if (inventory[2] == null) {
				inventory[2] = stack.copy()
			} else if (inventory[2]!!.item == stack.item) {
				inventory[2]!!.stackSize += stack.stackSize
			}

			if (inventory[0]!!.item == Item.getItemFromBlock(Blocks.SPONGE) && inventory[0]!!.metadata == 1 && inventory[1] != null && inventory[1]!!.item == Items.BUCKET) {
				inventory[1] = ItemStack(Items.WATER_BUCKET)
			}

			--inventory[0]!!.stackSize

			if (inventory[0]!!.stackSize <= 0) {
				inventory[0] = null
			}
		}
	}

	override fun isUsableByPlayer(player: EntityPlayer): Boolean {
		return player.getDistanceSq(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64
	}

	override fun openInventory(player: EntityPlayer?) {

	}

	override fun closeInventory(player: EntityPlayer?) {

	}

	override fun isItemValidForSlot(slot: Int, stack: ItemStack): Boolean {
		return slot != 2 && (slot != 1 || (TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack)))
	}

	override fun getField(id: Int): Int {
		return when (id) {
			0 -> furnaceBurnTime
			1 -> currentItemBurnTime
			2 -> cookTime
			3 -> totalCookTime
			else -> 0
		}
	}

	override fun setField(id: Int, value: Int) {
		when (id) {
			0 -> furnaceBurnTime = value
			1 -> currentItemBurnTime = value
			2 -> cookTime = value
			3 -> totalCookTime = value
		}
	}

	override fun getFieldCount(): Int {
		return 4
	}

	override fun clear() {
		for (i in 0.until(inventory.size)) {
			inventory[i] = null
		}
	}

	override fun markDirty() {
		super.markDirty()
	}

}