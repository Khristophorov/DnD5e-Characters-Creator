package me.khrys.dnd.charcreator.client.components.inputs.choosers

import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CollectFeatFeatures
import me.khrys.dnd.charcreator.client.components.dialogs.FeatsProps
import me.khrys.dnd.charcreator.client.components.dialogs.FeatureProps
import me.khrys.dnd.charcreator.client.components.dialogs.MultipleFeatureProps
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.toFeature
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.emptyFeat
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.Props
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

val ProficiencyChooser = FC<FeatureProps<String>> { props ->
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
}

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

val LanguageChooser = FC<FeatureProps<String>> { props ->
    if (props.open) {
        val values = props.function.values.toList().filter { !props.character.languages.contains(it) }
        ChooseOneOfMany {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.values = if (props.open) values.subList(2, values.size) else emptyList()
            this.setValue = props.setValue
        }
    }
}

val LanguagesChooser = FC<MultipleFeatureProps> { props ->
    if (props.open) {
        val values = props.function.values.toList().filter { !props.character.languages.contains(it) }
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

val SkillChooser = FC<FeatureProps<String>> { props ->
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
}

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

val FeatsChooser = FC<FeatsProps> { props ->
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

val ManeuversChooser = FC<MultipleFeatureProps> { props ->
    if (props.open) {
        val values = mutableListOf<String>()
        val features = props.character.features.map(Feature::name)
        for (i in 1 until props.function.values.size) {
            val value = props.function.values[i]
            if (i % 2 != 0 && !features.contains(value)) {
                values.add(value)
            }
        }
        ChooseSeveral {
            this.open = props.open
            this.setOpen = props.setOpen
            this.header = props.feature.name
            this.description = props.feature.description
            this.size = props.size
            this.values = values
            this.setValue = props.setValue
        }
    }
}
