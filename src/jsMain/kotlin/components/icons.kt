package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.createStyled
import react.ComponentType
import react.RBuilder
import styled.StyledProps

@JsModule("@material-ui/icons/Add")
@JsNonModule
private external val addModule: dynamic

@JsModule("@material-ui/icons/ArrowBack")
@JsNonModule
private external val arrowBackModule: dynamic

@JsModule("@material-ui/icons/Close")
@JsNonModule
private external val closeModule: dynamic

@JsModule("@material-ui/icons/RadioButtonUnchecked")
@JsNonModule
private external val radioButtonUncheckedModule: dynamic

@JsModule("@material-ui/icons/RadioButtonChecked")
@JsNonModule
private external val radioButtonCheckedModule: dynamic

@Suppress("UnsafeCastFromDynamic")
private val addIcon: ComponentType<StyledProps> = addModule.default

@Suppress("UnsafeCastFromDynamic")
private val arrowBackIcon: ComponentType<StyledProps> = arrowBackModule.default

@Suppress("UnsafeCastFromDynamic")
private val closeIcon: ComponentType<StyledProps> = closeModule.default

@Suppress("UnsafeCastFromDynamic")
private val radioCheckedIcon: ComponentType<StyledProps> = radioButtonCheckedModule.default

@Suppress("UnsafeCastFromDynamic")
private val radioUncheckedIcon: ComponentType<StyledProps> = radioButtonUncheckedModule.default

fun RBuilder.dAddIcon() = createStyled(addIcon)
fun RBuilder.dArrowBackIcon() = createStyled(arrowBackIcon)
fun RBuilder.dCloseIcon() = createStyled(closeIcon)
fun RBuilder.dRadioCheckedIcon() = createStyled(radioCheckedIcon)
fun RBuilder.dRadioUncheckedIcon() = createStyled(radioUncheckedIcon)
