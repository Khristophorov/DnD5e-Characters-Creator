package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.client.components.dialogs.windows.informWindow
import me.khrys.dnd.charcreator.client.components.inputs.choosers.abilitiesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.elementChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.featsChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.languageChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.languagesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.maneuversChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.proficienciesChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.proficiencyChooser
import me.khrys.dnd.charcreator.client.components.inputs.choosers.skillsChooser
import me.khrys.dnd.charcreator.client.format
import me.khrys.dnd.charcreator.client.useFeatureWindowSettings
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.hasFeature
import react.RBuilder
import react.fc
import react.useState

val collectRaceFeatures = fc<CharDialogProps> { props ->
    val features = props.character.race.features
    collectFeatures(
        open = props.open,
        setOpen = props.setOpen,
        character = props.character,
        features = features,
        featsMap = props.feats,
        useFeats = props.useFeats,
        action = props.action
    )
}

val collectSubraceFeatures = fc<CharDialogProps> { props ->
    val features = props.character.subrace.features
    collectFeatures(
        open = props.open,
        setOpen = props.setOpen,
        character = props.character,
        features = features,
        action = props.action
    )
}

val collectFeatFeatures = fc<FeatsProps> { props ->
    collectFeatures(
        open = props.open,
        setOpen = props.setOpen,
        character = props.character,
        features = listOf(props.feature),
        action = props.action
    )
}

