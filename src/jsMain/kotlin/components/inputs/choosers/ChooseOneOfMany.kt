package me.khrys.dnd.charcreator.client.components.inputs.choosers

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.targetValue
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.inputs.dValidatedList
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
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
    mDialog(open = open) {
        mDialogTitle(text = header)
        mDialogContent(dividers = true) {
            mDialogContentText(text = "") {
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
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}
