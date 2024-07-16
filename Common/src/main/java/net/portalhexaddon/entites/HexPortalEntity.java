package net.portalhexaddon.entites;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import qouteall.imm_ptl.core.portal.Portal;

public abstract class HexPortalEntity extends Portal {
    public HexPortalEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }
    public int portalSides;
    public double portalRoll;

}
