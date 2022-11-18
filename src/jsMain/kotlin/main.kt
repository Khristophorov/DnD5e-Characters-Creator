package me.khrys.dnd.charcreator.client

import csstype.Clear.Companion.both
import emotion.react.css
import me.khrys.dnd.charcreator.client.components.buttons.AddCharacter
import me.khrys.dnd.charcreator.client.components.buttons.CurrentCharacters
import me.khrys.dnd.charcreator.client.components.buttons.LogoutButton
import me.khrys.dnd.charcreator.client.components.validators.initValidators
import me.khrys.dnd.charcreator.common.LOGOUT_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import mui.material.CircularProgress
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useState

val MainDnd = FC<Props> {
    val (isLoading, setLoading) = useState(true)
    val (translations, setTranslations) = useState(emptyMap<String, String>())
    val (characters, setCharacters) = useState(emptyList<Character>())
    val (loadCharacters, setLoadCharacters) = useState(true)

    if (isLoading) {
        CircularProgress()
        loadMainData(
            loadTranslations = translations.isEmpty(),
            loadCharacters = loadCharacters,
            setLoadCharacters = { setLoadCharacters(it) },
            setTranslations = { setTranslations(it) },
            setCharacters = { setCharacters(it) },
            setLoading = { setLoading(it) }
        )
    } else {
        console.info("Rendering the app.")
        TranslationsContext.Provider(translations) {
            LogoutButton {
                +(translations[LOGOUT_TRANSLATION] ?: "")
            }

            CharactersContext.Provider(characters) {
                initValidators(characters)
                MainContent()
            }
        }
    }
}

private val MainContent = FC<Props> {
    console.info("Rendering content.")
    div {
        css { clear = both }
        AddCharacter()
        CurrentCharacters()
    }
}

private fun loadMainData(
    loadTranslations: Boolean,
    loadCharacters: Boolean,
    setLoadCharacters: (Boolean) -> Unit,
    setTranslations: (Map<String, String>) -> Unit,
    setCharacters: (List<Character>) -> Unit,
    setLoading: (Boolean) -> Unit
) {
    console.info("Loading initial data")
    if (loadTranslations) {
        loadTranslations { setTranslations(it) }
    }
    if (!loadTranslations && loadCharacters) {
        loadCharacters {
            setCharacters(it)
            setLoadCharacters(false)
        }
    }
    if (!(loadTranslations || loadCharacters)) {
        setLoading(false)
    }
}
