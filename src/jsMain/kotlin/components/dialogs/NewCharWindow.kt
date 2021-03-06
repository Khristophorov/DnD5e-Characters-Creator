package me.khrys.dnd.charcreator.client.components.dialogs

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.form.mFormGroup
import com.ccfraser.muirwik.components.mCircularProgress
import com.ccfraser.muirwik.components.targetValue
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.FlexWrap.wrap
import kotlinx.css.flexWrap
import kotlinx.html.InputType.file
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.backButton
import me.khrys.dnd.charcreator.client.components.collectRaceFeatures
import me.khrys.dnd.charcreator.client.components.collectSubraceFeatures
import me.khrys.dnd.charcreator.client.components.dAbilityBox
import me.khrys.dnd.charcreator.client.components.dButton
import me.khrys.dnd.charcreator.client.components.dCheckboxWithLabel
import me.khrys.dnd.charcreator.client.components.dSubmit
import me.khrys.dnd.charcreator.client.components.dValidatedList
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.fetchRaces
import me.khrys.dnd.charcreator.client.imageFromEvent
import me.khrys.dnd.charcreator.client.storeCharacter
import me.khrys.dnd.charcreator.client.validators.InputProps
import me.khrys.dnd.charcreator.client.validators.dTextValidator
import me.khrys.dnd.charcreator.client.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.ACROBATICS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ACROBATICS_TRANSLATION
import me.khrys.dnd.charcreator.common.ANIMAL_HANDLING_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ANIMAL_HANDLING_TRANSLATION
import me.khrys.dnd.charcreator.common.ARCANA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ARCANA_TRANSLATION
import me.khrys.dnd.charcreator.common.ATHLETICS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ATHLETICS_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CHAR_NAME_EXISTS
import me.khrys.dnd.charcreator.common.CLASS_DISABLED
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CONSTITUTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import me.khrys.dnd.charcreator.common.DECEPTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DECEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_ABILITIES_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_ABILITIES_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_IMAGE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_IMAGE_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_LABEL_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_RACE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_RACE_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SAVING_THROWS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SAVING_THROWS_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SKILLS_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SUBRACE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SUBRACE_TRANSLATION
import me.khrys.dnd.charcreator.common.FILE_INPUT_ID
import me.khrys.dnd.charcreator.common.FINISH_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.INTIMIDATION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTIMIDATION_TRANSLATION
import me.khrys.dnd.charcreator.common.INVESTIGATION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INVESTIGATION_TRANSLATION
import me.khrys.dnd.charcreator.common.MEDICINE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.MEDICINE_TRANSLATION
import me.khrys.dnd.charcreator.common.NAME_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.NATURE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.NATURE_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERCEPTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PERFORMANCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERFORMANCE_TRANSLATION
import me.khrys.dnd.charcreator.common.PERSUASION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERSUASION_TRANSLATION
import me.khrys.dnd.charcreator.common.RACE_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.RELIGION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.RELIGION_TRANSLATION
import me.khrys.dnd.charcreator.common.SLEIGHT_OF_HANDS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.SLEIGHT_OF_HANDS_TRANSLATION
import me.khrys.dnd.charcreator.common.STEALTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STEALTH_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.SUBRACE_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_TRANSLATION
import me.khrys.dnd.charcreator.common.UPLOAD_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_DUPLICATE_NAME
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.WISDOM_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Race
import me.khrys.dnd.charcreator.common.models.SavingThrows
import me.khrys.dnd.charcreator.common.models.Skills
import me.khrys.dnd.charcreator.common.models.emptyCharacter
import me.khrys.dnd.charcreator.common.models.emptyRace
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import react.RBuilder
import react.RProps
import react.RSetState
import react.child
import react.dom.p
import react.functionalComponent
import react.useContext
import react.useState
import styled.css
import styled.styledDiv

