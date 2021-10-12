package me.khrys.dnd.charcreator.client.components.inputs.choosers

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.targetValue
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.dialogs.FeatsProps
import me.khrys.dnd.charcreator.client.components.dialogs.FeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.MultipleFeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.collectFeatFeatures
import me.khrys.dnd.charcreator.client.components.inputs.dValidatedList
import me.khrys.dnd.charcreator.client.toFeature
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.emptyFeat
import react.fc
import react.useContext
import react.useState

val proficiencyChooser = fc<FeatureProps<String>> { props ->
    val values = props.function.values.toList().filter { !props.character.proficiencies.contains(it) }
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.subList(2, values.size) else emptyList(),
        setValue = props.setValue
    )
}

val proficienciesChooser = fc<MultipleFeatureProps> { props ->
    val values = props.function.values.toList().filter { !props.character.proficiencies.contains(it) }
    chooseSeveral(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        size = props.size,
        values = if (props.open) values.subList(3, values.size) else emptyList(),
        setValue = props.setValue
    )
}

val languageChooser = fc<FeatureProps<String>> { props ->
    val values = props.function.values.toList().filter { !props.character.languages.contains(it) }
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.subList(2, values.size) else emptyList(),
        setValue = props.setValue
    )
}

val languagesChooser = fc<MultipleFeatureProps> { props ->
    val values = props.function.values.toList().filter { !props.character.languages.contains(it) }
    chooseSeveral(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        size = props.size,
        values = if (props.open) values.subList(3, values.size) else emptyList(),
        setValue = props.setValue
    )
}

val skillsChooser = fc<FeatureProps<String>> { props ->
    val values = props.function.values
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.subList(2, values.size) else emptyList(),
        setValue = props.setValue
    )
}

val abilitiesChooser = fc<FeatureProps<String>> { props ->
    val values = props.function.values
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.subList(3, values.size) else emptyList(),
        setValue = props.setValue
    )
}

val elementChooser = fc<FeatureProps<String>> { props ->
    val values = props.function.values
    chooseOneOfMany(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        values = if (props.open) values.subList(1, values.size) else emptyList(),
        setValue = props.setValue
    )
}

val featsChooser = fc<FeatsProps> { props ->
    val (feat, setFeat) = useState(emptyFeat())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)

    val translations = useContext(TranslationsContext)
    mDialog(open = props.open) {
        mDialogTitle(text = props.feature.name)
        mDialogContent(dividers = true) {
            mDialogContentText(text = props.feature.description)
            dValidatorForm(onSubmit = {
                props.setOpen(false)
                setOpenFeatures(true)
            }) {
                dValidatedList(
                    label = props.feature.name,
                    value = feat._id,
                    validators = arrayOf(VALIDATION_REQUIRED),
                    errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: ""),
                    onChange = { event ->
                        setFeat(props.feats[event.targetValue] ?: emptyFeat())
                    },
                    menuItems = props.feats.mapValues { it.value.description },
                    setDescription = { setDescription(it) },
                    description = description
                )
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
    child(collectFeatFeatures) {
        attrs.character = props.character
        attrs.open = openFeatures
        attrs.setOpen = { setOpenFeatures(it) }
        attrs.action = props.action
        attrs.feature = feat.toFeature()
    }
}

val maneuversChooser = fc<MultipleFeatureProps> { props ->
    val values = mutableListOf<String>()
    val features = props.character.features.map(Feature::name)
    for (i in 1 until props.function.values.size) {
        val value = props.function.values[i]
        if (i % 2 != 0 && !features.contains(value)) {
            values.add(value)
        }
    }
    chooseSeveral(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        size = props.size,
        values = values,
        setValue = props.setValue
    )
}
