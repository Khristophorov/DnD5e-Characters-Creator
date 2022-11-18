package me.khrys.dnd.charcreator.client.components.inputs

import csstype.ClassName
import csstype.Display.Companion.inlineBlock
import csstype.Overflow
import csstype.px
import emotion.react.css
import me.khrys.dnd.charcreator.client.components.validators.SelectValidator
import me.khrys.dnd.charcreator.client.components.validators.SelectValidatorProps
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import mui.material.MenuItem
import react.FC
import react.PropsWithChildren
import react.dom.DangerouslySetInnerHTML
import react.dom.html.ReactHTML.div
import react.useState

external interface ValidatedListProps : SelectValidatorProps {
    var description: String
    var useDescription: Boolean
    var setDescription: (String) -> Unit
    var menuItems: Map<String, String>
}

val ValidatedList = FC<ValidatedListProps> { props ->
    val (currentDescription, setCurrentDescription) = useState("")
    div {
        this.className = ClassName(CLASS_INLINE)
        SelectValidator {
            this.label = props.label
            this.value = props.value
            this.validators = props.validators
            this.errorMessages = props.errorMessages
            this.onChange = props.onChange
            props.menuItems.forEach { (name, description) ->
                MenuItem {
                    this.value = name
                    this.onMouseOver = {
                        if (props.useDescription) {
                            props.setDescription(description)
                        }
                    }
                    this.onMouseLeave = {
                        if (props.useDescription) {
                            props.setDescription(currentDescription)
                        }
                    }
                    this.onClick = {
                        if (props.useDescription) {
                            setCurrentDescription(description)
                            props.setDescription(description)
                        }
                    }
                    +name
                }
            }
        }
        if (props.useDescription) {
            DescriptionFrame { +props.description }
        }
    }
}

private val DescriptionFrame = FC<PropsWithChildren> { props ->
    div {
        css(ClassName(CLASS_BORDERED)) {
            height = 300.px
            maxHeight = 300.px
            width = 800.px
            maxWidth = 800.px
            margin = 5.px
            display = inlineBlock
            overflow = "auto".unsafeCast<Overflow>()
        }
        this.dangerouslySetInnerHTML = DangerousHTML(props.children.toString()).unsafeCast<DangerouslySetInnerHTML>()
    }
}
