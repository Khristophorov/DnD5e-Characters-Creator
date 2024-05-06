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
import me.khrys.dnd.charcreator.common.FINESSE_TRANSLATION
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
import me.khrys.dnd.charcreator.common.RANGE_TRANSLATION
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
import me.khrys.dnd.charcreator.common.models.SimpleEquipment
import me.khrys.dnd.charcreator.common.models.Skill
import me.khrys.dnd.charcreator.common.models.Spell
import me.khrys.dnd.charcreator.common.models.SuperiorityDice
import me.khrys.dnd.charcreator.common.models.Weapon
import react.dom.DangerouslySetInnerHTML
import kotlin.math.floor
import kotlin.math.max

private const val MAXIMUM_LEVEL = 20

fun computeModifier(
    ability: Int,
    proficiencyBonus: Int = 0,
    proficient: Boolean = false,
    additionalBonus: Int = 0
): Int {
    val defaultModifier = floor((ability - 10).toDouble() / 2).toInt()
    return (if (proficient) defaultModifier + proficiencyBonus else defaultModifier) + additionalBonus
}

fun computeProficiencyBonus(level: Int): Int {
    if (level in 1..4) return 2
    if (level in 5..8) return 3
    if (level in 9..12) return 4
    return if (level in 13..16) 5
    else 6
}

fun computePassiveSkill(ability: Int, proficiencyBonus: Int = 0, proficient: Boolean = false, bonus: Int = 0) =
    computeModifier(ability, proficiencyBonus, proficient) + 10 + bonus

fun computeSpellLevel(level: Int, cantripTranslation: String, suffix: String?) =
    if (level == 0) cantripTranslation else "$level$suffix"

fun computeAttackBonus(
    character: Character,
    proficiencyBonus: Int,
    weapon: Weapon,
    translations: Map<String, String>
): Int {
    val attackModifier = computeAttackModifier(character.abilities, weapon.properties, translations)
    val hasBonus = character.proficiencies.any {
        it.contains(weapon._id, ignoreCase = true) || weapon._id.contains(it, ignoreCase = true)
                || weapon.type.contains(it, ignoreCase = true)
    }
    return (if (hasBonus) proficiencyBonus else 0) + attackModifier
}

fun computeAttackModifier(abilities: Abilities, properties: String, translations: Map<String, String>) =
    if (isRanged(properties, translations)) computeModifier(abilities.dexterity)
    else if (isFinesse(properties, translations)) computeModifier(max(abilities.strength, abilities.dexterity))
    else computeModifier(abilities.strength)

fun isRanged(properties: String, translations: Map<String, String>) =
    properties.contains(translations[RANGE_TRANSLATION] ?: "", ignoreCase = true)

fun isFinesse(properties: String, translations: Map<String, String>) =
    properties.contains(translations[FINESSE_TRANSLATION] ?: "", ignoreCase = true)

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
    skills = this.skills.map(Skill::clone),
    speed = this.speed,
    race = this.race,
    subrace = this.subrace,
    classes = this.classes.toMap(),
    features = this.features,
    proficiencies = this.proficiencies,
    languages = this.languages,
    maneuvers = this.maneuvers,
    spells = this.spells.toList(),
    additionalSpells = this.additionalSpells.toList(),
    equipment = this.equipment,
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

fun Skill.clone() = Skill(
    name = this.name,
    ability = this.ability,
    proficient = this.proficient,
    additionalBonus = this.additionalBonus
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
            "Increase Non Proficient Skills With Proficiency Bonus" ->
                increaseSkillsWithProficiencyBonus(false, function.values[0].toDouble())
            "Double Skill Bonus" -> increaseSkillsWithProficiencyBonus(function.values, 1.0)
            "Increase Initiative" -> increaseInitiative(function.values[0].toInt())
            "Increase Perception" -> increasePerception(function.values[0].toInt())
            "Increase Investigation" -> increaseInvestigation(function.values[0].toInt())
            "Increase Speed" -> increaseSpeed(function.values[0].toInt())
            "Increase Armor Class" -> increaseArmorClass(function.values[0].toInt())
            "Set Speed" -> setSpeed(function.values[0].toInt())
            "Set Spellcasting Ability" -> setSpellcastingAbility(function.values[0], translations)
            "Add Proficiencies" -> setProficiencies(function.values)
            "Add Languages" -> addLanguages(function.values)
            "Add Saving Throws" -> addSavingThrows(function.values, translations)
            "Add Skills" -> addSkills(function.values, translations)
            "Add Superiority Dices" -> addSuperiorityDices(Dice.valueOf(function.values[0]), function.values[1].toInt())
            "Add Spells" -> addSpells(function.values, spells)
            "Add Additional Spells" -> addAdditionalSpells(function.values, spells)
        }
    }
}

