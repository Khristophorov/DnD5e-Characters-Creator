package me.khrys.dnd.charcreator.server.rest

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import me.khrys.dnd.charcreator.server.mongo.TranslationService

suspend fun ApplicationCall.translations(translationService: TranslationService) {
    respond(translationService.readTranslations())
}
