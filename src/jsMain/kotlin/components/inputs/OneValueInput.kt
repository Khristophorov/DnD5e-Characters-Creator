package me.khrys.dnd.charcreator.client.components.inputs

import csstype.ClassName
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.DelayedTooltip
import me.khrys.dnd.charcreator.common.CLASS_BACKGROUND
import me.khrys.dnd.charcreator.common.CLASS_BOLD
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_ROUND_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_TEXT_CENTER
import mui.material.Avatar
import mui.material.Input
import mui.material.Typography
import mui.material.TypographyAlign.Companion.center
import react.FC
import react.PropsWithClassName
import react.ReactNode
import react.dom.html.ReactHTML.div

external interface OneValueProps : PropsWithClassName {
    var header: String
    var value: String
    var title: String?
    var isReadOnly: Boolean
}

val OneValueInput = FC<OneValueProps> { props ->
    console.info("Rendering one value input ${props.value}")
    DelayedTooltip {
        this.title = ReactNode(props.title ?: "")
        div {
            this.className = ClassName("$CLASS_INLINE ${props.className}")
            Avatar {
                this.className = ClassName("$CLASS_CENTER $CLASS_ROUND_BORDERED $CLASS_BACKGROUND")
                Typography {
                    +props.value
                    this.align = center
                }
            }
            Input {
                this.value = props.header.uppercase()
                this.readOnly = props.isReadOnly
                this.fullWidth = true
                this.className = ClassName("$CLASS_BORDERED $CLASS_BOLD $CLASS_TEXT_CENTER")
            }
        }
    }
}
