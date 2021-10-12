package me.khrys.dnd.charcreator.client.components.buttons

import kotlinx.css.FlexWrap.wrap
import kotlinx.css.flexWrap
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.CharactersContext
import me.khrys.dnd.charcreator.client.components.dialogs.windows.characterWindow
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import react.Props
import react.fc
import react.useContext
import react.useState
import styled.css
import styled.styledDiv

var currentCharacters = fc<Props> {
    val characters = useContext(CharactersContext)
    styledDiv {
        attrs.classes = setOf(CLASS_INLINE)
        css {
            flexWrap = wrap
        }
        characters.forEach { character ->
            val (openCharacter, setOpenCharacter) = useState(false)
            dAvatarButton(character) { setOpenCharacter(true) }
            child(characterWindow) {
                attrs {
                    this.open = openCharacter
                    this.setOpen = { setOpenCharacter(it) }
                    this.character = character
                }
            }
        }
    }
}
