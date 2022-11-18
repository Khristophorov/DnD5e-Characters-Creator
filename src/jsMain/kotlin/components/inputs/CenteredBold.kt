package me.khrys.dnd.charcreator.client.components.inputs

import csstype.ClassName
import csstype.TextAlign.Companion.center
import emotion.react.css
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.strong

val CenteredBold = FC<PropsWithChildren> { props ->
    strong {
        css(ClassName(CLASS_CENTER)) {
            textAlign = center
        }
        +props.children.toString().uppercase()
    }
}
