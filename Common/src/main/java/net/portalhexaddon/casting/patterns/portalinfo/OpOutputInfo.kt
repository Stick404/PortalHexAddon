package net.portalhexaddon.casting.patterns.portalinfo

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import qouteall.imm_ptl.core.portal.Portal
import qouteall.imm_ptl.core.portal.PortalManipulation

class OpOutputInfo : ConstMediaAction {
    /**
     * The number of arguments from the stack that this action requires.
     */
    override val argc: Int = 1
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val prtEnt: Entity = args.getEntity(0,argc)

        ctx.assertEntityInRange(prtEnt)

        var prt = (prtEnt as Portal)
        var prtOut = PortalManipulation.findReversePortal(prt)

        if (prtEnt.type !== Portal.entityType) {
            throw MishapBadEntity(prtEnt, Component.translatable("portaltranslate"))
        }

        if (prtOut !== null){
            return (prtOut as Entity).asActionResult
        }else{
            return prt.destination.asActionResult
        }

    }

}