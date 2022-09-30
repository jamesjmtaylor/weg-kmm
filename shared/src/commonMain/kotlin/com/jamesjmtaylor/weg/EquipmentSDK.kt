package com.jamesjmtaylor.weg

import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.models.SearchResults
import com.jamesjmtaylor.weg.network.Api
import com.jamesjmtaylor.weg.shared.cache.Database
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
import io.ktor.util.reflect.*

class EquipmentSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val db = Database(databaseDriverFactory)
    private val api = Api()

    @Throws(Exception::class)
    suspend fun getEquipment(equipmentType: EquipmentType? = null, forceReload: Boolean? = false): List<SearchResult>? {
        //TODO: Implement in-memory cache.
        //NOTE: trim() required because returned category strings have hidden characters.
        val cachedEquipment = db.getAllResults()
            .filter { it.categories.firstOrNull { it.trim() == equipmentType?.apiName } != null }
        return if (cachedEquipment.isNotEmpty() && forceReload != true) {
            cachedEquipment
        } else {
            api.getEquipment(equipmentType?.apiName ?: "").asList().also { results ->
                results?.let {
                    db.clearDatabase()
                    db.insertSearchResults(it)
                }
            }
        }
    }
}


enum class EquipmentType(val apiName: String) { LAND("Land"), AIR("Air"), SEA("Sea") }