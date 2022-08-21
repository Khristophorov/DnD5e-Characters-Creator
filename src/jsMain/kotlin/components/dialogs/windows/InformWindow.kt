package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog
import com.ccfraser.muirwik.components.dialogActions
import com.ccfraser.muirwik.components.dialogContent
import com.ccfraser.muirwik.components.dialogContentText
import com.ccfraser.muirwik.components.dialogTitle
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.dialogs.FeatureProps
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import react.RBuilder
import react.fc
import react.useContext
import styled.styledDiv

val informWindow = fc<FeatureProps<String>> { props ->
    dInformWindow(
        open = props.open,
        setOpen = props.setOpen,
        header = props.feature.name,
        description = props.feature.description,
        setValue = { props.setValue("") }
    )
}

fun RBuilder.dInformWindow(
    open: Boolean,
    setOpen: (Boolean) -> Unit,
    header: String,
    description: String,
    setValue: () -> Unit
) {
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
                setValue()
                setOpen(false)
            }) {
                dialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}
