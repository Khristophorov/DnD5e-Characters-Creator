package me.khrys.dnd.charcreator.client

import emotion.react.css
import me.khrys.dnd.charcreator.client.components.buttons.AddCharacter
import me.khrys.dnd.charcreator.client.components.buttons.CurrentCharacters
import me.khrys.dnd.charcreator.client.components.buttons.LogoutButton
import me.khrys.dnd.charcreator.client.components.validators.initValidators
import me.khrys.dnd.charcreator.client.utils.loadCharacters
import me.khrys.dnd.charcreator.client.utils.loadSpells
import me.khrys.dnd.charcreator.client.utils.loadTranslations
import me.khrys.dnd.charcreator.common.LOGOUT_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Spell
import mui.material.CircularProgress
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useState
import web.cssom.Clear.Companion.both

val MainDnd = FC<Props>("MainDnd") {
    val (translations, setTranslations) = useState<Map<String, String>?>(null)
    val (characters, setCharacters) = useState<List<Character>?>(null)
    val (spells, setSpells) = useState(emptyMap<String, Spell>())

    if (translations == null) {
        CircularProgress()
        loadTranslations { setTranslations(it) }
    }
    else if (characters == null) {
        CircularProgress()
        loadCharacters { setCharacters(it) }
    } else if (spells.isEmpty()) {
        CircularProgress()
        loadSpells { setSpells(it) }
    } else {
        console.info("Rendering the app.")
        TranslationsContext.Provider(translations) {
            LogoutButton {
                +(translations[LOGOUT_TRANSLATION] ?: "")
            }

            CharactersContext.Provider(characters) {
                SpellsContext.Provider(spells) {
                    initValidators(characters)
                    MainContent()
                }
            }
        }
    }
}

private val MainContent = FC<Props>("MainContent") {
    console.info("Rendering content.")
    div {
        css { clear = both }
        AddCharacter()
        CurrentCharacters()
    }
}
