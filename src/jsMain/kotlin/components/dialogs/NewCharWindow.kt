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
import me.khrys.dnd.charcreator.client.fetchFeats
import me.khrys.dnd.charcreator.client.fetchRaces
import me.khrys.dnd.charcreator.client.imageFromEvent
import me.khrys.dnd.charcreator.client.storeCharacter
import me.khrys.dnd.charcreator.client.validators.InputProps
import me.khrys.dnd.charcreator.client.validators.dTextValidator
import me.khrys.dnd.charcreator.client.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.CHARISMA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CHAR_NAME_EXISTS
import me.khrys.dnd.charcreator.common.CLASS_DISABLED
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CONSTITUTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
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
import me.khrys.dnd.charcreator.common.ENTER_SUBRACE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SUBRACE_TRANSLATION
import me.khrys.dnd.charcreator.common.FEATS_SELECT_TRANSLATION
import me.khrys.dnd.charcreator.common.FILE_INPUT_ID
import me.khrys.dnd.charcreator.common.FINISH_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.NAME_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.RACE_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.SUBRACE_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.UPLOAD_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_DUPLICATE_NAME
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.WISDOM_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Race
import me.khrys.dnd.charcreator.common.models.SavingThrows
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
    val (newCharacter, setNewCharacter) = useState(emptyCharacter())
    val (feats, setFeats) = useState(emptyMap<String, Feat>())

    if (props.open && feats.isEmpty()) {
        loadFeats(setFeats)
    }

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
        attrs.feats = feats
    }
    child(charSubraceWindow) {
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
        storeCharacter(newCharacter)
        setNewCharacter(emptyCharacter())
        setSavingThrowsDialogOpen(false)
    }
}

interface CharRaceProps : RProps {
    var newCharacter: Character
    var open: Boolean
    var setOpen: (Boolean) -> Unit
    var action: () -> Unit
    var backAction: () -> Unit
    var feats: Map<String, Feat>
}

val charRaceWindow = functionalComponent<CharRaceProps> { props ->
    val (races, setRaces) = useState(emptyMap<String, Race>())
    val (race, setRace) = useState(emptyRace())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    val (useFeats, setUseFeats) = useState(false)
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
                    if (race.features.any { it.withFeats }) {
                        dCheckboxWithLabel(
                            label = translations[FEATS_SELECT_TRANSLATION] ?: "",
                            checked = useFeats,
                            onChange = { _, value -> setUseFeats(value) }
                        )
                    }
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
            attrs.feats = props.feats
            attrs.useFeats = useFeats
        }
    }
}

val charSubraceWindow = functionalComponent<CharRaceProps> { props ->
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
                } else {
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
                    backButton {
                        setSubrace(emptyRace())
                        setDescription("")
                        props.backAction()
                    }
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
                    dSubmit(caption = translations[FINISH_TRANSLATION] ?: "")
                }
            }
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

fun RBuilder.loadFeats(
    setFeats: RSetState<Map<String, Feat>>
) {
    mCircularProgress()
    MainScope().launch {
        setFeats(fetchFeats().associateBy { it._id })
    }
}
