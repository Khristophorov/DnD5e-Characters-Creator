package me.khrys.dnd.charcreator.client

import com.ccfraser.muirwik.components.themeProvider
import kotlinx.browser.document
import kotlinx.browser.window
import me.khrys.dnd.charcreator.common.ROOT
import react.dom.div
import react.dom.render

fun main() {
    window.onload = {
        document.getElementById(ROOT)?.let {
            render(it) {
                themeProvider(defaultTheme) {
                    div {
                        child(mainDnd)
                    }
                }
            }
        }
    }
}
