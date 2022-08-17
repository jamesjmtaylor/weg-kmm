package com.jamesjmtaylor.weg.shared.cache

import com.jamesjmtaylor.weg.models.Image
import com.jamesjmtaylor.weg.models.SearchResult

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

    internal fun getAllResults(): List<SearchResult> {
        return dbQuery.selectAllSearchResults(::mapSearchResultSelecting).executeAsList()
    }

    internal fun insertSearchResults(results: List<SearchResult>) {
        dbQuery.transaction {
            results.forEach { result ->
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
    // or if `.executeAsList()` handles it
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