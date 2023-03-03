package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.client.ManeuversContext
import me.khrys.dnd.charcreator.client.SpellsContext
import me.khrys.dnd.charcreator.client.components.dialogs.windows.InformWindow
import me.khrys.dnd.charcreator.client.components.inputs.choosers.AbilityChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ElementChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.FeatsChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.LanguageChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.LanguagesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ManeuversChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ProficienciesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.ProficiencyChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.SkillChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.SpellsChooser
import me.khrys.dnd.charcreator.client.format
import me.khrys.dnd.charcreator.client.utils.useFeatureWindowSettings
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feature
import react.FC
import react.useContext
import react.useState

val CollectRaceFeatures = FC<CharDialogProps> { props ->
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
}

val CollectSubraceFeatures = FC<CharDialogProps> { props ->
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
}

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
    val inform = useFeatureWindowSettings()
    val proficiency = useFeatureWindowSettings()
    val proficiencies = useFeatureWindowSettings()
    val language = useFeatureWindowSettings()
    val languages = useFeatureWindowSettings()
    val skill = useFeatureWindowSettings()
    val ability = useFeatureWindowSettings()
    val element = useFeatureWindowSettings()
    val maneuvers = useFeatureWindowSettings()
    val spells = useFeatureWindowSettings()
    val feats = useFeatureWindowSettings()

    val (numberOfNewFunctions, setNumberOfNewFunctions) = useState(-1)

    val (featsFeatures, setFeatsFeatures) = useState(false)

    if (!props.open && numberOfNewFunctions >= 0) {
        setNumberOfNewFunctions(-1)
    }
    if (props.open && numberOfNewFunctions < 0) {
        val newFunctions = mutableListOf<() -> Unit>()
        props.features.filter { filterFeature(it, props.useFeats) }.forEach { feature ->
            if (feature.functions.isEmpty()) {
                props.character.features += feature
            } else {
                var addFeature = true
                feature.functions.forEach { function ->
                    when (function.name) {
                        "Inform" -> {
                            newFunctions.add {
                                inform.setParams(true, feature, function)
                            }
                            addFeature = false
                        }

                        "Choose Proficiency" -> {
                            newFunctions.add {
                                proficiency.setParams(true, feature, function)
                            }
                            addFeature = false
                        }

                        "Choose Proficiencies" -> {
                            newFunctions.add {
                                proficiencies.setParams(true, feature, function)
                            }
                            addFeature = false
                        }

                        "Choose Language" -> {
                            newFunctions.add {
                                language.setParams(true, feature, function)
                            }
                        }

                        "Choose Languages" -> {
                            newFunctions.add {
                                languages.setParams(true, feature, function)
                            }
                        }

                        "Choose Ability" -> {
                            newFunctions.add {
                                ability.setParams(true, feature, function)
                            }
                            addFeature = false
                        }

                        "Choose Skills" -> {
                            newFunctions.add {
                                skill.setParams(true, feature, function)
                            }
                            addFeature = false
                        }

                        "Choose Element" -> {
                            newFunctions.add {
                                element.setParams(true, feature, function)
                            }
                            addFeature = false
                        }

                        "Choose Feat" -> {
                            newFunctions.add {
                                feats.setParams(true, feature, function)
                            }
                            addFeature = false
                        }

                        "Choose Maneuvers" -> {
                            newFunctions.add {
                                maneuvers.setParams(true, feature, function)
                            }
                        }

                        "Choose Spells" -> {
                            newFunctions.add {
                                spells.setParams(true, feature, function)
                            }
                        }
                    }
                }
                if (addFeature) {
                    props.character.features += feature
                }
            }
        }
        setNumberOfNewFunctions(newFunctions.size)
        newFunctions.forEach { it() }
        if (newFunctions.isEmpty()) {
            props.action()
            props.setOpen(false)
        }
    }

    fun shouldOpen(open: Boolean) = open && !props.useFeats || open && props.useFeats && !featsFeatures

    InformWindow {
        this.open = shouldOpen(inform.open)
        this.setOpen = { inform.setOpen(it) }
        this.feature = inform.feature
        this.setValue = {
            console.info("Info for ${inform.feature.name}")
            props.character.features +=
                Feature(
                    name = inform.feature.name,
                    description = inform.function.values[0],
                    functions = emptyList(),
                    source = inform.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    ProficiencyChooser {
        this.open = shouldOpen(proficiency.open)
        this.setOpen = { proficiency.setOpen(it) }
        this.feature = proficiency.feature
        this.function = proficiency.function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen proficiency: $value")
            props.character.features +=
                Feature(
                    name = proficiency.feature.name,
                    description = proficiency.function.values[1].format(value),
                    functions = listOf(DnDFunction(proficiency.function.values[0], listOf(value))),
                    source = proficiency.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    ProficienciesChooser {
        this.open = shouldOpen(proficiencies.open)
        this.setOpen = { proficiencies.setOpen(it) }
        this.feature = proficiencies.feature
        this.function = proficiencies.function
        this.character = props.character
        this.size = if (proficiencies.function.values.isEmpty()) 0 else proficiencies.function.values[2].toInt()
        this.setValue = { values ->
            console.info("Chosen proficiencies: $values")
            val featureFunctions = listOf(DnDFunction(proficiencies.function.values[0], values))
            if (props.character.hasFeature(proficiencies.feature.name)) {
                props.character.features.filter { it.name == proficiencies.feature.name }[0].functions += featureFunctions
            } else {
                props.character.features +=
                    Feature(
                        name = proficiencies.feature.name,
                        description = proficiencies.function.values[1].format(*values.toTypedArray()),
                        functions = featureFunctions,
                        source = proficiencies.feature.source
                    )
            }
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    LanguageChooser {
        this.open = shouldOpen(language.open)
        this.setOpen = { language.setOpen(it) }
        this.feature = language.feature
        this.function = language.function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen language: $value")
            val functions =
                if (props.character.hasFeature(language.feature.name))
                    props.character.features.filter { language.feature.name == it.name }[0].functions
                else emptyList()
            val feature = Feature(
                name = language.feature.name,
                description = language.function.values[1].format(value),
                functions = functions + arrayOf(DnDFunction(language.function.values[0], listOf(value))),
                source = language.feature.source
            )
            val filteredFeatures = props.character.features.filter { language.feature.name != it.name }
            props.character.features = filteredFeatures + feature

            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    LanguagesChooser {
        this.open = shouldOpen(languages.open)
        this.setOpen = { languages.setOpen(it) }
        this.feature = languages.feature
        this.function = languages.function
        this.character = props.character
        this.size = if (languages.function.values.isEmpty()) 0 else languages.function.values[2].toInt()
        this.setValue = { values ->
            console.info("Chosen languages: $values")
            val functions =
                if (props.character.hasFeature(languages.feature.name))
                    props.character.features.filter { languages.feature.name == it.name }[0].functions
                else emptyList()
            val feature = Feature(
                name = languages.feature.name,
                description = languages.function.values[1].format(*values.toTypedArray()),
                functions = functions + arrayOf(DnDFunction(languages.function.values[0], values)),
                source = languages.feature.source
            )
            val filteredFeatures = props.character.features.filter { languages.feature.name != it.name }
            props.character.features = filteredFeatures + feature

            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    AbilityChooser {
        this.open = shouldOpen(ability.open)
        this.setOpen = { ability.setOpen(it) }
        this.feature = ability.feature
        this.function = ability.function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen ability: $value")
            props.character.features +=
                Feature(
                    name = ability.feature.name,
                    description = ability.function.values[1].format(value),
                    functions = ability.feature.functions.filter { it.name != ability.function.name } +
                            DnDFunction(ability.function.values[0], listOf(value, ability.function.values[2])),
                    source = ability.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    SkillChooser {
        this.open = shouldOpen(skill.open)
        this.setOpen = { skill.setOpen(it) }
        this.feature = skill.feature
        this.function = skill.function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen skill: $value")
            props.character.features +=
                Feature(
                    name = skill.feature.name,
                    description = skill.function.values[1].format(value),
                    functions = listOf(DnDFunction(skill.function.values[0], listOf(value))),
                    source = skill.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    ElementChooser {
        this.open = shouldOpen(element.open)
        this.setOpen = { element.setOpen(it) }
        this.feature = element.feature
        this.function = element.function
        this.character = props.character
        this.setValue = { value ->
            console.info("Chosen element: $value")
            props.character.features +=
                Feature(
                    name = element.feature.name,
                    description = element.function.values[0].format(value),
                    functions = emptyList(),
                    source = element.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    ManeuversChooser {
        val maneuversMap = useContext(ManeuversContext)
        val functionValues = maneuvers.function.values
        this.open = shouldOpen(maneuvers.open)
        this.setOpen = { maneuvers.setOpen(it) }
        this.feature = maneuvers.feature
        this.function = maneuvers.function
        this.character = props.character
        this.size = if (functionValues.isEmpty()) 0 else functionValues[1].toInt()
        this.setValue = { values ->
            console.info("Chosen maneuvers: $values")
            values.forEach { value ->
                maneuversMap[value]?.let { props.character.maneuvers += it }
            }
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    SpellsChooser {
        val spellsMap = useContext(SpellsContext)
        this.open = shouldOpen(spells.open)
        this.setOpen = { spells.setOpen(it) }
        this.feature = spells.feature
        this.function = spells.function
        this.character = props.character
        this.setValue = { spellsNames ->
            console.log("Spells selected: $spellsNames")
            spellsNames.forEach { spellName ->
                spellsMap[spellName]?.let { props.character.spells += it }
            }
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
        }
    }
    if (props.useFeats) {
        console.info("Use feats.")
        if (feats.open && !featsFeatures) {
            setFeatsFeatures(true)
        }
        FeatsChooser {
            this.open = feats.open
            this.setOpen = { feats.setOpen(it) }
            this.character = props.character
            this.feature = feats.feature
            this.feats = props.feats
            this.action = {
                if (featsFeatures) {
                    setFeatsFeatures(false)
                }
                endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, props.action, props.setOpen)
            }
        }
    }
}

private fun endAction(
    numberOfNewFunctions: Int,
    setNumberOfNewFunctions: (Int) -> Unit,
    action: () -> Unit,
    setOpen: (Boolean) -> Unit
) {
    val newNumberOfNewFunctions = numberOfNewFunctions - 1
    setNumberOfNewFunctions(newNumberOfNewFunctions)
    if (newNumberOfNewFunctions == 0) {
        action()
        setOpen(false)
    }
}

private fun filterFeature(feature: Feature, useFeats: Boolean) =
    (useFeats && (feature.withFeats || !feature.withoutFeats)) || !(useFeats || feature.withFeats)
