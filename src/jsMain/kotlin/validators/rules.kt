package me.khrys.dnd.charcreator.client.validators

import me.khrys.dnd.charcreator.common.VALIDATION_DUPLICATE_NAME
import me.khrys.dnd.charcreator.common.models.Character

fun initValidators(characters: Array<Character>) {
    validatorForm.addValidationRule<String?>(VALIDATION_DUPLICATE_NAME) { name ->
        notDuplicate(name, characters)
    }
}

private fun notDuplicate(name: String?, characters: Array<Character>): Boolean {
    for (character in characters) {
        if (character.name == name) return false
    }
    return true
}
