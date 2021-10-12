package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.dialogs.windows.newCharWindow
import me.khrys.dnd.charcreator.common.ADD_HOVER_TRANSLATION
import react.Props
import react.fc
import react.useContext
import react.useState

val addCharacter = fc<Props> {
    val (openDialog, setOpenDialog) = useState(false)
    val translations = useContext(TranslationsContext)
    child(newCharWindow) {
        attrs {
            open = openDialog
            setOpen = { setOpenDialog(it) }
        }
    }
    dPlusButton(translations[ADD_HOVER_TRANSLATION] ?: "") { setOpenDialog(true) }
}
