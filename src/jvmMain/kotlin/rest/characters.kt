package me.khrys.dnd.charcreator.server.rest

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.sessions.CurrentSession
import io.ktor.sessions.sessions
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.server.mongo.UserService
import me.khrys.dnd.charcreator.server.retrieveUserName

suspend fun ApplicationCall.saveCharacter(userService: UserService) {
    val character: Character = receive(Character::class)
    val user = requireUserName(sessions)

    userService.storeCharacter(user, character)
    respond(Created, "")
}

suspend fun ApplicationCall.characters(userService: UserService) {
    val user = requireUserName(sessions)
    respond(userService.readCharacters(user))
}

fun requireUserName(session: CurrentSession) =
    retrieveUserName(session) ?: throw IllegalStateException("Invalid session")
