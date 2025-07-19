package me.khrys.dnd.charcreator.server.authentication

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpMethod
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.OAuthAccessTokenResponse.OAuth2
import io.ktor.server.auth.OAuthServerSettings.OAuth2ServerSettings
import io.ktor.server.auth.authentication
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.sessions
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.common.ROOT_URL
import me.khrys.dnd.charcreator.server.models.LoginSession
import me.khrys.dnd.charcreator.server.models.UserInfo

fun initLoginProvider(config:ApplicationConfig):OAuth2ServerSettings = OAuth2ServerSettings(
    name = config.property("ktor.oauth.name").getString(),
    authorizeUrl = config.property("ktor.oauth.authorizeUrl").getString(),
    accessTokenUrl = config.property("ktor.oauth.accessTokenUrl").getString(),
    requestMethod = HttpMethod.Post,

    clientId = config.property("ktor.oauth.clientId").getString(),
    clientSecret = config.property("ktor.oauth.clientSecret").getString(),
    defaultScopes = config.property("ktor.oauth.defaultScopes").getList()
)

suspend fun authenticate(call: ApplicationCall, validationUrl: String?, httpClient: HttpClient) {
    val principal: OAuth2? = call.authentication.principal()
    val username = retrieveUsername(principal?.accessToken, validationUrl, httpClient)
    val loginSession = LoginSession(username)
    call.sessions.set(LOGIN_SESSION, loginSession)
    call.application.environment.log.info("$username has logged in")
    call.respondRedirect(ROOT_URL)
}

suspend fun retrieveUsername(accessToken: String?, validationUrl: String?, httpClient: HttpClient): String =
    httpClient.get(validationUrl + accessToken).body<UserInfo>().email