fun Character.increaseStrength(value: Int) {
    if (this.abilities.strength + value <= MAXIMUM_LEVEL) {
        this.abilities.strength += value
    }
}

fun Character.increaseDexterity(value: Int) {
    if (this.abilities.dexterity + value <= MAXIMUM_LEVEL) {
        this.abilities.dexterity += value
    }
}

fun Character.increaseConstitution(value: Int) {
    if (this.abilities.constitution + value <= MAXIMUM_LEVEL) {
        this.abilities.constitution += value
    }
}

fun Character.increaseIntelligence(value: Int) {
    if (this.abilities.intelligence + value <= MAXIMUM_LEVEL) {
        this.abilities.intelligence += value
    }
}

fun Character.increaseWisdom(value: Int) {
    if (this.abilities.wisdom + value <= MAXIMUM_LEVEL) {
        this.abilities.wisdom += value
    }
}

fun Character.increaseCharisma(value: Int) {
    if (this.abilities.charisma + value <= MAXIMUM_LEVEL) {
        this.abilities.charisma += value
    }
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

fun Character.increaseSkillsWithProficiencyBonus(proficient: Boolean, multiplier: Double) {
    val bonus = (computeProficiencyBonus(this.getCombinedLevel()) * multiplier).toInt()
    this.skills.forEach { skill ->
        if (skill.proficient == proficient) {
            skill.additionalBonus += bonus
        }
    }
}

fun Character.increaseSkillsWithProficiencyBonus(skills: List<String>, multiplier: Double) {
    val bonus = (computeProficiencyBonus(this.getCombinedLevel()) * multiplier).toInt()
    skills.forEach { skill ->
        this.skills.find { it.name == skill }?.additionalBonus = bonus
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

fun Character.setSpellcastingAbility(ability: String, translations: Map<String, String>) {
    this.spellcastingAbilities = (this.spellcastingAbilities + ability).distinct()
    this.spellSaveDC = 8 + computeProficiencyBonus(this.getCombinedLevel()) +
            computeModifier(this.getAbility(ability, translations))
    this.spellAttackBonus =
        computeProficiencyBonus(this.getCombinedLevel()) + computeModifier(this.getAbility(ability, translations))
}

fun Character.getAbility(ability: String, translations: Map<String, String>): Int {
    return when (ability) {
        translations[STRENGTH_TRANSLATION] -> this.abilities.strength
        translations[DEXTERITY_TRANSLATION] -> this.abilities.dexterity
        translations[CONSTITUTION_TRANSLATION] -> this.abilities.constitution
        translations[INTELLIGENCE_TRANSLATION] -> this.abilities.intelligence
        translations[WISDOM_TRANSLATION] -> this.abilities.wisdom
        translations[CHARISMA_TRANSLATION] -> this.abilities.charisma
        else -> {
            throw IllegalArgumentException("Unknown ability: $ability")
        }
    }
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
    this.skills += listOf(
        Skill(
            name = translations[ACROBATICS_TRANSLATION] ?: "",
            ability = translations[DEXTERITY_TRANSLATION] ?: "",
            proficient = skills.contains(translations[ACROBATICS_TRANSLATION])
        ),
        Skill(
            name = translations[ANIMAL_HANDLING_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[ANIMAL_HANDLING_TRANSLATION])
        ),
        Skill(
            name = translations[ARCANA_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[ARCANA_TRANSLATION])),
        Skill(
            name = translations[ATHLETICS_TRANSLATION] ?: "",
            ability = translations[STRENGTH_TRANSLATION] ?: "",
            proficient = skills.contains(translations[ATHLETICS_TRANSLATION])),
        Skill(
            name = translations[DECEPTION_TRANSLATION] ?: "",
            ability = translations[CHARISMA_TRANSLATION] ?: "",
            proficient = skills.contains(translations[DECEPTION_TRANSLATION])),
        Skill(
            name = translations[HISTORY_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[HISTORY_TRANSLATION])),
        Skill(
            name = translations[INSIGHT_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[INSIGHT_TRANSLATION])),
        Skill(
            name = translations[INTIMIDATION_TRANSLATION] ?: "",
            ability = translations[CHARISMA_TRANSLATION] ?: "",
            proficient = skills.contains(translations[INTIMIDATION_TRANSLATION])),
        Skill(
            name = translations[INVESTIGATION_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[INVESTIGATION_TRANSLATION])),
        Skill(
            name = translations[MEDICINE_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[MEDICINE_TRANSLATION])),
        Skill(
            name = translations[NATURE_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[NATURE_TRANSLATION])),
        Skill(
            name = translations[PERCEPTION_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[PERCEPTION_TRANSLATION])),
        Skill(
            name = translations[PERFORMANCE_TRANSLATION] ?: "",
            ability = translations[CHARISMA_TRANSLATION] ?: "",
            proficient = skills.contains(translations[PERFORMANCE_TRANSLATION])),
        Skill(
            name = translations[PERSUASION_TRANSLATION] ?: "",
            ability = translations[CHARISMA_TRANSLATION] ?: "",
            proficient = skills.contains(translations[PERSUASION_TRANSLATION])),
        Skill(
            name = translations[RELIGION_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[RELIGION_TRANSLATION])),
        Skill(
            name = translations[SLEIGHT_OF_HANDS_TRANSLATION] ?: "",
            ability = translations[DEXTERITY_TRANSLATION] ?: "",
            proficient = skills.contains(translations[SLEIGHT_OF_HANDS_TRANSLATION])
        ),
        Skill(
            name = translations[STEALTH_TRANSLATION] ?: "",
            ability = translations[DEXTERITY_TRANSLATION] ?: "",
            proficient = skills.contains(translations[STEALTH_TRANSLATION])),
        Skill(
            name = translations[SURVIVAL_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[SURVIVAL_TRANSLATION])),
    )
}

fun Character.getCombinedLevel(): Int = this.classes.values.sum()

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

fun Character.setDefaultHitPoints(dice: Dice) {
    if (this.hitPoints == 0) {
        this.hitPoints = dice.maxValue + computeModifier(this.abilities.constitution)
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

fun Character.addAdditionalSpells(spellsNames: List<String>, spells: Map<String, Spell>) {
    spellsNames.filter { !this.additionalSpells.map { spell -> spell._id }.contains(it) }
        .mapNotNull { spells[it] }
        .forEach { this.additionalSpells += it }
}

fun Character.addSpells(spellsNames: List<String>, spells: Map<String, Spell>) {
    spellsNames.filter { !this.spells.map { spell -> spell._id }.contains(it) }
        .mapNotNull { spells[it] }
        .forEach { this.spells += it }
}

fun Character.allSpells() = (this.spells + this.additionalSpells).distinct()

fun Character.isNotMaximumLevel() = this.getCombinedLevel() < MAXIMUM_LEVEL

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

fun validateWeapon(weapon: Weapon) =
    weapon._id.isNotBlank() && weapon.type.isNotBlank() && weapon.price.isNotBlank()
            && weapon.damage.isNotBlank() && weapon.weight.isNotBlank() && weapon.properties.isNotBlank()

fun validateSimpleEquipment(equipment: SimpleEquipment) =
    equipment._id.isNotBlank() && equipment.price.isNotBlank() && equipment.weight.isNotBlank()

fun String.format(vararg values: String): String {
    var newString = this
    values.forEach { newString = newString.replaceFirst("{}", it) }
    return newString
}

fun toDangerousHtml(value: String) = object : DangerousHTML {
    override var __html = value
}.unsafeCast<DangerouslySetInnerHTML>()
