package me.khrys.dnd.charcreator.common.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val _id: String,
    val characters: List<Character> = emptyList(),
    val images: Map<String, String> = emptyMap()
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
    var skills: List<Skill> = emptyList(),
    var speed: Int = 0,
    var armorClass: Int = 0,
    var race: String,
    var subrace: String? = null,
    var classes: Map<String, Int> = emptyMap(),
    var features: List<Feature> = emptyList(),
    var proficiencies: Set<String> = emptySet(),
    var languages: Set<String> = emptySet(),
    var maneuvers: List<Maneuver> = emptyList(),
    var additionalSpells: List<Spell> = emptyList(),
    var spells: List<Spell> = emptyList(),
    var spellcastingAbilities: List<String> = emptyList(),
    var spellSaveDC: Int = 0,
    var spellAttackBonus: Int = 0,
    var bonuses: CharBonuses = CharBonuses(),
    var equipment: Equipment,
    var superiorityDices: List<SuperiorityDice> = emptyList()
) {
    fun hasFeature(featureName: String) =
        this.features.map { it.name }.contains(featureName)
}

@Serializable
data class SuperiorityDice(
    var dice: Dice,
    var quantity: Int
)

@Serializable
data class CharBonuses(
    var initiative: Int = 0,
    var perception: Int = 0,
    var investigation: Int = 0
)

@Serializable
data class Abilities(
    var strength: Ability,
    var dexterity: Ability,
    var constitution: Ability,
    var intelligence: Ability,
    var wisdom: Ability,
    var charisma: Ability
)

@Serializable
data class Ability(
    var value: Int,
    var maxLimit: Int = 20
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
data class Skill(
    var name: String,
    var ability: String,
    var proficient: Boolean = false,
    var additionalBonus: Int = 0,
)

@Serializable
data class Race(
    val _id: String,
    val description: String,
    val features: List<Feature>,
    val subraces: List<Race> = emptyList(),
    val source: String = ""
)

@Serializable
data class Class(
    val _id: String,
    val description: String,
    val hitDice: Dice,
    val features: Map<Int, List<Feature>> = emptyMap(),
    val source: String = ""
)

@Serializable
data class Feature(
    var name: String,
    var description: String,
    var functions: List<DnDFunction> = emptyList(),
    var filters: List<Filter> = emptyList(),
    var withFeats: Boolean = false,
    var withoutFeats: Boolean = false,
    var singleClass: Boolean = false,
    var multiClass: Boolean = false,
    var source: String = "",
    var order: Int = 0
)

@Serializable
data class DnDFunction(
    val name: String,
    val values: List<String> = emptyList(),
    val addFeature: Boolean = false,
    val replaceCurrent: Boolean = false
)

@Serializable
data class Feat(
    val _id: String,
    val description: String,
    val functions: List<DnDFunction> = emptyList(),
    val filters: List<Filter> = emptyList(),
    val source: String = ""
)

@Serializable
data class Maneuver(
    val _id: String,
    val description: String,
    val source: String
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
        LEFT_HAND_TYPE,
        RIGHT_HAND_TYPE,
        ARMORS,
        WEAPONS,
        STRENGTH,
        DEXTERITY,
        CONSTITUTION,
        INTELLIGENCE,
        WISDOM,
        CHARISMA,
        LEVEL,
        FEATURES,
        MARTIAL_WEAPONS_SIZE
    }

    @Serializable
    enum class Comparator {
        CONTAINS,
        EQUALS,
        EQUALS_OR_HIGHER
    }
}

@Serializable
data class Spell(
    var _id: String,
    var description: String,
    var level: Int,
    var school: String,
    var ritual: Boolean = false,
    var castingTime: String,
    var range: String,
    var components: List<String>,
    var duration: String,
    var classes: List<String>,
    var source: String = ""
)

@Serializable
data class Equipment(
    var coinage: Coinage,
    var armors: List<Armor> = emptyList(),
    var weapons: List<Weapon> = emptyList(),
    var otherEquipment: List<SimpleEquipment> = emptyList()
)

@Serializable
data class SimpleEquipment(
    var _id: String,
    var description: String = "",
    var type: String? = null,
    var price: String,
    var weight: String,
    var functions: List<DnDFunction> = emptyList()
)

@Serializable
data class Weapon(
    var _id: String,
    var description: String = "",
    var type: String,
    var price: String,
    var damage: String,
    var weight: String,
    var properties: String = "",
    var functions: List<DnDFunction> = emptyList()
)

@Serializable
data class Armor(
    var _id: String,
    var description: String = "",
    var type: String,
    var price: String,
    var armorClass: String,
    var strength: Int? = null,
    var stealth: String,
    var weight: String,
    var functions: List<DnDFunction> = emptyList()
)

@Serializable
data class Coinage(
    var cp: Int = 0,
    var sp: Int = 0,
    var ep: Int = 0,
    var gp: Int = 0,
    var pp: Int = 0
)

@Serializable
enum class Dice(val maxValue: Int) {
    D4(4), D6(6), D8(8), D10(10), D12(12), D20(20), D100(100)
}

fun emptyCharacter() =
    Character(
        name = "",
        image = "",
        hitPoints = 0,
        abilities = initialAbilities(),
        savingThrows = initialSavingThrows(),
        race = "",
        features = emptyList(),
        equipment = emptyEquipment()
    )

fun emptyEquipment(): Equipment = Equipment(coinage = Coinage())

fun emptyRace() = Race("", "", emptyList())

fun emptyClass() = Class("", "", Dice.D4)

fun emptyFeat() = Feat("", "")

fun emptyManeuver() = Maneuver("", "", "")

fun emptyWeapon() = Weapon("", type = "", price = "", damage = "", weight = "", properties = "")

fun emptyArmor() = Armor("", type = "", price = "", armorClass = "", stealth = "", weight = "")

fun emptySimpleEquipment() = SimpleEquipment("", type = "", price = "", weight = "")

fun initialAbilities() = Abilities(Ability(10), Ability(10), Ability(10), Ability(10), Ability(10), Ability(10))

fun initialSavingThrows() = SavingThrows(
    strength = false,
    dexterity = false,
    constitution = false,
    intelligence = false,
    wisdom = false,
    charisma = false
)
