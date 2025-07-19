package me.khrys.dnd.charcreator.server

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.routing.routing
import me.khrys.dnd.charcreator.server.authentication.initLoginProvider

fun Application.main() {
    val config = environment.config
    val httpClient = HttpClient(Apache) {
        install(ContentNegotiation) {
            json()
        }
    }.apply {
        monitor.subscribe(ApplicationStopping) { close() }
    }
    val loginProvider = initLoginProvider(config)

    configurePlugins(httpClient, loginProvider)

    routing {
        routing(config, httpClient)
    }
}
