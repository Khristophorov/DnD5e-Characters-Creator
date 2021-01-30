package me.khrys.dnd.charcreator.server.rest

import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import me.khrys.dnd.charcreator.server.mongo.MongoService

suspend fun ApplicationCall.translations(mongoService: MongoService) {
    respond(mongoService.readTranslations())
}