package net.shadowfacts.craftingslabs.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.craftingslabs.compat.modules.CompatNEI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shadowfacts
 */
public class ModCompat {

	private static List<Class> modules = new ArrayList<Class>();

	private static Logger log = LogManager.getLogger("CraftingSlabs");

	public static void registerModules() {
	}

	public static void registerClientModules() {
		register(CompatNEI.class);
	}

	private static boolean register(Class clazz) {
		if (clazz.isAnnotationPresent(Compat.class)) {
			Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
			if (Loader.isModLoaded(annotation.value())) {
				modules.add(clazz);
				return true;
			} else {
				log.info("The mod %s was not loaded, skipping compatibility module.", annotation.value());
				return false;
			}
		}
		log.error("There was a problem register a compatibility module!");
		return false;
	}

	public static void preInit(FMLPreInitializationEvent event) {
		log.info("Attempting to run pre-initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.PreInit.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						log.error(String.format("There was an error trying to invoke the pre-initialization method of the compatibility module for %1$s", annotation.value()), e);
					}
				}
			}
		}
	}

	public static void init(FMLInitializationEvent event) {
		log.info("Attempting to run initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.Init.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						log.error(String.format("There was an error trying to invoke the initialization method of the compatibility module for %1$s", annotation.value()), e);
					}
				}
			}
		}
	}

	public static void postInit(FMLPostInitializationEvent event) {
		log.info("Attempting to run post-initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.PostInit.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						log.error(String.format("There was an error trying to invoke the post-initialization method of the compatibility module %1$s", annotation.value()), e);
					}
				}
			}
		}
	}

}
