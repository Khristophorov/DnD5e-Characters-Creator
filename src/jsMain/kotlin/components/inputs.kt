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
import kotlinx.css.Overflow.auto
import kotlinx.css.TextAlign
import kotlinx.css.WhiteSpace.preWrap
import kotlinx.css.borderBottom
import kotlinx.css.display
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.margin
import kotlinx.css.maxHeight
import kotlinx.css.maxWidth
import kotlinx.css.overflow
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
import me.khrys.dnd.charcreator.client.components.dialogs.FeatsProps
import me.khrys.dnd.charcreator.client.components.dialogs.FeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.MultipleFeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.SavingThrowsItem
import me.khrys.dnd.charcreator.client.computeModifier
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.format
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.client.toFeature
import me.khrys.dnd.charcreator.client.toSignedString
import me.khrys.dnd.charcreator.client.validators.InputProps
import me.khrys.dnd.charcreator.client.validators.dSelectValidator
import me.khrys.dnd.charcreator.client.validators.dTextValidator
import me.khrys.dnd.charcreator.client.validators.dValidatorForm
import me.khrys.dnd.charcreator.client.validators.validatorForm
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
import me.khrys.dnd.charcreator.common.VALIDATION_VALUE_ALREADY_PRESENT
import me.khrys.dnd.charcreator.common.VALUE_IS_ALREADY_SELECTED_TRANSLATION
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.emptyFeat
import me.khrys.dnd.charcreator.common.models.hasFeature
import org.w3c.dom.events.Event
import org.w3c.dom.events.InputEvent
import react.RBuilder
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
    setDescription: (String) -> Unit = {},
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
            dDescriptionFrame(description)
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
        feats = props.feats,
        useFeats = props.useFeats,
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
        action = props.action
    )
}

val collectFeatFeatures = functionalComponent<FeatsProps> { props ->
    collectFeatures(
        open = props.open,
        setOpen = props.setOpen,
        character = props.character,
        features = arrayOf(props.feature),
        action = props.action
    )
}

