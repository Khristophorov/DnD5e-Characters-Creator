package me.khrys.dnd.charcreator.client.components.validators

import react.ComponentType

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

@Suppress("UnsafeCastFromDynamic")
val SelectValidator: ComponentType<SelectValidatorProps> = formValidatorModule.SelectValidator

external interface SelectValidatorProps : TextValidatorProps
