package me.khrys.dnd.charcreator.client.components.inputs

import emotion.react.css
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.strong
import web.cssom.ClassName
import web.cssom.TextAlign.Companion.center

val CenteredBold = FC<PropsWithChildren> { props ->
    strong {
        css(ClassName(CLASS_CENTER)) {
            textAlign = center
        }
        +props.children.toString().uppercase()
    }
}
