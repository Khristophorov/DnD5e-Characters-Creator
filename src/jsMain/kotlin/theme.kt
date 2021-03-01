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
    theme.overrides.MuiCircularProgress = js("""{
        root: {
            position: 'fixed',
            top: '0',
            left: '0',
            right: '0',
            bottom: '0',
            margin: 'auto',
            zIndex: '3'
        }
    }""")
    theme.overrides.MuiAvatar = js("""{
        root: {
            width: '64px',
            height: '64px'
        }
    }""")
    theme.overrides.MuiPaper = js("""{
        root: {
            backgroundColor: '#fff3e0'
        }
    }""")
    return theme
}
