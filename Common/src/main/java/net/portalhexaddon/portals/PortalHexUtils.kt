package net.portalhexaddon.portals

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetEntitiesBy.Companion.isReasonablySelectable
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import qouteall.imm_ptl.core.portal.GeometryPortalShape
import qouteall.imm_ptl.core.portal.Portal
import java.util.function.BiConsumer
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.math.cos
import kotlin.math.sin

class PortalHexUtils {
    companion object {
        fun MakePortalNGon(portal: Portal, sides: Int, roll: Double) { //GOTTEN FROM IMMERSIVE PORTALS PortalCommand
            val shape = GeometryPortalShape()
            val twoPi = Math.PI * 2
            shape.triangles = IntStream.range(0, sides)
                .mapToObj { i: Int ->
                    GeometryPortalShape.TriangleInPlane(
                        0.0, 0.0,
                        portal.width * 0.5 * cos(twoPi * ((i.toDouble()) / sides + roll)),
                        portal.height * 0.5 * sin(twoPi * ((i.toDouble()) / sides + roll)),
                        portal.width * 0.5 * cos(twoPi * ((i.toDouble() + 1) / sides + roll)),
                        portal.height * 0.5 * sin(twoPi * ((i.toDouble() + 1) / sides + roll))
                    )
                }.collect(Collectors.toList())
            portal.specialShape = shape
            portal.cullableXStart = 0.0
            portal.cullableXEnd = 0.0
            portal.cullableYStart = 0.0
            portal.cullableYEnd = 0.0
        }

        fun GetPortalInAmbit(ctx: CastingContext, pos: Vec3): List<Entity> { //this used to be a lot bigger, but has gotten a bit smaller, still its own function due to... me not wanting to remove it lol
            val aabb = AABB(pos.add(Vec3(-32.0, -32.0, -32.0)), pos.add(Vec3(32.0, 32.0, 32.0))) //this non laggy solution gotten from this: https://github.com/FallingColors/HexMod/blob/c8510ed83d/Common/src/main/java/at/petrak/hexcasting/common/casting/operators/selectors/OpGetEntitiesBy.kt
            val entitiesGot = ctx.world.getEntities(Portal.entityType, aabb) {true}
            return entitiesGot
        }
        fun PortalVecRotate(prtRot: Vec3): List<Vec3> {
            var PrtRotCors: Vec3 = prtRot.cross(Vec3(0.0, 1.0, 0.0))
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
        //this is less of a "portal" util, and more of a "so Stickia does not lose her mind" util
        //I *really* dont want to remake this in Java for the entity Registry stuff. Or remake the fabric entry in Kotlin
        public fun <T> bind(registry: Registry<in T>): BiConsumer<T, ResourceLocation> =
            BiConsumer<T, ResourceLocation> { t, id -> Registry.register(registry, id, t) }
    }
}