val newCharWindow = functionalComponent<DialogProps> { props ->
    val translations = useContext(TranslationsContext)
    val (subraceDialogOpen, setSubraceDialogOpen) = useState(false)
    val (nameDialogOpen, setNameDialogOpen) = useState(false)
    val (imageDialogOpen, setImageDialogOpen) = useState(false)
    val (abilitiesDialogOpen, setAbilitiesDialogOpen) = useState(false)
    val (savingThrowsDialogOpen, setSavingThrowsDialogOpen) = useState(false)
    val (skillsDialogOpen, setSkillsDialogOpen) = useState(false)
    val (newCharacter, setNewCharacter) = useState(emptyCharacter())

    child(charRaceWindow) {
        attrs.newCharacter = newCharacter
        attrs.open = props.open
        attrs.setOpen = props.setOpen
        attrs.action = {
            if (newCharacter.race.subraces.isEmpty()) {
                setNameDialogOpen(true)
            } else {
                setSubraceDialogOpen(true)
            }
        }
    }
    child(charSubraseWindow) {
        attrs.newCharacter = newCharacter
        attrs.open = subraceDialogOpen
        attrs.backAction = {
            setSubraceDialogOpen(false)
            props.setOpen(true)
        }
        attrs.action = {
            setSubraceDialogOpen(false)
            setNameDialogOpen(true)
        }
    }
    charNameWindow(translations, newCharacter, nameDialogOpen, {
        setNameDialogOpen(false)
        props.setOpen(true)
    }) {
        setNameDialogOpen(false)
        setImageDialogOpen(true)
    }
    charImageWindow(translations, newCharacter, imageDialogOpen, {
        setImageDialogOpen(false)
        setNameDialogOpen(true)
    }) {
        setImageDialogOpen(false)
        setAbilitiesDialogOpen(true)
    }
    charAbilitiesWindow(translations, newCharacter, abilitiesDialogOpen, {
        setAbilitiesDialogOpen(false)
        setImageDialogOpen(true)
    }) {
        setAbilitiesDialogOpen(false)
        setSavingThrowsDialogOpen(true)
    }
    charSavingThrowsWindow(translations, newCharacter, savingThrowsDialogOpen, {
        setSavingThrowsDialogOpen(false)
        setAbilitiesDialogOpen(true)
    }) {
        setSavingThrowsDialogOpen(false)
        setSkillsDialogOpen(true)
    }
    charSkillsWindow(translations, newCharacter, skillsDialogOpen, {
        setSkillsDialogOpen(false)
        setSavingThrowsDialogOpen(true)
    }) {
        storeCharacter(newCharacter)
        setNewCharacter(emptyCharacter())
        setSkillsDialogOpen(false)
    }
}

interface CharRaceProps : RProps {
    var newCharacter: Character
    var open: Boolean
    var setOpen: (Boolean) -> Unit
    var action: () -> Unit
    var backAction: () -> Unit
}

val charRaceWindow = functionalComponent<CharRaceProps> { props ->
    val (races, setRaces) = useState(emptyMap<String, Race>())
    val (race, setRace) = useState(emptyRace())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    if (props.open && races.isEmpty()) {
        loadRaces(setRaces)
    } else {
        mDialog(open = props.open) {
            val translations = useContext(TranslationsContext)
            mDialogTitle(text = translations[ENTER_RACE_TRANSLATION] ?: "")
            mDialogContent(dividers = true) {
                mDialogContentText(text = translations[ENTER_RACE_CONTENT_TRANSLATION] ?: "")
                dValidatorForm(onSubmit = {
                    props.newCharacter.race = race
                    props.newCharacter.features = emptyArray()
                    setOpenFeatures(true)
                    props.setOpen(false)
                }) {
                    dValidatedList(
                        label = translations[ENTER_RACE_TRANSLATION] ?: "",
                        value = race._id,
                        validators = arrayOf(VALIDATION_REQUIRED),
                        errorMessages = arrayOf(translations[RACE_SHOULD_BE_FILLED_TRANSLATION] ?: ""),
                        onChange = { event ->
                            setRace(races[event.targetValue] ?: emptyRace())
                        },
                        menuItems = races.mapValues { it.value.description },
                        setDescription = setDescription,
                        description = description
                    )
                    mDialogActions {
                        dSubmit(translations[NEXT_TRANSLATION] ?: "")
                    }
                }
            }
        }
        child(collectRaceFeatures) {
            attrs.character = props.newCharacter
            attrs.open = openFeatures
            attrs.setOpen = setOpenFeatures
            attrs.action = props.action
        }
    }
}

