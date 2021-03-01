package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.MGridAlignContent
import com.ccfraser.muirwik.components.MGridAlignItems
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.mGridContainer
import com.ccfraser.muirwik.components.mGridItem
import kotlinx.browser.document
import kotlinx.css.height
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.InputType.file
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.imageFromEvent
import me.khrys.dnd.charcreator.client.storeCharacter
import me.khrys.dnd.charcreator.client.validators.InputProps
import me.khrys.dnd.charcreator.client.validators.dTextValidator
import me.khrys.dnd.charcreator.client.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.CLASS_DISABLED
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.ENTER_IMAGE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_IMAGE_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_LABEL_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_TRANSLATION
import me.khrys.dnd.charcreator.common.FILE_INPUT_ID
import me.khrys.dnd.charcreator.common.FINISH_TRANSLATION
import me.khrys.dnd.charcreator.common.NAME_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.SAVE_TRANSLATION
import me.khrys.dnd.charcreator.common.UPLOAD_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.emptyCharacter
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import react.RBuilder
import react.RProps
import react.functionalComponent
import react.useContext
import react.useState
import styled.css
import styled.styledImg

interface DialogProps : RProps {
    var open: Boolean
    var setOpen: (Boolean) -> Unit
}

interface CharDialogProps : DialogProps {
    var character: Character
}

val newCharWindow = functionalComponent<DialogProps> { props ->
    val translations = useContext(TranslationsContext)
    val (imageDialogOpen, setImageDialogOpen) = useState { false }
    val (newCharacter, setNewCharacter) = useState { emptyCharacter() }

    charNameWindow(translations, newCharacter, props.open) {
        setImageDialogOpen(true)
        props.setOpen(false)
    }
    charImageWindow(translations, newCharacter, imageDialogOpen, {
        setImageDialogOpen(false)
        props.setOpen(true)
    }) {
        setImageDialogOpen(false)
        storeCharacter(newCharacter)
        setNewCharacter(emptyCharacter())
    }
}

val characterWindow = functionalComponent<CharDialogProps> { props ->
    val translations = useContext(TranslationsContext)
    val character = props.character
    mDialog(open = props.open, fullScreen = true) {
        mDialogTitle(text = character.name) {
            closeButton { props.setOpen(false) }
        }
        mDialogContent(dividers = true) {
            dValidatorForm(onSubmit = { props.setOpen(false) }) {
                mGridContainer(
                    alignContent = MGridAlignContent.flexStart,
                    alignItems = MGridAlignItems.flexStart
                ) {
                    mGridItem {
                        styledImg(src = character.image) {
                            css {
                                width = 128.px
                                height = 128.px
                            }
                        }
                    }
                }
                mDialogActions {
                    dSubmit(translations[SAVE_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.charNameWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    action: (Event) -> Unit
) {
    mDialog(open = open) {
        val (inputValue, setInputValue) = useState("")

        mDialogTitle(text = translations[ENTER_NAME_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            mDialogContentText(text = translations[ENTER_NAME_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(onSubmit = { event ->
                newCharacter.name = inputValue
                setInputValue("")
                action(event)
            }) {
                dTextValidator(
                    label = translations[ENTER_NAME_LABEL_TRANSLATION] ?: "",
                    value = inputValue,
                    validators = arrayOf(VALIDATION_REQUIRED),
                    errorMessages = arrayOf(translations[NAME_SHOULD_BE_FILLED_TRANSLATION] ?: ""),
                    onChange = { event -> setInputValue((event.target as HTMLInputElement).value) }
                )
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

fun RBuilder.charImageWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    mDialog(open = open) {
        val (inputValue, setInputValue) = useState("")

        mDialogTitle(text = translations[ENTER_IMAGE_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            mDialogContentText(text = translations[ENTER_IMAGE_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(onSubmit = { event ->
                setInputValue("")
                action(event)
            }) {
                dTextValidator(
                    id = FILE_INPUT_ID,
                    type = file,
                    value = inputValue,
                    className = CLASS_DISABLED,
                    inputProps = InputProps(accept = "image/*"),
                    validators = arrayOf(VALIDATION_REQUIRED),
                    onChange = { event ->
                        setInputValue((event.target as HTMLInputElement).value)
                        imageFromEvent(event) { e -> newCharacter.image = (e.target as FileReader).result as String }
                    }
                )
                dButton(
                    caption = translations[UPLOAD_TRANSLATION] ?: ""
                ) { (document.getElementById(FILE_INPUT_ID) as HTMLElement).click() }
                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    backButton(backAction)
                    dSubmit(caption = translations[FINISH_TRANSLATION] ?: "")
                }
            }
        }
    }
}
