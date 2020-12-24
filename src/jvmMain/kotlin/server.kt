package me.khrys.dnd.charcreator.server

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.script
import kotlinx.html.title

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/output.js") {}
    }
}

fun Application.main() {
    install(Routing) {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        static("/static") {
            resources()
        }
    }
}
