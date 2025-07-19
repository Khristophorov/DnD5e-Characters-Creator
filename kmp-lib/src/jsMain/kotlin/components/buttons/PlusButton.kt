package me.khrys.dnd.charcreator.client.components.buttons

import emotion.react.css
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.Tooltip
import mui.icons.material.Add
import mui.material.ButtonProps
import react.FC
import react.dom.html.ReactHTML.div
import web.cssom.Float
import web.cssom.px

val PlusButton = FC<ButtonProps>("PlusButton") { props ->
    console.info("Rendering Plus Button")
    div {
        css {
            float = Float.right
            padding = 15.px
        }
        Tooltip {
            Fab {
                Add()
                this.onClick = props.onClick
            }
            props.children?.let { this.title = it }
        }
    }
}