fun RBuilder.collectFeatures(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    character: Character,
    features: Array<Feature>,
    feats: Map<String, Feat> = emptyMap(),
    useFeats: Boolean = false,
    action: () -> Unit
) {
    val (openInform, setOpenInform) = useState(false)
    val (informFeature, setInformFeature) = useState(Feature("", ""))
    val (informFunction, setInformFunction) = useState(DnDFunction(""))

    val (openProficiency, setOpenProficiency) = useState(false)
    val (proficiencyFeature, setProficiencyFeature) = useState(Feature("", ""))
    val (proficiencyFunction, setProficiencyFunction) = useState(DnDFunction(""))

    val (openProficiencies, setOpenProficiencies) = useState(false)
    val (proficienciesFeature, setProficienciesFeature) = useState(Feature("", ""))
    val (proficienciesFunction, setProficienciesFunction) = useState(DnDFunction(""))

    val (openLanguage, setOpenLanguage) = useState(false)
    val (languageFeature, setLanguageFeature) = useState(Feature("", ""))
    val (languageFunction, setLanguageFunction) = useState(DnDFunction(""))

    val (openLanguages, setOpenLanguages) = useState(false)
    val (languagesFeature, setLanguagesFeature) = useState(Feature("", ""))
    val (languagesFunction, setLanguagesFunction) = useState(DnDFunction(""))

    val (openSkills, setOpenSkills) = useState(false)
    val (skillsFeature, setSkillsFeature) = useState(Feature("", ""))
    val (skillsFunction, setSkillsFunction) = useState(DnDFunction(""))

    val (openAbilities, setOpenAbilities) = useState(false)
    val (abilitiesFeature, setAbilitiesFeature) = useState(Feature("", ""))
    val (abilitiesFunction, setAbilitiesFunction) = useState(DnDFunction(""))

    val (openElement, setOpenElement) = useState(false)
    val (elementFeature, setElementFeature) = useState(Feature("", ""))
    val (elementFunction, setElementFunction) = useState(DnDFunction(""))

    val (openManeuvers, setOpenManeuvers) = useState(false)
    val (maneuversFeature, setManeuversFeature) = useState(Feature("", ""))
    val (maneuversFunction, setManeuversFunction) = useState(DnDFunction(""))

    val (openFeats, setOpenFeats) = useState(false)
    val (featsFeature, setFeatsFeature) = useState(Feature("", ""))

    val (numberOfNewFunctions, setNumberOfNewFunctions) = useState(-1)

    if (!open && numberOfNewFunctions >= 0) {
        setNumberOfNewFunctions(-1)
    }
    if (open && numberOfNewFunctions < 0) {
        val newFunctions = mutableListOf<() -> Unit>()
        features.filter { filterFeature(it, useFeats) }.forEach { feature ->
            if (feature.functions.isEmpty()) {
                character.features += feature
            } else {
                var addFeature = true
                feature.functions.forEach { function ->
                    when (function.name) {
                        "Inform" -> {
                            newFunctions.add {
                                setOpenInform(true)
                                setInformFeature(feature)
                                setInformFunction(function)
                            }
                            addFeature = false
                        }
                        "Choose Proficiency" -> {
                            newFunctions.add {
                                setOpenProficiency(true)
                                setProficiencyFeature(feature)
                                setProficiencyFunction(function)
                            }
                            addFeature = false
                        }
                        "Choose Proficiencies" -> {
                            newFunctions.add {
                                setOpenProficiencies(true)
                                setProficienciesFeature(feature)
                                setProficienciesFunction(function)
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
                        "Choose Languages" -> {
                            newFunctions.add {
                                setOpenLanguages(true)
                                setLanguagesFeature(feature)
                                setLanguagesFunction(function)
                            }
                        }
                        "Choose Ability" -> {
                            newFunctions.add {
                                setOpenAbilities(true)
                                setAbilitiesFeature(feature)
                                setAbilitiesFunction(function)
                            }
                            addFeature = false
                        }
                        "Choose Skills" -> {
                            newFunctions.add {
                                setOpenSkills(true)
                                setSkillsFeature(feature)
                                setSkillsFunction(function)
                            }
                            addFeature = false
                        }
                        "Choose Element" -> {
                            newFunctions.add {
                                setOpenElement(true)
                                setElementFeature(feature)
                                setElementFunction(function)
                            }
                            addFeature = false
                        }
                        "Choose Feat" -> {
                            newFunctions.add {
                                setOpenFeats(true)
                                setFeatsFeature(feature)
                            }
                            addFeature = false
                        }
                        "Choose Maneuvers" -> {
                            newFunctions.add {
                                setOpenManeuvers(true)
                                setManeuversFeature(feature)
                                setManeuversFunction(function)
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
    child(informWindow) {
        attrs.open = openInform
        attrs.setOpen = setOpenInform
        attrs.feature = informFeature
        attrs.setValue = {
            character.features +=
                Feature(
                    name = informFeature.name,
                    description = informFunction.values[0],
                    functions = emptyArray(),
                    source = informFeature.source
                )
            val newNumberOfNewFunctions = numberOfNewFunctions - 1
            setNumberOfNewFunctions(newNumberOfNewFunctions)
            if (newNumberOfNewFunctions == 0) {
                action()
                setOpen(false)
            }
        }
    }
    child(proficiencyChooser) {
        attrs.open = openProficiency
        attrs.setOpen = setOpenProficiency
        attrs.feature = proficiencyFeature
        attrs.function = proficiencyFunction
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = proficiencyFeature.name,
                    description = proficiencyFunction.values[1].replace("{}", value),
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
    child(proficienciesChooser) {
        attrs.open = openProficiencies
        attrs.setOpen = setOpenProficiencies
        attrs.feature = proficienciesFeature
        attrs.function = proficienciesFunction
        attrs.character = character
        attrs.size = if (proficienciesFunction.values.isEmpty()) 0 else proficienciesFunction.values[2].toInt()
        attrs.setValue = { values ->
            val featureFunctions = arrayOf(DnDFunction(proficienciesFunction.values[0], values))
            if (character.hasFeature(proficienciesFeature.name)) {
                character.features.filter { it.name == proficienciesFeature.name }[0].functions += featureFunctions
            } else {
                character.features +=
                    Feature(
                        name = proficienciesFeature.name,
                        description = proficienciesFunction.values[1].format(*values),
                        functions = featureFunctions,
                        source = proficienciesFeature.source
                    )
            }
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
                description = languageFunction.values[1].replace("{}", value),
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
    child(languagesChooser) {
        attrs.open = openLanguages
        attrs.setOpen = setOpenLanguages
        attrs.feature = languagesFeature
        attrs.function = languagesFunction
        attrs.character = character
        attrs.size = if (languagesFunction.values.isEmpty()) 0 else languagesFunction.values[2].toInt()
        attrs.setValue = { values ->
            val functions =
                if (character.hasFeature(languagesFeature.name))
                    character.features.filter { languagesFeature.name == it.name }[0].functions
                else emptyArray()
            val feature = Feature(
                name = languagesFeature.name,
                description = languagesFunction.values[1].format(*values),
                functions = functions + arrayOf(DnDFunction(languagesFunction.values[0], values)),
                source = languagesFeature.source
            )
            val filteredFeatures = character.features.filter { languagesFeature.name != it.name }.toTypedArray()
            character.features = filteredFeatures + feature

            val newNumberOfNewFunctions = numberOfNewFunctions - 1
            setNumberOfNewFunctions(newNumberOfNewFunctions)
            if (newNumberOfNewFunctions == 0) {
                action()
                setOpen(false)
            }
        }
    }
    child(abilitiesChooser) {
        attrs.open = openAbilities
        attrs.setOpen = setOpenAbilities
        attrs.feature = abilitiesFeature
        attrs.function = abilitiesFunction
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = abilitiesFeature.name,
                    description = abilitiesFunction.values[1].replace("{}", value),
                    functions = abilitiesFeature.functions.filter { it.name != abilitiesFunction.name }.toTypedArray() +
                            DnDFunction(abilitiesFunction.values[0], arrayOf(value, abilitiesFunction.values[2])),
                    source = abilitiesFeature.source
                )
            val newNumberOfNewFunctions = numberOfNewFunctions - 1
            setNumberOfNewFunctions(newNumberOfNewFunctions)
            if (newNumberOfNewFunctions == 0) {
                action()
                setOpen(false)
            }
        }
    }
    child(skillsChooser) {
        attrs.open = openSkills
        attrs.setOpen = setOpenSkills
        attrs.feature = skillsFeature
        attrs.function = skillsFunction
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = skillsFeature.name,
                    description = skillsFunction.values[1].replace("{}", value),
                    functions = arrayOf(DnDFunction(skillsFunction.values[0], arrayOf(value))),
                    source = skillsFeature.source
                )
            val newNumberOfNewFunctions = numberOfNewFunctions - 1
            setNumberOfNewFunctions(newNumberOfNewFunctions)
            if (newNumberOfNewFunctions == 0) {
                action()
                setOpen(false)
            }
        }
    }
    child(elementChooser) {
        attrs.open = openElement
        attrs.setOpen = setOpenElement
        attrs.feature = elementFeature
        attrs.function = elementFunction
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = elementFeature.name,
                    description = elementFunction.values[0].replace("{}", value),
                    functions = emptyArray(),
                    source = elementFeature.source
                )
            val newNumberOfNewFunctions = numberOfNewFunctions - 1
            setNumberOfNewFunctions(newNumberOfNewFunctions)
            if (newNumberOfNewFunctions == 0) {
                action()
                setOpen(false)
            }
        }
    }
    child(maneuversChooser) {
        val functionValues = maneuversFunction.values
        attrs.open = openManeuvers
        attrs.setOpen = setOpenManeuvers
        attrs.feature = maneuversFeature
        attrs.function = maneuversFunction
        attrs.character = character
        attrs.size = if (functionValues.isEmpty()) 0 else functionValues[0].toInt()
        attrs.setValue = { values ->
            values.forEach { value ->
                character.features += Feature(
                    name = value,
                    description = functionValues[functionValues.indexOf(value) + 1],
                    functions = emptyArray(),
                    source = maneuversFeature.source
                )
            }

            val newNumberOfNewFunctions = numberOfNewFunctions - 1
            setNumberOfNewFunctions(newNumberOfNewFunctions)
            if (newNumberOfNewFunctions == 0) {
                action()
                setOpen(false)
            }
        }
    }
    if (useFeats) {
        child(featsChooser) {
            attrs.open = openFeats
            attrs.setOpen = setOpenFeats
            attrs.character = character
            attrs.feature = featsFeature
            attrs.feats = feats
            attrs.action = {
                val newNumberOfNewFunctions = numberOfNewFunctions - 1
                setNumberOfNewFunctions(newNumberOfNewFunctions)
                if (newNumberOfNewFunctions == 0) {
                    action()
                    setOpen(false)
                }
            }
        }
    }
}

val informWindow = functionalComponent<FeatureProps<String>> { props ->
    dInformWindow(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        setValue = { props.setValue("") }
    )
}

val proficiencyChooser = functionalComponent<FeatureProps<String>> { props ->
    val values = props.function.values.toList().filter { !props.character.proficiencies.contains(it) }
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.toTypedArray().copyOfRange(2, values.size) else emptyArray(),
        setValue = props.setValue
    )
}

val proficienciesChooser = functionalComponent<MultipleFeatureProps> { props ->
    val values = props.function.values.toList().filter { !props.character.proficiencies.contains(it) }
    chooseSeveral(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        size = props.size,
        values = if (props.open) values.toTypedArray().copyOfRange(3, values.size) else emptyArray(),
        setValue = props.setValue
    )
}

val languageChooser = functionalComponent<FeatureProps<String>> { props ->
    val values = props.function.values.toList().filter { !props.character.languages.contains(it) }
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.toTypedArray().copyOfRange(2, values.size) else emptyArray(),
        setValue = props.setValue
    )
}

val languagesChooser = functionalComponent<MultipleFeatureProps> { props ->
    val values = props.function.values.toList().filter { !props.character.languages.contains(it) }
    chooseSeveral(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        size = props.size,
        values = if (props.open) values.toTypedArray().copyOfRange(3, values.size) else emptyArray(),
        setValue = props.setValue
    )
}

val skillsChooser = functionalComponent<FeatureProps<String>> { props ->
    val values = props.function.values
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.copyOfRange(2, values.size) else emptyArray(),
        setValue = props.setValue
    )
}

val abilitiesChooser = functionalComponent<FeatureProps<String>> { props ->
    val values = props.function.values
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.copyOfRange(3, values.size) else emptyArray(),
        setValue = props.setValue
    )
}

val elementChooser = functionalComponent<FeatureProps<String>> { props ->
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

val featsChooser = functionalComponent<FeatsProps> { props ->
    val (feat, setFeat) = useState(emptyFeat())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)

    val translations = useContext(TranslationsContext)
    mDialog(open = props.open) {
        mDialogTitle(text = props.feature.name)
        mDialogContent(dividers = true) {
            mDialogContentText(text = props.feature.description)
            dValidatorForm(onSubmit = {
                props.setOpen(false)
                setOpenFeatures(true)
            }) {
                dValidatedList(
                    label = props.feature.name,
                    value = feat._id,
                    validators = arrayOf(VALIDATION_REQUIRED),
                    errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: ""),
                    onChange = { event ->
                        setFeat(props.feats[event.targetValue] ?: emptyFeat())
                    },
                    menuItems = props.feats.mapValues { it.value.description },
                    setDescription = setDescription,
                    description = description
                )
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
    child(collectFeatFeatures) {
        attrs.character = props.character
        attrs.open = openFeatures
        attrs.setOpen = setOpenFeatures
        attrs.action = props.action
        attrs.feature = feat.toFeature()
    }
}

val maneuversChooser = functionalComponent<MultipleFeatureProps> { props ->
    val values = mutableListOf<String>()
    val features = props.character.features.map(Feature::name)
    for (i in 1..props.function.values.size) {
        val value = props.function.values[i]
        if (i % 2 != 0 && !features.contains(value)) {
            values.add(value)
        }
    }
    chooseSeveral(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        size = props.size,
        values = values.toTypedArray(),
        setValue = props.setValue
    )
}

private fun filterFeature(feature: Feature, useFeats: Boolean) =
    (useFeats && (feature.withFeats || !feature.withoutFeats)) || !(useFeats || feature.withFeats)

private fun RBuilder.dDescriptionFrame(description: String) {
    styledDiv {
        attrs.classes = setOf(CLASS_BORDERED)
        css {
            height = 300.px
            maxHeight = 300.px
            width = 800.px
            maxWidth = 800.px
            margin = "5px"
            display = inlineBlock
            overflow = auto
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
            mDialogContentText(text = "") {
                styledDiv {
                    attrs[DANGEROUS_HTML] = DangerousHTML(description)
                }
            }
            dValidatorForm(onSubmit = {
                setValue(chosenValue)
                setOpen(false)
            }) {
                dValidatedList(
                    value = chosenValue,
                    validators = arrayOf(VALIDATION_REQUIRED),
                    errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: ""),
                    onChange = { event -> setChosenValue(event.targetValue as String) },
                    menuItems = values.associateWith { "" },
                    useDescription = false
                )
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.chooseSeveral(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    header: String,
    description: String,
    size: Int,
    values: Array<String>,
    setValue: (Array<String>) -> Unit
) {
    val (chosenValues, setChosenValues) = useState(Array(size) { "" })
    val translations = useContext(TranslationsContext)
    mDialog(open = open) {
        mDialogTitle(text = header)
        mDialogContent(dividers = true) {
            mDialogContentText(text = "") {
                styledDiv {
                    attrs[DANGEROUS_HTML] = DangerousHTML(description)
                }
            }
            dValidatorForm(onSubmit = {
                setValue(chosenValues)
                setOpen(false)
            }) {
                styledDiv {
                    attrs.classes = setOf(CLASS_INLINE)
                    for (i in 0 until size) {
                        validatorForm.values = chosenValues
                        dValidatedList(
                            value = chosenValues[i],
                            validators = arrayOf(VALIDATION_REQUIRED, VALIDATION_VALUE_ALREADY_PRESENT),
                            errorMessages = arrayOf(
                                translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: "",
                                translations[VALUE_IS_ALREADY_SELECTED_TRANSLATION] ?: ""
                            ),
                            onChange = { event ->
                                val newValues = chosenValues.copyOf()
                                newValues[i] = event.targetValue as String
                                setChosenValues(newValues)
                            },
                            menuItems = values.associateWith { "" },
                            useDescription = false
                        )
                    }
                }
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.dInformWindow(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    header: String,
    description: String,
    setValue: () -> Unit
) {
    val translations = useContext(TranslationsContext)

    mDialog(open = open) {
        mDialogTitle(text = header)
        mDialogContent(dividers = true) {
            mDialogContentText(text = "") {
                styledDiv {
                    attrs[DANGEROUS_HTML] = DangerousHTML(description)
                }
            }
            dValidatorForm(onSubmit = {
                setValue()
                setOpen(false)
            }) {
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
    dTooltip(tooltip) {
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
