package me.khrys.dnd.charcreator.client.components.dialogs.windows

import me.khrys.dnd.charcreator.client.FeatsContext
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.BackButton
import me.khrys.dnd.charcreator.client.components.buttons.FeatsCheckbox
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.dialogs.CollectClassFeatures
import me.khrys.dnd.charcreator.client.components.dialogs.HitPointsDialog
import me.khrys.dnd.charcreator.client.components.dialogs.memoDialog
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.getCombinedLevel
import me.khrys.dnd.charcreator.client.setDefaultHitPoints
import me.khrys.dnd.charcreator.client.utils.loadClasses
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CLASS_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_CLASS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_CLASS_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.models.Class
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.emptyClass
import mui.material.CircularProgress
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

var CharClassWindow = memoDialog(FC<CharBasedProps> { props ->
    val (classes, setClasses) = useState(emptyMap<String, Class>())
    val (charClass, setCharClass) = useState(emptyClass())
    val (features, setFeatures) = useState(emptyList<Feature>())
    val (classLevel, setClassLevel) = useState(0)
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    val (openHp, setOpenHp) = useState(false)
    val (useFeats, setUseFeats) = useState(false)
    val feats = useContext(FeatsContext)
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
                if (features.any { it.withFeats }) {
                    FeatsCheckbox {
                        this.translations = translations
                        this.checked = useFeats
                        this.setValue = { setUseFeats(it) }
                    }
                }
                ValidatorForm {
                    this.onSubmit = {
                        console.info("Chosen class: ${charClass._id}")
                        val currentClassLevel = props.character.classes[charClass._id] ?: 0
                        if (classLevel != currentClassLevel) {
                            setClassLevel(currentClassLevel)
                        }
                        props.character.classes += charClass._id to (currentClassLevel + 1)
                        if (props.character.getCombinedLevel() == 1) {
                            props.character.setDefaultHitPoints(charClass.hitDice)
                            setOpenFeatures(true)
                        } else {
                            setOpenHp(true)
                        }
                    }
                    ValidatedList {
                        this.label = ReactNode(translations[ENTER_CLASS_TRANSLATION])
                        this.value = charClass._id
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.errorMessages = arrayOf(translations[CLASS_SHOULD_BE_FILLED_TRANSLATION] ?: "")
                        this.onChange = { event ->
                            val chosenClass = classes[event.value()] ?: emptyClass()
                            val currentClassLevel = props.character.classes[chosenClass._id] ?: 0
                            setCharClass(chosenClass)
                            setFeatures(chosenClass.features[currentClassLevel + 1] ?: emptyList())
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
                                props.character.classes = emptyMap()
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
        this.features = features
        this.multiclass = false
        this.character = props.character
        this.open = openFeatures
        this.setOpen = { setOpenFeatures(it) }
        this.action = props.action
        this.feats = feats
        this.useFeats = useFeats
    }
    HitPointsDialog {
        this.open = openHp
        this.character = props.character
        this.translations = translations
        this.action = {
            setOpenHp(false)
            setOpenFeatures(true)
        }
        this.backAction = { event ->
            setOpenHp(false)
            this.backAction(event)
        }
    }
})
