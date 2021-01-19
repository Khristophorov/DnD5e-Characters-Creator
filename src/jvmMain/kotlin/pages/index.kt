@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.server.pages

import io.ktor.application.ApplicationCall
import io.ktor.html.respondHtml
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.script
import kotlinx.html.title

suspend fun ApplicationCall.index() {
    respondHtml {
        head {
            title { +"index page" }
        }
        body {
            div { id = "root" }
            script(src = "/static/output.js") {}
        }
    }
}
