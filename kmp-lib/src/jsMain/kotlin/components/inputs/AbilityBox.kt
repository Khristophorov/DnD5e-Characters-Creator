package me.khrys.dnd.charcreator.client.components.inputs

import me.khrys.dnd.charcreator.client.components.inputs.texts.CenteredLabel
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.DelayedTooltip
import me.khrys.dnd.charcreator.client.components.validators.TextValidator
import me.khrys.dnd.charcreator.client.components.validators.inputProps
import me.khrys.dnd.charcreator.client.computeModifier
import me.khrys.dnd.charcreator.common.ABILITY_MAXIMUM
import me.khrys.dnd.charcreator.common.ABILITY_MINIMUM
import me.khrys.dnd.charcreator.common.CLASS_ABILITY_BOX
import me.khrys.dnd.charcreator.common.CLASS_BACKGROUND
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_ROUND_BORDERED
import me.khrys.dnd.charcreator.common.VALIDATION_LOWER_0
import me.khrys.dnd.charcreator.common.VALIDATION_UPPER_20
import mui.material.Avatar
import mui.material.Typography
import mui.material.TypographyAlign.Companion.center
import org.w3c.dom.events.InputEvent
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.div
import web.cssom.ClassName
import web.html.InputType
import web.html.number

external interface AbilityBoxProps : Props {
    var title: String
    var label: String
    var value: Int
    var translations: Map<String, String>
    var readOnly: Boolean
    var onChange: (InputEvent) -> Unit
}

val AbilityBox = FC<AbilityBoxProps>("AbilityBox") { props ->
    console.info("Rendering ability box ${props.title}")
    DelayedTooltip {
        this.title = ReactNode(props.title)
        div {
            this.className = ClassName("$CLASS_ABILITY_BOX $CLASS_BORDERED $CLASS_CENTER")
            CenteredLabel {
                this.label = props.label
            }
            TextValidator {
                this.value = props.value.toString()
                this.type = InputType.number
                this.inputProps = inputProps(readonly = props.readOnly.toString())
                this.validators = arrayOf(VALIDATION_LOWER_0, VALIDATION_UPPER_20)
                this.errorMessages = if (props.readOnly) emptyArray() else arrayOf(
                    props.translations[ABILITY_MINIMUM] ?: "",
                    props.translations[ABILITY_MAXIMUM] ?: ""
                )
                this.onChange = props.onChange
            }
            Avatar {
                this.className = ClassName("$CLASS_CENTER $CLASS_ROUND_BORDERED $CLASS_BACKGROUND")
                Typography {
                    +computeModifier(props.value).toString()
                    this.align = center
                }
            }
        }
    }
}
