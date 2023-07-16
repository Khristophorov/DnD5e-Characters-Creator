package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.client.ManeuversContext
import me.khrys.dnd.charcreator.client.SpellsContext
import me.khrys.dnd.charcreator.client.components.dialogs.windows.InformWindow
import me.khrys.dnd.charcreator.client.components.inputs.choosers.AbilityChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ElementChooser
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
import me.khrys.dnd.charcreator.client.format
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feature
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
    "Choose Spells"
)

val CollectRaceFeatures = memoDialog(FC<FeatsProps> { props ->
    if (props.open) {
        val features = props.character.race.features
        console.info("Loading features for race: ${props.character.race._id}")
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

val CollectSubraceFeatures = memoDialog(FC<FeatsProps> { props ->
    if (props.open) {
        val features = props.character.subrace.features
        console.info("Loading features for subrace: ${props.character.subrace._id}")
        CollectFeatures {
            this.open = props.open
            this.setOpen = props.setOpen
            this.character = props.character
            this.features = features
            this.action = props.action
        }
    }
})

val CollectClassFeatures = memoDialog(FC<ClassBaseProps> { props ->
    if (props.open) {
        val features = (props.character.classes.filter { it.second._id == props.className }[0]
            .second.features[props.classLevel] ?: emptyList())
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
                                        .add(spellsChooser(feature, function, props, nextAction))
                                }

                                "Choose Skills and Proficiencies" -> {
                                    functionFeatures
                                        .add(skillsAndProficienciesChooser(feature, function, props, nextAction))
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
        this.setValue = { spellsNames ->
            console.log("Spells selected: $spellsNames")
            spellsNames.forEach { spellName ->
                spellsMap[spellName]?.let { props.character.spells += it }
            }
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
