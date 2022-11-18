package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.client.components.buttons.ButtonAction
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Feature
import react.Props
import react.PropsWithChildren

external interface DialogProps : PropsWithChildren {
    var open: Boolean
    var setOpen: (Boolean) -> Unit
}

external interface CharDialogProps : FeatsProps

external interface FeatureProps<T> : DialogProps {
    var character: Character
    var feature: Feature
    var function: DnDFunction
    var setValue: (T) -> Unit
}

external interface MultipleFeatureProps : FeatureProps<List<String>> {
    var size: Int
}

external interface FeatsProps : DialogProps {
    var feature: Feature
    var character: Character
    var feats: Map<String, Feat>
    var useFeats: Boolean
    var action: () -> Unit
}

external interface MultipleFeaturesFeatsProps : DialogProps {
    var features: List<Feature>
    var character: Character
    var feats: Map<String, Feat>
    var useFeats: Boolean
    var action: () -> Unit
}

external interface CharBasedProps : Props {
    var character: Character
    var open: Boolean
    var setOpen: (Boolean) -> Unit
    var action: () -> Unit
    var backAction: ButtonAction
    var feats: Map<String, Feat>
    var translations: Map<String, String>
}
