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
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.input
import styled.css
import styled.styledDiv

external interface WelcomeProps : RProps {
    var name: String
}

data class WelcomeState(
    val name: String,
    val translations: Map<String, String> = emptyMap(),
    val isLoading: Boolean = true
) : RState

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {

    init {
        state = WelcomeState(props.name)
    }

    override fun RBuilder.render() {

        if (state.isLoading) {
            MainScope().launch {
                val translations = fetchTranslations()
                setState(WelcomeState(state.name, translations, false))
            }
        } else {
            val translations = state.translations

            logoutButton(translations[LOGOUT_TRANSLATION])

            styledDiv {
                css { clear = both }
                div {
                    +"Hello, ${state.name}"
                }
                input {
                    attrs {
                        type = InputType.text
                        value = state.name
                        onChangeFunction = { event ->
                            setState(
                                WelcomeState(name = (event.target as HTMLInputElement).value, state.translations, true)
                            )
                        }
                    }
                }
            }
        }
    }
}

suspend fun fetchTranslations(): Map<String, String> =
    window.fetch(TRANSLATIONS_URL).await().json().await().unsafeCast<Array<Translation>>()
        .map { it._id to it.value }.toMap()
