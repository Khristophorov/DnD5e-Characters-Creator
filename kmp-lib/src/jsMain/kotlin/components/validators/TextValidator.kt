package me.khrys.dnd.charcreator.client.components.validators

import mui.material.InputBaseComponentProps
import mui.material.TextFieldProps
import org.w3c.dom.events.InputEvent
import react.ComponentType

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

@Suppress("UnsafeCastFromDynamic")
val TextValidator: ComponentType<TextValidatorProps> = formValidatorModule.TextValidator

data class InputProps(val accept: String? = null, val readonly: String? = null)

fun inputProps(accept: String? = null, readonly: String? = null) =
    InputProps(accept, readonly).unsafeCast<InputBaseComponentProps>()

external interface TextValidatorProps : TextFieldProps {
    var validators: Array<String>
    var errorMessages: Array<String>
    var onChange: ((InputEvent) -> Unit)
}
