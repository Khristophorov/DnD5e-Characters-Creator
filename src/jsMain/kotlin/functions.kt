package me.khrys.dnd.charcreator.client

import kotlin.math.floor
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.SavingThrows
import me.khrys.dnd.charcreator.common.models.Skills

fun computeModifier(ability: Int, proficiencyBonus: Int = 0, proficient: Boolean = false): Int {
    val defaultModifier = floor((ability - 10).toDouble() / 2).toInt()
    return if (proficient) defaultModifier + proficiencyBonus else defaultModifier
}

fun computeProficiencyBonus(level: Int): Int {
    if (level in 1..4) return 2
    if (level in 5..8) return 3
    if (level in 9..12) return 4
    return if (level in 13..16) 5
    else 6
}

fun computePassiveSkill(ability: Int, proficiencyBonus: Int = 0, proficient: Boolean = false): Int {
    return computeModifier(ability, proficiencyBonus, proficient) + 10
}

fun toSignedString(number: Int): String = if (number > 0) "+$number" else number.toString()

fun applyFeatures(character: Character): Character {
    val featuredCharacter = character.clone()
    character.features.forEach(featuredCharacter::applyFeature)
    return featuredCharacter
}

fun Character.clone() = Character(
    name = this.name,
    image = this.image,
    hitPoints = this.hitPoints,
    abilities = this.abilities.clone(),
    savingThrows = this.savingThrows.clone(),
    skills = this.skills.clone(),
    speed = this.speed,
    race = this.race,
    subrace = this.subrace,
    features = this.features.copyOf(),
    proficiencies = this.proficiencies.copyOf(),
    languages = this.languages.copyOf()
)

fun Abilities.clone() = Abilities(
    strength = this.strength,
    dexterity = this.dexterity,
    constitution = this.constitution,
    intelligence = this.intelligence,
    wisdom = this.wisdom,
    charisma = this.charisma
)

fun SavingThrows.clone() = SavingThrows(
    strength = this.strength,
    dexterity = this.dexterity,
    constitution = this.constitution,
    intelligence = this.intelligence,
    wisdom = this.wisdom,
    charisma = this.charisma
)

fun Skills.clone() = Skills(
    acrobatics = this.acrobatics,
    animalHandling = this.animalHandling,
    arcana = this.arcana,
    athletics = this.athletics,
    deception = this.deception,
    history = this.history,
    insight = this.insight,
    intimidation = this.intimidation,
    investigation = this.investigation,
    medicine = this.medicine,
    nature = this.nature,
    perception = this.perception,
    performance = this.performance,
    persuasion = this.persuasion,
    religion = this.religion,
    sleightOfHands = this.sleightOfHands,
    stealth = this.stealth,
    survival = this.survival
)

fun Character.applyFeature(feature: Feature) {
    feature.functions.forEach { function ->
        when (function.name) {
            "Increase Strength" -> increaseStrength(function.values[0].toInt())
            "Increase Dexterity" -> increaseDexterity(function.values[0].toInt())
            "Increase Constitution" -> increaseConstitution(function.values[0].toInt())
            "Increase Intelligence" -> increaseIntelligence(function.values[0].toInt())
            "Increase Charisma" -> increaseCharisma(function.values[0].toInt())
            "Increase Wisdom" -> increaseWisdom(function.values[0].toInt())
            "Set Speed" -> this.speed = function.values[0].toInt()
            "Increase HP by Level" -> increaseHPByLevel(function.values[0].toInt())
            "Set Proficiencies" -> setProficiencies(function.values)
            "Add Languages" -> addLanguages(function.values)
        }
    }
}

fun Character.increaseStrength(value: Int) {
    this.abilities.strength += value
}

fun Character.increaseDexterity(value: Int) {
    this.abilities.dexterity += value
}

fun Character.increaseConstitution(value: Int) {
    this.abilities.constitution += value
}

fun Character.increaseIntelligence(value: Int) {
    this.abilities.intelligence += value
}

fun Character.increaseWisdom(value: Int) {
    this.abilities.wisdom += value
}

fun Character.increaseCharisma(value: Int) {
    this.abilities.charisma += value
}

fun Character.increaseHPByLevel(value: Int) {
    this.hitPoints = this.hitPoints + (value * getCombinedLevel())
}

fun Character.setProficiencies(proficiencies: Array<String>) {
    this.proficiencies += proficiencies
}

fun Character.addLanguages(languages: Array<String>) {
    this.languages += languages
}

fun Character.getCombinedLevel(): Int = 1

fun Character.getArmorClass(): Int {
    return 10 + computeModifier(
        this.abilities.dexterity,
        computeProficiencyBonus(this.getCombinedLevel()),
        this.savingThrows.dexterity
    )
}

fun Character.getInitiative(): Int {
    return computeModifier(
        this.abilities.dexterity,
        computeProficiencyBonus(this.getCombinedLevel()),
        this.savingThrows.dexterity
    )
}
