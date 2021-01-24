@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.client

import kotlinx.css.Clear.both
import kotlinx.css.clear
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import me.khrys.dnd.charcreator.client.components.logoutButton
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

data class WelcomeState(val name: String) : RState

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {

    init {
        state = WelcomeState(props.name)
    }

    override fun RBuilder.render() {
        logoutButton()

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
                            WelcomeState(name = (event.target as HTMLInputElement).value)
                        )
                    }
                }
            }
        }
    }
}
