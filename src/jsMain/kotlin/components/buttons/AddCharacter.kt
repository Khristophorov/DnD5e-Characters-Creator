package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.dialogs.windows.NewCharWindow
import me.khrys.dnd.charcreator.common.ADD_HOVER_TRANSLATION
import react.FC
import react.Props
import react.useContext
import react.useState

val AddCharacter = FC<Props> {
    console.info("Add characters button rendering")
    val (open, setOpen) = useState(false)
    val translations = useContext(TranslationsContext)
    NewCharWindow {
        this.open = open
        this.setOpen = { setOpen(it) }
    }
    PlusButton {
        +(translations[ADD_HOVER_TRANSLATION] ?: "")
        this.onClick = {
            setOpen(true)
        }
    }
}
