package me.khrys.dnd.charcreator.common.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val _id: String, val characters: List<Character> = emptyList())

@Serializable
data class Translation(val _id: String, val value: String)

@Serializable
data class Character(
    var name: String,
    var image: String,
    var abilities: Abilities,
    var savingThrows: SavingThrows,
    var skills: Skills
)

@Serializable
data class Abilities(
    var strength: Int,
    var dexterity: Int,
    var constitution: Int,
    var intelligence: Int,
    var wisdom: Int,
    var charisma: Int
)

@Serializable
data class SavingThrows(
    var strength: Boolean,
    var dexterity: Boolean,
    var constitution: Boolean,
    var intelligence: Boolean,
    var wisdom: Boolean,
    var charisma: Boolean
)

@Serializable
data class Skills(
    val acrobatics: Boolean,
    val animalHandling: Boolean,
    val arcana: Boolean,
    val athletics: Boolean,
    val deception: Boolean,
    val history: Boolean,
    val insight: Boolean,
    val intimidation: Boolean,
    val investigation: Boolean,
    val medicine: Boolean,
    val nature: Boolean,
    val perception: Boolean,
    val performance: Boolean,
    val persuasion: Boolean,
    val religion: Boolean,
    val sleightOfHands: Boolean,
    val stealth: Boolean,
    val survival: Boolean,
)

fun emptyCharacter() = Character("", "", initialAbilities(), initialSavingThrows(), initialSkills())
fun initialAbilities() = Abilities(10, 10, 10, 10, 10, 10)
fun initialSavingThrows() = SavingThrows(
    strength = false,
    dexterity = false,
    constitution = false,
    intelligence = false,
    wisdom = false,
    charisma = false
)

fun initialSkills() = Skills(
    acrobatics = false,
    animalHandling = false,
    arcana = false,
    athletics = false,
    deception = false,
    history = false,
    insight = false,
    intimidation = false,
    investigation = false,
    medicine = false,
    nature = false,
    perception = false,
    performance = false,
    persuasion = false,
    religion = false,
    sleightOfHands = false,
    stealth = false,
    survival = false
)
