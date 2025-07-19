package me.khrys.dnd.charcreator.client.components.validators

import mui.base.FormControlProps
import react.ComponentType

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

external interface ValidatorFormProps : FormControlProps {
    var values: Array<String>?

    fun <T> addValidationRule(ruleName: String, rule: (T?) -> Boolean)
}

@Suppress("UnsafeCastFromDynamic")
val ValidatorForm: ComponentType<ValidatorFormProps> = formValidatorModule.ValidatorForm

@Suppress("UnsafeCastFromDynamic")
val validatorFormRules: ValidatorFormProps = formValidatorModule.ValidatorForm
