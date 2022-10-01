package com.jamesjmtaylor.weg

import com.jamesjmtaylor.weg.models.PageProgress
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
    suspend fun getEquipment(equipmentType: EquipmentType? = null, page: Int, forceReload: Boolean? = false): List<SearchResult>? {
        //TODO: Implement in-memory cache.
        val type = equipmentType?.apiName ?: return null
        val cachedPage = db.getPageProgressFor(equipmentType)
        val cachedEquipment = db.getAllResults().filter { it.categories.contains(type) }
        return if (cachedEquipment.isNotEmpty() && cachedPage >= page && forceReload != true) {
            cachedEquipment
        } else {
            api.getEquipment(type, page).asList().also { results -> results?.let { unwrappedResults ->
                //trim() is necessary because ODIN api has hidden characters in the category strings.
                val trimmedResults = mutableListOf<SearchResult>()
                for (result in unwrappedResults) {
                    val trimmedCategories = result.categories.map { it.trim() }
                    val trimmedResult = SearchResult(result.title,result.id,trimmedCategories,result.images)
                    trimmedResults.add(trimmedResult)
                }
                db.insertSearchResults(trimmedResults)
                db.insertPageProgress(PageProgress(equipmentType, page.toLong()))
            }
            }
        }
    }
}

/**
 * The type of equipment and the associated [apiName] (generally title cased).  This class allows
 * filtering of results based on the selected bottom navigation tab, as well as pagination caching
 * based on how far the user has scrolled on a particular tab.
 */
enum class EquipmentType(val apiName: String) { LAND("Land"), AIR("Air"), SEA("Sea") }