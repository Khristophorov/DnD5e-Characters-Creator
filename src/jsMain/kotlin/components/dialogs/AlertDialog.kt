package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.client.components.buttons.Button
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC

val AlertDialog = FC<DialogProps> { props ->
    Dialog {
        this.open = props.open
        DialogTitle {
            +props.header
        }
        DialogContent {
            DialogContentText {
                +props.children
            }
        }
        DialogActions {
            Button {
                this.onClick = { props.action() }
                +"OK"
            }
        }
    }
}
