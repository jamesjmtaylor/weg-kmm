package com.jamesjmtaylor.weg

import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.network.Api
import com.jamesjmtaylor.weg.network.Api.Companion.PAGE_SIZE
import com.jamesjmtaylor.weg.shared.cache.Database
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlin.math.max
import kotlin.native.concurrent.ThreadLocal

/**
 * Provides a repository pattern for iOS & Android presentation logic to draw models from.
 *
 * NOTE: Clients must either maintain a singleton reference themselves, or construct a new instance
 * for each use. This is because with the old Native Garbage Collector, variables in singletons
 * without @ThreadLocal can't be changed after initialization.
 *
 * The documentation for @ThreadLocal states "The object remains mutable and it is possible to
 * change its state, but every thread will have a distinct copy of this object, so changes in one
 * thread are not reflected in another."  Which completely defeats the purpose of the singleton
 * pattern.
 *
 * @param databaseDriverFactory Used to construct a new instance of the DB used for long-term
 * storage.
 *
 */
@OptIn(FlowPreview::class) // Used for multiplatform pagination.
@Suppress("MemberVisibilityCanBePrivate") //iOS uses landPager, airPager, and seaPager & needs them to be public.
class EquipmentSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val db = Database(databaseDriverFactory)
    private val api = Api()
    private val scope = MainScope()

    suspend fun getEquipmentDetails(id: Long): SearchResult {
        //TODO: Persist the result to the database using the various tables in the [Database] class.
        //TODO: Implement db retrieval
        // db.fetchDetails(id)
        val searchResult = api.getEquipmentById(id)
        searchResult.images?.map { it.url = Api.BASE_URL + it.url }
        return searchResult
    }

    /**
     * Retrieves equipment by category and page. NOTE: DB returns results in ascending id order. API returns
     * results using a consistent, but unknown, ordering system (id=2365 is always the first result).
     * This can cause the order of equipment to shift between first and subsequent launches. This is
     * only a problem if new equipment is added to the API on a previously cached page. This edge
     * case is minimized by the user's ability to [forceReload]. TODO: Implement force reload
     * @param type the type of equipment to retrieve.
     * @param page the page number to retrieve for infinite scrolling support. 0-based index.
     * @return a list of search results for the given page.
     */
    private suspend fun getEquipmentSearchResults(type: EquipmentType, page: Int): List<SearchResult>? {
        val dbResults = trimCategoryNames(db.fetchResults(page))
            .filter { it.categories.contains(type.apiName) }.toList()
        return dbResults.ifEmpty {
            val apiResults = api.getEquipmentSearchResults(type.apiName, page)
                .asList()?.toMutableList()
            apiResults?.map { r -> r.images?.map { it.url = Api.BASE_URL + it.url } }
            apiResults?.let { db.insertSearchResults(apiResults, page) }
            apiResults?.toList()
        }
    }


    val landPager = Pager(
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
    val landPagingData: CommonFlow<PagingData<SearchResult>>
        get() = landPager.pagingData
            .cachedIn(scope) // cachedIn from AndroidX Paging. on iOS, this is a no-op
            .asCommonFlow() // So that iOS can watch the Flow

    val airPager = Pager(
        clientScope = scope,
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false), // Ignored on iOS
        initialKey = 0,
        getItems = { currentKey, _ ->
            val items = getEquipmentSearchResults(EquipmentType.AIR, currentKey) ?: emptyList()
            PagingResult(
                items = items,
                currentKey = currentKey,
                prevKey = { max(currentKey - 1,0) },
                nextKey = { currentKey + 1 }
            )
        }
    )
    val airPagingData: CommonFlow<PagingData<SearchResult>>
        get() = airPager.pagingData
            .cachedIn(scope) // cachedIn from AndroidX Paging. on iOS, this is a no-op
            .asCommonFlow() // So that iOS can watch the Flow

    val seaPager = Pager(
        clientScope = scope,
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false), // Ignored on iOS
        initialKey = 0,
        getItems = { currentKey, _ ->
            val items = getEquipmentSearchResults(EquipmentType.SEA, currentKey) ?: emptyList()
            PagingResult(
                items = items,
                currentKey = currentKey,
                prevKey = { max(currentKey - 1,0) },
                nextKey = { currentKey + 1 }
            )
        }
    )

    val seaPagingData: CommonFlow<PagingData<SearchResult>>
        get() = seaPager.pagingData
            .cachedIn(scope) // cachedIn from AndroidX Paging. on iOS, this is a no-op
            .asCommonFlow() // So that iOS can watch the Flow
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
