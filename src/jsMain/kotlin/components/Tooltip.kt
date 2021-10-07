package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.MPopoverHorizontalPosition.center
import com.ccfraser.muirwik.components.MPopoverVerticalPosition.bottom
import com.ccfraser.muirwik.components.MPopoverVerticalPosition.top
import com.ccfraser.muirwik.components.MTypographyProps
import com.ccfraser.muirwik.components.mPopover
import com.ccfraser.muirwik.components.mThemeProvider
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.transformOriginHorizontal
import com.ccfraser.muirwik.components.transformOriginVertical
import me.khrys.dnd.charcreator.client.createDefaultTheme
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.CLASS_DISABLE_POINTER
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import org.w3c.dom.Node
import org.w3c.dom.events.EventTarget
import react.RBuilder
import react.ReactElement
import react.dom.div
import react.useState
import styled.StyledHandler

val tooltipTheme = createTooltipTheme()

fun createTooltipTheme(): Theme {
    val theme = createDefaultTheme()
    theme.overrides.MuiPopover = js("""{
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
    }""")
    return theme
}

fun RBuilder.dTooltip(
    title: String,
    id: String? = null,
    className: String? = null,
    handler: StyledHandler<MTypographyProps>? = null
): ReactElement {
    val (anchorE1, setAnchorE1) = useState<EventTarget?>(null)
    return div {
        mThemeProvider(tooltipTheme) {
            mTypography {
                id?.let { attrs.id = id }
                attrs.onMouseEnter = { event -> setAnchorE1(event.currentTarget) }
                attrs.onMouseLeave = { setAnchorE1(null) }
                handler?.let { handler() }
            }
            mPopover(
                className = "$className $CLASS_DISABLE_POINTER",
                open = anchorE1 != null,
                anchorOriginHorizontal = center,
                anchorOriginVertical = bottom,
                onClose = { _, _ -> setAnchorE1(null) }
            ) {
                attrs.anchorEl = anchorE1 as Node?
                attrs.disableRestoreFocus = true
                attrs.transformOriginHorizontal = center
                attrs.transformOriginVertical = top

                div {
                    attrs[DANGEROUS_HTML] = DangerousHTML(title)
                }
            }
        }
    }
}
