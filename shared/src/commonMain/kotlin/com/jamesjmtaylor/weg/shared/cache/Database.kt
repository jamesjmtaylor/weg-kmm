package com.jamesjmtaylor.weg.shared.cache

import com.jamesjmtaylor.weg.models.Image
import com.jamesjmtaylor.weg.models.SearchResult
import kotlinx.coroutines.Runnable

/**
 * SQLDelight database for caching REST API query results.
 * "internal" accessibility modifier means the Database is only accessible from within the
 * multiplatform module
 */
internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllResults()
            dbQuery.removeAllImages()
        }
    }

    /**
     * Rehydrates a List of [SearchResult] from the [AppDatabase]. Note that a LEFT JOIN is used to
     * return a row for each [Image] that has a matching [SearchResult] equipment id.  This means
     * that if a [SearchResult] has 3 images then there will be 3 [SearchResult]s, each with a
     * different image. This function groups duplicate [SearchResult.id]s so that it can flatten
     * the rows into a single [SearchResult.images] list row.
     */
    internal fun getAllResults(): List<SearchResult> {
        val ungroupedResults = dbQuery.selectAllSearchResults(::mapSearchResultSelecting).executeAsList()
        val groupedMap = ungroupedResults.groupBy { it.id }
        val groupedList = mutableListOf<SearchResult>()
        for (group in groupedMap) {
            val result = group.value.first()
            val groupedImages = group.value.flatMap { it.images }
            groupedList.add(SearchResult(result.title, result.id, result.categories, groupedImages ))
        }
        return groupedList
    }

    internal fun insertSearchResults(results: List<SearchResult>) {
        dbQuery.transaction {
            results.forEach { result ->
                //Checks if the result already has at least one image, and if not, inserts the fetched images
                val image = dbQuery.selectImagesByEquipmentId(result.id).executeAsOneOrNull()
                if (image == null) insertImage(result)
                insertResult(result)
            }
        }
    }

    private fun insertImage(result: SearchResult) {
        result.images.forEach { image ->
            dbQuery.insertImage(
                name = image.name,
                equipment_id = result.id,
                url = image.url
            )
        }
    }

    private fun insertResult(result: SearchResult) {
        dbQuery.insertResult(
            title = result.title,
            equipment_id = result.id,
            categories = result.categories.joinToString { it }
        )
    }
    //TODO: Determine whether this returns multiples of the same search result,
    // or if `.executeAsList()` handles it (answer: it's the former - need to fix)
    private fun mapSearchResultSelecting(
        equipmentId: Long,
        title: String?,
        categories: String?,
        imageEquipmentId: Long?,
        imageName: String?,
        imageUrl: String?
    ) : SearchResult {
        print(imageEquipmentId)
        return SearchResult(
            title = title,
            id = equipmentId,
            categories = categories?.split(",") ?: emptyList(),
            images = listOf(Image(imageName,imageUrl))
        )
    }
}