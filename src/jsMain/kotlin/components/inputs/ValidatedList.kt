package me.khrys.dnd.charcreator.client.components.inputs

import com.ccfraser.muirwik.components.menuItem
import kotlinx.css.Display.inlineBlock
import kotlinx.css.Overflow.auto
import kotlinx.css.display
import kotlinx.css.height
import kotlinx.css.margin
import kotlinx.css.maxHeight
import kotlinx.css.maxWidth
import kotlinx.css.overflow
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.validators.dSelectValidator
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import org.w3c.dom.events.Event
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.dValidatedList(
    label: String = "",
    value: String,
    validators: Array<String>,
    errorMessages: Array<String>,
    onChange: (Event) -> Unit,
    menuItems: Map<String, String>,
    setDescription: (String) -> Unit = {},
    description: String = "",
    useDescription: Boolean = true
) {
    styledDiv {
        attrs.classes = setOf(CLASS_INLINE)
        dSelectValidator(
            label = label,
            value = value,
            validators = validators,
            errorMessages = errorMessages,
            onChange = onChange
        ) {
            menuItems.forEach { (name, description) ->
                menuItem(value = name) {
                    attrs.onMouseOver = { setDescription(description) }
                    attrs.onSelect = { setDescription(description) }
                    +name
                }
            }
        }
        if (useDescription) {
            dDescriptionFrame(description)
        }
    }
}

private fun RBuilder.dDescriptionFrame(description: String) {
    styledDiv {
        attrs.classes = setOf(CLASS_BORDERED)
        css {
            height = 300.px
            maxHeight = 300.px
            width = 800.px
            maxWidth = 800.px
            margin = "5px"
            display = inlineBlock
            overflow = auto
        }
        attrs[DANGEROUS_HTML] = DangerousHTML(description)
    }
}
