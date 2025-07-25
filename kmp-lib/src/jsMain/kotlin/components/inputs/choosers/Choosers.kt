package me.khrys.dnd.charcreator.client.components.inputs.choosers

import kotlinx.serialization.json.Json
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
import me.khrys.dnd.charcreator.client.components.dialogs.MultipleStringFeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.SpellsFeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.memoDialog
import me.khrys.dnd.charcreator.client.components.dialogs.windows.SpellWindow
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.TextValidator
import me.khrys.dnd.charcreator.client.components.validators.TextValidatorProps
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.computeSpellLevel
import me.khrys.dnd.charcreator.client.isNumber
import me.khrys.dnd.charcreator.client.isProficient
import me.khrys.dnd.charcreator.client.toDangerousHtml
import me.khrys.dnd.charcreator.client.toFeature
import me.khrys.dnd.charcreator.client.utils.playSound
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.client.validateArmor
import me.khrys.dnd.charcreator.client.validateSimpleEquipment
import me.khrys.dnd.charcreator.client.validateWeapon
import me.khrys.dnd.charcreator.common.ALERT_TRANSLATION
import me.khrys.dnd.charcreator.common.ARMOR_CLASS_TRANSLATION
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import me.khrys.dnd.charcreator.common.CANTRIP_TRANSLATION
import me.khrys.dnd.charcreator.common.CELLS_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_COLLAPSED_CELL
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DAMAGE_TRANSLATION
import me.khrys.dnd.charcreator.common.DESCRIPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.NAME_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.PRICE_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENT
import me.khrys.dnd.charcreator.common.PROPERTIES_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_LEVEL_SUFFIX_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_LEVEL_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_NAME_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_SCHOOL_TRANSLATION
import me.khrys.dnd.charcreator.common.STEALTH_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.TOO_FEW_CHECKS_TRANSLATION
import me.khrys.dnd.charcreator.common.TOO_MANY_CHECKS_TRANSLATION
import me.khrys.dnd.charcreator.common.TYPE_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import me.khrys.dnd.charcreator.common.WEIGHT_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Armor
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.SimpleEquipment
import me.khrys.dnd.charcreator.common.models.Spell
import me.khrys.dnd.charcreator.common.models.Weapon
import me.khrys.dnd.charcreator.common.models.emptyArmor
import me.khrys.dnd.charcreator.common.models.emptyFeat
import me.khrys.dnd.charcreator.common.models.emptyManeuver
import me.khrys.dnd.charcreator.common.models.emptySimpleEquipment
import me.khrys.dnd.charcreator.common.models.emptyWeapon
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
import react.ReactNode
import react.dom.html.ReactHTML.span
import react.use
import react.useState
import web.cssom.ClassName

external interface ChooserProps<T> : Props {
    var open: Boolean
    var setOpen: (Boolean) -> Unit
    var header: String
    var description: String
    var values: List<String>
    var setValue: (T) -> Unit
    var size: Int
    var unique: Boolean
}

val ProficiencyChooser = memoDialog(FC<FeatureProps<String>>("ProficiencyChooser") { props ->
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

val ProficienciesChooser = FC<MultipleStringFeatureProps>("ProficienciesChooser") { props ->
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
            this.unique = true
        }
    }
}

val SkillsAndProficienciesChooser = FC<MultipleStringFeatureProps>("SkillsAndProficienciesChooser") { props ->
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
            this.unique = true
        }
    }
}

