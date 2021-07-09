package mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.Race

class RacesService(private val races: MongoCollection<Race>) {

    fun readRaces(): List<Race> {
        return races.find().toList()
    }
}
