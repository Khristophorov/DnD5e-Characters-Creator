package me.khrys.dnd.charcreator.client.components.dialogs.windows

import emotion.react.css
import me.khrys.dnd.charcreator.client.SpellsContext
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.allSpells
import me.khrys.dnd.charcreator.client.applyFeatures
import me.khrys.dnd.charcreator.client.components.buttons.Button
import me.khrys.dnd.charcreator.client.components.buttons.ButtonAction
import me.khrys.dnd.charcreator.client.components.buttons.CloseButton
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.dialogs.FeatsProps
import me.khrys.dnd.charcreator.client.components.dialogs.LevelUp
import me.khrys.dnd.charcreator.client.components.dialogs.grids.AbilitiesGrid
import me.khrys.dnd.charcreator.client.components.dialogs.grids.SavingThrowsGrid
import me.khrys.dnd.charcreator.client.components.dialogs.grids.SkillsGrid
import me.khrys.dnd.charcreator.client.components.inputs.CenteredBold
import me.khrys.dnd.charcreator.client.components.inputs.OneValueInput
import me.khrys.dnd.charcreator.client.components.inputs.texts.CenteredLabel
import me.khrys.dnd.charcreator.client.components.inputs.texts.TextBox
import me.khrys.dnd.charcreator.client.components.inputs.texts.TextWithTooltip
import me.khrys.dnd.charcreator.client.components.inputs.texts.WrappedText
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.computeArmorClass
import me.khrys.dnd.charcreator.client.computeAttackBonus
import me.khrys.dnd.charcreator.client.computeAttackModifier
import me.khrys.dnd.charcreator.client.computePassiveSkill
import me.khrys.dnd.charcreator.client.computeProficiencyBonus
import me.khrys.dnd.charcreator.client.computeSpellLevel
import me.khrys.dnd.charcreator.client.getCombinedLevel
import me.khrys.dnd.charcreator.client.getInitiative
import me.khrys.dnd.charcreator.client.isNotMaximumLevel
import me.khrys.dnd.charcreator.client.toDangerousHtml
import me.khrys.dnd.charcreator.client.toSignedString
import me.khrys.dnd.charcreator.client.utils.loadClasses
import me.khrys.dnd.charcreator.common.ARMOR_CLASS_TRANSLATION
import me.khrys.dnd.charcreator.common.ARMOR_TRANSLATION
import me.khrys.dnd.charcreator.common.ATTACK_BONUS_TRANSLATION
import me.khrys.dnd.charcreator.common.CANTRIP_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASSES_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_ABILITY_BOX
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_FEATURES_WIDTH
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_ITEMS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CLASS_NO_PADDINGS
import me.khrys.dnd.charcreator.common.CLASS_PADDINGS
import me.khrys.dnd.charcreator.common.CLASS_WIDE_ABILITY_BOX
import me.khrys.dnd.charcreator.common.DAMAGE_TYPE_TRANSLATION
import me.khrys.dnd.charcreator.common.DICE_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_RACE_TRANSLATION
import me.khrys.dnd.charcreator.common.EQUIPMENT_TRANSLATION
import me.khrys.dnd.charcreator.common.FEATURES_TRANSLATION
import me.khrys.dnd.charcreator.common.HIT_DICE_TRANSLATION
import me.khrys.dnd.charcreator.common.HIT_POINTS_TRANSLATION
import me.khrys.dnd.charcreator.common.INITIATIVE_TRANSLATION
import me.khrys.dnd.charcreator.common.INVESTIGATION_TRANSLATION
import me.khrys.dnd.charcreator.common.LANGUAGES_TRANSLATION
import me.khrys.dnd.charcreator.common.LEVEL_UP_TRANSLATION
import me.khrys.dnd.charcreator.common.MANEUVERS_TRANSLATION
import me.khrys.dnd.charcreator.common.NAME_TRANSLATION
import me.khrys.dnd.charcreator.common.PASSIVE_INVESTIGATION_TRANSLATION
import me.khrys.dnd.charcreator.common.PASSIVE_PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCIES_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCY_BONUS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCY_BONUS_TRANSLATION
import me.khrys.dnd.charcreator.common.QUANTITY_TRANSLATION
import me.khrys.dnd.charcreator.common.SAVE_TRANSLATION
import me.khrys.dnd.charcreator.common.SPEED_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELLCASTING_ABILITIES_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELLS_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_ATTACK_BONUS_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_LEVEL_SUFFIX_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_SAVE_DC_TRANSLATION
import me.khrys.dnd.charcreator.common.SUPERIORITY_DICES_TRANSLATION
import me.khrys.dnd.charcreator.common.TYPE_TRANSLATION
import me.khrys.dnd.charcreator.common.WEAPONS_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Class
import me.khrys.dnd.charcreator.common.models.Maneuver
import me.khrys.dnd.charcreator.common.models.Spell
import me.khrys.dnd.charcreator.common.models.SuperiorityDice
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.Box
import mui.material.CircularProgress
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogTitle
import mui.material.Grid
import mui.material.Tab
import mui.material.Table
import mui.material.TableBody
import mui.material.TableCell
import mui.material.TableContainer
import mui.material.TableHead
import mui.material.TableRow
import mui.material.Tabs
import mui.system.Breakpoint.Companion.xl
import react.FC
import react.Props
import react.PropsWithChildren
import react.ReactNode
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.strong
import react.useContext
import react.useState
import web.cssom.ClassName
import web.cssom.ObjectFit
import web.cssom.px
import web.html.InputType.Companion.number
import web.html.InputType.Companion.text

