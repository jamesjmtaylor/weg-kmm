package com.jamesjmtaylor.weg.shared.cache

import com.jamesjmtaylor.weg.models.Image
import com.jamesjmtaylor.weg.models.Contentlet

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
            dbQuery.removeAllContentletProperties()
        }
    }

    /**
     * Rehydrates a List of [Contentlet] from the [AppDatabase]. Note that a LEFT JOIN is used to
     * return a row for each [Image] that has a matching [Contentlet] equipment id.  This means
     * that if a [Contentlet] has 3 images then there will be 3 [Contentlet]s, each with a
     * different image. This function groups duplicate [Contentlet.id]s so that it can flatten
     * the rows into a single [Contentlet.images] list row.
     */
    internal fun fetchResults(page: Int? = null): List<Contentlet> {
        //TODO: Return grouped results again
        val ungroupedResults = if (page != null) {
            dbQuery.selectResultByPage(page.toLong(), ::mapContentletSelecting).executeAsList()
        } else {
            dbQuery.selectAllSearchResults(::mapContentletSelecting).executeAsList()
        }

//        val groupedMap = ungroupedResults.groupBy { it.identifier }
//        val groupedList = mutableListOf<Contentlet>()
//        for (group in groupedMap) {
//            val result = group.value.first()
//            val groupedImages = group.value.flatMap { it.images ?: emptyList() }
//            groupedList.add(Contentlet(result.title, result.identifier, result.domain, groupedImages ))
//        }
        return ungroupedResults
    }

    internal fun insertSearchResults(results: List<Contentlet>, page: Int) {
        dbQuery.transaction {
            results.forEach { result ->
                //Checks if the result already has at least one image, and if not, inserts the fetched images
                val image = dbQuery.selectImagesByEquipmentId(result.identifier).executeAsList()
                if (image.isEmpty()) insertImages(result)
                insertResult(result, page)
            }
        }
    }

    private fun insertImages(result: Contentlet) {
        result.images?.forEach { image ->
            dbQuery.insertImage(
                name = image.name,
                equipment_id = result.identifier,
                url = image.url
            )
        }
    }

    private fun insertResult(result: Contentlet, page: Int) {
        dbQuery.insertResult(
            title = result.title,
            equipment_id = result.identifier,
            categories = result.domain.keys.joinToString { it },
            page = page.toLong()
        )
    }

    /**
     * Converts the raw SQLite data values from the row into a [Contentlet] DTO. Unused parameters
     * are necessary because the column mapping is ordinally, not titularly, based.
     */
    @Suppress("UNUSED_PARAMETER")
    private fun mapContentletSelecting(
        equipmentId: String,
        title: String?,
        categories: String?,
        page: Long?,
        imageEquipmentId: Long?,
        imageName: String?,
        imageUrl: String?
    ) : Contentlet {
        return Contentlet(
            notes = "",
            title = "",
            identifier = equipmentId,
            images = emptyList(),
            sections = "",
            domain =  emptyMap(),
            proliferation =  emptyMap(),
            origin = emptyMap(),
            dateOfIntroduction = null
        )
    }
}