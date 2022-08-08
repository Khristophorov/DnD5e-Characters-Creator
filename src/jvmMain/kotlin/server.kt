@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.server

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationStopping
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.oauth
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.config.ApplicationConfig
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.locations.location
import io.ktor.locations.post
import io.ktor.locations.url
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.serialization.json
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.sessions
import io.ktor.util.pipeline.PipelineContext
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.common.LOGIN_URL
import me.khrys.dnd.charcreator.common.ROOT_URL
import me.khrys.dnd.charcreator.common.STATIC_URL
import me.khrys.dnd.charcreator.common.models.User
import me.khrys.dnd.charcreator.server.authentication.authenticate
import me.khrys.dnd.charcreator.server.authentication.initLoginProvider
import me.khrys.dnd.charcreator.server.authentication.logout
import me.khrys.dnd.charcreator.server.locations.Characters
import me.khrys.dnd.charcreator.server.locations.Feats
import me.khrys.dnd.charcreator.server.locations.Index
import me.khrys.dnd.charcreator.server.locations.Login
import me.khrys.dnd.charcreator.server.locations.Logout
import me.khrys.dnd.charcreator.server.locations.Races
import me.khrys.dnd.charcreator.server.locations.Spells
import me.khrys.dnd.charcreator.server.locations.Translations
import me.khrys.dnd.charcreator.server.models.LoginSession
import me.khrys.dnd.charcreator.server.mongo.FeatsService
import me.khrys.dnd.charcreator.server.mongo.MongoServiceFactory
import me.khrys.dnd.charcreator.server.mongo.SpellsService
import me.khrys.dnd.charcreator.server.mongo.TranslationService
import me.khrys.dnd.charcreator.server.mongo.UserService
import me.khrys.dnd.charcreator.server.pages.index
import me.khrys.dnd.charcreator.server.rest.characters
import me.khrys.dnd.charcreator.server.rest.feats
import me.khrys.dnd.charcreator.server.rest.races
import me.khrys.dnd.charcreator.server.rest.saveCharacter
import me.khrys.dnd.charcreator.server.rest.spells
import me.khrys.dnd.charcreator.server.rest.translations
import mongo.RacesService
import org.litote.kmongo.KMongo
import org.slf4j.event.Level.INFO

fun Application.main() {
    val config = environment.config

    val httpClient = HttpClient(Apache) { install(JsonFeature) }.apply {
        environment.monitor.subscribe(ApplicationStopping) { close() }
    }

    val mongoFactory = MongoServiceFactory(KMongo.createClient(config.property("ktor.mongo.url").getString()))
    val userService = UserService(mongoFactory.getUsers())
    val translationService = TranslationService(mongoFactory.getTranslations())
    val racesService = RacesService(mongoFactory.getRaces())
    val featsService = FeatsService(mongoFactory.getFeats())
    val spellsService = SpellsService(mongoFactory.getSpells())

    val loginProvider = initLoginProvider(config)

    install(CallLogging) { level = INFO }
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
    install(Routing) {
        authenticate { authenticate(config, httpClient) }
        get<Index> { index(userService) }
        get<Logout> { logout(call) }
        get<Translations> { call.translations(translationService) }
        get<Characters> { call.characters(userService) }
        get<Races> { call.races(racesService) }
        get<Feats> { call.feats(featsService) }
        get<Spells> { call.spells(spellsService) }
        post<Characters> { call.saveCharacter(userService) }
        static(STATIC_URL) { resources() }
    }
}

private fun Route.authenticate(config: ApplicationConfig, httpClient: HttpClient) {
    location<Login> {
        handle {
            val validationUrl = config.property("ktor.oauth.validationUrl").getString()
            authenticate(call, validationUrl, httpClient)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.index(userService: UserService) {
    val login = retrieveUserName(call.sessions)
    if (login == null) {
        call.respondRedirect(LOGIN_URL)
    } else {
        userService.storeUser(User(login))
        call.index()
    }
}
