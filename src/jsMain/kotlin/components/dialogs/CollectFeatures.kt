package me.khrys.dnd.charcreator.client.components.dialogs

import kotlinx.serialization.json.Json
import me.khrys.dnd.charcreator.client.ManeuversContext
import me.khrys.dnd.charcreator.client.SpellsContext
import me.khrys.dnd.charcreator.client.apply
import me.khrys.dnd.charcreator.client.components.dialogs.windows.InformWindow
import me.khrys.dnd.charcreator.client.components.inputs.choosers.AbilitiesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.AbilityChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ArmorsChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ElementChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.EquipmentPackChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.EquipmentsChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.FeatChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.FeatureChooser
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
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.Filter
import me.khrys.dnd.charcreator.common.models.SimpleEquipment
import me.khrys.dnd.charcreator.common.models.Weapon
import react.FC
import react.ReactNode
import react.createElement
import react.useContext
import react.useState

private typealias NextAction = (Feature, DnDFunction) -> Unit

val DEFAULT_NODE = ReactNode("")

val WINDOW_FUNCTIONS = listOf(
    "Inform",
    "Choose Ability",
    "Choose Abilities",
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
    "Choose Armor",
    "Choose Equipment Pack",
    "Choose Equipment",
    "Choose Feature",
    "Add Weapon",
    "Add Armor",
    "Add Equipments"
)

val FILTERS_TO_APPLY =
    setOf(Filter.Param.FEATURES, Filter.Param.ARMORS, Filter.Param.WEAPONS, Filter.Param.MARTIAL_WEAPONS_SIZE)

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
            this.translations = props.translations
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
            this.translations = props.translations
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
            this.translations = props.translations
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
        this.translations = props.translations
    }
}

val CollectFeatures = FC<MultipleFeaturesFeatsProps> { props ->
    val (child, setChild) = useState(DEFAULT_NODE)
    if (props.open) {
        if (child == DEFAULT_NODE) {
            val simpleFeatures = mutableListOf<Feature>()
            val functionFeatures = mutableListOf<FC<DialogProps>>()
            val functionsIterator = functionFeatures.iterator()
            val apply = {
                if (functionsIterator.hasNext()) {
                    setChild(createElement(functionsIterator.next()))
                } else {
                    props.character.features += simpleFeatures
                        .filter { shouldAddFeature(props.character, it, props.translations) }
                    props.action()
                    props.setOpen(false)
                }
            }
            val nextAction: NextAction = { feature, function ->
                if (function.addFeature && shouldAddFeature(props.character, feature, props.translations)) {
                    if (function.replaceCurrent) {
                        props.character.features -=
                            props.character.features.filter { it.name == feature.name }
                    }
                    props.character.features += feature
                }
                apply()
            }
            props.features.filter { filterFeats(it, props.useFeats) }.forEach { feature ->
                if (feature.functions.isEmpty() || !hasWindowFunctions(feature)) {
                    simpleFeatures.add(feature)
                } else {
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
                                    .add(featChooser(feature, function, props, nextAction))
                            }

                            "Choose Element" -> {
                                functionFeatures
                                    .add(elementChooser(feature, function, props, nextAction))
                            }

                            "Choose Ability" -> {
                                functionFeatures
                                    .add(abilityChooser(feature, function, props, nextAction))
                            }

                            "Choose Abilities" -> {
                                functionFeatures
                                    .add(abilitiesChooser(feature, function, props, nextAction))
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

                            "Choose Armor" -> {
                                functionFeatures
                                    .add(armorsChooser(feature, function, props, nextAction))
                            }

                            "Choose Equipment Pack" -> {
                                functionFeatures
                                    .add(equipmentPackChooser(feature, function, props, nextAction))
                            }

                            "Choose Equipment" -> {
                                functionFeatures
                                    .add(equipmentChooser(feature, function, props, nextAction))
                            }

                            "Choose Feature" -> {
                                functionFeatures
                                    .add(featureChooser(feature, function, props, nextAction))
                            }

                            "Add Weapon" -> {
                                functionFeatures.add(FC<DialogProps> {
                                    if (shouldAddFeature(props.character, feature, props.translations)) {
                                        val weapon = Json.decodeFromString<Weapon>(function.values[1])
                                        console.info("Adding weapon: ${weapon._id}")
                                        props.character.equipment.weapons += weapon
                                    }
                                    nextAction(feature, function)
                                })
                            }

                            "Add Armor" -> {
                                functionFeatures.add(FC<DialogProps> {
                                    if (shouldAddFeature(props.character, feature, props.translations)) {
                                        val armor = Json.decodeFromString<Armor>(function.values[1])
                                        console.info("Adding armor: ${armor._id}")
                                        props.character.equipment.armors += armor
                                    }
                                    nextAction(feature, function)
                                })
                            }

                            "Add Equipments" -> {
                                functionFeatures.add(FC<DialogProps> {
                                    if (shouldAddFeature(props.character, feature, props.translations)) {
                                        val equipments =
                                            Json.decodeFromString<List<SimpleEquipment>>(function.values[1])
                                        console.info("Adding equipments: ${equipments.map { it._id }}")
                                        props.character.equipment.otherEquipment += equipments
                                    }
                                    nextAction(feature, function)
                                })
                            }
                        }
                    }
                }
            }
            apply()
        }
        +child
    }
}

