package me.khrys.dnd.charcreator.client

import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Maneuver
import me.khrys.dnd.charcreator.common.models.Spell
import react.Context
import react.createContext

val TranslationsContext: Context<Map<String, String>> = createContext()

val CharactersContext: Context<List<Character>> = createContext()

val FeatsContext: Context<Map<String, Feat>> = createContext()

val ManeuversContext: Context<Map<String, Maneuver>> = createContext()

val SpellsContext: Context<Map<String, Spell>> = createContext()
