package me.khrys.dnd.charcreator.client.components.dialogs.windows

import me.khrys.dnd.charcreator.client.RacesContext
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.BackButton
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.dialogs.CollectSubraceFeatures
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.ENTER_SUBRACE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SUBRACE_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.SUBRACE_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.models.emptyRace
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.ReactNode
import react.useContext
import react.useState
import web.cssom.ClassName

val CharSubraceWindow = FC<CharBasedProps> { props ->
    val (subrace, setSubrace) = useState(emptyRace())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    val translations = useContext(TranslationsContext)
    val races = useContext(RacesContext)
    if (props.open) {
        val subraces = races.filter { props.character.race == it._id }[0].subraces
        console.info("Rendering character subrace window.")
        Dialog {
            this.open = props.open
            DialogTitle {
                +(translations[ENTER_SUBRACE_TRANSLATION] ?: "")
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    +(translations[ENTER_SUBRACE_CONTENT_TRANSLATION] ?: "")
                }
                ValidatorForm {
                    this.onSubmit = {
                        props.character.subrace = subrace._id
                        if (subrace.features.isEmpty()) {
                            props.action()
                        } else {
                            setOpenFeatures(true)
                        }
                    }
                    ValidatedList {
                        this.label = ReactNode(translations[ENTER_SUBRACE_TRANSLATION])
                        this.value = subrace._id
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.errorMessages = arrayOf(translations[SUBRACE_SHOULD_BE_FILLED_TRANSLATION] ?: "")
                        this.onChange = { event ->
                            setSubrace(subraces.filter { event.value() == it._id }[0])
                        }
                        this.menuItems = subraces.associate { it._id to it.description }
                        this.useDescription = true
                        this.setDescription = { setDescription(it) }
                        this.description = description
                    }
                    DialogActions {
                        this.className = ClassName(CLASS_JUSTIFY_BETWEEN)
                        BackButton {
                            this.onClick = {
                                setSubrace(emptyRace())
                                setDescription("")
                                props.backAction(it)
                            }
                        }
                        Submit { +(translations[NEXT_TRANSLATION] ?: "") }
                    }
                }
            }
        }
    }
    CollectSubraceFeatures {
        this.character = props.character
        this.features = subrace.features
        this.open = openFeatures
        this.setOpen = { setOpenFeatures(it) }
        this.action = props.action
    }
}
