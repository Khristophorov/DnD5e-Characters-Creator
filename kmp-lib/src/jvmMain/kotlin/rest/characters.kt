package me.khrys.dnd.charcreator.server.rest

import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.sessions.sessions
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.server.mongo.UserService

suspend fun ApplicationCall.saveCharacter(userService: UserService) {
    val character = receive(Character::class)
    val user = userService.requireUserName(sessions)

    userService.storeCharacter(user, character)
    respond(Created, "")
}

suspend fun ApplicationCall.characters(userService: UserService) {
    val user = userService.requireUserName(sessions)
    respond(userService.readCharacters(user))
}
