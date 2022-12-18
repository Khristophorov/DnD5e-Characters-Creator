package me.khrys.dnd.charcreator.client

import csstype.Clear.Companion.both
import emotion.react.css
import me.khrys.dnd.charcreator.client.components.buttons.AddCharacter
import me.khrys.dnd.charcreator.client.components.buttons.CurrentCharacters
import me.khrys.dnd.charcreator.client.components.buttons.LogoutButton
import me.khrys.dnd.charcreator.client.components.validators.initValidators
import me.khrys.dnd.charcreator.client.utils.loadCharacters
import me.khrys.dnd.charcreator.client.utils.loadTranslations
import me.khrys.dnd.charcreator.common.LOGOUT_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import mui.material.CircularProgress
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useState

val MainDnd = FC<Props> {
    val (translations, setTranslations) = useState<Map<String, String>?>(null)
    val (characters, setCharacters) = useState<List<Character>?>(null)

    if (translations == null) {
        CircularProgress()
        loadTranslations { setTranslations(it) }
    }
    else if (characters == null) {
        CircularProgress()
        loadCharacters { setCharacters(it) }
    } else {
        console.info("Rendering the app.")
        TranslationsContext.Provider(translations) {
            LogoutButton {
                +(translations[LOGOUT_TRANSLATION] ?: "")
            }

            CharactersContext.Provider(characters) {
                initValidators(characters)
                mainContent()
            }
        }
    }
}

private val mainContent = FC<Props> {
    console.info("Rendering content.")
    div {
        css { clear = both }
        AddCharacter()
        CurrentCharacters()
    }
}
