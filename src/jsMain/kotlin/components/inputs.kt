package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.MTypographyAlign.center
import com.ccfraser.muirwik.components.MTypographyColor.textPrimary
import com.ccfraser.muirwik.components.form.MFormControlLabelProps
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.input.mInputLabel
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mCheckbox
import com.ccfraser.muirwik.components.mTooltip
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.JustifyContent
import kotlinx.css.TextAlign
import kotlinx.css.borderBottom
import kotlinx.css.justifyContent
import kotlinx.css.padding
import kotlinx.css.paddingLeft
import kotlinx.css.px
import kotlinx.css.textAlign
import kotlinx.html.InputType.number
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.computeModifier
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.client.toSignedString
import me.khrys.dnd.charcreator.client.validators.InputProps
import me.khrys.dnd.charcreator.client.validators.dTextValidator
import me.khrys.dnd.charcreator.common.ABILITY_MAXIMUM
import me.khrys.dnd.charcreator.common.ABILITY_MINIMUM
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import me.khrys.dnd.charcreator.common.CLASS_ABILITY_BOX
import me.khrys.dnd.charcreator.common.CLASS_BACKGROUND
import me.khrys.dnd.charcreator.common.CLASS_BOLD
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_ROUND_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_TEXT_CENTER
import me.khrys.dnd.charcreator.common.VALIDATION_LOWER_0
import me.khrys.dnd.charcreator.common.VALIDATION_UPPER_20
import org.w3c.dom.events.Event
import org.w3c.dom.events.InputEvent
import react.RBuilder
import react.ReactElement
import styled.StyledHandler
import styled.css
import styled.styledDiv
import styled.styledSpan
import styled.styledStrong

fun RBuilder.dAbilityBox(
    title: String,
    label: String,
    value: Int,
    translations: Map<String, String>,
    readOnly: Boolean = false,
    onChange: (InputEvent) -> Unit = {}
) {
    mTooltip(title = title, enterDelay = 1_000) {
        styledDiv {
            attrs.classes = setOf(CLASS_ABILITY_BOX, CLASS_BORDERED, CLASS_CENTER)
            styledDiv {
                attrs.classes = setOf(CLASS_INLINE)
                css {
                    justifyContent = JustifyContent.center
                }
                mInputLabel(caption = label.uppercase(), shrink = true)
            }
            dTextValidator(
                value = value.toString(),
                type = number,
                inputProps = InputProps(readOnly = readOnly),
                validators = arrayOf(VALIDATION_LOWER_0, VALIDATION_UPPER_20),
                errorMessages = if (readOnly) emptyArray() else arrayOf(
                    translations[ABILITY_MINIMUM] ?: "",
                    translations[ABILITY_MAXIMUM] ?: ""
                ),
                onChange = onChange
            )
            mAvatar(className = "$CLASS_CENTER $CLASS_ROUND_BORDERED $CLASS_BACKGROUND") {
                mTypography(text = computeModifier(value).toString(), align = center, color = textPrimary)
            }
        }
    }
}

fun RBuilder.dOneValueInput(
    header: String,
    value: Int,
    title: String = "",
    readOnly: Boolean = false
) {
    mTooltip(title = title, enterDelay = 1_000) {
        styledDiv {
            attrs.classes = setOf(CLASS_INLINE)
            mAvatar(className = "$CLASS_CENTER $CLASS_ROUND_BORDERED $CLASS_BACKGROUND") {
                mTypography(text = value.toString(), align = center, color = textPrimary)
            }
            mInput(
                value = header.uppercase(),
                readOnly = readOnly,
                fullWidth = true,
                className = "$CLASS_BORDERED $CLASS_BOLD $CLASS_TEXT_CENTER"
            )
        }
    }
}

fun RBuilder.dCheckboxWithLabel(
    title: String = "",
    label: String,
    checked: Boolean,
    onChange: ((Event, Boolean) -> Unit)? = null,
    handler: StyledHandler<MFormControlLabelProps>? = null
): ReactElement {
    val checkBox = mCheckbox(checked = checked, onChange = { event, value ->
        playSound(BUTTON_SOUND_ID)
        onChange?.let { it(event, value) }
    }, addAsChild = false) {
        attrs.icon = dRadioUncheckedIcon()
        attrs.checkedIcon = dRadioCheckedIcon()
    }
    return mTooltip(title = title, enterDelay = 1_000) {
        mFormControlLabel(label = label, control = checkBox, checked = checked, handler = handler)
    }
}

fun RBuilder.dSavingThrowsGridItem(item: SavingThrowsItem) {
    mTooltip(title = item.title, enterDelay = 1_000) {
        styledDiv {
            attrs.classes = setOf(CLASS_INLINE)
            val modifier = computeModifier(item.value, item.proficiencyBonus, item.proficient)
            mCheckbox(checked = item.proficient) {
                attrs.icon = dRadioUncheckedIcon()
                attrs.checkedIcon = dRadioCheckedIcon()
            }
            styledDiv {
                css {
                    padding = "10px 2px"
                }
                styledSpan {
                    css {
                        borderBottom = "1px solid black"
                    }
                    +toSignedString(modifier)
                }
                styledSpan {
                    css {
                        paddingLeft = 10.px
                    }
                    +item.label
                }
            }
        }
    }
}

fun RBuilder.centeredBold(text: String) {
    styledStrong {
        attrs.classes = setOf(CLASS_CENTER)
        css { textAlign = TextAlign.center }
        +text.uppercase()
    }
}
