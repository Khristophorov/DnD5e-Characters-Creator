package me.khrys.dnd.charcreator.client

import com.ccfraser.muirwik.components.mCircularProgress
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.Clear.both
import kotlinx.css.clear
import me.khrys.dnd.charcreator.client.components.buttons.addCharacter
import me.khrys.dnd.charcreator.client.components.buttons.currentCharacters
import me.khrys.dnd.charcreator.client.components.buttons.dLogoutButton
import me.khrys.dnd.charcreator.client.components.validators.initValidators
import me.khrys.dnd.charcreator.common.LOGOUT_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import react.Props
import react.RBuilder
import react.fc
import react.useState
import styled.css
import styled.styledDiv

val mainDnd = fc<Props> {
    val (isLoading, setLoading) = useState(true)
    val (translations, setTranslations) = useState(emptyMap<String, String>())
    val (characters, setCharacters) = useState(emptyList<Character>())

    if (isLoading) {
        loadMainData(
            translations = translations,
            setTranslations = { setTranslations(it) },
            setCharacters = { setCharacters(it) },
            setLoading = { setLoading(it) }
        )
    } else {
        TranslationsContext.Provider(translations) {
            dLogoutButton(translations[LOGOUT_TRANSLATION])

            CharactersContext.Provider(characters) {
                initValidators(characters)
                renderMainContent()
            }
        }
    }
}

private fun RBuilder.renderMainContent() {
    styledDiv {
        css { clear = both }

        child(addCharacter)
        child(currentCharacters)
    }
}

private fun RBuilder.loadMainData(
    translations: Map<String, String>,
    setTranslations: (Map<String, String>) -> Unit,
    setCharacters: (List<Character>) -> Unit,
    setLoading: (Boolean) -> Unit
) {
    mCircularProgress()
    MainScope().launch {
        if (translations.isEmpty()) {
            setTranslations(fetchTranslations())
            setCharacters(fetchCharacters())
        }
        setLoading(false)
    }
}
