package me.khrys.dnd.charcreator.client.components.dialogs.grids

import me.khrys.dnd.charcreator.client.components.inputs.AbilityBox
import me.khrys.dnd.charcreator.common.CHARISMA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import react.FC
import react.Props

external interface AbilitiesProps : Props {
    var abilities: Abilities
    var translations: Map<String, String>
    var character: Character
}

val AbilitiesGrid = FC<AbilitiesProps> { props ->
    AbilityBox {
        this.title = props.translations[STRENGTH_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[STRENGTH_TRANSLATION] ?: ""
        this.readOnly = true
        this.value = props.abilities.strength
        this.translations = props.translations
    }
    AbilityBox {
        this.title = props.translations[DEXTERITY_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[DEXTERITY_TRANSLATION] ?: ""
        this.readOnly = true
        this.value = props.abilities.dexterity
        this.translations = props.translations
    }
    AbilityBox {
        this.title = props.translations[CONSTITUTION_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[CONSTITUTION_TRANSLATION] ?: ""
        this.readOnly = true
        this.value = props.abilities.constitution
        this.translations = props.translations
    }
    AbilityBox {
        this.title = props.translations[INTELLIGENCE_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[INTELLIGENCE_TRANSLATION] ?: ""
        this.readOnly = true
        this.value = props.abilities.intelligence
        this.translations = props.translations
    }
    AbilityBox {
        this.title = props.translations[WISDOM_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[WISDOM_TRANSLATION] ?: ""
        this.readOnly = true
        this.value = props.abilities.wisdom
        this.translations = props.translations
    }
    AbilityBox {
        this.title = props.translations[CHARISMA_CONTENT_TRANSLATION] ?: ""
        this.label = props.translations[CHARISMA_TRANSLATION] ?: ""
        this.readOnly = true
        this.value = props.abilities.charisma
        this.translations = props.translations
    }
}
