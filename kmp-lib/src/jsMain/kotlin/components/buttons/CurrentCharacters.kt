package me.khrys.dnd.charcreator.client.components.buttons

import emotion.react.css
import me.khrys.dnd.charcreator.client.CharactersContext
import me.khrys.dnd.charcreator.client.components.dialogs.windows.CharacterWindow
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.use
import react.useState
import web.cssom.ClassName
import web.cssom.FlexWrap.Companion.wrap

var CurrentCharacters = FC<Props>("CurrentCharacters") {
    console.info("Rendering characters.")
    val characters = use(CharactersContext)
    div {
        css(ClassName(CLASS_INLINE)) {
            flexWrap = wrap
        }
        characters.forEach { currentCharacter ->
            val (openCharacter, setOpenCharacter) = useState(false)
            AvatarButton {
                this.character = currentCharacter
                this.action = {
                    setOpenCharacter(true)
                }
            }
            CharacterWindow {
                this.open = openCharacter
                this.setOpen = {
                    if (openCharacter != it) {
                        setOpenCharacter(it)
                    }
                }
                this.character = currentCharacter
            }
        }
    }
}
