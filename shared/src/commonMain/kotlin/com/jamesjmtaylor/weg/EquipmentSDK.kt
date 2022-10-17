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
    private var cachedPageProgress = CachedPageProgress()
    private var totalHits = TotalResults()

    /**
     * Retrieves equipment by category. NOTE: DB returns results in ascending id order. API returns
     * results using a consistent, but unknown, ordering system (id=2365 is always the first result).
     * This can cause the order of equipment to shift between first and subsequent launches. This is
     * only a problem if new equipment is added to the API on a previously cached page. This edge
     * case is minimized by the user's ability to [forceReload].
     * @param equipmentType the type of equipment to retrieve.
     * @param page the page number to retrieve for infinite scrolling support. 0-based index.
     * @param forceReload triggered by the user pulling up on the search results to force a page reload.
     * @return a list of search results for the given page.
     */
    @Throws(Exception::class)
    suspend fun getEquipment(equipmentType: EquipmentType? = null, page: Int, forceReload: Boolean? = false): List<SearchResult>? {
        if (forceReload == true) db.clearDatabase()
        val type = equipmentType?.apiName ?: return null
        val cachedPage = getPaginationProgress(equipmentType)

        if (noMoreApiResults(equipmentType, page) || (cachedPage != 0 && cachedPage >= page)) {
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
                cachedPageProgress.setProgress(equipmentType, page)
                db.insertSearchResults(results)
                db.insertPageProgress(PageProgress(equipmentType, page.toLong()))
            }}
            return listResults
        }
    }

    /**
     * Retrieves how many pages the app has pulled down from the backend so far.
     */
    private fun getPaginationProgress(equipmentType: EquipmentType) : Int {
        val cachedProgress = cachedPageProgress.getProgress(equipmentType)
        if (cachedProgress == null) {
            val dbProgress = db.getPageProgressFor(equipmentType).toInt()
            cachedPageProgress.setProgress(equipmentType,dbProgress)
            return dbProgress
        } else {
            return cachedProgress
        }
    }

    /**
     * Necessary because SQL DB adds space characters in the category strings, preventing simple
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
            EquipmentType.LAND -> totalHits.land = searchResults.query?.totalHits?.toInt()
            EquipmentType.AIR -> totalHits.air = searchResults.query?.totalHits?.toInt()
            EquipmentType.SEA -> totalHits.sea = searchResults.query?.totalHits?.toInt()
        }
    }

    private fun noMoreApiResults(equipmentType: EquipmentType, page: Int): Boolean {
        val totalHits = when (equipmentType) {
            EquipmentType.LAND -> totalHits.land
            EquipmentType.AIR -> totalHits.air
            EquipmentType.SEA -> totalHits.sea
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
data class TotalResults(var land: Int? = null, var air: Int? = null, var sea: Int? = null)
data class CachedPageProgress(var land: Int? = null, var air: Int? = null, var sea: Int? = null) {
    fun getProgress(equipmentType: EquipmentType): Int? {
        return when (equipmentType) {
            EquipmentType.LAND -> land
            EquipmentType.AIR -> air
            EquipmentType.SEA -> sea
        }
    }
    fun setProgress(equipmentType: EquipmentType, progress: Int) {
        when (equipmentType) {
            EquipmentType.LAND -> land = progress
            EquipmentType.AIR -> air = progress
            EquipmentType.SEA -> sea = progress
        }
    }
}
