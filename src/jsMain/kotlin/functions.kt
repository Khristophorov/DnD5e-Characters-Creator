package me.khrys.dnd.charcreator.client

import kotlin.math.ceil
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
import me.khrys.dnd.charcreator.common.models.Ability
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Dice
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Feature
import me.khrys.dnd.charcreator.common.models.Filter
import me.khrys.dnd.charcreator.common.models.Filter.Comparator.CONTAINS
import me.khrys.dnd.charcreator.common.models.Filter.Comparator.EQUALS
import me.khrys.dnd.charcreator.common.models.Filter.Comparator.EQUALS_OR_HIGHER
import me.khrys.dnd.charcreator.common.models.Filter.Param.ARMORS
import me.khrys.dnd.charcreator.common.models.Filter.Param.CHARISMA
import me.khrys.dnd.charcreator.common.models.Filter.Param.CONSTITUTION
import me.khrys.dnd.charcreator.common.models.Filter.Param.DEXTERITY
import me.khrys.dnd.charcreator.common.models.Filter.Param.FEATURES
import me.khrys.dnd.charcreator.common.models.Filter.Param.INTELLIGENCE
import me.khrys.dnd.charcreator.common.models.Filter.Param.LEFT_HAND_TYPE
import me.khrys.dnd.charcreator.common.models.Filter.Param.LEVEL
import me.khrys.dnd.charcreator.common.models.Filter.Param.PROFICIENCIES
import me.khrys.dnd.charcreator.common.models.Filter.Param.RIGHT_HAND_TYPE
import me.khrys.dnd.charcreator.common.models.Filter.Param.STRENGTH
import me.khrys.dnd.charcreator.common.models.Filter.Param.WEAPONS
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
import me.khrys.dnd.charcreator.common.MARTIAL_WEAPON_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Armor
import me.khrys.dnd.charcreator.common.models.Filter.Param.MARTIAL_WEAPONS_SIZE

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
    if (isRanged(properties, translations)) computeModifier(abilities.dexterity.value)
    else if (isFinesse(properties, translations))
        computeModifier(max(abilities.strength.value, abilities.dexterity.value))
    else computeModifier(abilities.strength.value)

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
    character.features.sortedBy { it.order }.forEach { feature ->
        val accept =
            feature.filters.isEmpty() || feature.filters.map { it.apply(character, translations) }
                .reduce { old, new -> old && new }
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
    strength = this.strength.clone(),
    dexterity = this.dexterity.clone(),
    constitution = this.constitution.clone(),
    intelligence = this.intelligence.clone(),
    wisdom = this.wisdom.clone(),
    charisma = this.charisma.clone()
)

