package me.khrys.dnd.charcreator.client.components.dialogs

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

external interface CharDialogProps : DialogProps {
    var character: Character
    var action: () -> Unit
    var feats: Map<String, Feat>
    var useFeats: Boolean
}

external interface FeatureProps<T> : DialogProps {
    var character: Character
    var feature: Feature
    var function: DnDFunction
    var setValue: (T) -> Unit
}

external interface MultipleFeatureProps: FeatureProps<List<String>> {
    var size: Int
}

external interface FeatsProps : DialogProps {
    var feature: Feature
    var character: Character
    var feats: Map<String, Feat>
    var action: () -> Unit
}

external interface CharRaceProps : Props {
    var newCharacter: Character
    var open: Boolean
    var setOpen: (Boolean) -> Unit
    var action: () -> Unit
    var backAction: () -> Unit
    var feats: Map<String, Feat>
}
