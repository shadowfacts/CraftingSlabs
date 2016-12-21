package net.shadowfacts.craftingslabs.block

import mcmultipart.api.multipart.IMultipart
import mcmultipart.api.slot.EnumFaceSlot
import mcmultipart.api.slot.IPartSlot
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.shadowfacts.craftingslabs.property.PropertyDummy

/**
 * @author shadowfacts
 */
class BlockCraftingSlab: BlockSlab(Material.WOOD), IMultipart {

	companion object {
		val DUMMY = PropertyDummy("dummy")
	}

	init {
		setRegistryName("crafting_slab")
		unlocalizedName = registryName.toString()
		useNeighborBrightness = true
		setCreativeTab(CreativeTabs.DECORATIONS)

		defaultState = blockState.baseState.withProperty(DUMMY, PropertyDummy.Dummy).withProperty(HALF, EnumBlockHalf.BOTTOM)
	}

	override fun getTypeForItem(stack: ItemStack?): Comparable<*> {
		return PropertyDummy.Dummy
	}

	override fun isDouble(): Boolean {
		return false
	}

	override fun getUnlocalizedName(meta: Int): String {
		return unlocalizedName
	}

	override fun getVariantProperty(): IProperty<*> {
		return DUMMY
	}

	override fun createBlockState(): BlockStateContainer {
		return BlockStateContainer(this, HALF, DUMMY)
	}

	override fun getMetaFromState(state: IBlockState): Int {
		return if (state.getValue(HALF) == EnumBlockHalf.TOP) 1 else 0
	}

	@Deprecated("")
	override fun getStateFromMeta(meta: Int): IBlockState {
		return defaultState.withProperty(HALF, if (meta == 1) EnumBlockHalf.TOP else EnumBlockHalf.BOTTOM)
	}

	private fun getHalfForPlacement(facing: EnumFacing, hitY: Float): EnumBlockHalf {
		return when (facing) {
			EnumFacing.UP -> EnumBlockHalf.TOP
			EnumFacing.DOWN -> EnumBlockHalf.BOTTOM
			else -> if (hitY > 0.5) EnumBlockHalf.TOP else EnumBlockHalf.BOTTOM
		}
	}

	override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
		return defaultState.withProperty(HALF, getHalfForPlacement(facing, hitY))
	}

	override fun getSlotForPlacement(world: World, pos: BlockPos, state: IBlockState, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, placer: EntityLivingBase): IPartSlot {
		return when (getHalfForPlacement(facing, hitY)) {
			EnumBlockHalf.TOP -> EnumFaceSlot.UP
			EnumBlockHalf.BOTTOM -> EnumFaceSlot.DOWN
		}
	}

	override fun getSlotFromWorld(world: IBlockAccess, pos: BlockPos, state: IBlockState): IPartSlot {
		return when (state.getValue(HALF)!!) {
			EnumBlockHalf.TOP -> EnumFaceSlot.UP
			EnumBlockHalf.BOTTOM -> EnumFaceSlot.DOWN
		}
	}

}