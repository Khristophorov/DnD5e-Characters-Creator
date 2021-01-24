package me.khrys.dnd.charcreator.server.authentication

import io.ktor.application.ApplicationCall
import io.ktor.auth.OAuthAccessTokenResponse.OAuth2
import io.ktor.auth.authentication
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.response.respondRedirect
import io.ktor.sessions.sessions
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.common.ROOT_URL
import me.khrys.dnd.charcreator.server.models.LoginSession
import me.khrys.dnd.charcreator.server.models.UserInfo

suspend fun authenticate(call: ApplicationCall, validationUrl: String, httpClient: HttpClient) {
    val principal: OAuth2? = call.authentication.principal()
    val username = retrieveUsername(principal?.accessToken, validationUrl, httpClient)
    val loginSession = LoginSession(username)
    call.sessions.set(LOGIN_SESSION, loginSession)
    call.respondRedirect(ROOT_URL)
}

suspend fun retrieveUsername(accessToken: String?, validationUrl: String, httpClient: HttpClient): String {
    return httpClient.get<UserInfo>(validationUrl + accessToken).email
}
