package mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.Race

class RacesService(private val races: MongoCollection<Race>) {

    fun readRaces() = races.find().toList()
}
