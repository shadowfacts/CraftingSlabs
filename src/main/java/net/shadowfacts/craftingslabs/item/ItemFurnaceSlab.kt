package net.shadowfacts.craftingslabs.item

import mcmultipart.item.ItemMultiPart
import mcmultipart.multipart.IMultipart
import net.minecraft.block.BlockSlab
import net.minecraft.block.SoundType
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.shadowfacts.craftingslabs.MODID
import net.shadowfacts.craftingslabs.multipart.PartFurnaceSlab
import net.shadowfacts.shadowmc.item.ItemModelProvider

/**
 * @author shadowfacts
 */
class ItemFurnaceSlab: ItemMultiPart(), ItemModelProvider {

	init {
		setRegistryName("furnaceSlab")
		unlocalizedName = "furnaceSlab"
		creativeTab = CreativeTabs.DECORATIONS
	}

	override fun initItemModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, ModelResourceLocation("$MODID:partFurnaceSlab", "inventory"))
	}

	override fun createPart(world: World, pos: BlockPos, side: EnumFacing, hit: Vec3d, stack: ItemStack, player: EntityPlayer): IMultipart {
		val half = when (side) {
			EnumFacing.DOWN -> BlockSlab.EnumBlockHalf.BOTTOM
			EnumFacing.UP -> BlockSlab.EnumBlockHalf.TOP
			else -> if (hit.yCoord > 0.5) BlockSlab.EnumBlockHalf.TOP else BlockSlab.EnumBlockHalf.BOTTOM
		}
		return PartFurnaceSlab(half, getDirection(pos, player))
	}

	private fun getDirection(pos: BlockPos, entity: EntityLivingBase): EnumFacing {
		return EnumFacing.getFacingFromVector(entity.posX.toFloat() - pos.x, 0f, entity.posZ.toFloat() - pos.z)
	}

	override fun getPlacementSound(stack: ItemStack?): SoundType {
		return SoundType.STONE
	}

}