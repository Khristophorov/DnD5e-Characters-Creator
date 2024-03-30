package me.khrys.dnd.charcreator.client.components.dialogs

import kotlinx.browser.window
import me.khrys.dnd.charcreator.client.components.dialogs.windows.CharClassWindow
import me.khrys.dnd.charcreator.client.utils.storeCharacter
import react.FC

val LevelUp = FC<CharBasedProps> { props ->
    if (props.open) {
        console.info("Rendering Level up window")
        CharClassWindow {
            this.character = props.character
            this.open = props.open
            this.backAction = { window.location.reload() }
            this.action = {
                props.setOpen(false)
                storeCharacter(props.character)
            }
        }
    }
}
