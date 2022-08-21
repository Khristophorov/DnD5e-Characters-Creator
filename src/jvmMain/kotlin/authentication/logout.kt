package me.khrys.dnd.charcreator.server.authentication

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.sessions
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.common.LOGIN_URL

suspend fun logout(call: ApplicationCall) {
    call.sessions.clear(LOGIN_SESSION)
    call.respondRedirect(LOGIN_URL)
}
