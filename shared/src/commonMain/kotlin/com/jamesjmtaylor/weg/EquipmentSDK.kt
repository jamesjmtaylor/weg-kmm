package com.jamesjmtaylor.weg

import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.models.SearchResults
import com.jamesjmtaylor.weg.network.Api
import com.jamesjmtaylor.weg.shared.cache.Database
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory

class EquipmentSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val db = Database(databaseDriverFactory)
    private val api = Api()

    @Throws(Exception::class)
    suspend fun getEquipment(forceReload: Boolean): List<SearchResult>? {
        val cachedEquipment = db.getAllResults()
        return if (cachedEquipment.isNotEmpty() && !forceReload) {
            cachedEquipment
        } else {
            api.getEquipment().asList().also { results ->
                results?.let {
                    db.clearDatabase()
                    db.insertSearchResults(it)
                }
            }
        }
    }
}