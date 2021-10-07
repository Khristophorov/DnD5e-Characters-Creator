package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Feature
import react.RProps

interface DialogProps : RProps {
    var open: Boolean
    var setOpen: (Boolean) -> Unit
}

interface CharDialogProps : DialogProps {
    var character: Character
    var action: () -> Unit
    var feats: Map<String, Feat>
    var useFeats: Boolean
}

interface FeatureProps<T> : DialogProps {
    var character: Character
    var feature: Feature
    var function: DnDFunction
    var setValue: (T) -> Unit
}

interface MultipleFeatureProps: FeatureProps<Array<String>> {
    var size: Int
}

interface FeatsProps : DialogProps {
    var feature: Feature
    var character: Character
    var feats: Map<String, Feat>
    var action: () -> Unit
}
