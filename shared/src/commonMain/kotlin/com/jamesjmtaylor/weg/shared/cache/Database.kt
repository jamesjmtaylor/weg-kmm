package com.jamesjmtaylor.weg.shared.cache

import com.jamesjmtaylor.weg.models.DateOfIntroduction
import com.jamesjmtaylor.weg.models.Image
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.models.SearchResultDetails

/**
 * SQLDelight database for caching REST API query results.
 * "internal" accessibility modifier means the Database is only accessible from within the
 * multiplatform module.
 *
 * NOTE: Do **not** try to manually alter either the implementation of the [database] property's
 * [AppDatabase] class or its nested [dbQuery] functions.
 * These are auto-generated based off of the queries in the **AppDatabase.sq** file.
 */
class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries


    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllResults()
            dbQuery.removeAllImages()
            dbQuery.removeAllEquipmentDetails()
            dbQuery.removeAllSearchResultProperties()
        }
    }
    //TODO: Implement actual query & joins
    internal fun fetchDetails(id: Long): SearchResult {
        return SearchResult(id = id)
    }

    /**
     * Rehydrates a List of [SearchResult] from the [AppDatabase]. Note that a LEFT JOIN is used to
     * return a row for each [Image] that has a matching [SearchResult] equipment id.  This means
     * that if a [SearchResult] has 3 images then there will be 3 [SearchResult]s, each with a
     * different image. This function groups duplicate [SearchResult.id]s so that it can flatten
     * the rows into a single [SearchResult.images] list row.
     */
    internal fun fetchResults(page: Int? = null): List<SearchResult> {
        val ungroupedResults = if (page != null) {
            dbQuery.selectResultByPage(page.toLong(), ::mapSearchResultSelecting).executeAsList()
        } else {
            dbQuery.selectAllSearchResults(::mapSearchResultSelecting).executeAsList()
        }
        val groupedMap = ungroupedResults.groupBy { it.id }
        val groupedList = mutableListOf<SearchResult>()
        for (group in groupedMap) {
            val result = group.value.first()
            val groupedImages = group.value.flatMap { it.images ?: emptyList() }
            groupedList.add(SearchResult(result.title, result.id, result.categories, groupedImages ))
        }
        return groupedList
    }

    internal fun insertSearchResults(results: List<SearchResult>, page: Int) {
        dbQuery.transaction {
            results.forEach { result ->
                //Checks if the result already has at least one image, and if not, inserts the fetched images
                val image = dbQuery.selectImagesByEquipmentId(result.id).executeAsList()
                if (image.isEmpty()) insertImages(result)
                insertResult(result, page)
            }
        }
    }

    private fun insertImages(result: SearchResult) {
        result.images?.forEach { image ->
            dbQuery.insertImage(
                name = image.name,
                equipment_id = result.id,
                url = image.url
            )
        }
    }

    private fun insertResult(result: SearchResult, page: Int) {
        dbQuery.insertResult(
            title = result.title,
            equipment_id = result.id,
            categories = result.categories.joinToString { it },
            page = page.toLong()
        )
    }

    /**
     * Converts the raw SQLite data values from the row into a [SearchResult] DTO. Unused parameters
     * are necessary because the column mapping is ordinally, not titularly, based.
     */
    @Suppress("UNUSED_PARAMETER")
    private fun mapSearchResultSelecting(
        equipmentId: Long,
        title: String?,
        categories: String?,
        page: Long?,
        imageEquipmentId: Long?,
        imageName: String?,
        imageUrl: String?
    ) : SearchResult {
        return SearchResult(
            title = title,
            id = equipmentId,
            categories = categories?.split(",") ?: emptyList(),
            images = listOf(Image(imageName,imageUrl)),
            page = page
        )
    }
    //TODO: Persist the result to the database using the various tables in the [Database] class.
    /**
     * Rehydrates a [SearchResultDetails].
     */
    fun getSearchResultDetailsById(id: Long): SearchResultDetails? {
        val ungroupedResults = dbQuery.selectEquipmentDetailsByEquipmentId(id).executeAsList()
        return null
    }

    private fun mapDetailsSelecting(
        equipmentId: Long,
        tiers: String?,
        notes: String?,
        dateOfIntroduction: Long?,
        countryOfOrigin: String?,
        proliferation: String?,
        version: Long,
        a: String?,
        b: String?
    ) : SearchResultDetails {
        return SearchResultDetails(
            tiers = emptyList(), //TODO: actually split tiers string into Boolean list.
            notes = notes,
            dateOfIntroduction = DateOfIntroduction(
                value = dateOfIntroduction,
                description = if (dateOfIntroduction == 0L) "INA" else dateOfIntroduction.toString()),
            countryOfOrigin = countryOfOrigin,
            proliferation = proliferation,
            version = version
        )
    }

    fun insertSearchResultDetails(id: Long, details: SearchResultDetails) {
        details.variants.forEach { dbQuery.insertSearchResultVariants(id, it.name, it.notes)  }
        dbQuery.insertSearchResultDetails(
            equipment_id = id,
            tiers = details.tiers.toString(),
            notes = details.notes,
            date_of_introduction = details.dateOfIntroduction?.description,
            country_of_origin = details.countryOfOrigin,
            proliferation = details.proliferation
        )
    }
}