private fun inform(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    if (shouldAddFeature(props.character, feature, props.translations)) {
        InformWindow {
            this.open = open
            this.setOpen = { setOpen(it) }
            this.feature = feature
            this.setValue = {
                console.info("Info for ${feature.name}")
                nextAction(feature, function)
            }
        }
    } else {
        nextAction(feature, function)
    }
}

private fun proficiencyChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    if (shouldAddFeature(props.character, feature, props.translations)) {
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
                nextAction(feature, function)
            }
        }
    } else {
        nextAction(feature, function)
    }
}

private fun proficienciesChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    if (shouldAddFeature(props.character, feature, props.translations)) {
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
                nextAction(feature, function)
            }
        }
    } else {
        nextAction(feature, function)
    }
}

private fun languageChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
            nextAction(feature, function)
        }
    }
}

private fun languagesChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
            nextAction(feature, function)
        }
    }
}

private fun skillChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
            nextAction(feature, function)
        }
    }
}

private fun skillsChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    if (shouldAddFeature(props.character, feature, props.translations)) {
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
                nextAction(feature, function)
            }
        }
    } else {
        nextAction(feature, function)
    }
}

private fun skillsAndProficienciesChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
            nextAction(feature, function)
        }
    }
}

private fun featChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    FeatChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.character = props.character
        this.feature = feature
        this.feats = props.feats
        this.action = { nextAction(feature, function) }
    }
}

private fun featureChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    if (shouldAddFeature(props.character, feature, props.translations)) {
        FeatureChooser {
            this.open = open
            this.setOpen = { setOpen(it) }
            this.character = props.character
            this.feature = feature
            this.function = function
            this.setValue = { value ->
                console.info("Chosen feature: $value")
                val featureIndex = function.values.indexOf(value)
                props.character.features +=
                    Feature(
                        name = function.values[featureIndex],
                        description = function.values[featureIndex + 1],
                        source = function.values[featureIndex + 2]
                    )
                nextAction(feature, function)
            }
        }
    } else {
        nextAction(feature, function)
    }
}

private fun elementChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
            nextAction(feature, function)
        }
    }
}

private fun abilityChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
            nextAction(feature, function)
        }
    }
}

private fun abilitiesChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    AbilitiesChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.size = if (function.values.isEmpty()) 0 else function.values[1].toInt()
        this.setValue = { values ->
            console.info("Chosen abilities: $values")
            val functionName = function.values[0]
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[2].format(*values.toTypedArray()),
                    functions = values.map { DnDFunction(functionName, listOf(it, "1")) },
                    source = feature.source
                )
            nextAction(feature, function)
        }
    }
}

private fun maneuverChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    if (shouldAddFeature(props.character, feature, props.translations)) {
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
                nextAction(feature, function)
            }
        }
    } else {
        nextAction(feature, function)
    }
}

private fun spellsChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    isAdditional: Boolean,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    if (shouldAddFeature(props.character, feature, props.translations)) {
        SpellsChooser {
            val spellsMap = useContext(SpellsContext)
            this.open = open
            this.setOpen = { setOpen(it) }
            this.feature = feature
            this.function = function
            this.character = props.character
            this.isAdditionalSpells = isAdditional
            this.setValue = { spellsToAdd, spellsToRemove ->
                console.log("Spells selected: $spellsToAdd")
                if (isAdditional) {
                    spellsToAdd.forEach { spellName ->
                        spellsMap[spellName]?.let { props.character.additionalSpells += it }
                    }
                } else {
                    spellsToRemove.mapNotNull { spellsMap[it] }
                        .let { props.character.spells -= it }
                    spellsToAdd.mapNotNull { spellsMap[it] }
                        .let { props.character.spells = (it + props.character.spells).distinct() }
                }
                nextAction(feature, function)
            }
        }
    } else {
        nextAction(feature, function)
    }
}

private fun weaponsChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
                    description = function.values[3].format(*weaponsNames.toTypedArray()),
                    functions = listOf(DnDFunction(feature.name)),
                    source = feature.source
                )
            props.character.equipment.weapons += weapons
            nextAction(feature, function)
        }
    }
}

private fun armorsChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
) = FC<DialogProps> {
    val (open, setOpen) = useState(true)
    ArmorsChooser {
        this.open = open
        this.setOpen = { setOpen(it) }
        this.feature = feature
        this.function = function
        this.character = props.character
        this.setValue = { armors ->
            val weaponsNames = armors.map { it._id }
            console.info("Chosen weapons: $weaponsNames")
            props.character.features +=
                Feature(
                    name = feature.name,
                    description = function.values[2].format(*weaponsNames.toTypedArray()),
                    functions = listOf(DnDFunction(feature.name)),
                    source = feature.source
                )
            props.character.equipment.armors += armors
            nextAction(feature, function)
        }
    }
}

private fun equipmentChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
            nextAction(feature, function)
        }
    }
}

private fun equipmentPackChooser(
    feature: Feature,
    function: DnDFunction,
    props: MultipleFeaturesFeatsProps,
    nextAction: NextAction
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
            nextAction(feature, function)
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

private fun shouldAddFeature(character: Character, feature: Feature, translations: Map<String, String>) =
    feature.filters.filter { FILTERS_TO_APPLY.contains(it.param) }
        .map { it.apply(character, translations) }
        .fold(true) { b1, b2 -> b1 && b2 }
