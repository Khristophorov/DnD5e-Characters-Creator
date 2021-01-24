package me.khrys.dnd.charcreator.client

import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.styles.createMuiTheme

val defaultTheme = createDefaultTheme()

private fun createDefaultTheme(): Theme {
    val theme = createMuiTheme()
    theme.palette.background.default = "#fff3e0"
    theme.palette.primary.main = "#ffcc80"
    theme.palette.primary.dark = "#ffb74d"
    theme.palette.primary.contrastText = "black"
    theme.overrides.MuiButton = js("""{
        label: {
            border: '3px solid red',
            padding: '5px 15px',
            borderRadius: '5px'
        }
    }""")
    return theme
}
