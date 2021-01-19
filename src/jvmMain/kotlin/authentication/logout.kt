package me.khrys.dnd.charcreator.server.authentication

import io.ktor.application.ApplicationCall
import io.ktor.response.respondRedirect
import io.ktor.sessions.sessions
import me.khrys.dnd.charcreator.server.utils.LOGIN_SESSION
import me.khrys.dnd.charcreator.server.utils.LOGIN_URL

suspend fun logout(call: ApplicationCall) {
    call.sessions.clear(LOGIN_SESSION)
    call.respondRedirect(LOGIN_URL)
}
