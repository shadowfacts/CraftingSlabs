package net.shadowfacts.craftingslabs.property

import com.google.common.base.Optional
import net.minecraft.block.properties.PropertyHelper

/**
 * @author shadowfacts
 */
class PropertyDummy(name: String): PropertyHelper<PropertyDummy.Dummy>(name, Dummy::class.java) {

	object Dummy: Comparable<Dummy> {

		override fun compareTo(other: Dummy): Int {
			return 0
		}
	}

	override fun getName(value: Dummy?): String {
		return ""
	}

	override fun getAllowedValues(): MutableCollection<Dummy> {
		return mutableSetOf(Dummy)
	}

	override fun parseValue(value: String?): Optional<Dummy> {
		return Optional.of(Dummy)
	}

}