private const val MANEUVERS_INDEX = 0
private const val SPELLS_INDEX = 1

private external interface CharAbilitiesProps : Props {
    var value: String
    var translations: Map<String, String>
}

private external interface SuperiorDicesProps : Props {
    var translations: Map<String, String>
    var superiorityDices: List<SuperiorityDice>
}

private external interface ParametersProps : Props {
    var character: Character
    var translations: Map<String, String>
    var proficiencyBonus: Int
    var classes: Map<String, Class>
}

private external interface MultiValueProps : Props {
    var translations: Map<String, String>
    var values: Collection<String>
}

private external interface CharPropsWithValue : CharBasedProps {
    var value: Int
    var setValue: (Int) -> Unit
}

private external interface TitleProps : Props {
    var character: Character
    var translations: Map<String, String>
    var open: Boolean
    var setOpen: (Boolean) -> Unit
    var action: ButtonAction
}

private external interface ManeuversProps : Props {
    var maneuvers: List<Maneuver>
}

private external interface SpellsProps : Props {
    var spells: List<Spell>
}

val CharacterWindow = FC<FeatsProps>("CharacterWindow") { props ->
    console.info("Loading character window for ${props.character.name}")
    val translations = useContext(TranslationsContext)
    val spells = useContext(SpellsContext)
    val (openLevelUp, setOpenLevelUp) = useState(false)
    val (classes, setClasses) = useState(emptyMap<String, Class>())
    if (props.open && classes.isEmpty()) {
        CircularProgress()
        loadClasses { setClasses(it) }
    } else if (props.open) {
        Dialog {
            this.open = props.open
            this.fullScreen = true
            this.maxWidth = xl
            val character = applyFeatures(props.character, translations, spells)
            Title {
                this.open = props.open
                this.setOpen = props.setOpen
                this.character = character
                this.translations = translations
                this.action = { setOpenLevelUp(true) }
            }

            if (openLevelUp) {
                LevelUp {
                    this.open = openLevelUp
                    this.character = props.character
                    this.setOpen = { setOpenLevelUp(it) }
                }
            }

            DialogContent {
                this.dividers = true
                val proficiencyBonus = computeProficiencyBonus(props.character.getCombinedLevel())
                ValidatorForm {
                    this.onSubmit = {
                        console.info("Submitting character ${props.character.name}")
                        if (props.open) {
                            props.setOpen(false)
                        }
                    }
                    Grid {
                        this.container = true
                        MainParameters {
                            this.character = character
                            this.translations = translations
                            this.proficiencyBonus = proficiencyBonus
                        }
                        AdditionalAbilities {
                            this.character = character
                            this.translations = translations
                            this.proficiencyBonus = proficiencyBonus
                            this.classes = classes
                        }
                        Features {
                            this.character = character
                            this.translations = translations
                        }
                        SpecificParameters {
                            this.character = character
                            this.translations = translations
                        }
                    }
                    DialogActions {
                        Submit {
                            +(translations[SAVE_TRANSLATION] ?: "")
                        }
                    }
                }
            }
        }
    }
}

