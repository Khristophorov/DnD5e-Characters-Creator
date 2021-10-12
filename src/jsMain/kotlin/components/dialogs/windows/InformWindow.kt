package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.dialogs.FeatureProps
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
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

    mDialog(open = open) {
        mDialogTitle(text = header)
        mDialogContent(dividers = true) {
            mDialogContentText(text = "") {
                styledDiv {
                    attrs[DANGEROUS_HTML] = DangerousHTML(description)
                }
            }
            dValidatorForm(onSubmit = {
                setValue()
                setOpen(false)
            }) {
                mDialogActions {
                    dSubmit(translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}
