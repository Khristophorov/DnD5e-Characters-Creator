package me.khrys.dnd.charcreator.client

import com.ccfraser.muirwik.components.mThemeProvider
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import me.khrys.dnd.charcreator.common.ROOT
import react.dom.render
import styled.css
import styled.styledDiv

fun main() {
    window.onload = {
        render(document.getElementById(ROOT)) {
            mThemeProvider(defaultTheme) {
                styledDiv {
                    css { backgroundColor = Color(defaultTheme.palette.background.default) }
                    child(Welcome::class) {
                        attrs {
                            name = "Kotlin/JS"
                        }
                    }
                }
            }
        }
    }
}
