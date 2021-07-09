package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feature
import org.w3c.dom.events.Event
import react.RProps

interface DialogProps : RProps {
    var open: Boolean
    var setOpen: (Boolean) -> Unit
}

interface CharDialogProps : DialogProps {
    var character: Character
    var action: () -> Unit
    var checkPresence: () -> Boolean
}

interface ChooseOfManyProps : DialogProps {
    var header: String
    var description: String
    var values: Array<String>
    var setValue: (String) -> Unit
}

interface FeatureProps : DialogProps {
    var character: Character
    var feature: Feature
    var function: DnDFunction
    var setValue: (String) -> Unit
}
