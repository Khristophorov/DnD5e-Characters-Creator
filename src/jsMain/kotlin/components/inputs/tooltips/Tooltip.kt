package me.khrys.dnd.charcreator.client.components.inputs.tooltips

import csstype.ClassName
import me.khrys.dnd.charcreator.client.createDefaultTheme
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.CLASS_DISABLE_POINTER
import mui.material.Popover
import mui.material.PopoverOrigin
import mui.material.TooltipProps
import mui.material.Typography
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import org.w3c.dom.events.EventTarget
import react.FC
import react.dom.DangerouslySetInnerHTML
import react.dom.html.ReactHTML.div
import react.useState
import web.dom.Element

val tooltipTheme = createTooltipTheme()

fun createTooltipTheme(): Theme {
    return createDefaultTheme(
        js(
            """
            {
                components: {
                    MuiPopover: {
                        styleOverrides: {
                            paper: {
                                color: '#fff',
                                padding: '4px 8px',
                                fontSize: '0.625rem',
                                maxWidth: '300px',
                                wordWrap: 'break-word',
                                lineHeight: '1.4em',
                                borderRadius: '4px',
                                backgroundColor: 'rgba(97, 97, 97, 0.9)'
                            }
                        }
                    }
                }
            }
            """
        )
    )
}

val Tooltip = FC<TooltipProps> { props ->
    val (popoverAnchor, setPopoverAnchor) = useState<EventTarget?>(null)
    div {
        ThemeProvider {
            this.theme = tooltipTheme
            Typography {
                props.id?.let { this.id = it }
                this.onMouseEnter = { event -> setPopoverAnchor(event.currentTarget.unsafeCast<EventTarget>()) }
                this.onMouseLeave = { setPopoverAnchor(null) }
                child(props.children)
            }
            Popover {
                this.open = popoverAnchor != null
                this.anchorEl = popoverAnchor.unsafeCast<Element>()
                this.disableRestoreFocus = true
                this.transformOrigin = object : PopoverOrigin {
                    override var horizontal = "center"
                    override var vertical = "top"
                }
                this.className = ClassName("${props.className} $CLASS_DISABLE_POINTER")
                this.anchorOrigin = object : PopoverOrigin {
                    override var horizontal = "center"
                    override var vertical = "bottom"
                }
                this.onClose = { _, _ -> setPopoverAnchor(null) }

                div {
                    this.dangerouslySetInnerHTML =
                        DangerousHTML(props.title.toString()).unsafeCast<DangerouslySetInnerHTML>()
                }
            }
        }
    }
}
