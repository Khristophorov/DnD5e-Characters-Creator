package me.khrys.dnd.charcreator.client

import com.ccfraser.muirwik.components.mThemeProvider
import kotlinx.browser.document
import kotlinx.browser.window
import me.khrys.dnd.charcreator.common.ROOT
import react.dom.div
import react.dom.render

fun main() {
    window.onload = {
        render(document.getElementById(ROOT)) {
            mThemeProvider(defaultTheme) {
                div {
                    child(mainDnd)
                }
            }
        }
    }
}
