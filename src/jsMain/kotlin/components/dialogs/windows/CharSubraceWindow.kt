package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.targetValue
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.dBackButton
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.dialogs.CharRaceProps
import me.khrys.dnd.charcreator.client.components.dialogs.collectSubraceFeatures
import me.khrys.dnd.charcreator.client.components.inputs.dValidatedList
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.ENTER_SUBRACE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SUBRACE_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.SUBRACE_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.models.emptyRace
import react.fc
import react.useContext
import react.useState

val charSubraceWindow = fc<CharRaceProps> { props ->
    val subraces = props.newCharacter.race.subraces.associateBy { it._id }
    val (subrace, setSubrace) = useState(emptyRace())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    mDialog(open = props.open) {
        val translations = useContext(TranslationsContext)
        mDialogTitle(text = translations[ENTER_SUBRACE_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            mDialogContentText(text = translations[ENTER_SUBRACE_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(onSubmit = {
                props.newCharacter.subrace = subrace
                if (subrace.features.isEmpty()) {
                    props.action()
                } else {
                    setOpenFeatures(true)
                }
            }) {
                dValidatedList(
                    label = translations[ENTER_SUBRACE_TRANSLATION] ?: "",
                    value = subrace._id,
                    validators = arrayOf(VALIDATION_REQUIRED),
                    errorMessages = arrayOf(translations[SUBRACE_SHOULD_BE_FILLED_TRANSLATION] ?: ""),
                    onChange = { event -> setSubrace(subraces[event.targetValue] ?: emptyRace()) },
                    menuItems = subraces.mapValues { it.value.description },
                    setDescription = { setDescription(it) },
                    description = description
                )
                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    dBackButton {
                        setSubrace(emptyRace())
                        setDescription("")
                        props.backAction()
                    }
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
    child(collectSubraceFeatures) {
        attrs.character = props.newCharacter
        attrs.open = openFeatures
        attrs.setOpen = { setOpenFeatures(it) }
        attrs.action = props.action
    }
}