private val SpecificParameters = FC<ParametersProps>("SpecificParameters") { props ->
    val showTabs = shouldShowTabs(props.character)
    Grid {
        this.item = true
        this.className = ClassName(CLASS_PADDINGS)
        Image {
            +props.character.image
        }
        if (showTabs) {
            val (tabValue, setTabValue) = useState(computeTabValue(props.character))

            div {
                css {
                    maxWidth = 300.px
                }
                AdditionalTabs {
                    this.value = tabValue
                    this.setValue = { setTabValue(it) }
                    this.translations = props.translations
                    this.character = props.character
                }
                TabBoxes {
                    this.value = tabValue
                    this.character = props.character
                    this.translations = props.translations
                }
            }
        }
    }
}

private val AdditionalTabs = FC<CharPropsWithValue>("AdditionalTabs") { props ->
    Tabs {
        this.value = props.value
        this.onChange = { _, newTabValue -> props.setValue(newTabValue as Int) }
        if (props.character.maneuvers.isNotEmpty()) {
            Tab {
                this.label = ReactNode(props.translations[MANEUVERS_TRANSLATION])
                this.value = MANEUVERS_INDEX
            }
        }
        if (props.character.allSpells().isNotEmpty()) {
            Tab {
                this.label = ReactNode(props.translations[SPELLS_TRANSLATION])
                this.value = SPELLS_INDEX
            }
        }
    }
}

private val TabBoxes = FC<CharPropsWithValue>("TabBoxes") { props ->
    val character = props.character
    Box {
        if (props.value == MANEUVERS_INDEX) {
            SuperiorityDices {
                this.translations = props.translations
                this.superiorityDices = props.character.superiorityDices
            }
            ManeuversBox {
                this.maneuvers = character.maneuvers
            }
        }
    }
    Box {
        if (props.value == SPELLS_INDEX) {
            if (character.spellcastingAbilities.isNotEmpty()) {
                SpellcastingProperties {
                    this.character = character
                    this.translations = props.translations
                }
            }
            SpellsBox {
                this.spells = character.allSpells()
            }
        }
    }
}

private val ManeuversBox = FC<ManeuversProps>("ManeuversBox") { props ->
    Grid {
        this.container = true
        Grid {
            this.item = true
            props.maneuvers.forEach { maneuver ->
                Accordion {
                    AccordionSummary {
                        +maneuver._id
                    }
                    AccordionDetails {
                        this.dangerouslySetInnerHTML = toDangerousHtml(maneuver.description)
                    }
                }
            }
        }
    }
}

private val SuperiorityDices = FC<SuperiorDicesProps>("SuperiorityDices") { props ->
    Grid {
        this.container = true
        Grid {
            this.item = true
            this.className = ClassName(CLASS_BORDERED)
            SuperiorDices {
                this.translations = props.translations
                this.superiorityDices = props.superiorityDices
            }
            CenteredBold {
                +(props.translations[SUPERIORITY_DICES_TRANSLATION] ?: "")
            }
        }
    }
}

private val SpellsBox = FC<SpellsProps> { props ->
    val translations = useContext(TranslationsContext)
    Grid {
        this.container = true
        Grid {
            this.item = true
            props.spells.sortedBy { it.level }.forEach { spell ->
                Accordion {
                    AccordionSummary {
                        val spellLevel = computeSpellLevel(
                            spell.level,
                            translations[CANTRIP_TRANSLATION] ?: "",
                            translations[SPELL_LEVEL_SUFFIX_TRANSLATION]
                        )
                        +"$spellLevel ${spell._id}"
                    }
                    AccordionDetails {
                        SpellWindow {
                            this.spell = spell
                        }
                    }
                }
            }
        }
    }
}

private val SpellcastingProperties = FC<CharBasedProps> { props ->
    Grid {
        this.container = true
        Grid {
            this.item = true
            this.className = ClassName(CLASS_BORDERED)
            SpellcastingAbilities {
                this.value = props.character.spellcastingAbilities.joinToString(", ")
                this.translations = props.translations
            }
            SpellSaveDC {
                this.value = props.character.spellSaveDC.toString()
                this.translations = props.translations
            }
            SpellAttackBonus {
                this.value = props.character.spellAttackBonus.toString()
                this.translations = props.translations
            }
        }
    }
}

private val Image = FC<PropsWithChildren> { props ->
    Grid {
        this.container = true
        Grid {
            this.item = true
            img {
                this.src = props.children.toString()
                css {
                    width = 128.px
                    height = 128.px
                    objectFit = ObjectFit.contain
                }
            }
        }
    }
}

private val Features = FC<ParametersProps> { props ->
    Grid {
        this.item = true
        Grid {
            this.container = true
            Grid {
                this.item = true
                div {
                    this.className = ClassName("$CLASS_BORDERED $CLASS_FEATURES_WIDTH")
                    props.character.features.forEach { feature ->
                        TextWithTooltip {
                            this.title = ReactNode(feature.description)
                            +feature.name
                        }
                    }
                    CenteredBold {
                        +(props.translations[FEATURES_TRANSLATION] ?: "")
                    }
                }
            }
        }
    }
}

