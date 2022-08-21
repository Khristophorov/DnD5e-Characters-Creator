package me.khrys.dnd.charcreator.client.components.inputs.tooltips

import com.ccfraser.muirwik.components.PopoverHorizontalPosition.center
import com.ccfraser.muirwik.components.PopoverVerticalPosition.bottom
import com.ccfraser.muirwik.components.PopoverVerticalPosition.top
import com.ccfraser.muirwik.components.TypographyProps
import com.ccfraser.muirwik.components.anchorOriginHorizontal
import com.ccfraser.muirwik.components.anchorOriginVertical
import com.ccfraser.muirwik.components.onClose
import com.ccfraser.muirwik.components.popover
import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.themeProvider
import com.ccfraser.muirwik.components.transformOriginHorizontal
import com.ccfraser.muirwik.components.transformOriginVertical
import com.ccfraser.muirwik.components.typography
import me.khrys.dnd.charcreator.client.createDefaultTheme
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.CLASS_DISABLE_POINTER
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import org.w3c.dom.Node
import org.w3c.dom.events.EventTarget
import react.RBuilder
import react.dom.div
import react.useState
import styled.StyledHandler

val tooltipTheme = createTooltipTheme()

fun createTooltipTheme(): Theme {
    return createDefaultTheme(js("""
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
        }""")
    )
}

fun RBuilder.dTooltip(
    title: String,
    id: String? = null,
    className: String? = null,
    handler: StyledHandler<TypographyProps>? = null
) {
    val (anchorE1, setAnchorE1) = useState<EventTarget?>(null)
    div {
        themeProvider(tooltipTheme) {
            typography {
                id?.let { attrs.id = id }
                attrs.onMouseEnter = { event -> setAnchorE1(event.currentTarget) }
                attrs.onMouseLeave = { setAnchorE1(null) }
                handler?.let { handler() }
            }
            popover(
                open = anchorE1 != null
            ) {
                attrs.anchorEl = anchorE1 as Node?
                attrs.disableRestoreFocus = true
                attrs.transformOriginHorizontal = center
                attrs.transformOriginVertical = top
                attrs.className = "$className $CLASS_DISABLE_POINTER"
                attrs.anchorOriginHorizontal = center
                attrs.anchorOriginVertical = bottom
                attrs.onClose = { _, _ -> setAnchorE1(null) }

                div {
                    attrs[DANGEROUS_HTML] = DangerousHTML(title)
                }
            }
        }
    }
}
