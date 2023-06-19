package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.client.components.buttons.ButtonAction
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Feature
import react.FC
import react.PropsWithChildren
import react.memo

external interface DialogProps : PropsWithChildren {
    var open: Boolean
    var setOpen: (Boolean) -> Unit
    var header: String
    var action: () -> (Unit)
}

external interface CharDialogProps : FeatsProps

external interface FeatureProps<T> : DialogProps {
    var character: Character
    var feature: Feature
    var function: DnDFunction
    var value: T
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
}

external interface MultipleFeaturesFeatsProps : DialogProps {
    var features: List<Feature>
    var character: Character
    var feats: Map<String, Feat>
    var useFeats: Boolean
}

external interface CharBasedProps : DialogProps {
    var character: Character
    var backAction: ButtonAction
    var translations: Map<String, String>
}

fun <T : DialogProps> memoDialog(component: FC<T>) = memo(component) { p1, p2 -> p1.open == p2.open }
