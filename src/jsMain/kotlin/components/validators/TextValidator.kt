package me.khrys.dnd.charcreator.client.components.validators

import com.ccfraser.muirwik.components.utils.StyledPropsWithCommonAttributes
import kotlinx.html.InputType
import kotlinx.html.InputType.text
import org.w3c.dom.events.InputEvent
import react.ComponentType
import react.RBuilder
import styled.StyledHandler

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

@Suppress("UnsafeCastFromDynamic")
private val textValidator: ComponentType<TextValidatorProps> = formValidatorModule.TextValidator

data class InputProps(val accept: String = "", val readOnly: Boolean = false)

external interface TextValidatorProps : StyledPropsWithCommonAttributes {
    var label: String
    var type: InputType
    var value: String
    var inputProps: InputProps
    var validators: Array<String>
    var errorMessages: Array<String>
    var onChange: ((InputEvent) -> Unit)?
}

fun RBuilder.dTextValidator(
    id: String? = null,
    label: String = "",
    type: InputType = text,
    value: String = "",
    inputProps: InputProps? = null,
    validators: Array<String> = emptyArray(),
    errorMessages: Array<String> = emptyArray(),
    className: String? = null,
    onChange: ((InputEvent) -> Unit)? = null,
    handler: StyledHandler<TextValidatorProps>? = null
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
    textValidator
)
