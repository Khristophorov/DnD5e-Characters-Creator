package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.MTooltipProps
import com.ccfraser.muirwik.components.MTypographyAlign.center
import com.ccfraser.muirwik.components.MTypographyColor.textPrimary
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.form.MFormControlLabelProps
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.input.mInputLabel
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mCheckbox
import com.ccfraser.muirwik.components.mTooltip
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.menu.mMenuItem
import com.ccfraser.muirwik.components.targetValue
import kotlinx.css.Display.inlineBlock
import kotlinx.css.JustifyContent
import kotlinx.css.TextAlign
import kotlinx.css.WhiteSpace.preWrap
import kotlinx.css.borderBottom
import kotlinx.css.display
import kotlinx.css.justifyContent
import kotlinx.css.margin
import kotlinx.css.maxWidth
import kotlinx.css.minHeight
import kotlinx.css.padding
import kotlinx.css.paddingLeft
import kotlinx.css.px
import kotlinx.css.textAlign
import kotlinx.css.whiteSpace
import kotlinx.css.width
import kotlinx.html.InputType.number
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.dialogs.CharDialogProps
import me.khrys.dnd.charcreator.client.components.dialogs.FeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.SavingThrowsItem
import me.khrys.dnd.charcreator.client.computeModifier
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.client.toSignedString
import me.khrys.dnd.charcreator.client.validators.InputProps
import me.khrys.dnd.charcreator.client.validators.dSelectValidator
import me.khrys.dnd.charcreator.client.validators.dTextValidator
import me.khrys.dnd.charcreator.client.validators.dValidatorForm
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
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_LOWER_0
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALIDATION_UPPER_20
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.hasFeature
import org.w3c.dom.events.Event
import org.w3c.dom.events.InputEvent
import react.RBuilder
import react.RSetState
import react.ReactElement
import react.child
import react.functionalComponent
import react.useContext
import react.useState
import styled.StyledHandler
import styled.css
import styled.styledDiv
import styled.styledP
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
    dDelayedTooltip(title) {
        styledDiv {
            attrs.classes = setOf(CLASS_ABILITY_BOX, CLASS_BORDERED, CLASS_CENTER)
            dCenteredLabel(label)
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
    readOnly: Boolean = false,
    className: String = ""
) {
    dDelayedTooltip(title) {
        styledDiv {
            attrs.classes = setOf(CLASS_INLINE, className)
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

fun RBuilder.dTitledInput(
    label: String,
    value: String
) {
    styledDiv {
        mInputLabel(label)
        mInput(value) {
            attrs.readOnly = true
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
    return dDelayedTooltip(title) {
        mFormControlLabel(label = label, control = checkBox, checked = checked, handler = handler)
    }
}

fun RBuilder.dSavingThrowsGridItem(item: SavingThrowsItem) {
    dDelayedTooltip(item.title) {
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

fun RBuilder.dCenteredBold(text: String) {
    styledStrong {
        attrs.classes = setOf(CLASS_CENTER)
        css { textAlign = TextAlign.center }
        +text.uppercase()
    }
}

fun RBuilder.dDelayedTooltip(title: String, handler: StyledHandler<MTooltipProps>? = null) =
    mTooltip(title = title, enterDelay = 1_000, handler = handler)

fun RBuilder.dValidatedList(
    label: String = "",
    value: String,
    validators: Array<String>,
    errorMessages: Array<String>,
    onChange: (Event) -> Unit,
    menuItems: Map<String, String>,
    setDescription: RSetState<String> = {},
    description: String = "",
    useDescription: Boolean = true
) {
    styledDiv {
        attrs.classes = setOf(CLASS_INLINE)
        dSelectValidator(
            label = label,
            value = value,
            validators = validators,
            errorMessages = errorMessages,
            onChange = onChange
        ) {
            menuItems.forEach { (name, description) ->
                mMenuItem(value = name) {
                    attrs.onMouseOver = { setDescription(description) }
                    attrs.onSelect = { setDescription(description) }
                    +name
                }
            }
        }
        if (useDescription) {
            descriptionFrame(description)
        }
    }
}

val collectRaceFeatures = functionalComponent<CharDialogProps> { props ->
    val features = props.character.race.features
    collectFeatures(
        open = props.open,
        setOpen = props.setOpen,
        character = props.character,
        features = features,
        checkPresence = { !(features.isEmpty() || props.character.hasFeature(features[0].name)) },
        action = props.action
    )
}

val collectSubraceFeatures = functionalComponent<CharDialogProps> { props ->
    val features = props.character.subrace.features
    collectFeatures(
        open = props.open,
        setOpen = props.setOpen,
        character = props.character,
        features = features,
        checkPresence = { !(features.isEmpty() || props.character.hasFeature(features[0].name)) },
        action = props.action
    )
}

fun RBuilder.collectFeatures(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    character: Character,
    features: Array<Feature>,
    checkPresence: () -> Boolean,
    action: () -> Unit
) {
    val (openProficiency, setOpenProficiency) = useState(false)
    val (proficiencyFeature, setProficiencyFeature) = useState(Feature("", ""))
    val (proficiencyFunction, setProficiencyFunction) = useState(DnDFunction(""))
    val (openLanguage, setOpenLanguage) = useState(false)
    val (languageFeature, setLanguageFeature) = useState(Feature("", ""))
    val (languageFunction, setLanguageFunction) = useState(DnDFunction(""))
    val (numberOfNewFunctions, setNumberOfNewFunctions) = useState(-1)
    if (open && checkPresence()) {
        val newFunctions = mutableListOf<() -> Unit>()
        features.forEach { feature ->
            if (feature.functions.isEmpty()) {
                character.features += feature
            } else {
                var addFeature = true
                feature.functions.forEach { function ->
                    when (function.name) {
                        "Choose Proficiency" -> {
                            newFunctions.add {
                                setOpenProficiency(true)
                                setProficiencyFeature(feature)
                                setProficiencyFunction(function)
                            }
                            addFeature = false
                        }
                        "Choose Language" -> {
                            newFunctions.add {
                                setOpenLanguage(true)
                                setLanguageFeature(feature)
                                setLanguageFunction(function)
                            }
                        }
                    }
                }
                if (addFeature) {
                    character.features += feature
                }
            }
        }
        setNumberOfNewFunctions(newFunctions.size)
        if (newFunctions.isEmpty()) {
            action()
            setOpen(false)
        }
        newFunctions.forEach { function -> function() }
    }
    child(proficienciesChooser) {
        attrs.open = openProficiency
        attrs.setOpen = setOpenProficiency
        attrs.feature = proficiencyFeature
        attrs.function = proficiencyFunction
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = proficiencyFeature.name,
                    description = proficiencyFeature.description,
                    functions = arrayOf(DnDFunction(proficiencyFunction.values[0], arrayOf(value))),
                    source = proficiencyFeature.source
                )
            val newNumberOfNewFunctions = numberOfNewFunctions - 1
            setNumberOfNewFunctions(newNumberOfNewFunctions)
            if (newNumberOfNewFunctions == 0) {
                action()
                setOpen(false)
            }
        }
    }
    child(languageChooser) {
        attrs.open = openLanguage
        attrs.setOpen = setOpenLanguage
        attrs.feature = languageFeature
        attrs.function = languageFunction
        attrs.character = character
        attrs.setValue = { value ->
            val functions =
                if (character.hasFeature(languageFeature.name))
                    character.features.filter { languageFeature.name == it.name }[0].functions
                else emptyArray()
            val feature = Feature(
                name = languageFeature.name,
                description = languageFeature.description,
                functions = functions + arrayOf(DnDFunction(languageFunction.values[0], arrayOf(value))),
                source = languageFeature.source
            )
            val filteredFeatures = character.features.filter { languageFeature.name != it.name }.toTypedArray()
            character.features = filteredFeatures + feature
            val newNumberOfNewFunctions = numberOfNewFunctions - 1
            setNumberOfNewFunctions(newNumberOfNewFunctions)
            if (newNumberOfNewFunctions == 0) {
                action()
                setOpen(false)
            }
        }
    }
}

val proficienciesChooser = functionalComponent<FeatureProps> { props ->
    val values = props.function.values
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.copyOfRange(1, values.size) else emptyArray(),
        setValue = props.setValue
    )
}

val languageChooser = functionalComponent<FeatureProps> { props ->
    val values = props.function.values
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.copyOfRange(1, values.size) else emptyArray(),
        setValue = props.setValue
    )
}

private fun RBuilder.descriptionFrame(description: String) {
    styledDiv {
        attrs.classes = setOf(CLASS_BORDERED)
        css {
            minHeight = 200.px
            width = 800.px
            maxWidth = 800.px
            margin = "5px"
            display = inlineBlock
        }
        attrs[DANGEROUS_HTML] = DangerousHTML(description)
    }
}

fun RBuilder.chooseOneOfMany(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    header: String,
    description: String,
    values: Array<String>,
    setValue: (String) -> Unit
) {
    val (chosenValue, setChosenValue) = useState("")
    val translations = useContext(TranslationsContext)
    mDialog(open = open) {
        mDialogTitle(text = header)
        mDialogContent(dividers = true) {
            mDialogContentText(text = description)
            dValidatorForm(onSubmit = {
                setValue(chosenValue)
                setOpen(false)
            }) {
                dValidatedList(
                    value = chosenValue,
                    validators = arrayOf(VALIDATION_REQUIRED),
                    errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: ""),
                    onChange = { event -> setChosenValue(event.targetValue as String) },
                    menuItems = values.associate { it to "" },
                    useDescription = false
                )
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.dTextBox(
    value: Int,
    label: String
) {
    styledDiv {
        attrs.classes = setOf(CLASS_ABILITY_BOX, CLASS_BORDERED, CLASS_CENTER)
        dTextValidator(
            value = value.toString(),
            type = number,
            inputProps = InputProps(readOnly = true),
            validators = arrayOf(VALIDATION_LOWER_0, VALIDATION_UPPER_20)
        )
        dCenteredLabel(label)
    }
}

fun RBuilder.dWrappedText(
    label: String,
    text: Array<String>
) {
    styledDiv {
        mInputLabel(label)
        styledP {
            css {
                maxWidth = 300.px
                whiteSpace = preWrap
            }
            +text.joinToString(separator = ", ")
        }
    }
}

fun RBuilder.dTextWithTooltip(
    text: String,
    tooltip: String
) {
    dDelayedTooltip(tooltip) {
        styledP {
            css {
                maxWidth = 300.px
                whiteSpace = preWrap
            }
            +text
        }
    }
}

fun RBuilder.dCenteredLabel(
    label: String
) {
    styledDiv {
        attrs.classes = setOf(CLASS_INLINE)
        css {
            justifyContent = JustifyContent.center
        }
        mInputLabel(caption = label.uppercase(), shrink = true)
    }
}
