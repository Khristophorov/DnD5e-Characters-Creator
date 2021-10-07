package me.khrys.dnd.charcreator.server.rest

import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import me.khrys.dnd.charcreator.server.mongo.FeatsService

suspend fun ApplicationCall.feats(featsService: FeatsService) {
    respond(featsService.readFeats())
}