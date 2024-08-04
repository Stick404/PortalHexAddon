package net.portalhexaddon.entites;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import qouteall.imm_ptl.core.portal.Portal;

public class HexPortalEntity extends Portal {
    public HexPortalEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
        this.portalSides = 6;
        this.portalRoll = 0;
    }
    public HexPortalEntity(EntityType<?> entityType, Level world, int portalSides, double portalRoll) {
        super(entityType, world);
        this.portalSides = portalSides;
        this.portalRoll = portalRoll;
    }
    public int portalSides;
    public double portalRoll;

}
