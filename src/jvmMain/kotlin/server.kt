@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.server

import io.ktor.application.Application
import io.ktor.application.ApplicationStopping
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.OAuthServerSettings
import io.ktor.auth.authenticate
import io.ktor.auth.oauth
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.features.CallLogging
import io.ktor.http.HttpMethod
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.locations.location
import io.ktor.locations.url
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.sessions
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.common.LOGIN_URL
import me.khrys.dnd.charcreator.common.ROOT_URL
import me.khrys.dnd.charcreator.common.STATIC_URL
import me.khrys.dnd.charcreator.server.authentication.authenticate
import me.khrys.dnd.charcreator.server.authentication.logout
import me.khrys.dnd.charcreator.server.locations.Index
import me.khrys.dnd.charcreator.server.locations.Login
import me.khrys.dnd.charcreator.server.locations.Logout
import me.khrys.dnd.charcreator.server.models.LoginSession
import me.khrys.dnd.charcreator.server.pages.index
import org.slf4j.event.Level.INFO

fun Application.main() {
    val httpClient = HttpClient(Apache) { install(JsonFeature) }.apply {
        environment.monitor.subscribe(ApplicationStopping) { close() }
    }

    val loginProvider = OAuthServerSettings.OAuth2ServerSettings(
        name = environment.config.property("ktor.oauth.name").getString(),
        authorizeUrl = environment.config.property("ktor.oauth.authorizeUrl").getString(),
        accessTokenUrl = environment.config.property("ktor.oauth.accessTokenUrl").getString(),
        requestMethod = HttpMethod.Post,

        clientId = environment.config.property("ktor.oauth.clientId").getString(),
        clientSecret = environment.config.property("ktor.oauth.clientSecret").getString(),
        defaultScopes = environment.config.property("ktor.oauth.defaultScopes").getList()
    )

    install(CallLogging) { level = INFO }
    install(Sessions) {
        cookie<LoginSession>(LOGIN_SESSION, SessionStorageMemory()) { cookie.path = ROOT_URL }
    }
    install(Locations)
    install(Authentication) {
        oauth {
            client = httpClient
            providerLookup = { loginProvider }
            urlProvider = { url(Login()) }
        }
    }
    install(Routing) {
        authenticate {
            location<Login> {
                handle {
                    val validationUrl = environment.config.property("ktor.oauth.validationUrl").getString()
                    authenticate(call, validationUrl, httpClient)
                }
            }
        }
        get<Index> {
            if (call.sessions.get(LOGIN_SESSION) == null) {
                call.respondRedirect(LOGIN_URL)
            } else {
                call.index()
            }
        }
        get<Logout> { logout(call) }
        static(STATIC_URL) { resources() }
    }
}