private val AdditionalAbilities = FC<ParametersProps> { props ->
    Grid {
        this.item = true
        Grid {
            this.container = true
            Grid {
                this.item = true
                this.className = ClassName(CLASS_PADDINGS)
                div {
                    this.className = ClassName(CLASS_INLINE)
                    ArmorClass {
                        this.value = props.character.computeArmorClass().toString()
                        this.translations = props.translations
                    }
                    Initiative {
                        this.value = props.character.getInitiative().toString()
                        this.translations = props.translations
                    }
                    Speed {
                        this.value = props.character.speed.toString()
                        this.translations = props.translations
                    }
                }
                div {
                    this.className = ClassName(CLASS_INLINE)
                    HitPoints {
                        this.value = props.character.hitPoints.toString()
                        this.translations = props.translations
                    }
                }
                div {
                    this.className = ClassName(CLASS_INLINE)
                    HitDice {
                        this.character = props.character
                        this.translations = props.translations
                        this.classes = props.classes
                    }
                }
                Accordion {
                    AccordionSummary {
                        +props.translations[EQUIPMENT_TRANSLATION]
                    }
                    AccordionDetails {
                        this.className = ClassName(CLASS_NO_PADDINGS)
                        div {
                            this.className = ClassName(CLASS_INLINE)
                            Weapons {
                                this.character = props.character
                                this.translations = props.translations
                                this.proficiencyBonus = props.proficiencyBonus
                            }
                        }
                        div {
                            this.className = ClassName(CLASS_INLINE)
                            Armor {
                                this.character = props.character
                                this.translations = props.translations
                            }
                        }
                        div {
                            this.className = ClassName(CLASS_INLINE)
                            Equipment {
                                this.character = props.character
                                this.translations = props.translations
                            }
                        }
                    }
                }
            }
        }
    }
}

private val Speed = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[SPEED_TRANSLATION] ?: ""
        this.type = number
        this.classes = CLASS_ABILITY_BOX
    }
}

private val Initiative = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[INITIATIVE_TRANSLATION] ?: ""
        this.type = number
        this.classes = CLASS_ABILITY_BOX
    }
}

private val ArmorClass = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[ARMOR_CLASS_TRANSLATION] ?: ""
        this.type = number
        this.classes = CLASS_ABILITY_BOX
    }
}

private val HitPoints = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[HIT_POINTS_TRANSLATION] ?: ""
        this.type = number
        this.classes = CLASS_WIDE_ABILITY_BOX
    }
}

private val HitDice = FC<CharBasedProps> { props ->
    div {
        className = ClassName("$CLASS_WIDE_ABILITY_BOX $CLASS_BORDERED $CLASS_CENTER")
        props.character.classes.map { it.key }.forEach { className ->
            TextBox {
                this.value = props.classes[className]?.hitDice?.name ?: ""
                this.label = className
                this.type = text
                this.classes = CLASS_ABILITY_BOX
            }
        }
        CenteredLabel {
            this.label = props.translations[HIT_DICE_TRANSLATION] ?: ""
        }
    }
}

private val SpellcastingAbilities = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[SPELLCASTING_ABILITIES_TRANSLATION] ?: ""
        this.type = text
        this.classes = CLASS_WIDE_ABILITY_BOX
    }
}

private val SpellSaveDC = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[SPELL_SAVE_DC_TRANSLATION] ?: ""
        this.type = text
        this.classes = CLASS_WIDE_ABILITY_BOX
    }
}

private val SpellAttackBonus = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[SPELL_ATTACK_BONUS_TRANSLATION] ?: ""
        this.type = text
        this.classes = CLASS_WIDE_ABILITY_BOX
    }
}

