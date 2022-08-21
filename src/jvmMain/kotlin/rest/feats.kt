package me.khrys.dnd.charcreator.server.rest

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import me.khrys.dnd.charcreator.server.mongo.FeatsService

suspend fun ApplicationCall.feats(featsService: FeatsService) {
    respond(featsService.readFeats())
}
