package me.khrys.dnd.charcreator.client.components.inputs.choosers

import csstype.ClassName
import me.khrys.dnd.charcreator.client.ManeuversContext
import me.khrys.dnd.charcreator.client.SpellsContext
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.applyFeatures
import me.khrys.dnd.charcreator.client.clone
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.AlertDialog
import me.khrys.dnd.charcreator.client.components.dialogs.CollectFeatFeatures
import me.khrys.dnd.charcreator.client.components.dialogs.FeatsProps
import me.khrys.dnd.charcreator.client.components.dialogs.FeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.MultipleFeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.memoDialog
import me.khrys.dnd.charcreator.client.components.dialogs.windows.SpellWindow
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.computeSpellLevel
import me.khrys.dnd.charcreator.client.toDangerousHtml
import me.khrys.dnd.charcreator.client.toFeature
import me.khrys.dnd.charcreator.client.utils.playSound
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.ALERT_TRANSLATION
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import me.khrys.dnd.charcreator.common.CANTRIP_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_COLLAPSED_CELL
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_LEVEL_SUFFIX_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_LEVEL_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_NAME_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_SCHOOL_TRANSLATION
import me.khrys.dnd.charcreator.common.TOO_FEW_CHECKS_TRANSLATION
import me.khrys.dnd.charcreator.common.TOO_MANY_CHECKS_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import me.khrys.dnd.charcreator.common.models.emptyFeat
import me.khrys.dnd.charcreator.common.models.emptyManeuver
import mui.material.Checkbox
import mui.material.Collapse
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import mui.material.Table
import mui.material.TableBody
import mui.material.TableCell
import mui.material.TableContainer
import mui.material.TableHead
import mui.material.TableRow
import react.FC
import react.Props
import react.dom.html.ReactHTML.span
import react.useContext
import react.useState

external interface ChooserProps<T> : Props {
    var open: Boolean
    var setOpen: (Boolean) -> Unit
    var header: String
    var description: String
    var values: List<String>
    var setValue: (T) -> Unit
    var size: Int
}

val ProficiencyChooser = memoDialog(FC<FeatureProps<String>> { props ->
    if (props.open) {
        val values = props.function.values.toList().filter { !props.character.proficiencies.contains(it) }
        ChooseOneOfMany {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.values = if (props.open) values.subList(2, values.size) else emptyList()
            this.setValue = props.setValue
        }
    }
})

val ProficienciesChooser = FC<MultipleFeatureProps> { props ->
    if (props.open) {
        val values = props.function.values.toList().filter { !props.character.proficiencies.contains(it) }
        ChooseSeveral {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.size = props.size
            this.values = if (props.open) values.subList(3, values.size) else emptyList()
            this.setValue = props.setValue
        }
    }
}

val SkillsAndProficienciesChooser = FC<MultipleFeatureProps> { props ->
    if (props.open) {
        val values = props.function.values
        val skillValues = values[2].split(", ")
        val proficiencyValues = values[3].split(", ")
        ChooseSeveral {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.size = props.size
            this.values = if (props.open) skillValues + proficiencyValues else emptyList()
            this.setValue = props.setValue
        }
    }
}

val LanguageChooser = memoDialog(FC<FeatureProps<String>> { props ->
    if (props.open) {
        val translations = useContext(TranslationsContext)
        val filledCharacter = applyFeatures(props.character.clone(), translations)
        val values = props.function.values.toList().filter { !filledCharacter.languages.contains(it) }
        ChooseOneOfMany {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.values = if (props.open) values.subList(2, values.size) else emptyList()
            this.setValue = props.setValue
        }
    }
})

val LanguagesChooser = FC<MultipleFeatureProps> { props ->
    if (props.open) {
        val translations = useContext(TranslationsContext)
        val filledCharacter = applyFeatures(props.character.clone(), translations)
        val values = props.function.values.toList().filter { !filledCharacter.languages.contains(it) }
        ChooseSeveral {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.size = props.size
            this.values = if (props.open) values.subList(3, values.size) else emptyList()
            this.setValue = props.setValue
        }
    }
}

val SkillChooser = memoDialog(FC<FeatureProps<String>> { props ->
    if (props.open) {
        val values = props.function.values
        ChooseOneOfMany {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.values = if (props.open) values.subList(2, values.size) else emptyList()
            this.setValue = props.setValue
        }
    }
})

val AbilityChooser = FC<FeatureProps<String>> { props ->
    if (props.open) {
        val values = props.function.values
        ChooseOneOfMany {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.values = if (props.open) values.subList(3, values.size) else emptyList()
            this.setValue = props.setValue
        }
    }
}

val ElementChooser = FC<FeatureProps<String>> { props ->
    if (props.open) {
        val values = props.function.values
        ChooseOneOfMany {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.values = if (props.open) values.subList(1, values.size) else emptyList()
            this.setValue = props.setValue
        }
    }
}

val FeatChooser = FC<FeatsProps> { props ->
    val (feat, setFeat) = useState(emptyFeat())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)

    if (props.open) {
        val translations = useContext(TranslationsContext)
        Dialog {
            this.open = props.open
            DialogTitle {
                +props.feature.name
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    +props.feature.description
                }
                ValidatorForm {
                    this.onSubmit = {
                        props.setOpen(false)
                        setOpenFeatures(true)
                    }
                    ValidatedList {
                        this.label = props.feature.name
                        this.value = feat._id
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: "")
                        this.onChange = { event ->
                            setFeat(props.feats[event.value()] ?: emptyFeat())
                        }
                        this.useDescription = true
                        this.menuItems = props.feats.mapValues { it.value.description }
                        this.setDescription = { setDescription(it) }
                        this.description = description
                    }
                    DialogActions {
                        Submit { +(translations[NEXT_TRANSLATION] ?: "") }
                    }
                }
            }
        }
    }
    CollectFeatFeatures {
        this.character = props.character
        this.open = openFeatures
        this.setOpen = { setOpenFeatures(it) }
        this.action = props.action
        this.feature = feat.toFeature()
    }
}

