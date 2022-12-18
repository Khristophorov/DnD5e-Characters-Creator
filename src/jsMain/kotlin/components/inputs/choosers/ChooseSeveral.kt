package me.khrys.dnd.charcreator.client.components.inputs.choosers

import csstype.ClassName
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.components.validators.validatorFormRules
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALIDATION_VALUE_ALREADY_PRESENT
import me.khrys.dnd.charcreator.common.VALUE_IS_ALREADY_SELECTED_TRANSLATION
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.dom.DangerouslySetInnerHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import react.useContext
import react.useState

val ChooseSeveral = FC<ChooserProps<List<String>>> { props ->
    val (chosenValues, setChosenValues) = useState(emptyList<String>())
    val translations = useContext(TranslationsContext)
    Dialog {
        this.open = props.open
        if (props.open && chosenValues.isEmpty()) {
            setChosenValues(MutableList(props.size) { "" })
        }
        if (!(props.open || chosenValues.isEmpty())) {
            setChosenValues(emptyList())
        }
        DialogTitle {
            +props.header
        }
        DialogContent {
            this.dividers = true
            DialogContentText {
                span {
                    this.dangerouslySetInnerHTML =
                        DangerousHTML(props.description).unsafeCast<DangerouslySetInnerHTML>()
                }
            }
            ValidatorForm {
                this.onSubmit = {
                    props.setValue(chosenValues)
                    props.setOpen(false)
                }
                div {
                    this.className = ClassName(CLASS_INLINE)
                    for (i in 0 until props.size) {
                        validatorFormRules.values = chosenValues.toTypedArray()
                        ValidatedList {
                            this.value = if (chosenValues.isEmpty()) "" else chosenValues[i]
                            this.validators = arrayOf(VALIDATION_REQUIRED, VALIDATION_VALUE_ALREADY_PRESENT)
                            this.errorMessages = arrayOf(
                                translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: "",
                                translations[VALUE_IS_ALREADY_SELECTED_TRANSLATION] ?: ""
                            )
                            this.onChange = { event ->
                                val newValues = chosenValues.toMutableList()
                                newValues[i] = event.value()
                                setChosenValues(newValues)
                            }
                            this.menuItems = props.values.associateWith { "" }
                            this.useDescription = false
                        }
                    }
                }
                DialogActions {
                    Submit { +(translations[NEXT_TRANSLATION] ?: "") }
                }
            }
        }
    }
}
