package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.targetValue
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.buttons.dCheckboxWithLabel
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.dialogs.CharRaceProps
import me.khrys.dnd.charcreator.client.components.dialogs.collectRaceFeatures
import me.khrys.dnd.charcreator.client.components.inputs.dValidatedList
import me.khrys.dnd.charcreator.client.loadRaces
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.ENTER_RACE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_RACE_TRANSLATION
import me.khrys.dnd.charcreator.common.FEATS_SELECT_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.RACE_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.models.Race
import me.khrys.dnd.charcreator.common.models.emptyRace
import react.fc
import react.useContext
import react.useState

val charRaceWindow = fc<CharRaceProps> { props ->
    val (races, setRaces) = useState(emptyMap<String, Race>())
    val (race, setRace) = useState(emptyRace())
    val (description, setDescription) = useState("")
    val (openFeatures, setOpenFeatures) = useState(false)
    val (useFeats, setUseFeats) = useState(false)
    if (props.open && races.isEmpty()) {
        loadRaces { setRaces(it) }
    } else {
        mDialog(open = props.open) {
            val translations = useContext(TranslationsContext)
            mDialogTitle(text = translations[ENTER_RACE_TRANSLATION] ?: "")
            mDialogContent(dividers = true) {
                mDialogContentText(text = translations[ENTER_RACE_CONTENT_TRANSLATION] ?: "")
                dValidatorForm(onSubmit = {
                    props.newCharacter.race = race
                    props.newCharacter.features = emptyList()
                    setOpenFeatures(true)
                    props.setOpen(false)
                }) {
                    if (race.features.any { it.withFeats }) {
                        dCheckboxWithLabel(
                            label = translations[FEATS_SELECT_TRANSLATION] ?: "",
                            checked = useFeats,
                            onChange = { _, value -> setUseFeats(value) }
                        )
                    }
                    dValidatedList(
                        label = translations[ENTER_RACE_TRANSLATION] ?: "",
                        value = race._id,
                        validators = arrayOf(VALIDATION_REQUIRED),
                        errorMessages = arrayOf(translations[RACE_SHOULD_BE_FILLED_TRANSLATION] ?: ""),
                        onChange = { event ->
                            setRace(races[event.targetValue] ?: emptyRace())
                        },
                        menuItems = races.mapValues { it.value.description },
                        setDescription = { setDescription(it) },
                        description = description
                    )
                    mDialogActions {
                        dSubmit(translations[NEXT_TRANSLATION] ?: "")
                    }
                }
            }
        }
        child(collectRaceFeatures) {
            attrs.character = props.newCharacter
            attrs.open = openFeatures
            attrs.setOpen = { setOpenFeatures(it) }
            attrs.action = props.action
            attrs.feats = props.feats
            attrs.useFeats = useFeats
        }
    }
}
