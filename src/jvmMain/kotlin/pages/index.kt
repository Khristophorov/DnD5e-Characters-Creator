@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.server.pages

import io.ktor.application.ApplicationCall
import io.ktor.html.respondHtml
import kotlinx.html.audio
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.style
import kotlinx.html.title
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import me.khrys.dnd.charcreator.common.CLASS_BACKGROUND
import me.khrys.dnd.charcreator.common.CLICK_SOUND_URL
import me.khrys.dnd.charcreator.common.FAVICON_URL
import me.khrys.dnd.charcreator.common.OUTPUT_URL
import me.khrys.dnd.charcreator.common.PTERRA_URL
import me.khrys.dnd.charcreator.common.ROOT
import me.khrys.dnd.charcreator.common.TITLE

suspend fun ApplicationCall.index() {
    respondHtml {
        head {
            title { +TITLE }
            style {
                +"""
                @font-face {
                    font-family: Pterra;
                    src: url($PTERRA_URL);
                }
                * {
                    font-family: Pterra !important;
                }
                .disabled {
                    display: none !important;
                }
                .justify-between {
                    justify-content: space-between !important;
                }
                .center {
                    margin-left: auto !important;
                    margin-right: auto !important;
                    display: block !important
                }
                .background {
                    background-color: #fff3e0;
                }
                """
            }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1.0"
            }
            link {
                rel = "shortcut icon"
                href = FAVICON_URL
                type = "image/x-icon"
            }
        }
        body(classes = CLASS_BACKGROUND) {
            div { id = ROOT }
            script(src = OUTPUT_URL) {}
            audio {
                id = BUTTON_SOUND_ID
                src = CLICK_SOUND_URL
            }
        }
    }
}
