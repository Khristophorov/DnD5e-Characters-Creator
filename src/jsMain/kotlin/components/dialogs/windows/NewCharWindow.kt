package me.khrys.dnd.charcreator.client.components.dialogs.windows

import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.components.dialogs.DialogProps
import me.khrys.dnd.charcreator.client.loadFeats
import me.khrys.dnd.charcreator.client.storeCharacter
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.emptyCharacter
import react.fc
import react.useContext
import react.useState

val newCharWindow = fc<DialogProps> { props ->
    val translations = useContext(TranslationsContext)
    val (subraceDialogOpen, setSubraceDialogOpen) = useState(false)
    val (nameDialogOpen, setNameDialogOpen) = useState(false)
    val (imageDialogOpen, setImageDialogOpen) = useState(false)
    val (abilitiesDialogOpen, setAbilitiesDialogOpen) = useState(false)
    val (savingThrowsDialogOpen, setSavingThrowsDialogOpen) = useState(false)
    val (newCharacter, setNewCharacter) = useState(emptyCharacter())
    val (feats, setFeats) = useState(emptyMap<String, Feat>())

    if (props.open && feats.isEmpty()) {
        loadFeats { setFeats(it) }
    }

    child(charRaceWindow) {
        attrs.newCharacter = newCharacter
        attrs.open = props.open
        attrs.setOpen = props.setOpen
        attrs.action = {
            if (newCharacter.race.subraces.isEmpty()) {
                newCharacter.subrace = newCharacter.race
                setNameDialogOpen(true)
            } else {
                setSubraceDialogOpen(true)
            }
        }
        attrs.feats = feats
    }
    child(charSubraceWindow) {
        attrs.newCharacter = newCharacter
        attrs.open = subraceDialogOpen
        attrs.backAction = {
            setSubraceDialogOpen(false)
            props.setOpen(true)
        }
        attrs.action = {
            setSubraceDialogOpen(false)
            setNameDialogOpen(true)
        }
    }
    charNameWindow(translations, newCharacter, nameDialogOpen, {
        setNameDialogOpen(false)
        props.setOpen(true)
    }) {
        setNameDialogOpen(false)
        setImageDialogOpen(true)
    }
    charImageWindow(translations, newCharacter, imageDialogOpen, {
        setImageDialogOpen(false)
        setNameDialogOpen(true)
    }) {
        setImageDialogOpen(false)
        setAbilitiesDialogOpen(true)
    }
    charAbilitiesWindow(translations, newCharacter, abilitiesDialogOpen, {
        setAbilitiesDialogOpen(false)
        setImageDialogOpen(true)
    }) {
        setAbilitiesDialogOpen(false)
        setSavingThrowsDialogOpen(true)
    }
    charSavingThrowsWindow(translations, newCharacter, savingThrowsDialogOpen, {
        setSavingThrowsDialogOpen(false)
        setAbilitiesDialogOpen(true)
    }) {
        storeCharacter(newCharacter)
        setNewCharacter(emptyCharacter())
        setSavingThrowsDialogOpen(false)
    }
}
