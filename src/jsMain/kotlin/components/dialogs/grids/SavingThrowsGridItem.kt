package me.khrys.dnd.charcreator.client.components.dialogs.grids

import com.ccfraser.muirwik.components.mCheckbox
import kotlinx.css.borderBottom
import kotlinx.css.padding
import kotlinx.css.paddingLeft
import kotlinx.css.px
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.dRadioCheckedIcon
import me.khrys.dnd.charcreator.client.components.dRadioUncheckedIcon
import me.khrys.dnd.charcreator.client.components.dialogs.SavingThrowsItem
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.dDelayedTooltip
import me.khrys.dnd.charcreator.client.computeModifier
import me.khrys.dnd.charcreator.client.toSignedString
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import react.RBuilder
import react.buildElement
import styled.css
import styled.styledDiv
import styled.styledSpan

fun RBuilder.dSavingThrowsGridItem(item: SavingThrowsItem) {
    dDelayedTooltip(item.title) {
        styledDiv {
            attrs.classes = setOf(CLASS_INLINE)
            val modifier = computeModifier(item.value, item.proficiencyBonus, item.proficient)
            mCheckbox(checked = item.proficient) {
                attrs.icon = buildElement { dRadioUncheckedIcon() }
                attrs.checkedIcon = buildElement { dRadioCheckedIcon() }
            }
            styledDiv {
                css {
                    padding = "10px 2px"
                }
                styledSpan {
                    css {
                        borderBottom = "1px solid black"
                    }
                    +toSignedString(modifier)
                }
                styledSpan {
                    css {
                        paddingLeft = 10.px
                    }
                    +item.label
                }
            }
        }
    }
}
