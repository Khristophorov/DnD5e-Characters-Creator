package me.khrys.dnd.charcreator.server

import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import io.ktor.server.application.ApplicationCall
import io.ktor.server.resources.href
import io.ktor.server.sessions.CurrentSession
import io.ktor.server.util.createFromCall
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.server.models.LoginSession

fun retrieveUserName(session: CurrentSession): String? = (session.get(LOGIN_SESSION) as LoginSession?)?.username

inline fun <reified T : Any> ApplicationCall.buildUrl(resource: T): String {
    val urlBuilder = URLBuilder.createFromCall(this)
        .takeFrom(application.href(resource))
    urlBuilder.parameters.clear()
    return urlBuilder.buildString()
}
