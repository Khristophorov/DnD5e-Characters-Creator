package me.khrys.dnd.charcreator.client.components.validators

import com.ccfraser.muirwik.components.StyledPropsWithCommonAttributes
import com.ccfraser.muirwik.components.createStyled
import org.w3c.dom.events.Event
import react.ComponentType
import react.RBuilder
import styled.StyledHandler

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

external interface ValidatorForm {
    var values: Array<String>?

    fun <T> addValidationRule(ruleName: String, rule: (T?) -> Boolean)
}

interface ValidationFormComponent : ComponentType<ValidatorFormProps>, ValidatorForm

@Suppress("UnsafeCastFromDynamic")
val validatorForm: ValidationFormComponent = formValidatorModule.ValidatorForm

interface ValidatorFormProps : StyledPropsWithCommonAttributes

fun RBuilder.dValidatorForm(
    className: String? = null,
    onSubmit: ((Event) -> Unit) = {},
    handler: StyledHandler<ValidatorFormProps>? = null
) = createStyled(validatorForm, className, handler) {
    attrs.onSubmit = onSubmit
}
