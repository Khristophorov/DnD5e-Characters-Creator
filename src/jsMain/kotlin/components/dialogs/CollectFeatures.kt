package me.khrys.dnd.charcreator.client.components.dialogs

import kotlinx.serialization.json.Json
import me.khrys.dnd.charcreator.client.ManeuversContext
import me.khrys.dnd.charcreator.client.SpellsContext
import me.khrys.dnd.charcreator.client.components.dialogs.windows.InformWindow
import me.khrys.dnd.charcreator.client.components.inputs.choosers.AbilityChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ElementChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.EquipmentPackChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.EquipmentsChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.FeatChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.LanguageChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.LanguagesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ManeuverChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ProficienciesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ProficiencyChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.SkillChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.SkillsAndProficienciesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.SkillsChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.SpellsChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.WeaponsChooser
import me.khrys.dnd.charcreator.client.format
import me.khrys.dnd.charcreator.common.models.Armor
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.Weapon
import react.FC
import react.ReactNode
import react.createElement
import react.useContext
import react.useState

val DEFAULT_NODE = ReactNode("")

val WINDOW_FUNCTIONS = listOf(
    "Inform",
    "Choose Ability",
    "Choose Element",
    "Choose Feat",
    "Choose Language",
    "Choose Languages",
    "Choose Maneuver",
    "Choose Proficiencies",
    "Choose Proficiency",
    "Choose Skill",
    "Choose Skills",
    "Choose Skills and Proficiencies",
    "Choose Spells",
    "Choose Additional Spells",
    "Choose Weapon",
    "Choose Equipment Pack",
    "Choose Equipment",
    "Add Weapon",
    "Add Armor"
)

val CollectRaceFeatures = memoDialog(FC<RaceBaseProps> { props ->
    if (props.open) {
        console.info("Loading features for race: ${props.character.race}")
        CollectFeatures {
            this.open = props.open
            this.setOpen = props.setOpen
            this.character = props.character
            this.features = props.features
            this.feats = props.feats
            this.useFeats = props.useFeats
            this.action = props.action
        }
    }
})

val CollectSubraceFeatures = memoDialog(FC<RaceBaseProps> { props ->
    if (props.open) {
        console.info("Loading features for subrace: ${props.character.subrace}")
        CollectFeatures {
            this.open = props.open
            this.setOpen = props.setOpen
            this.character = props.character
            this.features = props.features
            this.action = props.action
        }
    }
})

val CollectClassFeatures = memoDialog(FC<ClassBaseProps> { props ->
    if (props.open) {
        val features = props.features
            .filter { filterMulticlass(it, props.multiclass) }
        console.info("Loading features for class: ${props.className}")
        CollectFeatures {
            this.open = props.open
            this.setOpen = props.setOpen
            this.character = props.character
            this.features = features
            this.feats = props.feats
            this.useFeats = props.useFeats
            this.action = props.action
        }
    }
})

val CollectFeatFeatures: FC<FeatsProps> = FC { props ->
    console.info("Loading feat features for ${props.feature.name}")
    CollectFeatures {
        this.open = props.open
        this.setOpen = props.setOpen
        this.character = props.character
        this.features = listOf(props.feature)
        this.action = props.action
    }
}

