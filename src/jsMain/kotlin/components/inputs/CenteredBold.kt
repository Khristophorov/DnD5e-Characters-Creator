package me.khrys.dnd.charcreator.client.components.inputs

import kotlinx.css.TextAlign
import kotlinx.css.textAlign
import kotlinx.html.classes
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import react.RBuilder
import styled.css
import styled.styledStrong

fun RBuilder.dCenteredBold(text: String) {
    styledStrong {
        attrs.classes = setOf(CLASS_CENTER)
        css { textAlign = TextAlign.center }
        +text.uppercase()
    }
}
