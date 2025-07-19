package me.khrys.dnd.charcreator.client.components.dialogs.windows

import emotion.react.css
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.computeSpellLevel
import me.khrys.dnd.charcreator.client.toDangerousHtml
import me.khrys.dnd.charcreator.common.CANTRIP_TRANSLATION
import me.khrys.dnd.charcreator.common.CASTING_TIME_TRANSLATION
import me.khrys.dnd.charcreator.common.COMPONENTS_TRANSLATION
import me.khrys.dnd.charcreator.common.DURATION_TRANSLATION
import me.khrys.dnd.charcreator.common.RITUAL_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_LEVEL_SUFFIX_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_LEVEL_TRANSLATION
import me.khrys.dnd.charcreator.common.SPELL_RANGE_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Spell
import react.FC
import react.Props
import react.dom.html.ReactHTML.b
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.p
import react.use
import web.cssom.TextAlign

external interface SpellProps : Props {
    var spell: Spell
}

val SpellWindow = FC<SpellProps> { props ->
    val spell = props.spell
    val translations = use(TranslationsContext)
    div {
        css { textAlign = TextAlign.left }
        h3 { +spell._id }
        i {
            +"${
                computeSpellLevel(
                    spell.level,
                    translations[CANTRIP_TRANSLATION] ?: "",
                    translations[SPELL_LEVEL_SUFFIX_TRANSLATION]
                )
            }  ${translations[SPELL_LEVEL_TRANSLATION]?.lowercase()}, ${spell.school}"
            if (spell.ritual) {
                +" (${translations[RITUAL_TRANSLATION]})"
            }
        }
        p {
            b { +"${translations[CASTING_TIME_TRANSLATION]}: " }
            +spell.castingTime
            br()
            b { +"${translations[SPELL_RANGE_TRANSLATION]}: " }
            +spell.range
            br()
            b { +"${translations[COMPONENTS_TRANSLATION]}: " }
            +spell.components.joinToString(postfix = ".")
            br()
            b { +"${translations[DURATION_TRANSLATION]}: " }
            +spell.duration
        }
        p { this.dangerouslySetInnerHTML = toDangerousHtml(spell.description) }
    }
}
