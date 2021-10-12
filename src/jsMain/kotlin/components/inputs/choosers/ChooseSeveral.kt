package me.khrys.dnd.charcreator.client.components.inputs.choosers

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.targetValue
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.inputs.dValidatedList
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.client.components.validators.validatorForm
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALIDATION_VALUE_ALREADY_PRESENT
import me.khrys.dnd.charcreator.common.VALUE_IS_ALREADY_SELECTED_TRANSLATION
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import react.RBuilder
import react.useContext
import react.useState
import styled.styledDiv

fun RBuilder.chooseSeveral(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    header: String,
    description: String,
    size: Int,
    values: List<String>,
    setValue: (List<String>) -> Unit
) {
    val (chosenValues, setChosenValues) = useState(emptyList<String>())
    val translations = useContext(TranslationsContext)
    mDialog(open = open) {
        if (open && chosenValues.isEmpty()) {
            setChosenValues(MutableList(size) { "" })
        }
        if (!(open || chosenValues.isEmpty())) {
            setChosenValues(emptyList())
        }
        mDialogTitle(text = header)
        mDialogContent(dividers = true) {
            mDialogContentText(text = "") {
                styledDiv {
                    attrs[DANGEROUS_HTML] = DangerousHTML(description)
                }
            }
            dValidatorForm(onSubmit = {
                setValue(chosenValues)
                setOpen(false)
            }) {
                styledDiv {
                    attrs.classes = setOf(CLASS_INLINE)
                    for (i in 0 until size) {
                        validatorForm.values = chosenValues.toTypedArray()
                        dValidatedList(
                            value = if (chosenValues.isEmpty()) "" else chosenValues[i],
                            validators = arrayOf(VALIDATION_REQUIRED, VALIDATION_VALUE_ALREADY_PRESENT),
                            errorMessages = arrayOf(
                                translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: "",
                                translations[VALUE_IS_ALREADY_SELECTED_TRANSLATION] ?: ""
                            ),
                            onChange = { event ->
                                val newValues = chosenValues.toMutableList()
                                newValues[i] = event.targetValue as String
                                setChosenValues(newValues)
                            },
                            menuItems = values.associateWith { "" },
                            useDescription = false
                        )
                    }
                }
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}
