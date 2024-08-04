package net.portalhexaddon.portals;

import com.samsthenerd.hexgloop.casting.ContextModificationHandlers.Modification;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.ArrayList;


public class PortalAmbit {
    public static Modification ambitModifier (CastingContext ctx, Vec3 pos, Boolean original){
        boolean modi = false;
        ArrayList<Entity> portalsInAmbit = (ArrayList<Entity>) PortalHexUtils.Companion.GetPortalInAmbit(ctx, ctx.getCaster().position());
        for (Entity e : portalsInAmbit) {
            Portal Prt = (Portal) e;
            Double ambitDistLeft = (32 - Prt.getEyePosition().distanceTo(ctx.getCaster().position())) / 2.0;
            if (ambitDistLeft >= pos.distanceTo(Prt.getDestPos())) {
                modi = true;
                break;
            } else {
                modi = false;
            }
        }
        if (modi) {
            return Modification.ENABLE;
        } else {
            return Modification.NONE;
        }
    }
}