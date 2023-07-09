package me.khrys.dnd.charcreator.client.components.dialogs.windows

import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.BackButton
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.dialogs.CollectClassFeatures
import me.khrys.dnd.charcreator.client.components.dialogs.memoDialog
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.utils.loadClasses
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CLASS_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_CLASS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_CLASS_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.models.Class
import me.khrys.dnd.charcreator.common.models.emptyClass
import mui.material.CircularProgress
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.useContext
import react.useState
import web.cssom.ClassName

var CharClassWindow = memoDialog(FC<CharBasedProps> { props ->
    val (classes, setClasses) = useState(emptyMap<String, Class>())
    val (charClass, setCharClass) = useState(emptyClass())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    val translations = useContext(TranslationsContext)
    if (props.open && classes.isEmpty()) {
        CircularProgress()
        loadClasses { setClasses(it) }
    } else if (props.open) {
        console.info("Rendering character class window")
        Dialog {
            this.open = props.open
            DialogTitle {
                +(translations[ENTER_CLASS_TRANSLATION] ?: "")
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    +(translations[ENTER_CLASS_CONTENT_TRANSLATION] ?: "")
                }
                ValidatorForm {
                    this.onSubmit = {
                        console.info("Chosen class: ${charClass._id}")
                        props.character.classes += 1 to charClass
                        setOpenFeatures(true)
                        props.setOpen(false)
                    }
                    ValidatedList {
                        this.label = translations[ENTER_CLASS_TRANSLATION] ?: ""
                        this.value = charClass._id
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.errorMessages = arrayOf(translations[CLASS_SHOULD_BE_FILLED_TRANSLATION] ?: "")
                        this.onChange = { event ->
                            setCharClass(classes[event.value()] ?: emptyClass())
                        }
                        this.menuItems = classes.mapValues { it.value.description }
                        this.useDescription = true
                        this.setDescription = { setDescription(it) }
                        this.description = description
                    }
                    DialogActions {
                        this.className = ClassName(CLASS_JUSTIFY_BETWEEN)
                        BackButton {
                            this.onClick = {
                                props.character.classes = emptyList()
                                setCharClass(emptyClass())
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
    CollectClassFeatures {
        this.className = charClass._id
        this.classLevel = 1
        this.character = props.character
        this.open = openFeatures
        this.setOpen = { setOpenFeatures(it) }
        this.action = props.action
        this.feats = feats
        this.useFeats = useFeats
    }
})
