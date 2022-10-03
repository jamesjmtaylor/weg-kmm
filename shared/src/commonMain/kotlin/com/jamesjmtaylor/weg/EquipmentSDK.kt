package com.jamesjmtaylor.weg

import com.jamesjmtaylor.weg.models.PageProgress
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.models.SearchResults
import com.jamesjmtaylor.weg.network.Api
import com.jamesjmtaylor.weg.network.Api.Companion.PAGE_SIZE
import com.jamesjmtaylor.weg.shared.cache.Database
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
import kotlin.math.min

class EquipmentSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val db = Database(databaseDriverFactory)
    private val api = Api()
    private var cache = emptyList<SearchResult>()
    private var totalLandHits : Int? = null
    private var totalAirHits : Int? = null
    private var totalSeaHits : Int? = null

    /**
     * Retrieves equipment by category.
     * @param equipmentType the type of equipment to retrieve.
     * @param page the page number to retrieve for infinite scrolling support.
     * @param forceReload triggered by the user pulling up on the search results to force a page reload.
     */
    @Throws(Exception::class)
    suspend fun getEquipment(equipmentType: EquipmentType? = null, page: Int, forceReload: Boolean? = false): List<SearchResult>? {
        if (forceReload == true) db.clearDatabase()

        val type = equipmentType?.apiName ?: return null
        val cachedPage = db.getPageProgressFor(equipmentType)

        if (pageLimitReached(equipmentType, page) ||  cachedPage >= page) {
            if (cache.isEmpty()) cache = trimCategoryNames(db.getAllResults())

            val typeFilteredCache = cache.filter { it.categories.contains(type) }
            val fromIndex = min(typeFilteredCache.size, page * PAGE_SIZE)
            val toIndex = min(typeFilteredCache.size, (page + 1) * PAGE_SIZE)
            return typeFilteredCache.subList(fromIndex, toIndex)
        } else {
            val searchResults = api.getEquipmentSearchResults(type, page)
            persistTotalHits(equipmentType, searchResults)
            val listResults = searchResults.asList().also { results -> results?.let {
                cache = cache.plus(results)
                db.insertSearchResults(results)
                db.insertPageProgress(PageProgress(equipmentType, page.toLong()))
            }}
            return listResults
        }
    }

    /**
     * Necessary because DB adds hidden characters in the category strings, preventing simple
     * filtering based on the selected bottom navigation equipment type.
     */
    private fun trimCategoryNames(results: List<SearchResult>): MutableList<SearchResult> {
        val trimmedResults = mutableListOf<SearchResult>()
        for (result in results) {
            val trimmedCategories = result.categories.map { it.trim() }
            val trimmedResult =
                SearchResult(result.title, result.id, trimmedCategories, result.images)
            trimmedResults.add(trimmedResult)
        }
        return trimmedResults
    }

    /**
     * Saves the total number of hits for a given category to in-memory cache so that we know when
     * to fetch new results from the API rather than return cached ones from the DB.
     */
    private fun persistTotalHits(
        equipmentType: EquipmentType,
        searchResults: SearchResults
    ) {
        when (equipmentType) {
            EquipmentType.LAND -> totalLandHits = searchResults.query?.totalHits?.toInt()
            EquipmentType.AIR -> totalAirHits = searchResults.query?.totalHits?.toInt()
            EquipmentType.SEA -> totalSeaHits = searchResults.query?.totalHits?.toInt()
        }
    }

    private fun pageLimitReached(equipmentType: EquipmentType, page: Int): Boolean {
        val totalHits = when (equipmentType) {
            EquipmentType.LAND -> totalLandHits
            EquipmentType.AIR -> totalAirHits
            EquipmentType.SEA -> totalSeaHits
        } ?: return false //we haven't received any hits yet, continue pagination.
        return page * PAGE_SIZE > totalHits
    }
}

/**
 * The type of equipment and the associated [apiName] (generally title cased).  This class allows
 * filtering of results based on the selected bottom navigation tab, as well as pagination caching
 * based on how far the user has scrolled on a particular tab.
 */
enum class EquipmentType(val apiName: String) { LAND("Land"), AIR("Air"), SEA("Sea") }