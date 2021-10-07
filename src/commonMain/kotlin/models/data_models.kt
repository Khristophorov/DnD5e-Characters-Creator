package me.khrys.dnd.charcreator.common.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val _id: String,
    val characters: List<Character> = emptyList()
)

@Serializable
data class Translation(
    val _id: String,
    val value: String
)

@Serializable
data class Character(
    var name: String,
    var image: String,
    var hitPoints: Int,
    var abilities: Abilities,
    var savingThrows: SavingThrows,
    var skills: Skills,
    var speed: Int = 0,
    var armorClass: Int = 0,
    var race: Race,
    var subrace: Race = race,
    var features: Array<Feature>,
    var proficiencies: Array<String> = emptyArray(),
    var languages: Array<String> = emptyArray(),
    var bonuses: CharBonuses = CharBonuses(),
    var superiorityDices: Array<SuperiorityDice> = emptyArray()
)

fun Character.hasFeature(featureName: String) =
    this.features.map { it.name }.contains(featureName)

@Serializable
data class SuperiorityDice(
    var dice: Dice,
    var quantity: Int
)

@Serializable
data class CharBonuses(
    var initiative: Int = 0
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
    var acrobatics: Boolean,
    var animalHandling: Boolean,
    var arcana: Boolean,
    var athletics: Boolean,
    var deception: Boolean,
    var history: Boolean,
    var insight: Boolean,
    var intimidation: Boolean,
    var investigation: Boolean,
    var medicine: Boolean,
    var nature: Boolean,
    var perception: Boolean,
    var performance: Boolean,
    var persuasion: Boolean,
    var religion: Boolean,
    var sleightOfHands: Boolean,
    var stealth: Boolean,
    var survival: Boolean
)

@Serializable
data class Race(
    val _id: String,
    val description: String,
    val features: Array<Feature>,
    val subraces: Array<Race> = emptyArray(),
    val source: String = ""
)

@Serializable
data class Feature(
    var name: String,
    var description: String,
    var functions: Array<DnDFunction> = emptyArray(),
    var filters: Array<Filter> = emptyArray(),
    var withFeats: Boolean = false,
    var withoutFeats: Boolean = false,
    var source: String = ""
)

@Serializable
data class DnDFunction(
    val name: String,
    val values: Array<String> = emptyArray()
)

@Serializable
data class Feat(
    val _id: String,
    val description: String,
    val functions: Array<DnDFunction> = emptyArray(),
    val filters: Array<Filter> = emptyArray(),
    val source: String = ""
)

@Serializable
data class Filter(
    val param: Param,
    val comparator: Comparator,
    val value: String
) {
    @Serializable
    enum class Param {
        PROFICIENCIES,
        WORE_TYPE,
        STRENGTH,
        DEXTERITY,
        CONSTITUTION,
        INTELLIGENCE,
        WISDOM,
        CHARISMA
    }
    @Serializable
    enum class Comparator {
        CONTAINS,
        EQUALS_OR_HIGHER
    }
}

@Serializable
enum class Dice {
    D4, D6, D8, D10, D12, D20, D100
}

fun emptyCharacter() =
    Character(
        name = "",
        image = "",
        hitPoints = 0,
        abilities = initialAbilities(),
        savingThrows = initialSavingThrows(),
        skills = initialSkills(),
        race = emptyRace(),
        features = emptyArray()
    )

fun emptyRace() = Race("", "", emptyArray())

fun emptyFeat() = Feat("", "")

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
