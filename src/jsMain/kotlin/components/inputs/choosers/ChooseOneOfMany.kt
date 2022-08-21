package me.khrys.dnd.charcreator.client.components.inputs.choosers

import com.ccfraser.muirwik.components.dialog
import com.ccfraser.muirwik.components.dialogActions
import com.ccfraser.muirwik.components.dialogContent
import com.ccfraser.muirwik.components.dialogContentText
import com.ccfraser.muirwik.components.dialogTitle
import com.ccfraser.muirwik.components.utils.targetValue
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.inputs.dValidatedList
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import react.RBuilder
import react.useContext
import react.useState
import styled.styledDiv

fun RBuilder.chooseOneOfMany(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    header: String,
    description: String,
    values: List<String>,
    setValue: (String) -> Unit
) {
    val (chosenValue, setChosenValue) = useState("")
    val translations = useContext(TranslationsContext)
    dialog(open = open) {
        dialogTitle(text = header)
        dialogContent(dividers = true) {
            dialogContentText(text = "") {
                styledDiv {
                    attrs[DANGEROUS_HTML] = DangerousHTML(description)
                }
            }
            dValidatorForm(onSubmit = {
                setValue(chosenValue)
                setOpen(false)
            }) {
                dValidatedList(
                    value = chosenValue,
                    validators = arrayOf(VALIDATION_REQUIRED),
                    errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: ""),
                    onChange = { event -> setChosenValue(event.targetValue as String) },
                    menuItems = values.associateWith { "" },
                    useDescription = false
                )
                dialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}
