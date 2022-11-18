package me.khrys.dnd.charcreator.client.components.dialogs.grids

import csstype.Border
import csstype.ClassName
import csstype.LineStyle.Companion.solid
import csstype.NamedColor.black
import csstype.Padding
import csstype.px
import emotion.react.css
import me.khrys.dnd.charcreator.client.components.dialogs.SavingThrowsItem
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.DelayedTooltip
import me.khrys.dnd.charcreator.client.computeModifier
import me.khrys.dnd.charcreator.client.toSignedString
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import mui.icons.material.RadioButtonChecked
import mui.icons.material.RadioButtonUnchecked
import mui.material.Checkbox
import react.FC
import react.Props
import react.ReactNode
import react.createElement
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span

external interface SavingThrowsGridItemProps : Props {
    var item: SavingThrowsItem
}

val SavingThrowsGridItem = FC<SavingThrowsGridItemProps> { props ->
    console.info("Rendering saving throws grid item: Title ${props.item.title} Value: ${props.item.value}")
    val item = props.item
    DelayedTooltip {
        this.title = ReactNode(item.title)
        div {
            this.className = ClassName(CLASS_INLINE)
            val modifier = computeModifier(item.value, item.proficiencyBonus, item.proficient)
            Checkbox {
                this.checked = item.proficient
                this.icon = createElement(RadioButtonUnchecked)
                this.checkedIcon = createElement(RadioButtonChecked)
            }
            div {
                css {
                    this.padding = Padding(10.px, 2.px)
                }
                span {
                    css {
                        this.borderBottom = Border(1.px, solid, black)
                    }
                    +toSignedString(modifier)
                }
                span {
                    css {
                        this.paddingLeft = 10.px
                    }
                    +item.label
                }
            }
        }
    }
}
