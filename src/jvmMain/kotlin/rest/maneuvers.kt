package me.khrys.dnd.charcreator.server.rest

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import me.khrys.dnd.charcreator.server.mongo.ManeuversService

suspend fun ApplicationCall.maneuvers(maneuversService: ManeuversService) {
    respond(maneuversService.readManeuvers())
}
