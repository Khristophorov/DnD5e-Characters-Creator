package me.khrys.dnd.charcreator.client.validators

import com.ccfraser.muirwik.components.StyledPropsWithCommonAttributes
import com.ccfraser.muirwik.components.createStyled
import com.ccfraser.muirwik.components.setStyledPropsAndRunHandler
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
private val textValidator: RComponent<TextValidatorProps, RState> = formValidatorModule.TextValidator

data class InputProps(val accept: String = "")

interface TextValidatorProps : StyledPropsWithCommonAttributes {
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
) = createStyled(textValidator, true) {
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