val LanguageChooser = memoDialog(FC<FeatureProps<String>>("LanguageChooser") { props ->
    if (props.open) {
        val translations = use(TranslationsContext)
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

val LanguagesChooser = FC<MultipleStringFeatureProps>("LanguagesChooser") { props ->
    if (props.open) {
        val translations = use(TranslationsContext)
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
            this.unique = true
        }
    }
}

val SkillChooser = memoDialog(FC<FeatureProps<String>>("SkillChooser") { props ->
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

val SkillsChooser = FC<MultipleStringFeatureProps>("SkillsChooser") { props ->
    if (props.open) {
        ChooseSeveral {
            val translations = use(TranslationsContext)
            val character = applyFeatures(props.character, translations)
            val values =
                if (props.open)
                    computeSkillsValues(character, props.function.values)
                else emptyList()
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.size = props.size
            this.values = values
            this.setValue = props.setValue
            this.unique = true
        }
    }
}

val AbilityChooser = FC<FeatureProps<String>>("AbilityChooser") { props ->
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

val AbilitiesChooser = FC<MultipleStringFeatureProps>("AbilitiesChooser") { props ->
    if (props.open) {
        val translations = use(TranslationsContext)
        val values = listOf(
            translations[STRENGTH_TRANSLATION],
            translations[DEXTERITY_TRANSLATION],
            translations[CONSTITUTION_TRANSLATION],
            translations[INTELLIGENCE_TRANSLATION],
            translations[WISDOM_TRANSLATION],
            translations[CHARISMA_TRANSLATION]
        ).map { it ?: "" }
        ChooseSeveral {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.size = props.size
            this.values = values
            this.setValue = props.setValue
            this.unique = false
        }
    }
}

val ElementChooser = FC<FeatureProps<String>>("ElementChooser") { props ->
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

val FeatureChooser = FC<FeatureProps<String>>("FeatureChooser") { props ->
    if (props.open) {
        val (chosenFeature, setChosenFeature) = useState("")
        val (description, setDescription) = useState("")
        val values = props.function.values
        val currentFeatures = props.character.features.map { it.name }
        val features = values.filterIndexed { index, _ -> (index + 1) % 3 != 0 }
            .chunked(2) { it[0] to it[1] }.toMap()
            .filter { !currentFeatures.contains(it.key) }
        val translations = use(TranslationsContext)
        Dialog {
            this.open = props.open
            DialogTitle {
                +props.feature.name
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    this.dangerouslySetInnerHTML = toDangerousHtml(props.feature.description)
                }
                ValidatorForm {
                    this.onSubmit = {
                        props.setOpen(false)
                        props.setValue(chosenFeature)
                    }
                    ValidatedList {
                        this.label = ReactNode(props.feature.name)
                        this.value = chosenFeature
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: "")
                        this.onChange = { setChosenFeature(it.value()) }
                        this.useDescription = true
                        this.menuItems = features
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

val FeatChooser = FC<FeatsProps>("FeatChooser") { props ->
    val (feat, setFeat) = useState(emptyFeat())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    if (props.open) {
        val translations = use(TranslationsContext)
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
                        this.label = ReactNode(props.feature.name)
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
        this.translations = translations
    }
}

val ManeuverChooser = FC<FeatureProps<String>>("ManeuverChooser") { props ->
    val (maneuver, setManeuver) = useState(emptyManeuver())
    val (description, setDescription) = useState("")
    val maneuvers = use(ManeuversContext)
    val translations = use(TranslationsContext)
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
                        this.label = ReactNode(props.feature.name)
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

val SpellsChooser = FC<SpellsFeatureProps>("SpellsChooser") { props ->
    val translations = use(TranslationsContext)
    val (chosenSpells, setChosenSpells) = useState(
        if (props.isAdditionalSpells) emptyList()
        else props.character.spells.map { it._id })
    val (spellsToRemove, setSpellsToRemove) = useState(emptyList<String>())
    val (openAlert, setOpenAlert) = useState(false)
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_FEW_CHECKS_TRANSLATION]
    }
    if (props.open) {
        val values = props.function.values
        val hasSpellsLimit = values[1].isNumber()
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
                        if (hasSpellsLimit && chosenSpells.size < values[0].toInt()) {
                            setOpenAlert(true)
                        } else {
                            props.setValue(chosenSpells, spellsToRemove)
                            props.setOpen(false)
                        }
                    }
                    TableContainer {
                        SpellsTable {
                            this.character = props.character
                            this.function = props.function
                            this.isAdditionalSpells = props.isAdditionalSpells
                            this.setValue = { toAdd, toRemove ->
                                setChosenSpells(toAdd)
                                setSpellsToRemove(spellsToRemove + toRemove)
                            }
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

val SpellsTable = FC<SpellsFeatureProps>("SpellsTable") { props ->
    val translations = use(TranslationsContext)
    val spells = use(SpellsContext)
        .filter { (name, _) -> !props.character.additionalSpells.map { it._id }.contains(name) }
    val (openAlert, setOpenAlert) = useState(false)
    val values = props.function.values
    val hasSpellsLimit = values[1].isNumber()
    val levelIndex = if (hasSpellsLimit) 1 else 0
    val classesIndex = levelIndex + 1
    val maxLevelIndex = levelIndex + 2
    val includeCantripsIndex = levelIndex + 3
    val level = values[levelIndex].toInt()
    val classes = values[classesIndex].split(", ")
    val isMaxLevel = values.getOrNull(maxLevelIndex).toBoolean()
    val includeCantrips = values.getOrNull(includeCantripsIndex).toBoolean()
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
                val startIndex = if (includeCantrips) 0 else 1
                if (isMaxLevel) it.level == level else it.level in startIndex..level
            }.filter { spell -> classes.any { spell.classes.contains(it) } }
                .sortedWith(compareBy(Spell::level, Spell::_id))
            filteredSpells.forEach { spell ->
                val (open, setOpen) = useState(false)
                TableRow {
                    TableCell {
                        val (checked, setChecked) = useState(props.value.contains(spell._id))
                        Checkbox {
                            this.checked = checked
                            this.onChange = { _, checked ->
                                playSound(BUTTON_SOUND_ID)
                                if (checked && hasSpellsLimit && props.value.size >= values[0].toInt()) {
                                    setOpenAlert(true)
                                    setChecked(false)
                                } else {
                                    setChecked(checked)
                                    props.setValue(
                                        if (checked) props.value + spell._id
                                        else props.value - spell._id,
                                        if (checked) emptyList()
                                        else listOf(spell._id)
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

val WeaponsChooser = FC<MultipleFeatureProps<List<Weapon>>>("WeaponsChooser") { props ->
    val translations = use(TranslationsContext)
    val (chosenWeapons, setChosenWeapons) = useState(emptyList<Weapon>())
    val (openAlert, setOpenAlert) = useState(false)
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_FEW_CHECKS_TRANSLATION]
    }
    if (props.open) {
        val values = props.function.values
        val minNumber = values[1].toInt()
        Dialog {
            this.open = props.open
            this.maxWidth = "lg"
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
                        if (chosenWeapons.size < minNumber) {
                            setOpenAlert(true)
                        } else {
                            props.setValue(chosenWeapons)
                            props.setOpen(false)
                        }
                    }
                    TableContainer {
                        WeaponsTable {
                            this.function = props.function
                            this.setValue = { setChosenWeapons(it) }
                            this.value = chosenWeapons
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

val ArmorsChooser = FC<MultipleFeatureProps<List<Armor>>>("ArmorsChooser") { props ->
    val translations = use(TranslationsContext)
    val (chosenArmors, setChosenArmors) = useState(emptyList<Armor>())
    val (openAlert, setOpenAlert) = useState(false)
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_FEW_CHECKS_TRANSLATION]
    }
    if (props.open) {
        val values = props.function.values
        val maxNumber = values[1].toInt()
        Dialog {
            this.open = props.open
            this.maxWidth = "lg"
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
                        if (chosenArmors.size < maxNumber) {
                            setOpenAlert(true)
                        } else {
                            props.setValue(chosenArmors)
                            props.setOpen(false)
                        }
                    }
                    TableContainer {
                        ArmorsTable {
                            this.function = props.function
                            this.setValue = { setChosenArmors(it) }
                            this.value = chosenArmors
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

val EquipmentsChooser = FC<MultipleFeatureProps<List<SimpleEquipment>>>("EquipmentsChooser") { props ->
    val translations = use(TranslationsContext)
    val (chosenEquipment, setChosenEquipment) = useState(emptyList<SimpleEquipment>())
    val (openAlert, setOpenAlert) = useState(false)
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_FEW_CHECKS_TRANSLATION]
    }
    if (props.open) {
        val values = props.function.values
        val maxNumber = values[1].toInt()
        Dialog {
            this.open = props.open
            this.maxWidth = "lg"
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
                        if (chosenEquipment.size < maxNumber) {
                            setOpenAlert(true)
                        } else {
                            props.setValue(chosenEquipment)
                            props.setOpen(false)
                        }
                    }
                    TableContainer {
                        EquipmentsTable {
                            this.function = props.function
                            this.setValue = { setChosenEquipment(it) }
                            this.value = chosenEquipment
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

val EquipmentPackChooser = FC<FeatureProps<Pair<String, List<SimpleEquipment>>>>("EquipmentPackChooser") { props ->
    val translations = use(TranslationsContext)
    val (chosenEquipment, setChosenEquipment) = useState("")
    val (description, setDescription) = useState("")
    val (openAlert, setOpenAlert) = useState(false)
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_FEW_CHECKS_TRANSLATION]
    }
    if (props.open) {
        val values = props.function.values.subList(1, props.function.values.size)
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
                        val equipmentsIndex = values.indexOf(chosenEquipment) + 2
                        val equipments = Json.decodeFromString<List<SimpleEquipment>>(values[equipmentsIndex])
                        props.setOpen(false)
                        props.setValue(Pair(chosenEquipment, equipments))
                    }
                    ValidatedList {
                        val equipmentPacks = values.filterIndexed { index, _ -> (index + 1) % 3 != 0 }
                            .chunked(2) { it[0] to it[1] }.toMap()
                        this.label = ReactNode(props.feature.name)
                        this.value = chosenEquipment
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: "")
                        this.onChange = { setChosenEquipment(it.value()) }
                        this.useDescription = true
                        this.menuItems = equipmentPacks
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

val WeaponsTable = FC<MultipleFeatureProps<List<Weapon>>>("WeaponsTable") { props ->
    val translations = use(TranslationsContext)
    val (openAlert, setOpenAlert) = useState(false)
    val (openTextAlert, setOpenTextAlert) = useState(false)
    val values = props.function.values
    val allowManual = values[0].toBoolean()
    val maxNumber = values[2].toInt()
    val weapons = Json.decodeFromString<List<Weapon>>(values[4])
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_MANY_CHECKS_TRANSLATION]
    }
    AlertDialog {
        this.open = openTextAlert
        this.action = { setOpenTextAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[CELLS_SHOULD_BE_FILLED_TRANSLATION]
    }
    Table {
        this.stickyHeader = true
        TableHead {
            TableRow {
                TableCell()
                TableCell { +translations[NAME_TRANSLATION] }
                TableCell { +translations[DESCRIPTION_TRANSLATION] }
                TableCell { +translations[TYPE_TRANSLATION] }
                TableCell { +translations[PRICE_TRANSLATION] }
                TableCell { +translations[DAMAGE_TRANSLATION] }
                TableCell { +translations[WEIGHT_TRANSLATION] }
                TableCell { +translations[PROPERTIES_TRANSLATION] }
            }
        }
        TableBody {
            weapons.forEach { weapon ->
                TableRow {
                    TableCell {
                        val (checked, setChecked) = useState(false)
                        Checkbox {
                            this.checked = checked
                            this.onChange = { _, checked ->
                                playSound(BUTTON_SOUND_ID)
                                if (checked && props.value.size >= maxNumber) {
                                    setOpenAlert(true)
                                    setChecked(false)
                                } else {
                                    setChecked(checked)
                                    props.setValue(
                                        if (checked) props.value + weapon
                                        else props.value - weapon
                                    )
                                    console.info("${weapon._id} is $checked")
                                }
                            }
                        }
                    }
                    TableCell { +weapon._id }
                    TableCell { this.dangerouslySetInnerHTML = toDangerousHtml(weapon.description) }
                    TableCell { +weapon.type }
                    TableCell { +weapon.price }
                    TableCell { +weapon.damage }
                    TableCell { +weapon.weight }
                    TableCell { +weapon.properties }
                }
            }
            if (allowManual) {
                for (i in 0 until maxNumber) {
                    TableRow {
                        val (checked, setChecked) = useState(false)
                        val (weapon, setWeapon) = useState(emptyWeapon())
                        TableCell {
                            Checkbox {
                                this.checked = checked
                                this.onChange = { _, checked ->
                                    playSound(BUTTON_SOUND_ID)
                                    if (checked && props.value.size >= maxNumber) {
                                        setOpenAlert(true)
                                        setChecked(false)
                                    } else if (checked && !validateWeapon(weapon)) {
                                        setOpenTextAlert(true)
                                        setChecked(false)
                                    } else {
                                        setChecked(checked)
                                        props.setValue(
                                            if (checked) props.value + weapon
                                            else props.value - weapon
                                        )
                                        console.info("${weapon._id} is $checked")
                                    }
                                }
                            }
                        }
                        EditableCell {
                            this.value = weapon._id
                            this.onChange = { event ->
                                weapon._id = event.value()
                                setWeapon(weapon)
                                setChecked(false)
                                props.setValue(props.value - weapon)
                            }
                        }
                        EditableCell {
                            this.value = weapon.description
                            this.onChange = { event ->
                                weapon.description = event.value()
                                setWeapon(weapon)
                                setChecked(false)
                                props.setValue(props.value - weapon)
                            }
                        }
                        EditableCell {
                            this.value = weapon.type
                            this.onChange = { event ->
                                weapon.type = event.value()
                                setWeapon(weapon)
                                setChecked(false)
                                props.setValue(props.value - weapon)
                            }
                        }
                        EditableCell {
                            this.value = weapon.price
                            this.onChange = { event ->
                                weapon.price = event.value()
                                setWeapon(weapon)
                                setChecked(false)
                                props.setValue(props.value - weapon)
                            }
                        }
                        EditableCell {
                            this.value = weapon.damage
                            this.onChange = { event ->
                                weapon.damage = event.value()
                                setWeapon(weapon)
                                setChecked(false)
                                props.setValue(props.value - weapon)
                            }
                        }
                        EditableCell {
                            this.value = weapon.weight
                            this.onChange = { event ->
                                weapon.weight = event.value()
                                setWeapon(weapon)
                                setChecked(false)
                                props.setValue(props.value - weapon)
                            }
                        }
                        EditableCell {
                            this.value = weapon.properties
                            this.onChange = { event ->
                                weapon.properties = event.value()
                                setWeapon(weapon)
                                setChecked(false)
                                props.setValue(props.value - weapon)
                            }
                        }
                    }
                }
            }
        }
    }
}

val ArmorsTable = FC<MultipleFeatureProps<List<Armor>>>("ArmorsTable") { props ->
    val translations = use(TranslationsContext)
    val (openAlert, setOpenAlert) = useState(false)
    val (openTextAlert, setOpenTextAlert) = useState(false)
    val values = props.function.values
    val allowManual = values[0].toBoolean()
    val maxNumber = values[1].toInt()
    val armors = Json.decodeFromString<List<Armor>>(values[3])
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_MANY_CHECKS_TRANSLATION]
    }
    AlertDialog {
        this.open = openTextAlert
        this.action = { setOpenTextAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[CELLS_SHOULD_BE_FILLED_TRANSLATION]
    }
    Table {
        this.stickyHeader = true
        TableHead {
            TableRow {
                TableCell()
                TableCell { +translations[NAME_TRANSLATION] }
                TableCell { +translations[DESCRIPTION_TRANSLATION] }
                TableCell { +translations[TYPE_TRANSLATION] }
                TableCell { +translations[PRICE_TRANSLATION] }
                TableCell { +translations[ARMOR_CLASS_TRANSLATION] }
                TableCell { +translations[STRENGTH_TRANSLATION] }
                TableCell { +translations[STEALTH_TRANSLATION] }
                TableCell { +translations[WEIGHT_TRANSLATION] }
            }
        }
        TableBody {
            armors.forEach { armor ->
                TableRow {
                    TableCell {
                        val (checked, setChecked) = useState(false)
                        Checkbox {
                            this.checked = checked
                            this.onChange = { _, checked ->
                                playSound(BUTTON_SOUND_ID)
                                if (checked && props.value.size >= maxNumber) {
                                    setOpenAlert(true)
                                    setChecked(false)
                                } else {
                                    setChecked(checked)
                                    props.setValue(
                                        if (checked) props.value + armor
                                        else props.value - armor
                                    )
                                    console.info("${armor._id} is $checked")
                                }
                            }
                        }
                    }
                    TableCell { +armor._id }
                    TableCell { this.dangerouslySetInnerHTML = toDangerousHtml(armor.description) }
                    TableCell { +armor.type }
                    TableCell { +armor.price }
                    TableCell { +armor.armorClass }
                    TableCell { +armor.strength.toString() }
                    TableCell { +armor.stealth }
                    TableCell { +armor.weight }
                }
            }
            if (allowManual) {
                for (i in 0 until maxNumber) {
                    TableRow {
                        val (checked, setChecked) = useState(false)
                        val (armor, setArmor) = useState(emptyArmor())
                        TableCell {
                            Checkbox {
                                this.checked = checked
                                this.onChange = { _, checked ->
                                    playSound(BUTTON_SOUND_ID)
                                    if (checked && props.value.size >= maxNumber) {
                                        setOpenAlert(true)
                                        setChecked(false)
                                    } else if (checked && !validateArmor(armor)) {
                                        setOpenTextAlert(true)
                                        setChecked(false)
                                    } else {
                                        setChecked(checked)
                                        props.setValue(
                                            if (checked) props.value + armor
                                            else props.value - armor
                                        )
                                        console.info("${armor._id} is $checked")
                                    }
                                }
                            }
                        }
                        EditableCell {
                            this.value = armor._id
                            this.onChange = { event ->
                                armor._id = event.value()
                                setArmor(armor)
                                setChecked(false)
                                props.setValue(props.value - armor)
                            }
                        }
                        EditableCell {
                            this.value = armor.description
                            this.onChange = { event ->
                                armor.description = event.value()
                                setArmor(armor)
                                setChecked(false)
                                props.setValue(props.value - armor)
                            }
                        }
                        EditableCell {
                            this.value = armor.type
                            this.onChange = { event ->
                                armor.type = event.value()
                                setArmor(armor)
                                setChecked(false)
                                props.setValue(props.value - armor)
                            }
                        }
                        EditableCell {
                            this.value = armor.price
                            this.onChange = { event ->
                                armor.price = event.value()
                                setArmor(armor)
                                setChecked(false)
                                props.setValue(props.value - armor)
                            }
                        }
                        EditableCell {
                            this.value = armor.armorClass
                            this.onChange = { event ->
                                armor.armorClass = event.value()
                                setArmor(armor)
                                setChecked(false)
                                props.setValue(props.value - armor)
                            }
                        }
                        EditableCell {
                            this.value = armor.strength
                            this.onChange = { event ->
                                armor.strength = event.value().toInt()
                                setArmor(armor)
                                setChecked(false)
                                props.setValue(props.value - armor)
                            }
                        }
                        EditableCell {
                            this.value = armor.stealth
                            this.onChange = { event ->
                                armor.stealth = event.value()
                                setArmor(armor)
                                setChecked(false)
                                props.setValue(props.value - armor)
                            }
                        }
                        EditableCell {
                            this.value = armor.weight
                            this.onChange = { event ->
                                armor.weight = event.value()
                                setArmor(armor)
                                setChecked(false)
                                props.setValue(props.value - armor)
                            }
                        }
                    }
                }
            }
        }
    }
}

val EquipmentsTable = FC<MultipleFeatureProps<List<SimpleEquipment>>>("EquipmentsTable") { props ->
    val translations = use(TranslationsContext)
    val (openAlert, setOpenAlert) = useState(false)
    val (openTextAlert, setOpenTextAlert) = useState(false)
    val values = props.function.values
    val allowManual = values[0].toBoolean()
    val maxNumber = values[1].toInt()
    val equipments = Json.decodeFromString<List<SimpleEquipment>>(values[3])
    AlertDialog {
        this.open = openAlert
        this.action = { setOpenAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[TOO_MANY_CHECKS_TRANSLATION]
    }
    AlertDialog {
        this.open = openTextAlert
        this.action = { setOpenTextAlert(false) }
        this.header = translations[ALERT_TRANSLATION] ?: ""
        +translations[CELLS_SHOULD_BE_FILLED_TRANSLATION]
    }
    Table {
        this.stickyHeader = true
        TableHead {
            TableRow {
                TableCell()
                TableCell { +translations[NAME_TRANSLATION] }
                TableCell { +translations[DESCRIPTION_TRANSLATION] }
                TableCell { +translations[TYPE_TRANSLATION] }
                TableCell { +translations[PRICE_TRANSLATION] }
                TableCell { +translations[WEIGHT_TRANSLATION] }
            }
        }
        TableBody {
            equipments.forEach { equipment ->
                TableRow {
                    TableCell {
                        val (checked, setChecked) = useState(false)
                        Checkbox {
                            this.checked = checked
                            this.onChange = { _, checked ->
                                playSound(BUTTON_SOUND_ID)
                                if (checked && props.value.size >= maxNumber) {
                                    setOpenAlert(true)
                                    setChecked(false)
                                } else {
                                    setChecked(checked)
                                    props.setValue(
                                        if (checked) props.value + equipment
                                        else props.value - equipment
                                    )
                                    console.info("${equipment._id} is $checked")
                                }
                            }
                        }
                    }
                    TableCell { +equipment._id }
                    TableCell { this.dangerouslySetInnerHTML = toDangerousHtml(equipment.description) }
                    TableCell { +equipment.type }
                    TableCell { +equipment.price }
                    TableCell { +equipment.weight }
                }
            }
            if (allowManual) {
                for (i in 0 until maxNumber) {
                    TableRow {
                        val (checked, setChecked) = useState(false)
                        val (equipment, setEquipment) = useState(emptySimpleEquipment())
                        TableCell {
                            Checkbox {
                                this.checked = checked
                                this.onChange = { _, checked ->
                                    playSound(BUTTON_SOUND_ID)
                                    if (checked && props.value.size >= maxNumber) {
                                        setOpenAlert(true)
                                        setChecked(false)
                                    } else if (checked && !validateSimpleEquipment(equipment)) {
                                        setOpenTextAlert(true)
                                        setChecked(false)
                                    } else {
                                        setChecked(checked)
                                        props.setValue(
                                            if (checked) props.value + equipment
                                            else props.value - equipment
                                        )
                                        console.info("${equipment._id} is $checked")
                                    }
                                }
                            }
                        }
                        EditableCell {
                            this.value = equipment._id
                            this.onChange = { event ->
                                equipment._id = event.value()
                                setEquipment(equipment)
                                setChecked(false)
                                props.setValue(props.value - equipment)
                            }
                        }
                        EditableCell {
                            this.value = equipment.description
                            this.onChange = { event ->
                                equipment.description = event.value()
                                setEquipment(equipment)
                                setChecked(false)
                                props.setValue(props.value - equipment)
                            }
                        }
                        EditableCell {
                            this.value = equipment.type ?: ""
                            this.onChange = { event ->
                                if (event.value().isNotBlank()) equipment.type = event.value()
                                else equipment.type = null
                                setEquipment(equipment)
                                setChecked(false)
                                props.setValue(props.value - equipment)
                            }
                        }
                        EditableCell {
                            this.value = equipment.price
                            this.onChange = { event ->
                                equipment.price = event.value()
                                setEquipment(equipment)
                                setChecked(false)
                                props.setValue(props.value - equipment)
                            }
                        }
                        EditableCell {
                            this.value = equipment.weight
                            this.onChange = { event ->
                                equipment.weight = event.value()
                                setEquipment(equipment)
                                setChecked(false)
                                props.setValue(props.value - equipment)
                            }
                        }
                    }
                }
            }
        }
    }
}

val EditableCell = FC<TextValidatorProps>("EditableCell") { props ->
    val (value, setValue) = useState(props.value)
    TableCell {
        TextValidator {
            this.value = value
            this.validators = emptyArray()
            this.errorMessages = emptyArray()
            this.onChange = { event ->
                setValue(event.value())
                props.onChange(event)
            }
        }
    }
}

private fun computeSkillsValues(
    character: Character,
    values: List<String>
): List<String> {
    if (values[3] == PROFICIENT) {
        return character.skills.filter { it.proficient }.map { it.name }
    }
    return values.subList(3, values.size).filter { !character.isProficient(it) }
}
