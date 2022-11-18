package me.khrys.dnd.charcreator.client.components.dialogs.windows

import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.FeatureProps
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.dom.DangerouslySetInnerHTML
import react.dom.html.ReactHTML.span
import react.useContext

val InformWindow = FC<FeatureProps<String>> { props ->
    if (props.open) {
        val translations = useContext(TranslationsContext)
        console.info("Rendering info for ${props.feature.name}")

        Dialog {
            this.open = props.open
            DialogTitle {
                +props.feature.name
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    span {
                        this.dangerouslySetInnerHTML =
                            DangerousHTML(props.feature.description).unsafeCast<DangerouslySetInnerHTML>()
                    }
                }
                ValidatorForm {
                    this.onSubmit = {
                        props.setValue("")
                        props.setOpen(false)
                    }
                    DialogActions {
                        Submit { +(translations[NEXT_TRANSLATION] ?: "") }
                    }
                }
            }
        }
    }
}