val CollectFeatures = FC<MultipleFeaturesFeatsProps> { props ->
    val (child, setChild) = useState(DEFAULT_NODE)
    if (props.open) {
        if (child == DEFAULT_NODE) {
            val functionFeatures = mutableListOf<FC<DialogProps>>()
            val functionsIterator = functionFeatures.iterator()
            val nextAction = {
                if (functionsIterator.hasNext()) {
                    setChild(createElement(functionsIterator.next()))
                } else {
                    props.action()
                    props.setOpen(false)
                }
            }
            props.features.filter { filterFeats(it, props.useFeats) }.forEach { feature ->
                if (feature.functions.isEmpty()) {
                    props.character.features += feature
                } else {
                    if (hasWindowFunctions(feature)) {
                        feature.functions.forEach { function ->
                            when (function.name) {
                                "Inform" -> {
                                    functionFeatures
                                        .add(inform(feature, function, props, nextAction))
                                }

                                "Choose Proficiency" -> {
                                    functionFeatures
                                        .add(proficiencyChooser(feature, function, props, nextAction))
                                }

                                "Choose Proficiencies" -> {
                                    functionFeatures
                                        .add(proficienciesChooser(feature, function, props, nextAction))
                                }

                                "Choose Language" -> {
                                    functionFeatures
                                        .add(languageChooser(feature, function, props, nextAction))
                                }

                                "Choose Languages" -> {
                                    functionFeatures
                                        .add(languagesChooser(feature, function, props, nextAction))
                                }

                                "Choose Skill" -> {
                                    functionFeatures
                                        .add(skillChooser(feature, function, props, nextAction))
                                }

                                "Choose Skills" -> {
                                    functionFeatures
                                        .add(skillsChooser(feature, function, props, nextAction))
                                }

                                "Choose Feat" -> {
                                    functionFeatures
                                        .add(featChooser(feature, props, nextAction))
                                }

                                "Choose Element" -> {
                                    functionFeatures
                                        .add(elementChooser(feature, function, props, nextAction))
                                }

                                "Choose Ability" -> {
                                    functionFeatures
                                        .add(abilityChooser(feature, function, props, nextAction))
                                }

                                "Choose Maneuver" -> {
                                    functionFeatures
                                        .add(maneuverChooser(feature, function, props, nextAction))
                                }

                                "Choose Spells" -> {
                                    functionFeatures
                                        .add(spellsChooser(feature, function, props, false, nextAction))
                                }

                                "Choose Additional Spells" -> {
                                    functionFeatures
                                        .add(spellsChooser(feature, function, props, true, nextAction))
                                }

                                "Choose Skills and Proficiencies" -> {
                                    functionFeatures
                                        .add(skillsAndProficienciesChooser(feature, function, props, nextAction))
                                }

                                "Choose Weapon" -> {
                                    functionFeatures
                                        .add(weaponsChooser(feature, function, props, nextAction))
                                }

                                "Choose Equipment Pack" -> {
                                    functionFeatures
                                        .add(equipmentPackChooser(feature, function, props, nextAction))
                                }

                                "Choose Equipment" -> {
                                    functionFeatures
                                        .add(equipmentChooser(feature, function, props, nextAction))
                                }

                                "Add Weapon" -> {
                                    val weapon = Json.decodeFromString<Weapon>(function.values[1])
                                    props.character.equipment.weapons += weapon
                                }

                                "Add Armor" -> {
                                    val armor = Json.decodeFromString<Armor>(function.values[1])
                                    props.character.equipment.armor += armor
                                }
                            }
                            if (function.addFeature) {
                                props.character.features += feature
                            }
                        }
                    } else {
                        props.character.features += feature
                    }
                }
            }
            nextAction()
        }
        this.child(child)
    }
}

private fun inform(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    InformWindow {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.setValue = {
            console.info("Info for ${feature.name}")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[0],
                    functions = emptyList(),
                    source = feature.source
                )
            nextAction()
        }
    }
}

private fun proficiencyChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    ProficiencyChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen proficiency: $value")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[1].format(value),
                    functions = listOf(DnDFunction(function.values[0], listOf(value))),
                    source = feature.source
                )
            nextAction()
        }
    }
}

private fun proficienciesChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    ProficienciesChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.size = if (function.values.isEmpty()) 0 else function.values[2].toInt()
        this.setValue = { values ->
            console.info("Chosen proficiencies: $values")
            val featureFunctions = listOf(DnDFunction(function.values[0], values))
            if (props.character.hasFeature(feature.name)) {
                props.character.features.filter { it.name == feature.name }[0].functions += featureFunctions
            } else {
                props.character.features +=
                    Feature(
                        name = feature.name,
                        description = function.values[1].format(*values.toTypedArray()),
                        functions = featureFunctions,
                        source = feature.source
                    )
            }
            nextAction()
        }
    }
}

private fun languageChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    LanguageChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen language: $value")
            val functions =
                if (props.character.hasFeature(feature.name))
                    props.character.features.filter { feature.name == it.name }[0].functions
                else emptyList()
            val languageFeature = Feature(
                name = feature.name,
                description = function.values[1].format(value),
                functions = functions + arrayOf(DnDFunction(function.values[0], listOf(value))),
                source = feature.source
            )
            val filteredFeatures = props.character.features.filter { feature.name != it.name }
            props.character.features = filteredFeatures + languageFeature
            nextAction()
        }
    }
}

private fun languagesChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    LanguagesChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.size = if (function.values.isEmpty()) 0 else function.values[2].toInt()
        this.setValue = { values ->
            console.info("Chosen languages: $values")
            val functions =
                if (props.character.hasFeature(feature.name))
                    props.character.features.filter { feature.name == it.name }[0].functions
                else emptyList()
            val languageFeature = Feature(
                name = feature.name,
                description = function.values[1].format(*values.toTypedArray()),
                functions = functions + arrayOf(DnDFunction(function.values[0], values)),
                source = feature.source
            )
            val filteredFeatures = props.character.features.filter { feature.name != it.name }
            props.character.features = filteredFeatures + languageFeature
            nextAction()
        }
    }
}

