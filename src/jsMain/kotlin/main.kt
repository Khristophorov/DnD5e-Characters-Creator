@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.client

import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.Clear.both
import kotlinx.css.clear
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import me.khrys.dnd.charcreator.client.components.logoutButton
import me.khrys.dnd.charcreator.common.LOGOUT_TRANSLATION
import me.khrys.dnd.charcreator.common.TRANSLATIONS_URL
import me.khrys.dnd.charcreator.common.models.Translation
import org.w3c.dom.HTMLInputElement
import react.RProps
import react.dom.div
import react.dom.input
import react.functionalComponent
import react.useState
import styled.css
import styled.styledDiv

val mainDnd = functionalComponent<RProps> {
    val (name, setName) = useState("Kotlin/JS")
    val (isLoading, setLoading) = useState(true)
    val (translations, setTranslations) = useState(emptyMap<String, String>())

    if (isLoading) {
        MainScope().launch {
            if (translations.isEmpty()) {
                setTranslations(fetchTranslations())
            }
            setLoading(false)
        }
    } else {
        logoutButton(translations[LOGOUT_TRANSLATION])

        styledDiv {
            css { clear = both }
            div {
                +"Hello, $name"
            }
            input {
                attrs {
                    type = InputType.text
                    value = name
                    onChangeFunction = { event ->
                        setName((event.target as HTMLInputElement).value)
                    }
                }
            }
        }
    }
}

suspend fun fetchTranslations(): Map<String, String> =
    window.fetch(TRANSLATIONS_URL).await().json().await().unsafeCast<Array<Translation>>()
        .map { it._id to it.value }.toMap()
