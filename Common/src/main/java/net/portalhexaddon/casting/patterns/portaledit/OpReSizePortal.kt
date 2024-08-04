package net.portalhexaddon.casting.patterns.portaledit

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapEntityTooFarAway
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.portalhexaddon.entites.HexPortalEntity
import net.portalhexaddon.portals.PortalHexUtils
import qouteall.imm_ptl.core.portal.Portal
import qouteall.imm_ptl.core.portal.PortalManipulation

class OpReSizePortal : SpellAction {
    /**
     * The number of arguments from the stack that this action requires.
     */
    override val argc: Int = 3

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
        val prtHight: Double = args.getDoubleBetween(1,1.0/10.0,10.0,argc)
        val prtWidth: Double = args.getDoubleBetween(2,1.0/10.0,10.0,argc)

        ctx.assertEntityInRange(prtEnt)

        if (prtEnt.type !== Portal.entityType) {
            throw MishapBadEntity(prtEnt, Component.translatable("portaltranslate"))
        }

        val cost = ((prtWidth + prtHight)/2).toInt() * 2 * MediaConstants.SHARD_UNIT
        //16 * MediaConstants.SHARD_UNIT
        return Triple(
            Spell(prtEnt,prtHight,prtWidth),
            cost,
            listOf(ParticleSpray.burst(ctx.caster.position(), 1.0))
        )
    }

    private data class Spell(var prtEntity: Entity, var prtHight: Double, var prtWidth: Double) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            val prt = (prtEntity as Portal)
            var revFlipPrt = prt
            //val prtKeepCasts = (prt as HexPortalEntity)
            //val prtSides = prtKeepCasts.portalSides
            //val prtRoll = prtKeepCasts.portalRoll

            val flipPrt = PortalManipulation.findFlippedPortal(prt)
            val revPrt = PortalManipulation.findReversePortal(prt)
            if (revPrt !== null) {
               revFlipPrt = PortalManipulation.findFlippedPortal(revPrt)!!
            }

            prt.width = prtWidth
            prt.height = prtHight
            PortalHexUtils.MakePortalNGon(prt, 6, 0.0)
            prt.reloadAndSyncToClient()

            if (flipPrt != null) {
                flipPrt.width = prtWidth
                flipPrt.height = prtHight
                PortalHexUtils.MakePortalNGon(flipPrt, 6, 0.0)
                flipPrt.reloadAndSyncToClient()
            }

            if (revPrt != null) {
                revPrt.width = prtWidth
                revPrt.height = prtHight
                PortalHexUtils.MakePortalNGon(revPrt, 6, 0.0)
                revPrt.reloadAndSyncToClient()
            }
            if (revFlipPrt != null && revFlipPrt != prt) {
                revFlipPrt.width = prtWidth
                revFlipPrt.height = prtHight
                PortalHexUtils.MakePortalNGon(revFlipPrt, 6, 0.0)
                revFlipPrt.reloadAndSyncToClient()
            }
        }
    }
}