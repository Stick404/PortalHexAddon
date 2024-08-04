package net.portalhexaddon.casting.patterns.portaledit

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapEntityTooFarAway
import com.mojang.math.Quaternion
import com.mojang.math.Vector3f
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import net.portalhexaddon.portals.PortalHexUtils.Companion.PortalVecRotate
import qouteall.imm_ptl.core.api.PortalAPI
import qouteall.imm_ptl.core.portal.Portal
import qouteall.imm_ptl.core.portal.PortalManipulation
import qouteall.q_misc_util.my_util.DQuaternion

//right now this spell is not in the game
//just dont want to work on it lol
class OpRotateSideOfPortal : SpellAction {
    /**
     * The number of arguments from the stack that this action requires.
     */
    override val argc: Int = 2
    private val cost = 16 * MediaConstants.DUST_UNIT

    /**
     * The method called when this Action is actually executed. Accepts the [args]
     * that were on the stack (there will be [argc] of them), and the [ctx],
     * which contains things like references to the caster, the ServerLevel,
     * methods to determine whether locations and entities are in ambit, etc.
     * Returns a triple of things. The [RenderedSpell] is responsible for the spell actually
     * doing things in the world, the [Int] is how much media the spell should cost,
     * and the [List] of [ParticleSpray] renders particle effects for the result of the SpellAction.
     *
     * The [execute] method should only contain code to find the targets of the spell and validate
     * them. All the code that actually makes changes to the world (breaking blocks, teleporting things,
     * etc.) should be in the private [Spell] data class below.
     */
    override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        val prtEnt: Entity = args.getEntity(0,argc)
        val prtRot: Vec3 = args.getVec3(1,argc)

        ctx.assertEntityInRange(prtEnt)

        if (prtEnt.type !== Portal.entityType) {
            throw MishapBadEntity(prtEnt, Component.translatable("portaltranslate"))
        }

        return Triple(
            Spell(prtEnt, prtRot),
            cost,
            listOf(ParticleSpray.burst(ctx.caster.position(), 1.0))
        )
    }

    private data class Spell(var prtEntity: Entity, var prtRot: Vec3) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            val prt = (prtEntity as Portal)
            var revFlipPrt = prt
            val quat = DQuaternion.fromMcQuaternion(Quaternion.fromXYZDegrees(Vector3f(prtRot.x.toFloat(),prtRot.y.toFloat(),prtRot.z.toFloat())))
            //Quaternion.fromXYZ(prtRot.x.toFloat(),prtRot.y.toFloat(),prtRot.z.toFloat()))
            println(prt)

            val flipPrt = PortalManipulation.findFlippedPortal(prt)
            val revPrt = PortalManipulation.findReversePortal(prt)
            if (revPrt !== null) {
                revFlipPrt = PortalManipulation.findFlippedPortal(revPrt)!!
            }

            prt.rotation = quat.toMcQuaternion()
            prt.reloadAndSyncToClient()

            if (flipPrt !== null) {
                PortalAPI.setPortalOrientationQuaternion(flipPrt, quat)
                flipPrt.reloadAndSyncToClient()
            }
            if (revPrt !== null) {
                revPrt.setOrientation(PortalVecRotate(prtRot)[0], PortalVecRotate(prtRot)[1])
                revPrt.reloadAndSyncToClient()
            }
            if (revFlipPrt !== null) {
                revFlipPrt.setOrientation(PortalVecRotate(prtRot)[0], PortalVecRotate(prtRot)[1].multiply(Vec3(-1.0,-1.0,-1.0)))
                revFlipPrt.reloadAndSyncToClient()
            }
        }
    }
}