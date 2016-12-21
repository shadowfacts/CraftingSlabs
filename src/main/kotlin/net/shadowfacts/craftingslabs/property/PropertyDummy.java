package net.shadowfacts.craftingslabs.property;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.properties.PropertyHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author shadowfacts
 */
public class PropertyDummy extends PropertyHelper<PropertyDummy.Dummy> {

	public PropertyDummy(String name) {
		super(name, Dummy.class);
	}

	@Override
	public Collection<Dummy> getAllowedValues() {
		return ImmutableList.of(Dummy.INSTANCE);
	}

	@Override
	public Optional<Dummy> parseValue(String value) {
		return Optional.of(Dummy.INSTANCE);
	}

	@Override
	public String getName(Dummy value) {
		return "dummy";
	}

	public static class Dummy implements Comparable<Dummy> {
		public static final Dummy INSTANCE = new Dummy();
		private Dummy() {}

		@Override
		public int compareTo(@NotNull Dummy o) {
			return 0;
		}
	}

}
