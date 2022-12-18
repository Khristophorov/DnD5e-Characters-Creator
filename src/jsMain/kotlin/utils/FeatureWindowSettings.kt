package me.khrys.dnd.charcreator.client.utils

import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feature
import react.StateSetter
import react.useState

class FeatureWindowSettings(
    val open: Boolean,
    val setOpen: StateSetter<Boolean>,
    val feature: Feature,
    val setFeature: StateSetter<Feature>,
    val function: DnDFunction,
    val setFunction: StateSetter<DnDFunction>
) {
    fun setParams(open: Boolean, feature: Feature, function: DnDFunction) {
        this.setOpen(open)
        this.setFeature(feature)
        this.setFunction(function)
    }
}

fun useFeatureWindowSettings(): FeatureWindowSettings {
    val (open, setOpen) = useState(false)
    val (feature, setFeature) = useState(Feature("", ""))
    val (function, setFunction) = useState(DnDFunction(""))

    return FeatureWindowSettings(open, setOpen, feature, setFeature, function, setFunction)
}
