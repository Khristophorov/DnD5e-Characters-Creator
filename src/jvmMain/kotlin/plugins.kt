@file:OptIn(KtorExperimentalLocationsAPI::class)

package me.khrys.dnd.charcreator.server

import io.ktor.client.HttpClient
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings.OAuth2ServerSettings
import io.ktor.server.auth.oauth
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Locations
import io.ktor.server.locations.url
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.common.ROOT_URL
import me.khrys.dnd.charcreator.server.locations.Login
import me.khrys.dnd.charcreator.server.models.LoginSession

fun Application.configurePlugins(httpClient: HttpClient, loginProvider: OAuth2ServerSettings) {
    install(Sessions) {
        cookie<LoginSession>(LOGIN_SESSION, SessionStorageMemory()) { cookie.path = ROOT_URL }
    }
    install(Locations)
    install(ContentNegotiation) {
        json()
    }
    install(Authentication) {
        oauth {
            client = httpClient
            providerLookup = { loginProvider }
            urlProvider = { url(Login()) }
        }
    }
}
