package me.khrys.dnd.charcreator.server.rest

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.sessions.sessions
import io.ktor.util.decodeBase64Bytes
import me.khrys.dnd.charcreator.server.mongo.UserService

suspend fun ApplicationCall.image(userService: UserService, name: String) {
    val user = userService.requireUserName(sessions)
    val image = userService.readImage(user, name)
    if (image == null) {
        respond(HttpStatusCode.NotFound, "No image present for the character: $name")
    } else {
        respond(convert(image))
    }
}

private fun convert(image: String) = image.split(",")[1].decodeBase64Bytes()
