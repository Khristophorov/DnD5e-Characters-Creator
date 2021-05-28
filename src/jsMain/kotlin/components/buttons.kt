package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.MColor.primary
import com.ccfraser.muirwik.components.MTypographyAlign.center
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonSize.large
import com.ccfraser.muirwik.components.button.MButtonSize.medium
import com.ccfraser.muirwik.components.button.MButtonSize.small
import com.ccfraser.muirwik.components.button.MButtonVariant.contained
import com.ccfraser.muirwik.components.button.MFabProps
import com.ccfraser.muirwik.components.button.MIconButtonProps
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mFab
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mTooltip
import com.ccfraser.muirwik.components.mTypography
import kotlinx.browser.window
import kotlinx.css.Display.block
import kotlinx.css.FlexWrap.wrap
import kotlinx.css.Float.left
import kotlinx.css.Float.right
import kotlinx.css.display
import kotlinx.css.flexWrap
import kotlinx.css.float
import kotlinx.css.margin
import kotlinx.css.padding
import kotlinx.html.InputType.submit
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.CharactersContext
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.ADD_HOVER_TRANSLATION
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.LOGOUT_URL
import me.khrys.dnd.charcreator.common.models.Character
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.useContext
import react.useState
import styled.StyledHandler
import styled.css
import styled.styledDiv

fun RBuilder.dButton(caption: String, action: (Event) -> Unit) {
    mButton(
        caption = caption,
        color = primary,
        variant = contained,
        onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        },
        size = small
    )
}

fun RBuilder.dIconButton(action: (Event) -> Unit, handler: StyledHandler<MIconButtonProps>? = null) {
    mIconButton(
        onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        },
        handler = handler
    )
}

fun RBuilder.dFab(
    size: MButtonSize = medium,
    className: String? = null,
    action: (Event) -> Unit,
    handler: StyledHandler<MFabProps>? = null
) {
    mFab(
        color = primary,
        size = size,
        onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        },
        className = className,
        handler = handler
    )
}

fun RBuilder.dSubmit(caption: String) {
    mInput(type = submit, value = caption) {
        attrs.onClick = { playSound(BUTTON_SOUND_ID) }
    }
}

fun RBuilder.logoutButton(caption: String?) {
    styledDiv {
        css { float = right }
        dButton(caption = caption ?: "", action = { window.location.href = LOGOUT_URL })
    }
}

val addCharacter = functionalComponent<RProps> {
    val (openDialog, setOpenDialog) = useState(false)
    val translations = useContext(TranslationsContext)
    child(newCharWindow) {
        attrs {
            open = openDialog
            setOpen = setOpenDialog
        }
    }
    plusButton(translations[ADD_HOVER_TRANSLATION] ?: "") { setOpenDialog(true) }
}

var currentCharacters = functionalComponent<RProps> {
    val characters = useContext(CharactersContext)
    styledDiv {
        attrs.classes = setOf(CLASS_INLINE)
        css {
            flexWrap = wrap
        }
        characters.forEach { character ->
            val (openCharacter, setOpenCharacter) = useState(false)
            avatarButton(character) { setOpenCharacter(true) }
            child(characterWindow) {
                attrs {
                    this.open = openCharacter
                    this.setOpen = setOpenCharacter
                    this.character = character
                }
            }
        }
    }
}

fun RBuilder.avatarButton(character: Character, action: (Event) -> Unit) {
    styledDiv {
        css {
            display = block
            margin = "5px"
        }
        dFab(size = large, className = CLASS_CENTER, action = action) {
            mAvatar(src = character.image)
        }
        mTypography(
            text = character.name,
            align = center
        )
    }
}

fun RBuilder.plusButton(title: String, action: (Event) -> Unit) {
    styledDiv {
        css {
            float = right
            padding = "15px"
        }
        mTooltip(title = title) {
            dFab(action = action) { dAddIcon() }
        }
    }
}

fun RBuilder.closeButton(action: (Event) -> Unit) {
    styledDiv {
        css {
            float = right
        }
        dIconButton(action = action) { dCloseIcon() }
    }
}

fun RBuilder.backButton(action: (Event) -> Unit) {
    styledDiv {
        css { float = left }
        dFab(action = action) { dArrowBackIcon() }
    }
}
