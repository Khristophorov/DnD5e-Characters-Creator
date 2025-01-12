package me.khrys.dnd.charcreator.client.components.buttons

import emotion.react.css
import kotlinx.browser.window
import me.khrys.dnd.charcreator.common.LOGOUT_URL
import mui.material.ButtonProps
import react.FC
import react.dom.html.ReactHTML.div
import web.cssom.Float

val LogoutButton = FC<ButtonProps>("LogoutButton") { props ->
    console.info("Logout button rendering")
    div {
        css { float = Float.right }
        Button {
            +props.children
            this.onClick = { window.location.href = LOGOUT_URL }
        }
    }
}
