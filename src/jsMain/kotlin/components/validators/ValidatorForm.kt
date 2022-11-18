package me.khrys.dnd.charcreator.client.components.validators

import me.khrys.dnd.charcreator.client.components.buttons.ButtonAction
import react.ComponentType
import react.PropsWithClassName

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

external interface ValidatorFormProps : PropsWithClassName {
    var values: Array<String>?
    var onSubmit: ButtonAction

    fun <T> addValidationRule(ruleName: String, rule: (T?) -> Boolean)
}

@Suppress("UnsafeCastFromDynamic")
val ValidatorForm: ComponentType<ValidatorFormProps> = formValidatorModule.ValidatorForm

@Suppress("UnsafeCastFromDynamic")
val validatorFormRules: ValidatorFormProps = formValidatorModule.ValidatorForm
