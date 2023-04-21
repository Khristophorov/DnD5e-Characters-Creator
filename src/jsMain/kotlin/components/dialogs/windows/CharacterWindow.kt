package me.khrys.dnd.charcreator.client.components.dialogs.windows

import csstype.ClassName
import csstype.ObjectFit
import csstype.px
import emotion.react.css
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.applyFeatures
import me.khrys.dnd.charcreator.client.components.buttons.CloseButton
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.dialogs.CharDialogProps
import me.khrys.dnd.charcreator.client.components.dialogs.grids.AbilitiesGrid
import me.khrys.dnd.charcreator.client.components.dialogs.grids.AbilitiesProps
import me.khrys.dnd.charcreator.client.components.dialogs.grids.SavingThrowsGrid
import me.khrys.dnd.charcreator.client.components.dialogs.grids.SkillsGrid
import me.khrys.dnd.charcreator.client.components.inputs.CenteredBold
import me.khrys.dnd.charcreator.client.components.inputs.OneValueInput
import me.khrys.dnd.charcreator.client.components.inputs.texts.TextBox
import me.khrys.dnd.charcreator.client.components.inputs.texts.TextWithTooltip
import me.khrys.dnd.charcreator.client.components.inputs.texts.TitledInput
import me.khrys.dnd.charcreator.client.components.inputs.texts.WrappedText
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.computeArmorClass
import me.khrys.dnd.charcreator.client.computePassiveSkill
import me.khrys.dnd.charcreator.client.computeProficiencyBonus
import me.khrys.dnd.charcreator.client.computeSpellLevel
import me.khrys.dnd.charcreator.client.getInitiative
import me.khrys.dnd.charcreator.client.toDangerousHtml
import me.khrys.dnd.charcreator.common.ARMOR_CLASS_TRANSLATION
import me.khrys.dnd.charcreator.common.CANTRIP_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CLASS_PADDINGS
import me.khrys.dnd.charcreator.common.DICE_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_RACE_TRANSLATION
import me.khrys.dnd.charcreator.common.FEATURES_TRANSLATION
import me.khrys.dnd.charcreator.common.INITIATIVE_TRANSLATION
import me.khrys.dnd.charcreator.common.LANGUAGES_TRANSLATION
import me.khrys.dnd.charcreator.common.MANEUVERS_TRANSLATION
import me.khrys.dnd.charcreator.common.PASSIVE_INVESTIGATION_TRANSLATION
import me.khrys.dnd.charcreator.common.PASSIVE_PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCIES_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCY_BONUS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCY_BONUS_TRANSLATION
import me.khrys.dnd.charcreator.common.QUANTITY_TRANSLATION
import me.khrys.dnd.charcreator.common.SAVE_TRANSLATION
import me.khrys.dnd.charcreator.common.SPEED_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELLS_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_LEVEL_SUFFIX_TRANSLATION
import me.khrys.dnd.charcreator.common.SUPERIORITY_DICES_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Maneuver
import me.khrys.dnd.charcreator.common.models.Spell
import me.khrys.dnd.charcreator.common.models.SuperiorityDice
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.Box
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
import react.useContext
import react.useState

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
}

private external interface MultiValueProps : Props {
    var translations: Map<String, String>
    var values: List<String>
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
}

private external interface ManeuversProps : Props {
    var maneuvers: List<Maneuver>
}

private external interface SpellsProps : Props {
    var spells: List<Spell>
}

val CharacterWindow = FC<CharDialogProps> { props ->
    console.info("Loading character window for ${props.character.name}")
    val translations = useContext(TranslationsContext)
    Dialog {
        this.open = props.open
        this.fullScreen = true
        this.maxWidth = xl
        val character = applyFeatures(props.character, translations)
        Title {
            this.open = props.open
            this.setOpen = props.setOpen
            this.character = character
            this.translations = translations
        }

        DialogContent {
            this.dividers = true
            val proficiencyBonus = computeProficiencyBonus(1)
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

private val SpecificParameters = FC<AbilitiesProps> { props ->
    val showTabs = shouldShowTabs(props.character)
    Grid {
        this.item = true
        this.className = ClassName(CLASS_PADDINGS)
        Image {
            +props.character.image
        }
        if (showTabs) {
            val (tabValue, setTabValue) = useState(computeTabValue(props.character))

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

private val AdditionalTabs = FC<CharPropsWithValue> { props ->
    Tabs {
        this.value = props.value
        this.onChange = { _, newTabValue -> props.setValue(newTabValue as Int) }
        if (props.character.maneuvers.isNotEmpty()) {
            Tab {
                this.label = ReactNode(props.translations[MANEUVERS_TRANSLATION] ?: "")
                this.value = MANEUVERS_INDEX
            }
        }
        if (props.character.spells.isNotEmpty()) {
            Tab {
                this.label = ReactNode(props.translations[SPELLS_TRANSLATION] ?: "")
                this.value = SPELLS_INDEX
            }
        }
    }
}

private val TabBoxes = FC<CharPropsWithValue> { props ->
    Box {
        if (props.value == MANEUVERS_INDEX) {
            SuperiorityDices {
                this.translations = props.translations
                this.superiorityDices = props.character.superiorityDices
            }
            ManeuversBox {
                this.maneuvers = props.character.maneuvers
            }
        }
    }
    Box {
        if (props.value == SPELLS_INDEX) {
            SpellsBox {
                this.spells = props.character.spells
            }
        }
    }
}

private val ManeuversBox = FC<ManeuversProps> { props ->
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

private val SuperiorityDices = FC<SuperiorDicesProps> { props ->
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

private val Features = FC<AbilitiesProps> { props ->
    Grid {
        this.item = true
        Grid {
            this.container = true
            Grid {
                this.item = true
                div {
                    this.className = ClassName(CLASS_BORDERED)
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

private val AdditionalAbilities = FC<AbilitiesProps> { props ->
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
            }
        }
    }
}

private val Speed = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[SPEED_TRANSLATION] ?: ""
    }
}

private val Initiative = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[INITIATIVE_TRANSLATION] ?: ""
    }
}

private val ArmorClass = FC<CharAbilitiesProps> { props ->
    TextBox {
        this.value = props.value
        this.label = props.translations[ARMOR_CLASS_TRANSLATION] ?: ""
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
                ability = props.character.abilities.wisdom,
                proficiencyBonus = props.proficiencyBonus,
                proficient = props.character.skills.perception,
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
                ability = props.character.abilities.intelligence,
                proficiencyBonus = props.proficiencyBonus,
                proficient = props.character.skills.investigation,
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
        }
    }
}

private val Title = FC<TitleProps> { props ->
    DialogTitle {
        div {
            this.className = ClassName("$CLASS_INLINE $CLASS_JUSTIFY_BETWEEN")
            +props.character.name
            TitledInput {
                this.label = props.translations[ENTER_RACE_TRANSLATION] ?: ""
                this.value = props.character.subrace._id
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

fun shouldShowTabs(character: Character) = character.maneuvers.isNotEmpty() || character.spells.isNotEmpty()

fun computeTabValue(character: Character): Int {
    if (character.maneuvers.isNotEmpty()) {
        return MANEUVERS_INDEX
    }
    return SPELLS_INDEX
}