val charSubraseWindow = functionalComponent<CharRaceProps> { props ->
    val subraces = props.newCharacter.race.subraces.associateBy { it._id }
    val (subrace, setSubrace) = useState(emptyRace())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    mDialog(open = props.open) {
        val translations = useContext(TranslationsContext)
        mDialogTitle(text = translations[ENTER_SUBRACE_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            mDialogContentText(text = translations[ENTER_SUBRACE_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(onSubmit = {
                props.newCharacter.subrace = subrace
                if (subrace.features.isEmpty()) {
                    props.action()
                }
                else {
                    setOpenFeatures(true)
                }
            }) {
                dValidatedList(
                    label = translations[ENTER_SUBRACE_TRANSLATION] ?: "",
                    value = subrace._id,
                    validators = arrayOf(VALIDATION_REQUIRED),
                    errorMessages = arrayOf(translations[SUBRACE_SHOULD_BE_FILLED_TRANSLATION] ?: ""),
                    onChange = { event -> setSubrace(subraces[event.targetValue] ?: emptyRace()) },
                    menuItems = subraces.mapValues { it.value.description },
                    setDescription = setDescription,
                    description = description
                )
                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    backButton { props.backAction() }
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
    child(collectSubraceFeatures) {
        attrs.character = props.newCharacter
        attrs.open = openFeatures
        attrs.setOpen = setOpenFeatures
        attrs.action = props.action
    }
}

fun RBuilder.charNameWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    mDialog(open = open) {
        val (charName, setCharName) = useState("")

        mDialogTitle(text = translations[ENTER_NAME_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            mDialogContentText(text = translations[ENTER_NAME_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(onSubmit = { event ->
                newCharacter.name = charName
                action(event)
            }) {
                dTextValidator(
                    label = translations[ENTER_NAME_LABEL_TRANSLATION] ?: "",
                    value = charName,
                    validators = arrayOf(VALIDATION_REQUIRED, VALIDATION_DUPLICATE_NAME),
                    errorMessages = arrayOf(
                        translations[NAME_SHOULD_BE_FILLED_TRANSLATION] ?: "",
                        translations[CHAR_NAME_EXISTS] ?: ""
                    ),
                    onChange = { event -> setCharName(event.targetValue as String) }
                )
                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    backButton { event ->
                        backAction(event)
                    }
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.charImageWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    mDialog(open = open) {
        val (charImage, setCharImage) = useState("")

        mDialogTitle(text = translations[ENTER_IMAGE_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            mDialogContentText(text = translations[ENTER_IMAGE_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(onSubmit = { event ->
                setCharImage("")
                action(event)
            }) {
                dTextValidator(
                    id = FILE_INPUT_ID,
                    type = file,
                    value = charImage,
                    className = CLASS_DISABLED,
                    inputProps = InputProps(accept = "image/*"),
                    validators = arrayOf(VALIDATION_REQUIRED),
                    onChange = { event ->
                        setCharImage(event.targetValue as String)
                        imageFromEvent(event) { e -> newCharacter.image = (e.target as FileReader).result as String }
                    }
                )
                dButton(
                    caption = translations[UPLOAD_TRANSLATION] ?: ""
                ) { (document.getElementById(FILE_INPUT_ID) as HTMLElement).click() }
                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    backButton { event ->
                        setCharImage("")
                        backAction(event)
                    }
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.charAbilitiesWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    mDialog(open = open) {
        val (strength, setStrength) = useState(10)
        val (dexterity, setDexterity) = useState(10)
        val (constitution, setConstitution) = useState(10)
        val (intelligence, setIntelligence) = useState(10)
        val (wisdom, setWisdom) = useState(10)
        val (charisma, setCharisma) = useState(10)

        mDialogTitle(text = translations[ENTER_ABILITIES_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            mDialogContentText("") {
                p {
                    attrs[DANGEROUS_HTML] =
                        DangerousHTML(translations[ENTER_ABILITIES_CONTENT_TRANSLATION] ?: "")
                }
            }
            dValidatorForm(
                onSubmit = { event ->
                    newCharacter.abilities =
                        Abilities(strength, dexterity, constitution, intelligence, wisdom, charisma)
                    action(event)
                }) {
                styledDiv {
                    attrs.classes = setOf(CLASS_INLINE)
                    css {
                        flexWrap = wrap
                    }
                    dAbilityBox(
                        title = translations[STRENGTH_CONTENT_TRANSLATION] ?: "",
                        label = translations[STRENGTH_TRANSLATION] ?: "",
                        value = strength,
                        translations = translations,
                        onChange = { event ->
                            (event.targetValue as String).toIntOrNull()?.let { setStrength(it) }
                        }
                    )
                    dAbilityBox(
                        title = translations[DEXTERITY_CONTENT_TRANSLATION] ?: "",
                        label = translations[DEXTERITY_TRANSLATION] ?: "",
                        value = dexterity,
                        translations = translations,
                        onChange = { event ->
                            (event.targetValue as String).toIntOrNull()?.let { setDexterity(it) }
                        }
                    )
                    dAbilityBox(
                        title = translations[CONSTITUTION_CONTENT_TRANSLATION] ?: "",
                        label = translations[CONSTITUTION_TRANSLATION] ?: "",
                        value = constitution,
                        translations = translations,
                        onChange = { event ->
                            (event.targetValue as String).toIntOrNull()?.let { setConstitution(it) }
                        }
                    )
                    dAbilityBox(
                        title = translations[INTELLIGENCE_CONTENT_TRANSLATION] ?: "",
                        label = translations[INTELLIGENCE_TRANSLATION] ?: "",
                        value = intelligence,
                        translations = translations,
                        onChange = { event ->
                            (event.targetValue as String).toIntOrNull()?.let { setIntelligence(it) }
                        }
                    )
                    dAbilityBox(
                        title = translations[WISDOM_CONTENT_TRANSLATION] ?: "",
                        label = translations[WISDOM_TRANSLATION] ?: "",
                        value = wisdom,
                        translations = translations,
                        onChange = { event ->
                            (event.target as HTMLInputElement).value.toIntOrNull()?.let { setWisdom(it) }
                        }
                    )
                    dAbilityBox(
                        title = translations[CHARISMA_CONTENT_TRANSLATION] ?: "",
                        label = translations[CHARISMA_TRANSLATION] ?: "",
                        value = charisma,
                        translations = translations,
                        onChange = { event ->
                            (event.targetValue as String).toIntOrNull()?.let { setCharisma(it) }
                        }
                    )
                }
                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    backButton(backAction)
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.charSavingThrowsWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    val (strength, setStrength) = useState(false)
    val (dexterity, setDexterity) = useState(false)
    val (constitution, setConstitution) = useState(false)
    val (intelligence, setIntelligence) = useState(false)
    val (wisdom, setWisdom) = useState(false)
    val (charisma, setCharisma) = useState(false)
    mDialog(open = open) {
        mDialogTitle(text = translations[ENTER_SAVING_THROWS_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            mDialogContentText(text = translations[ENTER_SAVING_THROWS_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(
                onSubmit = { event ->
                    newCharacter.savingThrows =
                        SavingThrows(strength, dexterity, constitution, intelligence, wisdom, charisma)
                    action(event)
                }
            ) {
                styledDiv {
                    attrs.classes = setOf(CLASS_JUSTIFY_BETWEEN, CLASS_INLINE)
                    mFormGroup {
                        dCheckboxWithLabel(
                            label = translations[STRENGTH_TRANSLATION] ?: "",
                            checked = strength,
                            onChange = { _, checked -> setStrength(checked) }
                        )
                        dCheckboxWithLabel(
                            label = translations[DEXTERITY_TRANSLATION] ?: "",
                            checked = dexterity,
                            onChange = { _, checked -> setDexterity(checked) }
                        )
                    }
                    mFormGroup {
                        dCheckboxWithLabel(
                            label = translations[CONSTITUTION_TRANSLATION] ?: "",
                            checked = constitution,
                            onChange = { _, checked -> setConstitution(checked) }
                        )
                        dCheckboxWithLabel(
                            label = translations[INTELLIGENCE_TRANSLATION] ?: "",
                            checked = intelligence,
                            onChange = { _, checked -> setIntelligence(checked) }
                        )
                    }
                    mFormGroup {
                        dCheckboxWithLabel(
                            label = translations[WISDOM_TRANSLATION] ?: "",
                            checked = wisdom,
                            onChange = { _, checked -> setWisdom(checked) }
                        )
                        dCheckboxWithLabel(
                            label = translations[CHARISMA_TRANSLATION] ?: "",
                            checked = charisma,
                            onChange = { _, checked -> setCharisma(checked) }
                        )
                    }
                }

                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    backButton(backAction)
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.charSkillsWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    val (acrobatics, setAcrobatics) = useState(false)
    val (animalHandling, setAnimalHandling) = useState(false)
    val (arcana, setArcana) = useState(false)
    val (athletics, setAthletics) = useState(false)
    val (deception, setDeception) = useState(false)
    val (history, setHistory) = useState(false)
    val (insight, setInsight) = useState(false)
    val (intimidation, setIntimidation) = useState(false)
    val (investigation, setInvestigation) = useState(false)
    val (medicine, setMedicine) = useState(false)
    val (nature, setNature) = useState(false)
    val (perception, setPerception) = useState(false)
    val (performance, setPerformance) = useState(false)
    val (persuasion, setPersuasion) = useState(false)
    val (religion, setReligion) = useState(false)
    val (sleightOfHands, setSleightOfHands) = useState(false)
    val (stealth, setStealth) = useState(false)
    val (survival, setSurvival) = useState(false)
    mDialog(open = open) {
        mDialogTitle(text = translations[ENTER_SKILLS_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            dValidatorForm(
                onSubmit = { event ->
                    newCharacter.skills = Skills(
                        acrobatics = acrobatics,
                        animalHandling = animalHandling,
                        arcana = arcana,
                        athletics = athletics,
                        deception = deception,
                        history = history,
                        insight = insight,
                        intimidation = intimidation,
                        investigation = investigation,
                        medicine = medicine,
                        nature = nature,
                        perception = perception,
                        performance = performance,
                        persuasion = persuasion,
                        religion = religion,
                        sleightOfHands = sleightOfHands,
                        stealth = stealth,
                        survival = survival
                    )
                    action(event)
                }
            ) {
                styledDiv {
                    attrs.classes = setOf(CLASS_JUSTIFY_BETWEEN, CLASS_INLINE)
                    buildCheckboxCreateElements(listOf(translations[ACROBATICS_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[ACROBATICS_CONTENT_TRANSLATION] ?: "",
                                label = translations[ACROBATICS_TRANSLATION] ?: "",
                                checked = acrobatics,
                                onChange = { _, checked -> setAcrobatics(checked) }
                            ), translations[ANIMAL_HANDLING_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[ANIMAL_HANDLING_CONTENT_TRANSLATION] ?: "",
                                label = translations[ANIMAL_HANDLING_TRANSLATION] ?: "",
                                checked = animalHandling,
                                onChange = { _, checked -> setAnimalHandling(checked) }
                            ), translations[ARCANA_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[ARCANA_CONTENT_TRANSLATION] ?: "",
                                label = translations[ARCANA_TRANSLATION] ?: "",
                                checked = arcana,
                                onChange = { _, checked -> setArcana(checked) }
                            ), translations[ATHLETICS_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[ATHLETICS_CONTENT_TRANSLATION] ?: "",
                                label = translations[ATHLETICS_TRANSLATION] ?: "",
                                checked = athletics,
                                onChange = { _, checked -> setAthletics(checked) }
                            ), translations[DECEPTION_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[DECEPTION_CONTENT_TRANSLATION] ?: "",
                                label = translations[DECEPTION_TRANSLATION] ?: "",
                                checked = deception,
                                onChange = { _, checked -> setDeception(checked) }
                            ), translations[HISTORY_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[HISTORY_CONTENT_TRANSLATION] ?: "",
                                label = translations[HISTORY_TRANSLATION] ?: "",
                                checked = history,
                                onChange = { _, checked -> setHistory(checked) }
                            ), translations[INSIGHT_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[INSIGHT_CONTENT_TRANSLATION] ?: "",
                                label = translations[INSIGHT_TRANSLATION] ?: "",
                                checked = insight,
                                onChange = { _, checked -> setInsight(checked) }
                            ), translations[INTIMIDATION_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[INTIMIDATION_CONTENT_TRANSLATION] ?: "",
                                label = translations[INTIMIDATION_TRANSLATION] ?: "",
                                checked = intimidation,
                                onChange = { _, checked -> setIntimidation(checked) }
                            ), translations[INVESTIGATION_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[INVESTIGATION_CONTENT_TRANSLATION] ?: "",
                                label = translations[INVESTIGATION_TRANSLATION] ?: "",
                                checked = investigation,
                                onChange = { _, checked -> setInvestigation(checked) }
                            ), translations[MEDICINE_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[MEDICINE_CONTENT_TRANSLATION] ?: "",
                                label = translations[MEDICINE_TRANSLATION] ?: "",
                                checked = medicine,
                                onChange = { _, checked -> setMedicine(checked) }
                            ), translations[NATURE_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[NATURE_CONTENT_TRANSLATION] ?: "",
                                label = translations[NATURE_TRANSLATION] ?: "",
                                checked = nature,
                                onChange = { _, checked -> setNature(checked) }
                            ), translations[PERCEPTION_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[PERCEPTION_CONTENT_TRANSLATION] ?: "",
                                label = translations[PERCEPTION_TRANSLATION] ?: "",
                                checked = perception,
                                onChange = { _, checked -> setPerception(checked) }
                            ), translations[PERFORMANCE_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[PERFORMANCE_CONTENT_TRANSLATION] ?: "",
                                label = translations[PERFORMANCE_TRANSLATION] ?: "",
                                checked = performance,
                                onChange = { _, checked -> setPerformance(checked) }
                            ), translations[PERSUASION_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[PERSUASION_CONTENT_TRANSLATION] ?: "",
                                label = translations[PERSUASION_TRANSLATION] ?: "",
                                checked = persuasion,
                                onChange = { _, checked -> setPersuasion(checked) }
                            ), translations[RELIGION_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[RELIGION_CONTENT_TRANSLATION] ?: "",
                                label = translations[RELIGION_TRANSLATION] ?: "",
                                checked = religion,
                                onChange = { _, checked -> setReligion(checked) }
                            ), translations[SLEIGHT_OF_HANDS_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[SLEIGHT_OF_HANDS_CONTENT_TRANSLATION] ?: "",
                                label = translations[SLEIGHT_OF_HANDS_TRANSLATION] ?: "",
                                checked = sleightOfHands,
                                onChange = { _, checked -> setSleightOfHands(checked) }
                            ), translations[STEALTH_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[STEALTH_CONTENT_TRANSLATION] ?: "",
                                label = translations[STEALTH_TRANSLATION] ?: "",
                                checked = stealth,
                                onChange = { _, checked -> setStealth(checked) }
                            ), translations[SURVIVAL_TRANSLATION] to
                            CheckboxWithLabelModel(
                                title = translations[SURVIVAL_CONTENT_TRANSLATION] ?: "",
                                label = translations[SURVIVAL_TRANSLATION] ?: "",
                                checked = survival,
                                onChange = { _, checked -> setSurvival(checked) }
                            )).sortedBy { it.first }.map { it.second })
                }
                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    backButton(backAction)
                    dSubmit(caption = translations[FINISH_TRANSLATION] ?: "")
                }
            }
        }
    }
}

private fun RBuilder.buildCheckboxCreateElements(elements: List<CheckboxWithLabelModel>) {
    mFormGroup {
        for (i in 0..5) {
            dCheckboxWithLabel(
                title = elements[i].title,
                label = elements[i].label,
                checked = elements[i].checked,
                onChange = elements[i].onChange
            )
        }
    }
    mFormGroup {
        for (i in 6..11) {
            dCheckboxWithLabel(
                title = elements[i].title,
                label = elements[i].label,
                checked = elements[i].checked,
                onChange = elements[i].onChange
            )
        }
    }
    mFormGroup {
        for (i in 12..17) {
            dCheckboxWithLabel(
                title = elements[i].title,
                label = elements[i].label,
                checked = elements[i].checked,
                onChange = elements[i].onChange
            )
        }
    }
}

fun RBuilder.loadRaces(
    setRaces: RSetState<Map<String, Race>>
) {
    mCircularProgress()
    MainScope().launch {
        setRaces(fetchRaces().associateBy { it._id })
    }
}
