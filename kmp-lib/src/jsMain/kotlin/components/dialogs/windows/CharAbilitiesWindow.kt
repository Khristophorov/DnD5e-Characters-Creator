package me.khrys.dnd.charcreator.client.components.dialogs.windows

import emotion.react.css
import me.khrys.dnd.charcreator.client.components.buttons.BackButton
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.inputs.AbilityBox
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.toDangerousHtml
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.CHARISMA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CONSTITUTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_ABILITIES_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_ABILITIES_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Ability
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.Props
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import react.useState
import web.cssom.ClassName
import web.cssom.FlexWrap.Companion.wrap

private external interface AbilitiesProps : Props {
    var translations: Map<String, String>
    var value: Int
    var setValue: (Int) -> Unit
}

val CharAbilitiesWindow = FC<CharBasedProps> { props ->
    if (props.open) {
        console.info("Rendering abilities")
        Dialog {
            this.open = props.open
            val (strength, setStrength) = useState(10)
            val (dexterity, setDexterity) = useState(10)
            val (constitution, setConstitution) = useState(10)
            val (intelligence, setIntelligence) = useState(10)
            val (wisdom, setWisdom) = useState(10)
            val (charisma, setCharisma) = useState(10)

            DialogTitle {
                +(props.translations[ENTER_ABILITIES_TRANSLATION] ?: "")
            }
            DialogContent {
                this.dividers = true
                Title { +(props.translations[ENTER_ABILITIES_CONTENT_TRANSLATION] ?: "") }
                ValidatorForm {
                    this.onSubmit = {
                        props.character.abilities =
                            Abilities(
                                Ability(strength),
                                Ability(dexterity),
                                Ability(constitution),
                                Ability(intelligence),
                                Ability(wisdom),
                                Ability(charisma)
                            )
                        props.action()
                    }
                    div {
                        css(ClassName(CLASS_INLINE)) {
                            flexWrap = wrap
                        }
                        Strength {
                            this.translations = props.translations
                            this.value = strength
                            this.setValue = { setStrength(it) }
                        }
                        Dexterity {
                            this.translations = props.translations
                            this.value = dexterity
                            this.setValue = { setDexterity(it) }
                        }
                        Constitution {
                            this.translations = props.translations
                            this.value = constitution
                            this.setValue = { setConstitution(it) }
                        }
                        Intelligence {
                            this.translations = props.translations
                            this.value = intelligence
                            this.setValue = { setIntelligence(it) }
                        }
                        Wisdom {
                            this.translations = props.translations
                            this.value = wisdom
                            this.setValue = { setWisdom(it) }
                        }
                        Charisma {
                            this.translations = props.translations
                            this.value = charisma
                            this.setValue = { setCharisma(it) }
                        }
                    }
                    DialogActions {
                        this.className = ClassName(CLASS_JUSTIFY_BETWEEN)
                        BackButton {
                            this.onClick = props.backAction
                        }
                        Submit { +(props.translations[NEXT_TRANSLATION] ?: "") }
                    }
                }
            }
        }
    }
}

private val Charisma = FC<AbilitiesProps> { props ->
    AbilityBox {
        this.title = props.translations[CHARISMA_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[CHARISMA_TRANSLATION] ?: ""
        this.value = props.value
        this.readOnly = false
        this.translations = props.translations
        this.onChange = { event ->
            event.value().toIntOrNull()?.let { props.setValue(it) }
        }
    }
}

private val Wisdom = FC<AbilitiesProps> { props ->
    AbilityBox {
        this.title = props.translations[WISDOM_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[WISDOM_TRANSLATION] ?: ""
        this.value = props.value
        this.readOnly = false
        this.translations = props.translations
        this.onChange = { event ->
            event.value().toIntOrNull()?.let { props.setValue(it) }
        }
    }
}

private val Intelligence = FC<AbilitiesProps> { props ->
    AbilityBox {
        this.title = props.translations[INTELLIGENCE_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[INTELLIGENCE_TRANSLATION] ?: ""
        this.value = props.value
        this.readOnly = false
        this.translations = props.translations
        this.onChange = { event ->
            event.value().toIntOrNull()?.let { props.setValue(it) }
        }
    }
}

private val Constitution = FC<AbilitiesProps> { props ->
    AbilityBox {
        this.title = props.translations[CONSTITUTION_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[CONSTITUTION_TRANSLATION] ?: ""
        this.value = props.value
        this.readOnly = false
        this.translations = props.translations
        this.onChange = { event ->
            event.value().toIntOrNull()?.let { props.setValue(it) }
        }
    }
}

private val Dexterity = FC<AbilitiesProps> { props ->
    AbilityBox {
        this.title = props.translations[DEXTERITY_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[DEXTERITY_TRANSLATION] ?: ""
        this.value = props.value
        this.readOnly = false
        this.translations = props.translations
        this.onChange = { event ->
            event.value().toIntOrNull()?.let { props.setValue(it) }
        }
    }
}

private val Strength = FC<AbilitiesProps> { props ->
    AbilityBox {
        this.title = props.translations[STRENGTH_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[STRENGTH_TRANSLATION] ?: ""
        this.value = props.value
        this.readOnly = false
        this.translations = props.translations
        this.onChange = { event ->
            event.value().toIntOrNull()?.let { props.setValue(it) }
        }
    }
}

private val Title = FC<PropsWithChildren> { props ->
    DialogContentText {
        span {
            this.dangerouslySetInnerHTML = toDangerousHtml(props.children.toString())
        }
    }
}
