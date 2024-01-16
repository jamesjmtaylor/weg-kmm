package com.jamesjmtaylor.weg2015

import kotlinx.coroutines.MainScope

class EquipmentSDK() {
    private val api = Api()
    private val scope = MainScope()
//
//    suspend fun getEquipmentDetails(id: Long): Contentlet? {
//        val dbResult = db.getContentletDetailsById(id)
//        return if (dbResult == null) {
//            val apiResult = api.getContentletById(id)
//            apiResult.details?.let { db.insertContentletDetails(id, it) }
//            apiResult
//        } else {
//            return null //TODO: Make non-nullable
//            Contentlet.copy(details = dbResult)
//        }
//    }

    /**
     * Retrieves equipment by category and page. NOTE: DB returns results in ascending id order. API returns
     * results using a consistent, but unknown, ordering system (id=2365 is always the first result).
     * This can cause the order of equipment to shift between first and subsequent launches. This is
     * only a problem if new equipment is added to the API on a previously cached page. This edge
     * case is minimized by the user's ability to [forceReload]. TODO: Implement force reload
     * @param type the type of equipment to retrieve.
     * @param page the page number to retrieve for infinite scrolling support. 0-based index.
     * @return a list of search results for the given page.
     * TODO: See https://proandroiddev.com/writing-swift-friendly-kotlin-multiplatform-apis-part-ix-flow-d4b6ada59395 for making this more "Swifty"
     */
    suspend fun getEquipmentSearchResults(type: EquipmentType, page: Int): List<Contentlet> {
        return api.getSearchResults(type.apiName, page).entity.jsonObjectView.contentlets
    }

//TODO: Fix paging, see https://developer.android.com/topic/libraries/architecture/paging/v3-overview
//    val landPager = Pager(
//        clientScope = scope,
//        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false), // Ignored on iOS
//        initialKey = 0,
//        getItems = { currentKey, _ ->
//            val items = getEquipmentSearchResults(EquipmentType.LAND, currentKey)
//            PagingResult(
//                items = items,
//                currentKey = currentKey,
//                prevKey = { max(currentKey - 1,0) },
//                nextKey = { currentKey + 1 }
//            )
//        }
//    )
//    val landPagingData: CommonFlow<PagingData<Contentlet>>
//        get() = landPager.pagingData
//            .cachedIn(scope) // cachedIn from AndroidX Paging. on iOS, this is a no-op
//            .asCommonFlow() // So that iOS can watch the Flow
}

/**
 * The type of equipment and the associated [apiName] (generally title cased).  This class allows
 * filtering of results based on the selected bottom navigation tab, as well as pagination caching
 * based on how far the user has scrolled on a particular tab.
 * TODO: Fetch new category names on each run with the category "variable" parameters (NOT the "key" parameters).
 * https://odin.tradoc.army.mil/dotcms/api/subnav/weg
 */
enum class EquipmentType(val apiName: String) { LAND("land-f5e1db"), AIR("air-e61af2"), SEA("sea-35e296") }