@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.client

import com.ccfraser.muirwik.components.mCircularProgress
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.Clear.both
import kotlinx.css.clear
import me.khrys.dnd.charcreator.client.components.addCharacter
import me.khrys.dnd.charcreator.client.components.currentCharacters
import me.khrys.dnd.charcreator.client.components.logoutButton
import me.khrys.dnd.charcreator.client.validators.initValidators
import me.khrys.dnd.charcreator.common.LOGOUT_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import react.RBuilder
import react.RProps
import react.RSetState
import react.child
import react.functionalComponent
import react.useState
import styled.css
import styled.styledDiv

val mainDnd = functionalComponent<RProps> {
    val (isLoading, setLoading) = useState(true)
    val (translations, setTranslations) = useState(emptyMap<String, String>())
    val (characters, setCharacters) = useState(emptyArray<Character>())

    if (isLoading) {
        loadMainData(
            translations = translations,
            setTranslations = setTranslations,
            setCharacters = setCharacters,
            setLoading = setLoading
        )
    } else {
        TranslationsContext.Provider(translations) {
            logoutButton(translations[LOGOUT_TRANSLATION])

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
    setTranslations: RSetState<Map<String, String>>,
    setCharacters: RSetState<Array<Character>>,
    setLoading: RSetState<Boolean>
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
