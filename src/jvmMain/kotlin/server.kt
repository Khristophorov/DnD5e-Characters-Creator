@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.server

import io.ktor.application.Application
import io.ktor.application.ApplicationStopping
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.oauth
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.features.CallLogging
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
import me.khrys.dnd.charcreator.common.models.User
import me.khrys.dnd.charcreator.server.authentication.authenticate
import me.khrys.dnd.charcreator.server.authentication.initLoginProvider
import me.khrys.dnd.charcreator.server.authentication.logout
import me.khrys.dnd.charcreator.server.locations.Index
import me.khrys.dnd.charcreator.server.locations.Login
import me.khrys.dnd.charcreator.server.locations.Logout
import me.khrys.dnd.charcreator.server.models.LoginSession
import me.khrys.dnd.charcreator.server.mongo.MongoService
import me.khrys.dnd.charcreator.server.pages.index
import org.litote.kmongo.KMongo
import org.slf4j.event.Level.INFO

fun Application.main() {
    val config = environment.config

    val httpClient = HttpClient(Apache) { install(JsonFeature) }.apply {
        environment.monitor.subscribe(ApplicationStopping) { close() }
    }

    val mongoService = MongoService(KMongo.createClient(config.property("ktor.mongo.url").getString()))

    val loginProvider = initLoginProvider(config)

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
                    val validationUrl = config.property("ktor.oauth.validationUrl").getString()
                    authenticate(call, validationUrl, httpClient)
                }
            }
        }
        get<Index> {
            val login = call.sessions.get(LOGIN_SESSION) as LoginSession?
            if (login == null) {
                call.respondRedirect(LOGIN_URL)
            } else {
                mongoService.storeUser(User(login.username))
                call.index()
            }
        }
        get<Logout> { logout(call) }
        static(STATIC_URL) { resources() }
    }
}
