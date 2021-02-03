package me.khrys.dnd.charcreator.server.rest

import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import me.khrys.dnd.charcreator.server.mongo.TranslationService

suspend fun ApplicationCall.translations(translationService: TranslationService) {
    respond(translationService.readTranslations())
}