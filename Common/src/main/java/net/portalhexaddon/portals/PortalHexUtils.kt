package net.portalhexaddon.portals

import at.petrak.hexcasting.api.spell.casting.CastingContext
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import qouteall.imm_ptl.core.portal.GeometryPortalShape
import qouteall.imm_ptl.core.portal.Portal
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.collections.ArrayList
import kotlin.math.cos
import kotlin.math.sin

class PortalHexUtils {
    companion object {
        fun MakePortalNGon(portal: Portal, sides: Int) { //GOTTEN FROM IMMERSIVE PORTALS PortalCommand
            val shape = GeometryPortalShape()
            val twoPi = Math.PI * 2
            shape.triangles = IntStream.range(0, sides)
                .mapToObj { i: Int ->
                    GeometryPortalShape.TriangleInPlane(
                        0.0, 0.0,
                        portal.width * 0.5 * cos(twoPi * (i.toDouble()) / sides),
                        portal.height * 0.5 * sin(twoPi * (i.toDouble()) / sides),
                        portal.width * 0.5 * cos(twoPi * (i.toDouble() + 1) / sides),
                        portal.height * 0.5 * sin(twoPi * (i.toDouble() + 1) / sides)
                    )
                }.collect(Collectors.toList())
            portal.specialShape = shape
            portal.cullableXStart = 0.0
            portal.cullableXEnd = 0.0
            portal.cullableYStart = 0.0
            portal.cullableYEnd = 0.0
        }

        fun GetPortalInAmbit(ctx: CastingContext): ArrayList<Entity> {
            val prtEnt: Iterable<Entity> = ctx.world.getLevel().getAllEntities()
            val portalsinambit: ArrayList<Entity> = ArrayList()
            for (Entity in prtEnt) {
                if ((Entity.getEyePosition().distanceToSqr(ctx.position) <= 32.0 * 32.0)
                    && Portal.entityType == Entity.getType()
                    && ((Entity as Portal).teleportable)
                ) {
                    portalsinambit.add(Entity)
                }
            }
            return portalsinambit
        }
        fun PortalVecRotate(prtRot: Vec3): List<Vec3> {
            var PrtRotCors: Vec3 = prtRot.cross(Vec3(0.0, 1.0, 0.0)) //should be 0.0 0.25 0
            var PrtRotCorsCors: Vec3 = PrtRotCors.cross(prtRot)

            when (prtRot.y()) {
                1.0 -> {
                    PrtRotCors = Vec3(0.0,0.0,1.0)
                    PrtRotCorsCors = Vec3(1.0,0.0,0.0)
                }
                -1.0 -> {
                    PrtRotCors = Vec3(1.0,0.0,0.0)
                    PrtRotCorsCors = Vec3(0.0,0.0,-1.0)
                }
            }
            return listOf(PrtRotCors,PrtRotCorsCors)
        }
    }
}