fun Ability.clone() = Ability(this.value, this.maxLimit)

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
        val values = function.values
        when (function.name) {
            "Increase Strength" -> increaseStrength(values[0].toInt())
            "Increase Dexterity" -> increaseDexterity(values[0].toInt())
            "Increase Constitution" -> increaseConstitution(values[0].toInt())
            "Increase Intelligence" -> increaseIntelligence(values[0].toInt())
            "Increase Charisma" -> increaseCharisma(values[0].toInt())
            "Increase Wisdom" -> increaseWisdom(values[0].toInt())
            "Increase Maximum Strength" -> increaseMaximumStrength(values[0].toInt())
            "Increase Maximum Constitution" -> increaseMaximumConstitution(values[0].toInt())
            "Increase Ability" -> increaseAbility(values[0], values[1].toInt(), translations)
            "Increase Ability with Saving Throws" -> increaseAbilityAndSavingThrows(
                values[0],
                values[1].toInt(),
                translations
            )

            "Increase Non Proficient Skills With Proficiency Bonus" -> increaseSkillsWithProficiencyBonus(
                false,
                values[0].toDouble(),
                values.subList(1, values.size)
            )

            "Increase Non Proficient Skills With Proficiency Bonus Ceil" -> increaseSkillsWithProficiencyBonusCeil(
                false,
                values[0].toDouble(),
                values.subList(1, values.size)
            )

            "Increase Initiative" -> increaseInitiative(values[0].toInt())
            "Increase Perception" -> increasePerception(values[0].toInt())
            "Increase Investigation" -> increaseInvestigation(values[0].toInt())
            "Increase Speed" -> increaseSpeed(values[0].toInt())
            "Increase Armor Class" -> increaseArmorClass(values[0].toInt())
            "Increase HP by Level" -> increaseHitPointsByLevel(values[0].toInt())
            "Double Skill Bonus" -> increaseSkillsWithProficiencyBonus(values, 1.0)
            "Set Speed" -> setSpeed(values[0].toInt())
            "Set Spellcasting Ability" -> setSpellcastingAbility(values[0], translations)
            "Add Proficiencies" -> setProficiencies(values)
            "Add Languages" -> addLanguages(values)
            "Add Saving Throws" -> addSavingThrows(values, translations)
            "Add Skills" -> addSkills(values, translations)
            "Add Superiority Dices" -> addSuperiorityDices(Dice.valueOf(values[0]), values[1].toInt())
            "Add Spells" -> addSpells(values, spells)
            "Add Additional Spells" -> addAdditionalSpells(values, spells)
        }
    }
}

fun Character.increaseStrength(value: Int) {
    val strength = this.abilities.strength
    if (strength.value + value <= strength.maxLimit) {
        strength.value += value
    }
}

fun Character.increaseDexterity(value: Int) {
    val dexterity = this.abilities.dexterity
    if (dexterity.value + value <= dexterity.maxLimit) {
        dexterity.value += value
    }
}

fun Character.increaseConstitution(value: Int) {
    val constitution = this.abilities.constitution
    if (constitution.value + value <= constitution.maxLimit) {
        constitution.value += value
    }
}

fun Character.increaseIntelligence(value: Int) {
    val intelligence = this.abilities.intelligence
    if (intelligence.value + value <= intelligence.maxLimit) {
        intelligence.value += value
    }
}

fun Character.increaseWisdom(value: Int) {
    val wisdom = this.abilities.wisdom
    if (wisdom.value + value <= wisdom.maxLimit) {
        wisdom.value += value
    }
}

fun Character.increaseCharisma(value: Int) {
    val charisma = this.abilities.charisma
    if (charisma.value + value <= charisma.maxLimit) {
        charisma.value += value
    }
}

fun Character.increaseMaximumStrength(value: Int) {
    this.abilities.strength.maxLimit += value
}

