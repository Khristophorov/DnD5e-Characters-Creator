package me.khrys.dnd.charcreator.client.validators

import com.ccfraser.muirwik.components.StyledPropsWithCommonAttributes
import com.ccfraser.muirwik.components.createStyled
import com.ccfraser.muirwik.components.setStyledPropsAndRunHandler
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RState
import styled.StyledHandler

@JsModule("react-material-ui-form-validator")
@JsNonModule
private external val formValidatorModule: dynamic

@Suppress("UnsafeCastFromDynamic")
private val validatorForm: RComponent<ValidatorFormProps, RState> = formValidatorModule.ValidatorForm

interface ValidatorFormProps : StyledPropsWithCommonAttributes

fun RBuilder.dValidatorForm(
    className: String? = null,
    onSubmit: ((Event) -> Unit) = {},
    handler: StyledHandler<ValidatorFormProps>? = null
) = createStyled(validatorForm, true) {
    attrs.onSubmit = onSubmit
    setStyledPropsAndRunHandler(className, handler)
}
