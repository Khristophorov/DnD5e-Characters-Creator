package me.khrys.dnd.charcreator.client.components.buttons

import emotion.react.css
import me.khrys.dnd.charcreator.client.components.dialogs.FeatsProps
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import mui.material.Avatar
import mui.material.Size
import mui.material.Typography
import mui.material.TypographyAlign.Companion.center
import react.FC
import react.dom.html.ReactHTML.div
import web.cssom.ClassName
import web.cssom.Display.Companion.block
import web.cssom.px

val AvatarButton = FC<FeatsProps>("AvatarButton") { props ->
    console.info("Loading avatar for ${props.character.name}")
    div {
        css {
            display = block
            margin = 5.px
        }
        Fab {
            size = Size.large
            className = ClassName(CLASS_CENTER)
            onClick = { props.action() }
            Avatar {
                src = props.character.image
            }
        }
        Typography {
            +props.character.name
            align = center
        }
    }
}
