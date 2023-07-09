package me.khrys.dnd.charcreator.client

import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.common.ACROBATICS_TRANSLATION
import me.khrys.dnd.charcreator.common.ANIMAL_HANDLING_TRANSLATION
import me.khrys.dnd.charcreator.common.ARCANA_TRANSLATION
import me.khrys.dnd.charcreator.common.ATHLETICS_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DECEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.INTIMIDATION_TRANSLATION
import me.khrys.dnd.charcreator.common.INVESTIGATION_TRANSLATION
import me.khrys.dnd.charcreator.common.MEDICINE_TRANSLATION
import me.khrys.dnd.charcreator.common.NATURE_TRANSLATION
import me.khrys.dnd.charcreator.common.PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PERFORMANCE_TRANSLATION
import me.khrys.dnd.charcreator.common.PERSUASION_TRANSLATION
import me.khrys.dnd.charcreator.common.RELIGION_TRANSLATION
import me.khrys.dnd.charcreator.common.SLEIGHT_OF_HANDS_TRANSLATION
import me.khrys.dnd.charcreator.common.STEALTH_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Dice
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.Filter
import me.khrys.dnd.charcreator.common.models.Filter.Comparator.CONTAINS
import me.khrys.dnd.charcreator.common.models.Filter.Comparator.EQUALS_OR_HIGHER
import me.khrys.dnd.charcreator.common.models.Filter.Param.CHARISMA
import me.khrys.dnd.charcreator.common.models.Filter.Param.CONSTITUTION
import me.khrys.dnd.charcreator.common.models.Filter.Param.DEXTERITY
import me.khrys.dnd.charcreator.common.models.Filter.Param.INTELLIGENCE
import me.khrys.dnd.charcreator.common.models.Filter.Param.LEFT_HAND_TYPE
import me.khrys.dnd.charcreator.common.models.Filter.Param.LEVEL
import me.khrys.dnd.charcreator.common.models.Filter.Param.PROFICIENCIES
import me.khrys.dnd.charcreator.common.models.Filter.Param.RIGHT_HAND_TYPE
import me.khrys.dnd.charcreator.common.models.Filter.Param.STRENGTH
import me.khrys.dnd.charcreator.common.models.Filter.Param.WISDOM
import me.khrys.dnd.charcreator.common.models.Filter.Param.WORE_TYPE
import me.khrys.dnd.charcreator.common.models.SavingThrows
import me.khrys.dnd.charcreator.common.models.Skills
import me.khrys.dnd.charcreator.common.models.Spell
import me.khrys.dnd.charcreator.common.models.SuperiorityDice
import react.dom.DangerouslySetInnerHTML
import kotlin.math.floor

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

fun computePassiveSkill(ability: Int, proficiencyBonus: Int = 0, proficient: Boolean = false, bonus: Int = 0): Int {
    return computeModifier(ability, proficiencyBonus, proficient) + 10 + bonus
}

fun computeSpellLevel(level: Int, cantripTranslation: String, suffix: String?): String {
    return if (level == 0) cantripTranslation else "$level$suffix"
}

fun Int.toSignedString() = if (this > 0) "+$this" else this.toString()