private fun skillChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    SkillChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen skill: $value")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[1].format(value),
                    functions = listOf(DnDFunction(function.values[0], listOf(value))),
                    source = feature.source
                )
            nextAction()
        }
    }
}

private fun skillsChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    SkillsChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.size = if (function.values.isEmpty()) 0 else function.values[1].toInt()
        this.setValue = { values ->
            console.info("Chosen skills: $values")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[2].format(*values.toTypedArray()),
                    functions = listOf(DnDFunction(function.values[0], values)),
                    source = feature.source
                )
            nextAction()
        }
    }
}

private fun skillsAndProficienciesChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    SkillsAndProficienciesChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.size =
            if (function.values.isEmpty()) 0 else function.values[5].toInt()
        this.setValue = { values ->
            val skillsList = function.values[2].split(", ")
            val skillValues = mutableListOf<String>()
            val proficiencyValues = mutableListOf<String>()
            val featureFunctions = mutableListOf<DnDFunction>()
            values.forEach {
                if (skillsList.contains(it)) {
                    skillValues += it
                } else {
                    proficiencyValues += it
                }
            }
            if (skillValues.isNotEmpty()) {
                featureFunctions += DnDFunction(function.values[0], skillValues)
            }
            if (proficiencyValues.isNotEmpty()) {
                featureFunctions += DnDFunction(function.values[1], proficiencyValues)
            }

            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[4].format(*values.toTypedArray()),
                    functions = featureFunctions,
                    source = feature.source
                )
            nextAction()
        }
    }
}

private fun featChooser(
    feature: Feature,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    FeatChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.character = props.character
        this.feature = feature
        this.feats = props.feats
        this.action = nextAction
    }
}

private fun elementChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    ElementChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen element: $value")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[0].format(value),
                    functions = emptyList(),
                    source = feature.source
                )
            nextAction()
        }
    }
}

private fun abilityChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    AbilityChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen ability: $value")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[1].format(value),
                    functions = feature.functions.filter { it.name != function.name } +
                            DnDFunction(function.values[0], listOf(value, function.values[2])),
                    source = feature.source
                )
            nextAction()
        }
    }
}

private fun maneuverChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    ManeuverChooser {
        val maneuversMap = useContext(ManeuversContext)
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen maneuver: $value")
            maneuversMap[value]?.let { props.character.maneuvers += it }
            nextAction()
        }
    }
}

private fun spellsChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    isAdditional: Boolean,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    SpellsChooser {
        val spellsMap = useContext(SpellsContext)
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.isAdditionalSpells = isAdditional
        this.setValue = { spellsNames ->
            console.log("Spells selected: $spellsNames")
            if (isAdditional) {
                spellsNames.forEach { spellName ->
                    spellsMap[spellName]?.let { props.character.additionalSpells += it }
                }
            } else {
                spellsNames.mapNotNull { spellsMap[it] }
                    .let { props.character.spells = it }
            }
            nextAction()
        }
    }
}

private fun weaponsChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    WeaponsChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { weapons ->
            val weaponsNames = weapons.map { it._id }
            console.info("Chosen weapons: $weaponsNames")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[2].format(*weaponsNames.toTypedArray()),
                    functions = listOf(DnDFunction(feature.name)),
                    source = feature.source
                )
            props.character.equipment.weapons += weapons
            nextAction()
        }
    }
}

private fun equipmentChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    EquipmentsChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { equipments ->
            val equipmentsNames = equipments.map { it._id }
            console.info("Chosen equipments: $equipmentsNames")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[2].format(*equipmentsNames.toTypedArray()),
                    functions = listOf(DnDFunction(feature.name)),
                    source = feature.source
                )
            props.character.equipment.otherEquipment += equipments
            nextAction()
        }
    }
}

private fun equipmentPackChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: () -> Unit
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    EquipmentPackChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { equipments ->
            val equipmentsPack = equipments.first
            console.info("Chosen equipment: $equipmentsPack")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[0].format(equipmentsPack),
                    functions = listOf(DnDFunction(feature.name)),
                    source = feature.source
                )
            props.character.equipment.otherEquipment += equipments.second
            nextAction()
        }
    }
}

private fun filterFeats(feature: Feature, useFeats: Boolean) =
    (useFeats && (feature.withFeats || !feature.withoutFeats)) || !(useFeats || feature.withFeats)

fun filterMulticlass(feature: Feature, multiclass: Boolean): Boolean =
    if (multiclass) {
        !feature.singleClass
    } else {
        !feature.multiClass
    }

private fun hasWindowFunctions(feature: Feature) =
    feature.functions.map { it.name }.any { WINDOW_FUNCTIONS.contains(it) }