private val Weapons = FC<ParametersProps> { props ->
    div {
        this.className = ClassName("$CLASS_WIDE_ABILITY_BOX $CLASS_BORDERED $CLASS_CENTER")
        val character = props.character
        TableContainer {
            Table {
                TableHead {
                    TableRow {
                        TableCell {
                            strong {
                                +(props.translations[NAME_TRANSLATION] ?: "")
                            }
                        }
                        TableCell {
                            strong {
                                +(props.translations[ATTACK_BONUS_TRANSLATION] ?: "")
                            }
                        }
                        TableCell {
                            strong {
                                +(props.translations[DAMAGE_TYPE_TRANSLATION] ?: "")
                            }
                        }
                    }
                }
                TableBody {
                    character.equipment.weapons.forEach {
                        TableRow {
                            TableCell {
                                TextWithTooltip {
                                    this.title = ReactNode(it.description)
                                    +it._id
                                }
                            }
                            TableCell {
                                +computeAttackBonus(
                                    character,
                                    props.proficiencyBonus,
                                    it,
                                    props.translations
                                ).toSignedString()
                            }
                            TableCell {
                                +"${it.damage}${
                                    computeAttackModifier(
                                        character.abilities,
                                        it.properties,
                                        props.translations
                                    ).toSignedString()
                                }"
                            }
                        }
                    }
                }
            }
        }
        CenteredLabel {
            this.label = props.translations[WEAPONS_TRANSLATION] ?: ""
        }
    }
}

private val Armor = FC<ParametersProps> { props ->
    div {
        this.className = ClassName("$CLASS_WIDE_ABILITY_BOX $CLASS_BORDERED $CLASS_CENTER")
        val character = props.character
        TableContainer {
            Table {
                TableHead {
                    TableRow {
                        TableCell {
                            strong {
                                +(props.translations[NAME_TRANSLATION] ?: "")
                            }
                        }
                        TableCell {
                            strong {
                                +(props.translations[ARMOR_CLASS_TRANSLATION] ?: "")
                            }
                        }
                        TableCell {
                            strong {
                                +(props.translations[TYPE_TRANSLATION] ?: "")
                            }
                        }
                    }
                }
                TableBody {
                    character.equipment.armors.forEach {
                        TableRow {
                            TableCell {
                                TextWithTooltip {
                                    this.title = ReactNode(it.description)
                                    +it._id
                                }
                            }
                            TableCell {
                                +it.armorClass
                            }
                            TableCell {
                                +it.type
                            }
                        }
                    }
                }
            }
        }
        CenteredLabel {
            this.label = props.translations[ARMOR_TRANSLATION] ?: ""
        }
    }
}

private val Equipment = FC<ParametersProps> { props ->
    div {
        this.className = ClassName("$CLASS_WIDE_ABILITY_BOX $CLASS_BORDERED $CLASS_CENTER")
        val character = props.character
        TableContainer {
            Table {
                TableHead {
                    TableRow {
                        TableCell {
                            strong {
                                +(props.translations[NAME_TRANSLATION] ?: "")
                            }
                        }
                        TableCell {
                            strong {
                                +(props.translations[TYPE_TRANSLATION] ?: "")
                            }
                        }
                    }
                }
                TableBody {
                    character.equipment.otherEquipment.forEach {
                        TableRow {
                            TableCell {
                                TextWithTooltip {
                                    this.title = ReactNode(it.description)
                                    +it._id
                                }
                            }
                            TableCell {
                                +it.type
                            }
                        }
                    }
                }
            }
        }
        CenteredLabel {
            this.label = props.translations[EQUIPMENT_TRANSLATION] ?: ""
        }
    }
}

private val MainParameters = FC<ParametersProps> { props ->
    Grid {
        this.item = true
        MainSkills {
            this.character = props.character
            this.translations = props.translations
            this.proficiencyBonus = props.proficiencyBonus
        }
        PassivePerception {
            this.translations = props.translations
            this.character = props.character
            this.proficiencyBonus = props.proficiencyBonus
        }
        PassiveInvestigation {
            this.translations = props.translations
            this.character = props.character
            this.proficiencyBonus = props.proficiencyBonus
        }
        LanguageAndProficiencies {
            this.translations = props.translations
            this.character = props.character
        }
    }
}

private val LanguageAndProficiencies = FC<ParametersProps> { props ->
    Grid {
        this.item = true
        this.className = ClassName("$CLASS_PADDINGS $CLASS_BORDERED")
        Languages {
            this.translations = props.translations
            this.values = props.character.languages
        }
        Proficiencies {
            this.translations = props.translations
            this.values = props.character.proficiencies
        }
    }
}

private val Proficiencies = FC<MultiValueProps> { props ->
    WrappedText {
        this.label = props.translations[PROFICIENCIES_TRANSLATION] ?: ""
        this.values = props.values
    }
}

private val Languages = FC<MultiValueProps> { props ->
    WrappedText {
        this.label = props.translations[LANGUAGES_TRANSLATION] ?: ""
        this.values = props.values
    }
}

