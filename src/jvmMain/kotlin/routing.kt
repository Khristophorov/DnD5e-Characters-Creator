@file:OptIn(KtorExperimentalLocationsAPI::class)

package me.khrys.dnd.charcreator.server

import io.ktor.client.HttpClient
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.get
import io.ktor.server.locations.location
import io.ktor.server.locations.post
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.sessions.sessions
import io.ktor.util.pipeline.PipelineContext
import me.khrys.dnd.charcreator.common.IMAGES
import me.khrys.dnd.charcreator.common.IMAGES_FOLDER
import me.khrys.dnd.charcreator.common.LOGIN_URL
import me.khrys.dnd.charcreator.common.MONGO_URL_PARAM
import me.khrys.dnd.charcreator.common.STATIC_URL
import me.khrys.dnd.charcreator.common.models.User
import me.khrys.dnd.charcreator.server.authentication.authenticate
import me.khrys.dnd.charcreator.server.authentication.logout
import me.khrys.dnd.charcreator.server.locations.Characters
import me.khrys.dnd.charcreator.server.locations.Feats
import me.khrys.dnd.charcreator.server.locations.Index
import me.khrys.dnd.charcreator.server.locations.Login
import me.khrys.dnd.charcreator.server.locations.Logout
import me.khrys.dnd.charcreator.server.locations.Maneuvers
import me.khrys.dnd.charcreator.server.locations.Races
import me.khrys.dnd.charcreator.server.locations.Spells
import me.khrys.dnd.charcreator.server.locations.Translations
import me.khrys.dnd.charcreator.server.mongo.FeatsService
import me.khrys.dnd.charcreator.server.mongo.ManeuversService
import me.khrys.dnd.charcreator.server.mongo.MongoServiceFactory
import me.khrys.dnd.charcreator.server.mongo.RacesService
import me.khrys.dnd.charcreator.server.mongo.SpellsService
import me.khrys.dnd.charcreator.server.mongo.TranslationService
import me.khrys.dnd.charcreator.server.mongo.UserService
import me.khrys.dnd.charcreator.server.pages.index
import me.khrys.dnd.charcreator.server.rest.characters
import me.khrys.dnd.charcreator.server.rest.feats
import me.khrys.dnd.charcreator.server.rest.maneuvers
import me.khrys.dnd.charcreator.server.rest.races
import me.khrys.dnd.charcreator.server.rest.saveCharacter
import me.khrys.dnd.charcreator.server.rest.spells
import me.khrys.dnd.charcreator.server.rest.translations
import org.litote.kmongo.KMongo
import java.io.File

private const val ASSETS = "assets"

fun Route.routing(config: ApplicationConfig, httpClient: HttpClient) {
    val mongoFactory = MongoServiceFactory(KMongo.createClient(config.property(MONGO_URL_PARAM).getString()))
    val userService = UserService(mongoFactory.getUsers())
    val translationService = TranslationService(mongoFactory.getTranslations())
    val racesService = RacesService(mongoFactory.getRaces())
    val featsService = FeatsService(mongoFactory.getFeats())
    val maneuversService = ManeuversService(mongoFactory.getManeuvers())
    val spellsService = SpellsService(mongoFactory.getSpells())

    val imageFolder = config.property(IMAGES_FOLDER).getString()

    authenticate { authenticate(config, httpClient) }
    get<Index> { index(userService) }
    get<Logout> { logout(call) }
    get<Translations> { call.translations(translationService) }
    get<Characters> { call.characters(userService) }
    get<Races> { call.races(racesService) }
    get<Feats> { call.feats(featsService) }
    get<Maneuvers> { call.maneuvers(maneuversService) }
    get<Spells> { call.spells(spellsService) }
    post<Characters> { call.saveCharacter(userService) }
    staticResources(STATIC_URL, ASSETS)
    staticFiles(IMAGES, File(imageFolder))
}

private fun Route.authenticate(config: ApplicationConfig?, httpClient: HttpClient) {
    location<Login> {
        handle {
            val validationUrl = config?.property("ktor.oauth.validationUrl")?.getString()
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
