package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.createStyled
import react.RBuilder
import react.RComponent
import react.RState
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

@Suppress("UnsafeCastFromDynamic")
private val addIcon: RComponent<StyledProps, RState> = addModule.default

@Suppress("UnsafeCastFromDynamic")
private val arrowBackIcon: RComponent<StyledProps, RState> = arrowBackModule.default

@Suppress("UnsafeCastFromDynamic")
private val closeIcon: RComponent<StyledProps, RState> = closeModule.default

fun RBuilder.dAddIcon() = createStyled(addIcon, true) {}
fun RBuilder.dArrowBackIcon() = createStyled(arrowBackIcon, true) {}
fun RBuilder.dCloseIcon() = createStyled(closeIcon, true) {}
