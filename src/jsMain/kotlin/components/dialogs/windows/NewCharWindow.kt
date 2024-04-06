package me.khrys.dnd.charcreator.client.components.dialogs.windows

import me.khrys.dnd.charcreator.client.FeatsContext
import me.khrys.dnd.charcreator.client.ManeuversContext
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.dialogs.DialogProps
import me.khrys.dnd.charcreator.client.utils.loadFeats
import me.khrys.dnd.charcreator.client.utils.loadManeuvers
import me.khrys.dnd.charcreator.client.utils.storeCharacter
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Maneuver
import me.khrys.dnd.charcreator.common.models.emptyCharacter
import mui.material.CircularProgress
import react.FC
import react.useContext
import react.useState

val NewCharWindow = FC<DialogProps> { props ->
    console.info("Rendering new character window.")
    val translations = useContext(TranslationsContext)
    val (raceDialogOpen, setRaceDialogOpen) = useState(true)
    val (subraceDialogOpen, setSubraceDialogOpen) = useState(false)
    val (classDialogOpen, setClassDialogOpen) = useState(false)
    val (nameDialogOpen, setNameDialogOpen) = useState(false)
    val (imageDialogOpen, setImageDialogOpen) = useState(false)
    val (abilitiesDialogOpen, setAbilitiesDialogOpen) = useState(false)
    val (newCharacter, setNewCharacter) = useState(emptyCharacter())
    val (feats, setFeats) = useState(emptyMap<String, Feat>())
    val (maneuvers, setManeuvers) = useState(emptyMap<String, Maneuver>())

    if (props.open && feats.isEmpty()) {
        CircularProgress()
        loadFeats { setFeats(it) }
    } else if (props.open && maneuvers.isEmpty()) {
        CircularProgress()
        loadManeuvers { setManeuvers(it) }
    } else if (props.open) {
        FeatsContext.Provider(feats) {
            ManeuversContext.Provider(maneuvers) {
                CharRaceWindow {
                    this.character = newCharacter
                    this.open = raceDialogOpen
                    this.setOpen = { setRaceDialogOpen(it) }
                    this.action = {
                        if (newCharacter.race.subraces.isEmpty()) {
                            newCharacter.subrace = newCharacter.race
                            setClassDialogOpen(true)
                        } else {
                            setSubraceDialogOpen(true)
                        }
                    }
                }
                CharSubraceWindow {
                    this.character = newCharacter
                    this.open = subraceDialogOpen
                    this.backAction = {
                        setSubraceDialogOpen(false)
                        setRaceDialogOpen(true)
                    }
                    this.action = {
                        setSubraceDialogOpen(false)
                        setClassDialogOpen(true)
                    }
                }
                CharClassWindow {
                    this.character = newCharacter
                    this.open = classDialogOpen
                    this.backAction = {
                        setRaceDialogOpen(true)
                        setClassDialogOpen(false)
                    }
                    this.action = {
                        setNameDialogOpen(true)
                        setClassDialogOpen(false)
                    }
                }
                CharNameWindow {
                    this.translations = translations
                    this.character = newCharacter
                    this.open = nameDialogOpen
                    this.backAction = {
                        setNameDialogOpen(false)
                        setClassDialogOpen(true)
                    }
                    this.action = {
                        setNameDialogOpen(false)
                        setImageDialogOpen(true)
                    }
                }
                CharImageWindow {
                    this.translations = translations
                    this.character = newCharacter
                    this.open = imageDialogOpen
                    this.backAction = {
                        setImageDialogOpen(false)
                        setNameDialogOpen(true)
                    }
                    this.action = {
                        setImageDialogOpen(false)
                        setAbilitiesDialogOpen(true)
                    }
                }
                CharAbilitiesWindow {
                    this.translations = translations
                    this.character = newCharacter
                    this.open = abilitiesDialogOpen
                    this.backAction = {
                        setAbilitiesDialogOpen(false)
                        setImageDialogOpen(true)
                    }
                    this.action = {
                        setAbilitiesDialogOpen(false)
                        storeCharacter(newCharacter)
                        setNewCharacter(emptyCharacter())
                    }
                }
            }
        }
    }
}