fun Character.increaseMaximumConstitution(value: Int) {
    this.abilities.constitution.maxLimit += value
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

fun Character.increaseSkillsWithProficiencyBonus(
    proficient: Boolean,
    multiplier: Double,
    skills: List<String>
) {
    val bonus = (computeProficiencyBonus(this.getCombinedLevel()) * multiplier).toInt()
    this.skills.filter { skills.contains(it.ability) }.forEach { skill ->
        if (skill.proficient == proficient) {
            skill.additionalBonus += bonus
        }
    }
}

fun Character.increaseSkillsWithProficiencyBonusCeil(
    proficient: Boolean,
    multiplier: Double,
    skills: List<String>
) {
    val bonus = ceil(computeProficiencyBonus(this.getCombinedLevel()) * multiplier).toInt()
    this.skills.filter { skills.contains(it.ability) }.forEach { skill ->
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

fun Character.increaseHitPointsByLevel(value: Int) {
    val level = this.getCombinedLevel()
    this.hitPoints += level * value
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
        translations[STRENGTH_TRANSLATION] -> this.abilities.strength.value
        translations[DEXTERITY_TRANSLATION] -> this.abilities.dexterity.value
        translations[CONSTITUTION_TRANSLATION] -> this.abilities.constitution.value
        translations[INTELLIGENCE_TRANSLATION] -> this.abilities.intelligence.value
        translations[WISDOM_TRANSLATION] -> this.abilities.wisdom.value
        translations[CHARISMA_TRANSLATION] -> this.abilities.charisma.value
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
    this.addSkill(
        skills, translations[ACROBATICS_TRANSLATION] ?: "", Skill(
            name = translations[ACROBATICS_TRANSLATION] ?: "",
            ability = translations[DEXTERITY_TRANSLATION] ?: "",
            proficient = skills.contains(translations[ACROBATICS_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[ANIMAL_HANDLING_TRANSLATION] ?: "", Skill(
            name = translations[ANIMAL_HANDLING_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[ANIMAL_HANDLING_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[ARCANA_TRANSLATION] ?: "", Skill(
            name = translations[ARCANA_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[ARCANA_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[ATHLETICS_TRANSLATION] ?: "", Skill(
            name = translations[ATHLETICS_TRANSLATION] ?: "",
            ability = translations[STRENGTH_TRANSLATION] ?: "",
            proficient = skills.contains(translations[ATHLETICS_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[DECEPTION_TRANSLATION] ?: "", Skill(
            name = translations[DECEPTION_TRANSLATION] ?: "",
            ability = translations[CHARISMA_TRANSLATION] ?: "",
            proficient = skills.contains(translations[DECEPTION_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[HISTORY_TRANSLATION] ?: "", Skill(
            name = translations[HISTORY_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[HISTORY_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[INSIGHT_TRANSLATION] ?: "", Skill(
            name = translations[INSIGHT_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[INSIGHT_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[INTIMIDATION_TRANSLATION] ?: "", Skill(
            name = translations[INTIMIDATION_TRANSLATION] ?: "",
            ability = translations[CHARISMA_TRANSLATION] ?: "",
            proficient = skills.contains(translations[INTIMIDATION_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[INVESTIGATION_TRANSLATION] ?: "", Skill(
            name = translations[INVESTIGATION_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[INVESTIGATION_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[MEDICINE_TRANSLATION] ?: "", Skill(
            name = translations[MEDICINE_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[MEDICINE_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[NATURE_TRANSLATION] ?: "", Skill(
            name = translations[NATURE_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[NATURE_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[PERCEPTION_TRANSLATION] ?: "", Skill(
            name = translations[PERCEPTION_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[PERCEPTION_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[PERFORMANCE_TRANSLATION] ?: "", Skill(
            name = translations[PERFORMANCE_TRANSLATION] ?: "",
            ability = translations[CHARISMA_TRANSLATION] ?: "",
            proficient = skills.contains(translations[PERFORMANCE_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[PERSUASION_TRANSLATION] ?: "", Skill(
            name = translations[PERSUASION_TRANSLATION] ?: "",
            ability = translations[CHARISMA_TRANSLATION] ?: "",
            proficient = skills.contains(translations[PERSUASION_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[RELIGION_TRANSLATION] ?: "", Skill(
            name = translations[RELIGION_TRANSLATION] ?: "",
            ability = translations[INTELLIGENCE_TRANSLATION] ?: "",
            proficient = skills.contains(translations[RELIGION_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[SLEIGHT_OF_HANDS_TRANSLATION] ?: "", Skill(
            name = translations[SLEIGHT_OF_HANDS_TRANSLATION] ?: "",
            ability = translations[DEXTERITY_TRANSLATION] ?: "",
            proficient = skills.contains(translations[SLEIGHT_OF_HANDS_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[STEALTH_TRANSLATION] ?: "", Skill(
            name = translations[STEALTH_TRANSLATION] ?: "",
            ability = translations[DEXTERITY_TRANSLATION] ?: "",
            proficient = skills.contains(translations[STEALTH_TRANSLATION])
        )
    )
    this.addSkill(
        skills, translations[SURVIVAL_TRANSLATION] ?: "", Skill(
            name = translations[SURVIVAL_TRANSLATION] ?: "",
            ability = translations[WISDOM_TRANSLATION] ?: "",
            proficient = skills.contains(translations[SURVIVAL_TRANSLATION])
        )
    )
}

fun Character.addSkill(toSetProficient: List<String>, skillName: String, newSkill: Skill) {
    val currentSkill = this.skills.find { it.name == skillName } ?: newSkill
    currentSkill.proficient = toSetProficient.contains(skillName) || currentSkill.proficient
    this.skills = this.skills.filter { it.name != skillName } + currentSkill
}

fun Character.getCombinedLevel(): Int = this.classes.values.sum()

fun Character.computeArmorClass(): Int {
    return 10 + computeModifier(
        this.abilities.dexterity.value,
        computeProficiencyBonus(this.getCombinedLevel()),
        this.savingThrows.dexterity
    ) + this.armorClass
}

fun Character.getInitiative(): Int {
    return computeModifier(
        this.abilities.dexterity.value,
        computeProficiencyBonus(this.getCombinedLevel()),
        this.savingThrows.dexterity
    ) + bonuses.initiative
}

fun Character.setDefaultHitPoints(dice: Dice) {
    if (this.hitPoints == 0) {
        this.hitPoints = dice.maxValue + computeModifier(this.abilities.constitution.value)
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

fun Character.isProficient(skill: String) = this.skills.find { it.name == skill }?.proficient ?: false

fun Feat.toFeature(): Feature = Feature(
    name = this._id,
    description = this.description,
    functions = this.functions,
    filters = this.filters,
    source = this.source
)

fun Filter.apply(character: Character, translations: Map<String, String>): Boolean {
    val dataSet = when (this.param) {
        PROFICIENCIES -> character.proficiencies
        WORE_TYPE -> emptyList()
        LEFT_HAND_TYPE -> emptyList()
        RIGHT_HAND_TYPE -> emptyList()
        ARMORS -> character.equipment.armors.map { it._id }
        WEAPONS -> character.equipment.weapons.map { it._id }
        FEATURES -> character.features.map { it.name }
        else -> emptyList()
    }
    val dataNum = when (this.param) {
        STRENGTH -> character.abilities.strength.value
        DEXTERITY -> character.abilities.dexterity.value
        CONSTITUTION -> character.abilities.constitution.value
        INTELLIGENCE -> character.abilities.intelligence.value
        WISDOM -> character.abilities.wisdom.value
        CHARISMA -> character.abilities.charisma.value
        LEVEL -> character.getCombinedLevel()
        MARTIAL_WEAPONS_SIZE ->
            character.equipment.weapons.filter { it.type == translations[MARTIAL_WEAPON_TRANSLATION] }.size

        else -> 0
    }
    return when (this.comparator) {
        CONTAINS -> dataSet.contains(value)
        EQUALS -> dataNum == value.toInt()
        EQUALS_OR_HIGHER -> dataNum >= value.toInt()
    }
}

fun validateWeapon(weapon: Weapon) =
    weapon._id.isNotBlank() && weapon.type.isNotBlank() && weapon.price.isNotBlank()
            && weapon.damage.isNotBlank() && weapon.weight.isNotBlank() && weapon.properties.isNotBlank()

fun validateArmor(armor: Armor) =
    armor._id.isNotBlank() && armor.type.isNotBlank() && armor.price.isNotBlank()
            && armor.armorClass.isNotBlank() && armor.weight.isNotBlank()

fun validateSimpleEquipment(equipment: SimpleEquipment) =
    equipment._id.isNotBlank() && equipment.price.isNotBlank() && equipment.weight.isNotBlank()

fun String.format(vararg values: String): String {
    var newString = this
    values.forEach { newString = newString.replaceFirst("{}", it) }
    return newString
}

fun String.isNumber() = this.toIntOrNull() != null

fun toDangerousHtml(value: String) = object : DangerousHTML {
    override var __html = value
}.unsafeCast<DangerouslySetInnerHTML>()
