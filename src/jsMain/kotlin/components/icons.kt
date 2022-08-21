package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.utils.createStyled
import react.ComponentType
import react.RBuilder
import styled.StyledProps

@JsModule("@mui/icons-material/Add")
@JsNonModule
private external val addModule: dynamic

@JsModule("@mui/icons-material/ArrowBack")
@JsNonModule
private external val arrowBackModule: dynamic

@JsModule("@mui/icons-material/Close")
@JsNonModule
private external val closeModule: dynamic

@JsModule("@mui/icons-material/RadioButtonUnchecked")
@JsNonModule
private external val radioButtonUncheckedModule: dynamic

@JsModule("@mui/icons-material/RadioButtonChecked")
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
