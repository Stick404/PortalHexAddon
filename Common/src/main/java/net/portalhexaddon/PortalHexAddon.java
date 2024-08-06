package net.portalhexaddon;

import net.portalhexaddon.portals.PortalAmbit;
import net.portalhexaddon.registry.PortalHexAddonIotaTypeRegistry;
import net.portalhexaddon.registry.PortalHexAddonItemRegistry;
import net.portalhexaddon.registry.PortalHexAddonPatternRegistry;
import net.portalhexaddon.networking.PortalHexAddonNetworking;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is effectively the loading entrypoint for most of your code, at least
 * if you are using Architectury as intended.
 */
public class PortalHexAddon {
    public static final String MOD_ID = "hexways";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Portal Hex Addon says hello!");

        PortalHexAddonAbstractions.initPlatformSpecific();
        PortalHexAddonItemRegistry.init();
        PortalHexAddonIotaTypeRegistry.init();
        PortalHexAddonPatternRegistry.init();
		PortalHexAddonNetworking.init();
        com.samsthenerd.hexgloop.casting.ContextModificationHandlers.registerAmbitModifier(PortalAmbit::ambitModifier, 0);
        LOGGER.info(PortalHexAddonAbstractions.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static ResourceLocation id(String string) {
        return new ResourceLocation(MOD_ID, string);
    }
}
