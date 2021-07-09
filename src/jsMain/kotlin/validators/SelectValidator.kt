package me.khrys.dnd.charcreator.client.validators

import kotlinx.html.InputType
import kotlinx.html.InputType.text
import org.w3c.dom.events.InputEvent
import react.RBuilder
import react.RComponent
import react.RState
import styled.StyledHandler

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

@Suppress("UnsafeCastFromDynamic")
private val selectValidator: RComponent<SelectValidatorProps, RState> = formValidatorModule.SelectValidator

data class SelectProps(val native: Boolean = false, val autoWidth: Boolean = false)

interface SelectValidatorProps : TextValidatorProps {
    @JsName("SelectProps")
    var selectProps: SelectProps
}

fun RBuilder.dSelectValidator(
    id: String? = null,
    label: String = "",
    type: InputType = text,
    value: String = "",
    inputProps: InputProps? = null,
    validators: Array<String> = emptyArray(),
    errorMessages: Array<String> = emptyArray(),
    className: String? = null,
    onChange: ((InputEvent) -> Unit)? = null,
    native: Boolean = false,
    handler: StyledHandler<SelectValidatorProps>? = null
) = defaultValidatorComponent(
    id,
    label,
    type,
    value,
    inputProps,
    validators,
    errorMessages,
    onChange,
    className,
    handler,
    selectValidator,
    additionalHandling = { attrs.selectProps = SelectProps(native = native, autoWidth = true) }
)
