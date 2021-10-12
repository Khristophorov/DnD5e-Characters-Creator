package me.khrys.dnd.charcreator.client.components.validators

import com.ccfraser.muirwik.components.createStyled
import kotlinx.html.InputType
import org.w3c.dom.events.InputEvent
import react.ComponentType
import react.RBuilder
import styled.StyledHandler

fun <P: TextValidatorProps> RBuilder.defaultValidatorComponent(
    id: String?,
    label: String,
    type: InputType,
    value: String,
    inputProps: InputProps?,
    validators: Array<String>,
    errorMessages: Array<String>,
    onChange: ((InputEvent) -> Unit)?,
    className: String?,
    handler: StyledHandler<P>?,
    validatorComponent: ComponentType<P>,
    additionalHandling: StyledHandler<P>? = null
) = createStyled(validatorComponent, className, handler) {
    additionalHandling?.let { additionalHandling() }
    id?.let { attrs.id = id }
    attrs.label = label
    attrs.type = type
    attrs.value = value
    inputProps?.let { attrs.inputProps = inputProps }
    attrs.validators = validators
    attrs.errorMessages = errorMessages
    onChange.let { attrs.onChange = onChange }
}
