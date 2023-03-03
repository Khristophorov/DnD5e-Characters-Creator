package me.khrys.dnd.charcreator.server.pages

import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtml
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
import kotlinx.html.unsafe
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import me.khrys.dnd.charcreator.common.CLASS_ABILITY_BOX
import me.khrys.dnd.charcreator.common.CLASS_ROUND_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_BACKGROUND
import me.khrys.dnd.charcreator.common.CLASS_BOLD
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_COLLAPSED_CELL
import me.khrys.dnd.charcreator.common.CLASS_DISABLED
import me.khrys.dnd.charcreator.common.CLASS_DISABLE_POINTER
import me.khrys.dnd.charcreator.common.CLASS_FLOAT_LEFT
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CLASS_PADDINGS
import me.khrys.dnd.charcreator.common.CLASS_TEXT_CENTER
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
                unsafe {
                    +"""
                @font-face {
                    font-family: Pterra;
                    src: url($PTERRA_URL);
                }
                * {
                    font-family: Pterra !important;
                }
                input::-webkit-inner-spin-button {
                     -webkit-appearance: none;
                }
                .${CLASS_DISABLED} {
                    display: none !important;
                }
                .${CLASS_JUSTIFY_BETWEEN} {
                    justify-content: space-between !important;
                }
                .${CLASS_CENTER} {
                    margin-left: auto !important;
                    margin-right: auto !important;
                    display: block !important;
                }
                .${CLASS_BACKGROUND} {
                    background-color: #fff3e0 !important;
                }
                .${CLASS_ABILITY_BOX} {
                    width: 100px;
                    font-weight: bold;
                    margin: 5px;
                }
                .${CLASS_ABILITY_BOX} input {
                    font-size: 250%;
                    text-align: center;
                }
                .${CLASS_ROUND_BORDERED} {
                    width: 25px !important;
                    height: 25px !important;
                    border: 2px solid black;
                }
                .${CLASS_BORDERED} {
                    border: 1px solid black;
                }
                .${CLASS_INLINE} {
                    display: flex;
                }
                .${CLASS_BOLD} {
                    font-weight: bold !important;
                }
                .${CLASS_TEXT_CENTER} input {
                    text-align: center;
                }
                .${CLASS_FLOAT_LEFT} {
                    float: left;
                    margin: 7px 7px 7px 0;
                }
                .${CLASS_PADDINGS} {
                    padding: 20px 5px;
                }
                .${CLASS_DISABLE_POINTER} {
                    pointer-events: none;
                }
                .${CLASS_COLLAPSED_CELL} {
                    padding-bottom: 0 !important;
                    padding-top: 0 !important;
                }
                """
                }
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