val ManeuverChooser = FC<FeatureProps<String>> { props ->
    val (maneuver, setManeuver) = useState(emptyManeuver())
    val (description, setDescription) = useState("")
    val maneuvers = useContext(ManeuversContext)
    val translations = useContext(TranslationsContext)
    val filteredManeuvers = maneuvers.filter { (name, _) ->
        !props.character.maneuvers.map { it._id }.contains(name)
    }
    if (props.open) {
        Dialog {
            this.open = props.open
            DialogTitle {
                +props.feature.name
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    span {
                        dangerouslySetInnerHTML = toDangerousHtml(props.feature.description)
                    }
                }
                ValidatorForm {
                    this.onSubmit = {
                        props.setOpen(false)
                        props.setValue(maneuver._id)
                    }
                    ValidatedList {
                        this.label = props.feature.name
                        this.value = maneuver._id
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: "")
                        this.onChange = { setManeuver(maneuvers[it.value()] ?: emptyManeuver()) }
                        this.useDescription = true
                        this.menuItems = filteredManeuvers.mapValues { it.value.description }
                        this.setDescription = { setDescription(it) }
                        this.description = description
                    }
                    DialogActions {
                        Submit { +(translations[NEXT_TRANSLATION] ?: "") }
                    }
                }
            }
        }
    }
}

val SpellsChooser = FC<MultipleFeatureProps> { props ->
    val translations = useContext(TranslationsContext)
    val (chosenSpells, setChosenSpells) = useState(emptyList<String>())
    val (openAlert, setOpenAlert) = useState(false)
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_FEW_CHECKS_TRANSLATION]
    }
    if (props.open) {
        val values = props.function.values
        val maxSpellsNumber = values[0].toInt()
        Dialog {
            this.open = props.open
            DialogTitle {
                +props.feature.name
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    span {
                        this.dangerouslySetInnerHTML = toDangerousHtml(props.feature.description)
                    }
                }
                ValidatorForm {
                    this.onSubmit = {
                        if (chosenSpells.size < maxSpellsNumber) {
                            setOpenAlert(true)
                        } else {
                            props.setValue(chosenSpells)
                            props.setOpen(false)
                        }
                    }
                    TableContainer {
                        SpellsTable {
                            this.character = props.character
                            this.function = props.function
                            this.setValue = { setChosenSpells(it) }
                            this.value = chosenSpells
                        }
                    }
                    DialogActions {
                        Submit { +(translations[NEXT_TRANSLATION] ?: "") }
                    }
                }
            }
        }
    }
}

val SpellsTable = FC<MultipleFeatureProps> { props ->
    val translations = useContext(TranslationsContext)
    val spells = useContext(SpellsContext).filter { (name, _) -> !props.character.spells.map { it._id }.contains(name) }
    val (openAlert, setOpenAlert) = useState(false)
    val values = props.function.values
    val maxSpellsNumber = values[0].toInt()
    val level = values[1].toInt()
    val classes = values[2].split(", ")
    val isMaxLevel = values.getOrNull(3).toBoolean()
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_MANY_CHECKS_TRANSLATION]
    }
    Table {
        this.stickyHeader = true
        TableHead {
            TableRow {
                TableCell()
                TableCell { +translations[SPELL_NAME_TRANSLATION] }
                TableCell { +translations[SPELL_LEVEL_TRANSLATION] }
                TableCell { +translations[SPELL_SCHOOL_TRANSLATION] }
            }
        }
        TableBody {
            val filteredSpells = spells.values.filter {
                if (isMaxLevel) it.level == level else it.level <= level
            }.filter { spell -> classes.any { spell.classes.contains(it) } }
            filteredSpells.forEach { spell ->
                val (open, setOpen) = useState(false)
                TableRow {
                    TableCell {
                        val (checked, setChecked) = useState(false)
                        Checkbox {
                            this.checked = checked
                            this.onChange = { _, checked ->
                                playSound(BUTTON_SOUND_ID)
                                if (checked && props.value.size >= maxSpellsNumber) {
                                    setOpenAlert(true)
                                    setChecked(false)
                                } else {
                                    setChecked(checked)
                                    props.setValue(
                                        if (checked) props.value + spell._id
                                        else props.value - spell._id
                                    )
                                    console.info("${spell._id} is $checked")
                                }
                            }
                        }
                    }
                    TableCell {
                        this.onClick = { setOpen(!open) }
                        +spell._id
                    }
                    TableCell {
                        this.onClick = { setOpen(!open) }
                        +computeSpellLevel(
                            spell.level,
                            translations[CANTRIP_TRANSLATION] ?: "",
                            translations[SPELL_LEVEL_SUFFIX_TRANSLATION]
                        )
                    }
                    TableCell { +spell.school }
                }
                TableRow {
                    TableCell {
                        this.colSpan = 4
                        this.className = ClassName(CLASS_COLLAPSED_CELL)
                        Collapse {
                            this.`in` = open
                            this.timeout = "auto"
                            SpellWindow {
                                this.spell = spell
                            }
                        }
                    }
                }
            }
        }
    }
}
