package me.khrys.dnd.charcreator.server.rest

import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import mongo.RacesService

suspend fun ApplicationCall.races(racesService: RacesService) {
    respond(racesService.readRaces())
}
