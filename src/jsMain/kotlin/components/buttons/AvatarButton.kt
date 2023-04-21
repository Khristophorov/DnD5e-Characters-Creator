package me.khrys.dnd.charcreator.client.components.buttons

import csstype.ClassName
import csstype.Display.Companion.block
import csstype.px
import emotion.react.css
import me.khrys.dnd.charcreator.client.components.dialogs.CharDialogProps
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import mui.material.Avatar
import mui.material.Size.large
import mui.material.Typography
import mui.material.TypographyAlign.Companion.center
import react.FC
import react.dom.html.ReactHTML.div

val AvatarButton = FC<CharDialogProps> { props ->
    console.info("Loading avatar for ${props.character.name}")
    div {
        css {
            display = block
            margin = 5.px
        }
        Fab {
            size = large
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
