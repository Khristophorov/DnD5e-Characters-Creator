package me.khrys.dnd.charcreator.client.components.dialogs.windows

import me.khrys.dnd.charcreator.client.FeatsContext
import me.khrys.dnd.charcreator.client.RacesContext
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.FeatsCheckbox
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.dialogs.CollectRaceFeatures
import me.khrys.dnd.charcreator.client.components.dialogs.memoDialog
import me.khrys.dnd.charcreator.client.components.inputs.ValidatedList
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.ENTER_RACE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_RACE_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.RACE_SHOULD_BE_FILLED_TRANSLATION
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

val CharRaceWindow = memoDialog(FC<CharBasedProps> { props ->
    val (race, setRace) = useState(emptyRace())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    val (useFeats, setUseFeats) = useState(false)
    val translations = useContext(TranslationsContext)
    val feats = useContext(FeatsContext)
    val races = useContext(RacesContext)
    if (props.open) {
        console.info("Rendering character race window")
        Dialog {
            this.open = props.open
            DialogTitle {
                +(translations[ENTER_RACE_TRANSLATION] ?: "")
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    +(translations[ENTER_RACE_CONTENT_TRANSLATION] ?: "")
                }
                ValidatorForm {
                    this.onSubmit = {
                        console.info("Chosen race: ${race._id}")
                        props.character.race = race._id
                        props.character.features = emptyList()
                        setOpenFeatures(true)
                        props.setOpen(false)
                    }
                    if (race.features.any { it.withFeats }) {
                        FeatsCheckbox {
                            this.translations = translations
                            this.checked = useFeats
                            this.setValue = { setUseFeats(it) }
                        }
                    }
                    ValidatedList {
                        this.label = ReactNode(translations[ENTER_RACE_TRANSLATION])
                        this.value = race._id
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.errorMessages = arrayOf(translations[RACE_SHOULD_BE_FILLED_TRANSLATION] ?: "")
                        this.onChange = { event ->
                            setRace(races.filter { it._id == event.value() }[0])
                        }
                        this.menuItems = races.associate { it._id to it.description }
                        this.useDescription = true
                        this.setDescription = { setDescription(it) }
                        this.description = description
                    }
                    DialogActions {
                        Submit { +(translations[NEXT_TRANSLATION] ?: "") }
                    }
                }
            }
        }
    }
    CollectRaceFeatures {
        this.character = props.character
        this.features = race.features
        this.open = openFeatures
        this.setOpen = { setOpenFeatures(it) }
        this.action = props.action
        this.feats = feats
        this.useFeats = useFeats
    }
})