private val PassivePerception = FC<ParametersProps> { props ->
    Grid {
        this.item = true
        OneValueInput {
            this.header = props.translations[PASSIVE_PERCEPTION_TRANSLATION] ?: ""
            this.value = computePassiveSkill(
                ability = props.character.abilities.wisdom.value,
                proficiencyBonus = props.proficiencyBonus,
                proficient = props.character.skills
                    .first { it.name == props.translations[PERCEPTION_TRANSLATION] }.proficient,
                bonus = props.character.bonuses.perception
            ).toString()
        }
    }
}

private val PassiveInvestigation = FC<ParametersProps> { props ->
    Grid {
        this.item = true
        OneValueInput {
            this.header = props.translations[PASSIVE_INVESTIGATION_TRANSLATION] ?: ""
            this.value = computePassiveSkill(
                ability = props.character.abilities.intelligence.value,
                proficiencyBonus = props.proficiencyBonus,
                proficient = props.character.skills
                    .first { it.name == props.translations[INVESTIGATION_TRANSLATION] }.proficient,
                bonus = props.character.bonuses.investigation
            ).toString()
        }
    }
}

private val MainSkills = FC<ParametersProps> { props ->
    Grid {
        this.container = true
        Abilities {
            this.character = props.character
            this.translations = props.translations
        }
        Grid {
            this.item = true
            this.className = ClassName(CLASS_PADDINGS)
            ProficiencyBonus {
                this.translations = props.translations
                this.proficiencyBonus = props.proficiencyBonus
            }
            SavingThrowsGrid {
                this.character = props.character
                this.translations = props.translations
                this.proficiencyBonus = props.proficiencyBonus
            }
            SkillsGrid {
                this.character = props.character
                this.translations = props.translations
                this.proficiencyBonus = props.proficiencyBonus
            }
        }
    }
}

private val ProficiencyBonus = FC<ParametersProps> { props ->
    OneValueInput {
        this.header = props.translations[PROFICIENCY_BONUS_TRANSLATION] ?: ""
        this.value = props.proficiencyBonus.toString()
        this.title = props.translations[PROFICIENCY_BONUS_CONTENT_TRANSLATION] ?: ""
        this.isReadOnly = true
    }
}

private val Abilities = FC<ParametersProps> { props ->
    Grid {
        this.item = true
        this.className = ClassName(CLASS_PADDINGS)
        AbilitiesGrid {
            this.abilities = props.character.abilities
            this.translations = props.translations
            this.proficiencyBonus = props.proficiencyBonus
        }
    }
}

private val Title = FC<TitleProps> { props ->
    DialogTitle {
        div {
            this.className = ClassName("$CLASS_INLINE $CLASS_JUSTIFY_BETWEEN")
            +props.character.name
            WrappedText {
                this.label = props.translations[ENTER_RACE_TRANSLATION] ?: ""
                this.values = listOf(props.character.subrace ?: props.character.race)
            }
            WrappedText {
                this.label = props.translations[CLASSES_TRANSLATION] ?: ""
                this.values = props.character.classes.map { "${it.key} (${it.value})" }
            }
            if (props.character.isNotMaximumLevel()) {
                div {
                    this.className = ClassName("$CLASS_INLINE $CLASS_ITEMS_CENTER")
                    Button {
                        this.onClick = props.action
                        +props.translations[LEVEL_UP_TRANSLATION]
                    }
                }
            }
            CloseButton {
                this.onClick = {
                    if (props.open) {
                        props.setOpen(false)
                    }
                }
            }
        }
    }
}

private val SuperiorDices = FC<SuperiorDicesProps> { props ->
    TableContainer {
        Table {
            TableHead {
                TableRow {
                    TableCell {
                        p {
                            +(props.translations[DICE_TRANSLATION] ?: "")
                        }
                    }
                    TableCell {
                        p {
                            +(props.translations[QUANTITY_TRANSLATION] ?: "")
                        }
                    }
                }
            }
            TableBody {
                props.superiorityDices.forEach { superiorityDice ->
                    TableRow {
                        TableCell {
                            p {
                                +superiorityDice.dice.toString().lowercase()
                            }
                        }
                        TableCell {
                            p {
                                +superiorityDice.quantity.toString()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun shouldShowTabs(character: Character) = character.maneuvers.isNotEmpty() || character.allSpells().isNotEmpty()

fun computeTabValue(character: Character): Int {
    if (character.maneuvers.isNotEmpty()) {
        return MANEUVERS_INDEX
    }
    return SPELLS_INDEX
}
