package me.khrys.dnd.charcreator.client.components.inputs.texts

import com.ccfraser.muirwik.components.input.mInputLabel
import kotlinx.css.WhiteSpace.preWrap
import kotlinx.css.maxWidth
import kotlinx.css.px
import kotlinx.css.whiteSpace
import react.RBuilder
import styled.css
import styled.styledDiv
import styled.styledP

fun RBuilder.dWrappedText(
    label: String,
    text: List<String>
) {
    styledDiv {
        mInputLabel(label)
        styledP {
            css {
                maxWidth = 300.px
                whiteSpace = preWrap
            }
            +text.joinToString(separator = ", ")
        }
    }
}
