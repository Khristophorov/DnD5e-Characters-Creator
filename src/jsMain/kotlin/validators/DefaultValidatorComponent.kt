package me.khrys.dnd.charcreator.client.validators

import com.ccfraser.muirwik.components.createStyled
import com.ccfraser.muirwik.components.setStyledPropsAndRunHandler
import kotlinx.html.InputType
import org.w3c.dom.events.InputEvent
import react.RBuilder
import react.RComponent
import react.RState
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
    validatorComponent: RComponent<P, RState>,
    additionalHandling: StyledHandler<P>? = null
) = createStyled(validatorComponent, true) {
    additionalHandling?.let { additionalHandling() }
    id?.let { attrs.id = id }
    attrs.label = label
    attrs.type = type
    attrs.value = value
    inputProps?.let { attrs.inputProps = inputProps }
    attrs.validators = validators
    attrs.errorMessages = errorMessages
    onChange.let { attrs.onChange = onChange }
    setStyledPropsAndRunHandler(className, handler)
}
