package me.khrys.dnd.charcreator.client.components.validators

import me.khrys.dnd.charcreator.common.VALIDATION_VALUE_ALREADY_PRESENT
import me.khrys.dnd.charcreator.common.VALIDATION_DUPLICATE_NAME
import me.khrys.dnd.charcreator.common.models.Character

fun initValidators(characters: List<Character>) {
    validatorForm.addValidationRule<String?>(VALIDATION_DUPLICATE_NAME) { name ->
        notDuplicate(name, characters)
    }
    validatorForm.addValidationRule<String>(VALIDATION_VALUE_ALREADY_PRESENT) { value ->
        valueAlreadyPresent(validatorForm.values, value)
    }
}

private fun notDuplicate(name: String?, characters: List<Character>): Boolean {
    for (character in characters) {
        if (character.name == name) return false
    }
    return true
}

private fun valueAlreadyPresent(values: Array<String>?, value: String?): Boolean {
    if (values == null) {
        return true
    }
    val reducedValues = values.toMutableList()
    reducedValues.remove(value)
    return !reducedValues.contains(value)
}
