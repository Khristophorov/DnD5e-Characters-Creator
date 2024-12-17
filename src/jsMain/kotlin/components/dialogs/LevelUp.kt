package me.khrys.dnd.charcreator.client.components.dialogs

import kotlinx.browser.window
import me.khrys.dnd.charcreator.client.FeatsContext
import me.khrys.dnd.charcreator.client.ManeuversContext
import me.khrys.dnd.charcreator.client.components.dialogs.windows.CharClassWindow
import me.khrys.dnd.charcreator.client.utils.loadFeats
import me.khrys.dnd.charcreator.client.utils.loadManeuvers
import me.khrys.dnd.charcreator.client.utils.storeCharacter
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Maneuver
import mui.material.CircularProgress
import react.FC
import react.useState

val LevelUp = FC<CharBasedProps> { props ->
    val (feats, setFeats) = useState(emptyMap<String, Feat>())
    val (maneuvers, setManeuvers) = useState(emptyMap<String, Maneuver>())

    if (props.open && feats.isEmpty()) {
        CircularProgress()
        loadFeats { setFeats(it) }
    } else if (props.open && maneuvers.isEmpty()) {
        CircularProgress()
        loadManeuvers { setManeuvers(it) }
    } else if (props.open) {
        console.info("Rendering Level up window")
        FeatsContext.Provider(feats) {
            ManeuversContext.Provider(maneuvers) {
                CharClassWindow {
                    this.character = props.character
                    this.open = props.open
                    this.backAction = { window.location.reload() }
                    this.action = {
                        props.setOpen(false)
                        storeCharacter(props.character)
                    }
                }
            }
        }
    }
}