fun applyFeatures(
    character: Character,
    translations: Map<String, String>,
    spells: Map<String, Spell> = emptyMap()
): Character {
    val featuredCharacter = character.clone()
    character.features.forEach { feature ->
        val accept =
            feature.filters.isEmpty() || feature.filters.map { it.apply(character) }.reduce { old, new -> old && new }
        if (accept) {
            featuredCharacter.applyFeature(feature, translations, spells)
        }
    }
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
    classes = this.classes.toList(),
    features = this.features,
    proficiencies = this.proficiencies,
    languages = this.languages,
    maneuvers = this.maneuvers,
    spells = this.spells.toList(),
    superiorityDices = this.superiorityDices.toList()
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

fun Character.applyFeature(feature: Feature, translations: Map<String, String>, spells: Map<String, Spell>) {
    feature.functions.forEach { function ->
        when (function.name) {
            "Increase Strength" -> increaseStrength(function.values[0].toInt())
            "Increase Dexterity" -> increaseDexterity(function.values[0].toInt())
            "Increase Constitution" -> increaseConstitution(function.values[0].toInt())
            "Increase Intelligence" -> increaseIntelligence(function.values[0].toInt())
            "Increase Charisma" -> increaseCharisma(function.values[0].toInt())
            "Increase Wisdom" -> increaseWisdom(function.values[0].toInt())
            "Increase Ability" -> increaseAbility(function.values[0], function.values[1].toInt(), translations)
            "Increase Ability with Saving Throws" -> increaseAbilityAndSavingThrows(
                function.values[0],
                function.values[1].toInt(),
                translations
            )

            "Increase HP by Level" -> increaseHPByLevel(function.values[0].toInt())
            "Increase Initiative" -> increaseInitiative(function.values[0].toInt())
            "Increase Perception" -> increasePerception(function.values[0].toInt())
            "Increase Investigation" -> increaseInvestigation(function.values[0].toInt())
            "Increase Speed" -> increaseSpeed(function.values[0].toInt())
            "Increase Armor Class" -> increaseArmorClass(function.values[0].toInt())
            "Set Speed" -> setSpeed(function.values[0].toInt())
            "Add Proficiencies" -> setProficiencies(function.values)
            "Add Languages" -> addLanguages(function.values)
            "Add Saving Throws" -> addSavingThrows(function.values, translations)
            "Add Skills" -> addSkills(function.values, translations)
            "Add Superiority Dices" -> addSuperiorityDices(Dice.valueOf(function.values[0]), function.values[1].toInt())
            "Add Spells" -> addSpells(function.values, spells)
        }
    }
}

fun Character.increaseStrength(value: Int) {
    if (this.abilities.strength + value <= 20) {
        this.abilities.strength += value
    }
}

fun Character.increaseDexterity(value: Int) {
    if (this.abilities.dexterity + value <= 20) {
        this.abilities.dexterity += value
    }
}

fun Character.increaseConstitution(value: Int) {
    if (this.abilities.constitution + value <= 20) {
        this.abilities.constitution += value
    }
}

fun Character.increaseIntelligence(value: Int) {
    if (this.abilities.intelligence + value <= 20) {
        this.abilities.intelligence += value
    }
}

fun Character.increaseWisdom(value: Int) {
    if (this.abilities.wisdom + value <= 20) {
        this.abilities.wisdom += value
    }
}

fun Character.increaseCharisma(value: Int) {
    if (this.abilities.charisma + value <= 20) {
        this.abilities.charisma += value
    }
}

fun Character.increaseHPByLevel(value: Int) {
    this.hitPoints = this.hitPoints + (value * getCombinedLevel())
}

fun Character.increaseAbility(abilityName: String, value: Int, translations: Map<String, String>) {
    when (abilityName) {
        translations[STRENGTH_TRANSLATION] -> increaseStrength(value)
        translations[DEXTERITY_TRANSLATION] -> increaseDexterity(value)
        translations[CONSTITUTION_TRANSLATION] -> increaseConstitution(value)
        translations[INTELLIGENCE_TRANSLATION] -> increaseIntelligence(value)
        translations[WISDOM_TRANSLATION] -> increaseWisdom(value)
        translations[CHARISMA_TRANSLATION] -> increaseCharisma(value)
    }
}

fun Character.increaseAbilityAndSavingThrows(abilityName: String, value: Int, translations: Map<String, String>) {
    when (abilityName) {
        translations[STRENGTH_TRANSLATION] -> {
            increaseStrength(value)
            this.savingThrows.strength = true
        }

        translations[DEXTERITY_TRANSLATION] -> {
            increaseDexterity(value)
            this.savingThrows.dexterity = true
        }

        translations[CONSTITUTION_TRANSLATION] -> {
            increaseConstitution(value)
            this.savingThrows.constitution = true
        }

        translations[INTELLIGENCE_TRANSLATION] -> {
            increaseIntelligence(value)
            this.savingThrows.intelligence = true
        }

        translations[WISDOM_TRANSLATION] -> {
            increaseWisdom(value)
            this.savingThrows.wisdom = true
        }

        translations[CHARISMA_TRANSLATION] -> {
            increaseCharisma(value)
            this.savingThrows.charisma = true
        }
    }
}

fun Character.increaseInitiative(value: Int) {
    this.bonuses.initiative += value
}

fun Character.increasePerception(value: Int) {
    this.bonuses.perception += value
}

fun Character.increaseInvestigation(value: Int) {
    this.bonuses.investigation += value
}

fun Character.increaseSpeed(value: Int) {
    this.speed += value
}

fun Character.increaseArmorClass(value: Int) {
    this.armorClass += value
}

fun Character.setSpeed(speed: Int) {
    this.speed = speed
}

fun Character.setProficiencies(proficiencies: List<String>) {
    this.proficiencies += proficiencies
}

fun Character.addLanguages(languages: List<String>) {
    this.languages += languages
}

fun Character.addSavingThrows(savingThrows: List<String>, translations: Map<String, String>) {
    savingThrows.forEach {
        when (it) {
            translations[STRENGTH_TRANSLATION] -> {
                this.savingThrows.strength = true
            }

            translations[DEXTERITY_TRANSLATION] -> {
                this.savingThrows.dexterity = true
            }

            translations[CONSTITUTION_TRANSLATION] -> {
                this.savingThrows.constitution = true
            }

            translations[INTELLIGENCE_TRANSLATION] -> {
                this.savingThrows.intelligence = true
            }

            translations[WISDOM_TRANSLATION] -> {
                this.savingThrows.wisdom = true
            }

            translations[CHARISMA_TRANSLATION] -> {
                this.savingThrows.charisma = true
            }
        }
    }
}

fun Character.addSkills(skills: List<String>, translations: Map<String, String>) {
    skills.forEach {
        when (it) {
            translations[ACROBATICS_TRANSLATION] -> this.skills.acrobatics = true
            translations[ANIMAL_HANDLING_TRANSLATION] -> this.skills.animalHandling = true
            translations[ARCANA_TRANSLATION] -> this.skills.arcana = true
            translations[ATHLETICS_TRANSLATION] -> this.skills.athletics = true
            translations[DECEPTION_TRANSLATION] -> this.skills.deception = true
            translations[HISTORY_TRANSLATION] -> this.skills.history = true
            translations[INSIGHT_TRANSLATION] -> this.skills.insight = true
            translations[INTIMIDATION_TRANSLATION] -> this.skills.intimidation = true
            translations[INVESTIGATION_TRANSLATION] -> this.skills.investigation = true
            translations[MEDICINE_TRANSLATION] -> this.skills.medicine = true
            translations[NATURE_TRANSLATION] -> this.skills.nature = true
            translations[PERCEPTION_TRANSLATION] -> this.skills.perception = true
            translations[PERFORMANCE_TRANSLATION] -> this.skills.performance = true
            translations[PERSUASION_TRANSLATION] -> this.skills.persuasion = true
            translations[RELIGION_TRANSLATION] -> this.skills.religion = true
            translations[SLEIGHT_OF_HANDS_TRANSLATION] -> this.skills.sleightOfHands = true
            translations[STEALTH_TRANSLATION] -> this.skills.stealth = true
            translations[SURVIVAL_TRANSLATION] -> this.skills.survival = true
        }
    }
}

fun Character.getCombinedLevel(): Int = this.classes.sumOf { it.first }

fun Character.computeArmorClass(): Int {
    return 10 + computeModifier(
        this.abilities.dexterity,
        computeProficiencyBonus(this.getCombinedLevel()),
        this.savingThrows.dexterity
    ) + this.armorClass
}

fun Character.getInitiative(): Int {
    return computeModifier(
        this.abilities.dexterity,
        computeProficiencyBonus(this.getCombinedLevel()),
        this.savingThrows.dexterity
    ) + bonuses.initiative
}

fun Character.setDefaultHitPoints() {
    if (this.hitPoints == 0) {
        this.hitPoints = this.classes[0].second.hitDice.maxValue + computeModifier(this.abilities.constitution)
    }
}

fun Character.addSuperiorityDices(dice: Dice, quantity: Int) {
    var newDice = true
    this.superiorityDices.forEach { superiorityDice ->
        if (superiorityDice.dice == dice) {
            superiorityDice.quantity += quantity
            newDice = true
        }
    }
    if (newDice) {
        this.superiorityDices += SuperiorityDice(dice, quantity)
    }
}

fun Character.addSpells(spellsNames: List<String>, spells: Map<String, Spell>) {
    spellsNames.filter { !this.spells.map { spell -> spell._id }.contains(it) }
        .mapNotNull { spells[it] }
        .forEach { this.spells = this.spells + it }
}

fun Feat.toFeature(): Feature = Feature(
    name = this._id,
    description = this.description,
    functions = this.functions,
    filters = this.filters,
    source = this.source
)

fun Filter.apply(character: Character): Boolean {
    val dataSet = when (this.param) {
        PROFICIENCIES -> character.proficiencies
        WORE_TYPE -> emptyList()
        LEFT_HAND_TYPE -> emptyList()
        RIGHT_HAND_TYPE -> emptyList()
        else -> emptyList()
    }
    val dataNum = when (this.param) {
        STRENGTH -> character.abilities.strength
        DEXTERITY -> character.abilities.dexterity
        CONSTITUTION -> character.abilities.constitution
        INTELLIGENCE -> character.abilities.intelligence
        WISDOM -> character.abilities.wisdom
        CHARISMA -> character.abilities.charisma
        LEVEL -> character.getCombinedLevel()
        else -> 0
    }
    return when (this.comparator) {
        CONTAINS -> dataSet.contains(value)
        EQUALS_OR_HIGHER -> dataNum >= value.toInt()
    }
}

fun String.format(vararg values: String): String {
    var newString = this
    values.forEach { newString = newString.replaceFirst("{}", it) }
    return newString
}

fun toDangerousHtml(value: String) = object : DangerousHTML {
    override var __html = value
}.unsafeCast<DangerouslySetInnerHTML>()
