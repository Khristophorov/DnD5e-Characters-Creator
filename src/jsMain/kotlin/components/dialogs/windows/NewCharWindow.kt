package me.khrys.dnd.charcreator.client.components.dialogs.windows

import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.dialogs.DialogProps
import me.khrys.dnd.charcreator.client.loadFeats
import me.khrys.dnd.charcreator.client.storeCharacter
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.emptyCharacter
import mui.material.CircularProgress
import react.FC
import react.useContext
import react.useState

val NewCharWindow = FC<DialogProps> { props ->
    console.info("Rendering new character window.")
    val translations = useContext(TranslationsContext)
    val (subraceDialogOpen, setSubraceDialogOpen) = useState(false)
    val (nameDialogOpen, setNameDialogOpen) = useState(false)
    val (imageDialogOpen, setImageDialogOpen) = useState(false)
    val (abilitiesDialogOpen, setAbilitiesDialogOpen) = useState(false)
    val (newCharacter, setNewCharacter) = useState(emptyCharacter())
    val (feats, setFeats) = useState(emptyMap<String, Feat>())

    if (props.open && feats.isEmpty()) {
        CircularProgress()
        loadFeats { setFeats(it) }
    } else {
        CharRaceWindow {
            this.character = newCharacter
            this.open = props.open
            this.setOpen = props.setOpen
            this.action = {
                if (newCharacter.race.subraces.isEmpty()) {
                    newCharacter.subrace = newCharacter.race
                    setNameDialogOpen(true)
                } else {
                    setSubraceDialogOpen(true)
                }
            }
            this.feats = feats
        }
        CharSubraceWindow {
            this.character = newCharacter
            this.open = subraceDialogOpen
            this.backAction = {
                setSubraceDialogOpen(false)
                props.setOpen(true)
            }
            this.action = {
                setSubraceDialogOpen(false)
                setNameDialogOpen(true)
            }
        }
        CharNameWindow {
            this.translations = translations
            this.character = newCharacter
            this.open = nameDialogOpen
            this.backAction = {
                setNameDialogOpen(false)
                props.setOpen(true)
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
