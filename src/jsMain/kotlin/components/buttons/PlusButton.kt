package me.khrys.dnd.charcreator.client.components.buttons

import csstype.Float
import csstype.px
import emotion.react.css
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.Tooltip
import mui.icons.material.Add
import mui.material.ButtonProps
import react.FC
import react.dom.html.ReactHTML.div

val PlusButton = FC<ButtonProps> { props ->
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
