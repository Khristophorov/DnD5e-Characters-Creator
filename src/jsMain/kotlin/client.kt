package me.khrys.dnd.charcreator.client

import me.khrys.dnd.charcreator.common.ROOT
import mui.material.styles.ThemeProvider
import react.FC
import react.Props
import react.createElement
import react.dom.client.createRoot
import react.dom.html.ReactHTML.div
import web.dom.document
import web.window.window

fun main() {
    window.onload = {
        document.getElementById(ROOT)?.let {
            console.info("Starting the app.")
            val mainElement = FC<Props> {
                ThemeProvider {
                    this.theme = defaultTheme
                    div {
                        MainDnd()
                    }
                }
            }
            createRoot(it).render(createElement(mainElement))
        }
    }
}
