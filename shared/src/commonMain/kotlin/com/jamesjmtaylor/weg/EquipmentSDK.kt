package com.jamesjmtaylor.weg

import com.jamesjmtaylor.weg.models.PageProgress
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.models.SearchResults
import com.jamesjmtaylor.weg.network.Api
import com.jamesjmtaylor.weg.network.Api.Companion.PAGE_SIZE
import com.jamesjmtaylor.weg.shared.cache.Database
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlin.math.max
import kotlin.math.min

class EquipmentSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val db = Database(databaseDriverFactory)
    private val api = Api()
    private val scope = MainScope()

    private suspend fun getEquipmentSearchResults(type: EquipmentType, key: Int): List<SearchResult>? {
        if (cachedPageProgress.getProgress(type) == null) {
            cachedPageProgress.setProgress(type, db.getPageProgressFor(type).toInt())
        }
        val cachedPage: Int = cachedPageProgress.getProgress(type) ?: 0
        if (totalHits.noMoreApiResults(type, key) || (cachedPage != 0 && cachedPage >= key)) {
            val filtered = trimCategoryNames(db.getAllResults()).filter { it.categories.contains(type.apiName) }
            val fromIndex = min(filtered.size, key * PAGE_SIZE)
            val toIndex = min(filtered.size, (key + 1) * PAGE_SIZE)
            return filtered.subList(fromIndex, toIndex)
        } else {
            //TODO: Commented out `totalHits.persistTotalHits` and `cachedPageProgress.setProgress` break iOS, but they're needed for pagination.
            //Either save equipment page to DB or figure a way around above limitation.
            val searchResults = api.getEquipmentSearchResults(type.apiName, key)
//            totalHits.persistTotalHits(type, searchResults)
            val searchResultList = searchResults.asList()?.toMutableList()
            searchResultList?.map { r -> r.images?.map { it.url = Api.BASE_URL + it.url}}
//            cachedPageProgress.setProgress(type, key)
            db.insertSearchResults(searchResultList ?: emptyList())
            db.insertPageProgress(PageProgress(type, (key).toLong()))
            return searchResultList?.toList()
        }
    }


    //TODO: Implement pagers for other equipment types
    //TODO: Check if Suppression still needed once used by Android project.
    @Suppress("MemberVisibilityCanBePrivate") val pager = Pager(
        clientScope = scope,
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false), // Ignored on iOS
        initialKey = 0,
        getItems = { currentKey, _ ->
            val items = getEquipmentSearchResults(EquipmentType.LAND, currentKey) ?: emptyList()
            PagingResult(
                items = items,
                currentKey = currentKey,
                prevKey = { max(currentKey - 1,0) },
                nextKey = { currentKey + 1 }
            )
        }
    )

    //TODO: Check if Suppression still needed once used by Android project.
    @Suppress("unused") val pagingData: CommonFlow<PagingData<SearchResult>>
        get() = pager.pagingData
            .cachedIn(scope) // cachedIn from AndroidX Paging. on iOS, this is a no-op
            .asCommonFlow() // So that iOS can consume the Flow

    private var cache = emptyList<SearchResult>()
    private var cachedPageProgress = CachedPageProgress()
    private var totalHits = TotalResults()

    @Throws(Exception::class)
    suspend fun getEquipmentNoCache(equipmentType: EquipmentType? = null, page: Int = 0, forceReload: Boolean? = false): List<SearchResult>? {
        if (forceReload == true) db.clearDatabase()
        val type = equipmentType?.apiName ?: return null
        val cachedPage = cachedPageProgress.getProgress(equipmentType) ?: 0

        if (totalHits.noMoreApiResults(equipmentType, page) || (cachedPage != 0 && cachedPage >= page)) {
            var filtered = trimCategoryNames(db.getAllResults()).filter { it.categories.contains(type) }
            val fromIndex = min(filtered.size, page * PAGE_SIZE)
            val toIndex = min(filtered.size, (page + 1) * PAGE_SIZE)
            return filtered.subList(fromIndex, toIndex)
        } else {
            val searchResults = api.getEquipmentSearchResults(type, page)
            totalHits.persistTotalHits(equipmentType, searchResults)
            val listResults = searchResults.asList().also { results -> results?.let {
                results.map { r -> r.images?.map { it.url = Api.BASE_URL + it.url } }
                cachedPageProgress.setProgress(equipmentType, page)
                db.insertSearchResults(results)
                db.insertPageProgress(PageProgress(equipmentType, page.toLong()))
            }}
            return listResults
        }
    }

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
    suspend fun getEquipment(equipmentType: EquipmentType? = null, page: Int = 0, forceReload: Boolean? = false): List<SearchResult>? {
        if (forceReload == true) db.clearDatabase()
        val type = equipmentType?.apiName ?: return null
        val cachedPage = cachedPageProgress.getProgress(equipmentType) ?: 0

        if (totalHits.noMoreApiResults(equipmentType, page) || (cachedPage != 0 && cachedPage >= page)) {
            if (cache.isEmpty()) cache = trimCategoryNames(db.getAllResults())

            val typeFilteredCache = cache.filter { it.categories.contains(type) }
            val fromIndex = min(typeFilteredCache.size, page * PAGE_SIZE)
            val toIndex = min(typeFilteredCache.size, (page + 1) * PAGE_SIZE)
            return typeFilteredCache.subList(fromIndex, toIndex)
        } else {
            val searchResults = api.getEquipmentSearchResults(type, page)
            totalHits.persistTotalHits(equipmentType, searchResults)
            val listResults = searchResults.asList().also { results -> results?.let {
                results.map { it.images?.map { it.url = Api.BASE_URL + it.url } }
                cache = cache.plus(results)
                cachedPageProgress.setProgress(equipmentType, page)
                db.insertSearchResults(results)
                db.insertPageProgress(PageProgress(equipmentType, page.toLong()))
            }}
            return listResults
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
}

/**
 * The type of equipment and the associated [apiName] (generally title cased).  This class allows
 * filtering of results based on the selected bottom navigation tab, as well as pagination caching
 * based on how far the user has scrolled on a particular tab.
 */
enum class EquipmentType(val apiName: String) { LAND("Land"), AIR("Air"), SEA("Sea") }

data class TotalResults(var land: Int? = null, var air: Int? = null, var sea: Int? = null) {
    /**
     * Saves the total number of hits for a given category to in-memory cache so that we know when
     * to fetch new results from the API rather than return cached ones from the DB.
     */
    fun persistTotalHits(
        equipmentType: EquipmentType,
        searchResults: SearchResults
    ) {
        when (equipmentType) {
            EquipmentType.LAND -> land = searchResults.query?.totalHits?.toInt()
            EquipmentType.AIR -> air = searchResults.query?.totalHits?.toInt()
            EquipmentType.SEA -> sea = searchResults.query?.totalHits?.toInt()
        }
    }

    fun noMoreApiResults(equipmentType: EquipmentType, page: Int): Boolean {
        val totalHits = when (equipmentType) {
            EquipmentType.LAND -> land
            EquipmentType.AIR -> air
            EquipmentType.SEA -> sea
        } ?: return false //we haven't received any hits yet, continue pagination.
        return page * PAGE_SIZE > totalHits
    }
}

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