fun RBuilder.collectFeatures(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    character: Character,
    features: List<Feature>,
    featsMap: Map<String, Feat> = emptyMap(),
    useFeats: Boolean = false,
    action: () -> Unit
) {
    val inform = useFeatureWindowSettings()
    val proficiency = useFeatureWindowSettings()
    val proficiencies = useFeatureWindowSettings()
    val language = useFeatureWindowSettings()
    val languages = useFeatureWindowSettings()
    val skills = useFeatureWindowSettings()
    val abilities = useFeatureWindowSettings()
    val element = useFeatureWindowSettings()
    val maneuvers = useFeatureWindowSettings()
    val feats = useFeatureWindowSettings()

    val (numberOfNewFunctions, setNumberOfNewFunctions) = useState(-1)

    if (!open && numberOfNewFunctions >= 0) {
        setNumberOfNewFunctions(-1)
    }
    if (open && numberOfNewFunctions < 0) {
        val newFunctions = mutableListOf<() -> Unit>()
        features.filter { filterFeature(it, useFeats) }.forEach { feature ->
            if (feature.functions.isEmpty()) {
                character.features += feature
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
                                abilities.setParams(true, feature, function)
                            }
                            addFeature = false
                        }
                        "Choose Skills" -> {
                            newFunctions.add {
                                skills.setParams(true, feature, function)
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
                    }
                }
                if (addFeature) {
                    character.features += feature
                }
            }
        }
        setNumberOfNewFunctions(newFunctions.size)
        if (newFunctions.isEmpty()) {
            action()
            setOpen(false)
        }
        newFunctions.forEach { function -> function() }
    }
    child(informWindow) {
        attrs.open = inform.open
        attrs.setOpen = { inform.setOpen(it) }
        attrs.feature = inform.feature
        attrs.setValue = {
            character.features +=
                Feature(
                    name = inform.feature.name,
                    description = inform.function.values[0],
                    functions = emptyList(),
                    source = inform.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    child(proficiencyChooser) {
        attrs.open = proficiency.open
        attrs.setOpen = { proficiency.setOpen(it) }
        attrs.feature = proficiency.feature
        attrs.function = proficiency.function
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = proficiency.feature.name,
                    description = proficiency.function.values[1].format(value),
                    functions = listOf(DnDFunction(proficiency.function.values[0], listOf(value))),
                    source = proficiency.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    child(proficienciesChooser) {
        attrs.open = proficiencies.open
        attrs.setOpen = { proficiencies.setOpen(it) }
        attrs.feature = proficiencies.feature
        attrs.function = proficiencies.function
        attrs.character = character
        attrs.size = if (proficiencies.function.values.isEmpty()) 0 else proficiencies.function.values[2].toInt()
        attrs.setValue = { values ->
            val featureFunctions = listOf(DnDFunction(proficiencies.function.values[0], values))
            if (character.hasFeature(proficiencies.feature.name)) {
                character.features.filter { it.name == proficiencies.feature.name }[0].functions += featureFunctions
            } else {
                character.features +=
                    Feature(
                        name = proficiencies.feature.name,
                        description = proficiencies.function.values[1].format(*values.toTypedArray()),
                        functions = featureFunctions,
                        source = proficiencies.feature.source
                    )
            }
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    child(languageChooser) {
        attrs.open = language.open
        attrs.setOpen = { language.setOpen(it) }
        attrs.feature = language.feature
        attrs.function = language.function
        attrs.character = character
        attrs.setValue = { value ->
            val functions =
                if (character.hasFeature(language.feature.name))
                    character.features.filter { language.feature.name == it.name }[0].functions
                else emptyList()
            val feature = Feature(
                name = language.feature.name,
                description = language.function.values[1].format(value),
                functions = functions + arrayOf(DnDFunction(language.function.values[0], listOf(value))),
                source = language.feature.source
            )
            val filteredFeatures = character.features.filter { language.feature.name != it.name }
            character.features = filteredFeatures + feature

            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    child(languagesChooser) {
        attrs.open = languages.open
        attrs.setOpen = { languages.setOpen(it) }
        attrs.feature = languages.feature
        attrs.function = languages.function
        attrs.character = character
        attrs.size = if (languages.function.values.isEmpty()) 0 else languages.function.values[2].toInt()
        attrs.setValue = { values ->
            val functions =
                if (character.hasFeature(languages.feature.name))
                    character.features.filter { languages.feature.name == it.name }[0].functions
                else emptyList()
            val feature = Feature(
                name = languages.feature.name,
                description = languages.function.values[1].format(*values.toTypedArray()),
                functions = functions + arrayOf(DnDFunction(languages.function.values[0], values)),
                source = languages.feature.source
            )
            val filteredFeatures = character.features.filter { languages.feature.name != it.name }
            character.features = filteredFeatures + feature

            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    child(abilitiesChooser) {
        attrs.open = abilities.open
        attrs.setOpen = { abilities.setOpen(it) }
        attrs.feature = abilities.feature
        attrs.function = abilities.function
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = abilities.feature.name,
                    description = abilities.function.values[1].format(value),
                    functions = abilities.feature.functions.filter { it.name != abilities.function.name } +
                            DnDFunction(abilities.function.values[0], listOf(value, abilities.function.values[2])),
                    source = abilities.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    child(skillsChooser) {
        attrs.open = skills.open
        attrs.setOpen = { skills.setOpen(it) }
        attrs.feature = skills.feature
        attrs.function = skills.function
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = skills.feature.name,
                    description = skills.function.values[1].format(value),
                    functions = listOf(DnDFunction(skills.function.values[0], listOf(value))),
                    source = skills.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    child(elementChooser) {
        attrs.open = element.open
        attrs.setOpen = { element.setOpen(it) }
        attrs.feature = element.feature
        attrs.function = element.function
        attrs.character = character
        attrs.setValue = { value ->
            character.features +=
                Feature(
                    name = element.feature.name,
                    description = element.function.values[0].format(value),
                    functions = emptyList(),
                    source = element.feature.source
                )
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    child(maneuversChooser) {
        val functionValues = maneuvers.function.values
        attrs.open = maneuvers.open
        attrs.setOpen = { maneuvers.setOpen(it) }
        attrs.feature = maneuvers.feature
        attrs.function = maneuvers.function
        attrs.character = character
        attrs.size = if (functionValues.isEmpty()) 0 else functionValues[0].toInt()
        attrs.setValue = { values ->
            values.forEach { value ->
                character.features += Feature(
                    name = value,
                    description = functionValues[functionValues.indexOf(value) + 1],
                    functions = emptyList(),
                    source = maneuvers.feature.source
                )
            }
            endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen)
        }
    }
    if (useFeats) {
        child(featsChooser) {
            attrs.open = feats.open
            attrs.setOpen = { feats.setOpen(it) }
            attrs.character = character
            attrs.feature = feats.feature
            attrs.feats = featsMap
            attrs.action = { endAction(numberOfNewFunctions, { setNumberOfNewFunctions(it) }, action, setOpen) }
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
