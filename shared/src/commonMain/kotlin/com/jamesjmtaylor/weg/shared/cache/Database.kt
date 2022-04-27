package com.jamesjmtaylor.weg.shared.cache

import com.jamesjmtaylor.weg.Models.Image
import com.jamesjmtaylor.weg.Models.SearchResult

//"internal" accessibility modifier means Database is- only accessible from within the multiplatform
// module.
internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    // TODO: Currently throws "Undefined symbols for architecture x86_64:
    //  "_sqlite3_column_type", referenced from:
    //      _co_touchlab_sqliter_sqlite3_sqlite3_column_type_wrapper108 in shared(result.o)
    //     (maybe you meant: _co_touchlab_sqliter_sqlite3_sqlite3_column_type_wrapper108, knifunptr_co_touchlab_sqliter_sqlite3112_sqlite3_column_type )"
    //   on iOS.
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllResults()
        }
    }

    internal fun getAllResults(): List<SearchResult> {
        //TODO: See https://stackoverflow.com/questions/68632268/sqldelight-relationships for one-to-many relationships.
        // Each table row that invokes this function is a combination of searchResult & image, so there will be 4 rows that will need to be joined on searchResult.id
        return dbQuery.selectAllSearchResultWithImages<SearchResult>(::mapSearchResultSelecting).executeAsList()
    }


    //TODO: Resume from https://play.kotlinlang.org/hands-on/Networking%20and%20Data%20Storage%20with%20Kotlin%20Multiplatfrom%20Mobile/05_Configuring_SQLDelight_an_implementing_cache
    // in order to implement INSERT

    private fun mapSearchResultSelecting(
        id: Long,
        title: String?,
        categories: String?,
        searchResultId: Long,
        name: String?,
        url: String?
    ) : SearchResult {
        print(searchResultId)
        return SearchResult(
            title = title,
            id = id,
            categories = categories?.split(",") ?: emptyList(),
            images = listOf(Image(name,url))
        )
    }

    private fun mapImageSelecting(
        name: String,
        url: String
    ) : Image {
        return Image(
            name = name,
            url = url
        )
    }
}