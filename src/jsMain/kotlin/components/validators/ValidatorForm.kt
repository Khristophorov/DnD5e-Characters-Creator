package me.khrys.dnd.charcreator.client.components.validators

import com.ccfraser.muirwik.components.utils.StyledPropsWithCommonAttributes
import com.ccfraser.muirwik.components.utils.createStyled
import org.w3c.dom.events.Event
import react.ComponentType
import react.RBuilder
import styled.StyledHandler

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

external interface ValidatorFormProps : StyledPropsWithCommonAttributes {
    var values: Array<String>?

    fun <T> addValidationRule(ruleName: String, rule: (T?) -> Boolean)
}

@Suppress("UnsafeCastFromDynamic")
val validatorForm: ComponentType<ValidatorFormProps> = formValidatorModule.ValidatorForm

@Suppress("UnsafeCastFromDynamic")
val validatorFormRules: ValidatorFormProps = formValidatorModule.ValidatorForm

fun RBuilder.dValidatorForm(
    className: String? = null,
    onSubmit: ((Event) -> Unit) = {},
    handler: StyledHandler<ValidatorFormProps>? = null
) = createStyled(validatorForm, className, handler) {
    attrs.onSubmit = onSubmit
}
