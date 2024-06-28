package net.portalhexaddon.registry;

import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.spell.Action;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import kotlin.Triple;
import net.portalhexaddon.casting.patterns.portaledit.*;
import net.portalhexaddon.casting.patterns.portalinfo.OpOutputInfo;
import net.portalhexaddon.casting.patterns.spells.OpOneWayPortal;
import net.portalhexaddon.casting.patterns.spells.OpPonderingMyOrb;
import net.portalhexaddon.casting.patterns.spells.OpTwoWayPortal;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static net.portalhexaddon.PortalHexAddon.id;

public class PortalHexAddonPatternRegistry {
    public static List<Triple<HexPattern, ResourceLocation, Action>> PATTERNS = new ArrayList<>();
    public static List<Triple<HexPattern, ResourceLocation, Action>> PER_WORLD_PATTERNS = new ArrayList<>();
    // IMPORTANT: be careful to keep the registration calls looking like this, or you'll have to edit the hexdoc pattern regex.
    public static HexPattern TWOWAYPORTAL = registerPerWorld(HexPattern.fromAngles("wdeeqawqwqwadeaqadeaedaqae", HexDir.WEST), "twowayportal", new OpTwoWayPortal());
    public static HexPattern ONEWAYPORTAL = registerPerWorld(HexPattern.fromAngles("awqwqwadadadaadadaqwdee", HexDir.EAST), "onewayportal", new OpOneWayPortal());
    public static HexPattern REMOVEPORTAL = register(HexPattern.fromAngles("wdeeqawqwqwaedaqwqad", HexDir.WEST),"removeportal", new OpRemovePortal());
    public static HexPattern ROTATEPORTAL = register(HexPattern.fromAngles("waqqedaqqqa", HexDir.EAST),"rotateportal", new OpRotatePortal());
    public static HexPattern MOVEPORTALINPUT = register(HexPattern.fromAngles("waqqedwewewdqee", HexDir.EAST), "moveportalinput", new OpMovePortalInput());
    public static HexPattern MOVEPORTALOUTPUT = register(HexPattern.fromAngles("eedwaqqedwewewd", HexDir.NORTH_EAST), "moveportaloutput", new OpMovePortalOutput());
    public static HexPattern SETPORTALSIDES = register(HexPattern.fromAngles("waqqqadawqadadaq", HexDir.EAST), "setportalsides", new OpPortalSides());
    public static HexPattern RESIZEPORTAL = register(HexPattern.fromAngles("waqqwdedwqqwwdwewdw", HexDir.EAST), "resizeportal", new OpReSizePortal());
    public static HexPattern SUMMONSCRY = register(HexPattern.fromAngles("wdeeqawqwqwa", HexDir.WEST), "summonscry", new OpPonderingMyOrb());
    public static HexPattern GETOUTPUTINFO = register(HexPattern.fromAngles("waqqedwewewdawdwwwdw", HexDir.EAST), "getoutputinfo", new OpOutputInfo());
    //public static HexPattern ROTATESIDEOFPORTAL = registerPerWorld(HexPattern.fromAngles("waqqqqqdeewewwadeeed", HexDir.EAST), "rotatesideofportal", new OpRotateSideOfPortal());

    public static void init() {
        try {
            for (Triple<HexPattern, ResourceLocation, Action> patternTriple : PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird());
            }
            for (Triple<HexPattern, ResourceLocation, Action> patternTriple : PER_WORLD_PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird(), true);
            }
        } catch (PatternRegistry.RegisterPatternException e) {
            e.printStackTrace();
        }
    }

    private static HexPattern register(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, ResourceLocation, Action> triple = new Triple<>(pattern, id(name), action);
        PATTERNS.add(triple);
        return pattern;
    }

    private static HexPattern registerPerWorld(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, ResourceLocation, Action> triple = new Triple<>(pattern, id(name), action);
        PER_WORLD_PATTERNS.add(triple);
        return pattern;
    }
}
