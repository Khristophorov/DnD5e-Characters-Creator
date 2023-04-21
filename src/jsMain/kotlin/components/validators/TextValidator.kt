package me.khrys.dnd.charcreator.client.components.validators

import org.w3c.dom.events.InputEvent
import react.ComponentType
import react.PropsWithClassName
import web.html.InputType

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

@Suppress("UnsafeCastFromDynamic")
val TextValidator: ComponentType<TextValidatorProps> = formValidatorModule.TextValidator

data class InputProps(val accept: String = "", val readonly: String = "false")

external interface TextValidatorProps : PropsWithClassName {
    var id: String
    var label: String
    var type: InputType
    var value: String
    var inputProps: InputProps
    var validators: Array<String>
    var errorMessages: Array<String>
    var onChange: ((InputEvent) -> Unit)?
}
