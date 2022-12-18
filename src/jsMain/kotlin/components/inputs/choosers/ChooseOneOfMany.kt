package me.khrys.dnd.charcreator.client.components.inputs.choosers

import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.VALUE_SHOULD_BE_CHOSEN_TRANSLATION
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.dom.DangerouslySetInnerHTML
import react.dom.html.ReactHTML.span
import react.useContext
import react.useState

val ChooseOneOfMany = FC<ChooserProps<String>> { props ->
    val (chosenValue, setChosenValue) = useState("")
    val translations = useContext(TranslationsContext)
    Dialog {
        this.open = props.open
        DialogTitle {
            +props.header
        }
        DialogContent {
            this.dividers = true
            DialogContentText {
                span {
                    dangerouslySetInnerHTML = DangerousHTML(props.description).unsafeCast<DangerouslySetInnerHTML>()
                }
            }
            ValidatorForm {
                this.onSubmit = {
                    props.setValue(chosenValue)
                    props.setOpen(false)
                }
                ValidatedList {
                    this.value = chosenValue
                    this.validators = arrayOf(VALIDATION_REQUIRED)
                    this.errorMessages = arrayOf(translations[VALUE_SHOULD_BE_CHOSEN_TRANSLATION] ?: "")
                    this.onChange = { setChosenValue(it.value()) }
                    this.menuItems = props.values.associateWith { "" }
                    this.useDescription = false
                }
                DialogActions {
                    Submit { +(translations[NEXT_TRANSLATION] ?: "") }
                }
            }
        }
    }
}
