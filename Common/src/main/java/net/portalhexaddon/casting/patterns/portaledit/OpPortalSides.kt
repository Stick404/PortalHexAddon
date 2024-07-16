package net.portalhexaddon.casting.patterns.portaledit

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.portalhexaddon.entites.HexPortalEntity
import net.portalhexaddon.portals.PortalHexUtils
import qouteall.imm_ptl.core.portal.Portal
import qouteall.imm_ptl.core.portal.PortalManipulation
import kotlin.math.roundToInt

class OpPortalSides : SpellAction {
    /**
     * The number of arguments from the stack that this action requires.
     */
    override val argc: Int = 3
    private val cost = 0

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
        val prtSides: Int = args.getIntBetween(1,3,16,argc)
        val prtRoll: Double = args.getDoubleBetween(2,0.0,1.0,argc)

        ctx.isEntityInRange(prtEnt)


        if (prtEnt.type !== Portal.entityType) {
            throw MishapBadEntity(prtEnt, Component.translatable("portaltranslate"))
        }

        return Triple(
            Spell(prtEnt, prtSides, prtRoll),
            cost,
            listOf(ParticleSpray.burst(ctx.caster.position(), 1.0))
        )
    }

    private data class Spell(var prtEntity: Entity, var prtSides: Int, var prtRoll: Double) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            val prt = (prtEntity as Portal)
            var revFlipPrt = prt
            val prtKeepCasts = (prt as HexPortalEntity)
            prtKeepCasts.portalSides = prtSides
            prtKeepCasts.portalRoll = prtRoll

            val flipPrt = PortalManipulation.findFlippedPortal(prt)
            val revPrt = PortalManipulation.findReversePortal(prt)

            if (revPrt !== null) {
               revFlipPrt = PortalManipulation.findFlippedPortal(revPrt)!!
            }

            PortalHexUtils.MakePortalNGon(prt,prtSides, prtRoll)
            prt.reloadAndSyncToClient()

            if (flipPrt != null) {
                PortalHexUtils.MakePortalNGon(flipPrt,prtSides, prtRoll)
                flipPrt.reloadAndSyncToClient()
            }

            if (revPrt != null) {
                PortalHexUtils.MakePortalNGon(revPrt,prtSides, prtRoll)
                revPrt.reloadAndSyncToClient()
            }
            if (revFlipPrt != null && revFlipPrt != prt) {
                PortalHexUtils.MakePortalNGon(revFlipPrt,prtSides.toInt(), prtRoll)
                revFlipPrt.reloadAndSyncToClient()
            }
        }
    }
}