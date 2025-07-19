package me.khrys.dnd.charcreator.server

import io.ktor.client.HttpClient
import io.ktor.server.auth.authenticate
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.resource
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.sessions.sessions
import java.io.File
import me.khrys.dnd.charcreator.common.IMAGES
import me.khrys.dnd.charcreator.common.IMAGES_FOLDER
import me.khrys.dnd.charcreator.common.LOGIN_URL
import me.khrys.dnd.charcreator.common.MONGO_URL_PARAM
import me.khrys.dnd.charcreator.common.STATIC_URL
import me.khrys.dnd.charcreator.common.models.User
import me.khrys.dnd.charcreator.server.authentication.authenticate
import me.khrys.dnd.charcreator.server.authentication.logout
import me.khrys.dnd.charcreator.server.mongo.ClassesService
import me.khrys.dnd.charcreator.server.mongo.FeatsService
import me.khrys.dnd.charcreator.server.mongo.ManeuversService
import me.khrys.dnd.charcreator.server.mongo.MongoServiceFactory
import me.khrys.dnd.charcreator.server.mongo.RacesService
import me.khrys.dnd.charcreator.server.mongo.SpellsService
import me.khrys.dnd.charcreator.server.mongo.TranslationService
import me.khrys.dnd.charcreator.server.mongo.UserService
import me.khrys.dnd.charcreator.server.pages.index
import me.khrys.dnd.charcreator.server.resources.Characters
import me.khrys.dnd.charcreator.server.resources.Classes
import me.khrys.dnd.charcreator.server.resources.Feats
import me.khrys.dnd.charcreator.server.resources.Image
import me.khrys.dnd.charcreator.server.resources.Index
import me.khrys.dnd.charcreator.server.resources.Login
import me.khrys.dnd.charcreator.server.resources.Logout
import me.khrys.dnd.charcreator.server.resources.Maneuvers
import me.khrys.dnd.charcreator.server.resources.Races
import me.khrys.dnd.charcreator.server.resources.Spells
import me.khrys.dnd.charcreator.server.resources.Translations
import me.khrys.dnd.charcreator.server.rest.characters
import me.khrys.dnd.charcreator.server.rest.classes
import me.khrys.dnd.charcreator.server.rest.feats
import me.khrys.dnd.charcreator.server.rest.image
import me.khrys.dnd.charcreator.server.rest.maneuvers
import me.khrys.dnd.charcreator.server.rest.races
import me.khrys.dnd.charcreator.server.rest.saveCharacter
import me.khrys.dnd.charcreator.server.rest.spells
import me.khrys.dnd.charcreator.server.rest.translations
import org.litote.kmongo.KMongo

private const val ASSETS = "assets"

fun Route.routing(config: ApplicationConfig, httpClient: HttpClient) {
    val mongoFactory = MongoServiceFactory(KMongo.createClient(config.property(MONGO_URL_PARAM).getString()))
    val userService = UserService(mongoFactory.getUsers())
    val translationService = TranslationService(mongoFactory.getTranslations())
    val racesService = RacesService(mongoFactory.getRaces())
    val classesService = ClassesService(mongoFactory.getClasses())
    val featsService = FeatsService(mongoFactory.getFeats())
    val maneuversService = ManeuversService(mongoFactory.getManeuvers())
    val spellsService = SpellsService(mongoFactory.getSpells())

    val imageFolder = config.property(IMAGES_FOLDER).getString()

    authenticate { authenticate(config, httpClient) }
    get<Index> { index(userService) }
    get<Logout> { logout(call) }
    get<Image> { call.image(userService, it.name) }
    get<Translations> { call.translations(translationService) }
    get<Characters> { call.characters(userService) }
    get<Races> { call.races(racesService) }
    get<Classes> { call.classes(classesService) }
    get<Feats> { call.feats(featsService) }
    get<Maneuvers> { call.maneuvers(maneuversService) }
    get<Spells> { call.spells(spellsService) }
    post<Characters> { call.saveCharacter(userService) }
    staticResources(STATIC_URL, ASSETS)
    staticFiles(IMAGES, File(imageFolder))
}

private fun Route.authenticate(config: ApplicationConfig?, httpClient: HttpClient) {
    resource<Login> {
        handle {
            val validationUrl = config?.property("ktor.oauth.validationUrl")?.getString()
            authenticate(call, validationUrl, httpClient)
        }
    }
}

private suspend fun RoutingContext.index(userService: UserService) {
    val login = retrieveUserName(call.sessions)
    if (login == null) {
        call.respondRedirect(LOGIN_URL)
    } else {
        userService.storeUser(User(login))
        call.index()
    }
}
