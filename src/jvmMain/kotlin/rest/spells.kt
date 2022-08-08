package me.khrys.dnd.charcreator.server.rest

import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import me.khrys.dnd.charcreator.server.mongo.SpellsService

suspend fun ApplicationCall.spells(spellsService: SpellsService) {
    respond(spellsService.readSpells())
}
