package com.jamesjmtaylor.weg.shared.cache

import com.squareup.sqldelight.db.SqlDriver

@Suppress("NO_ACTUAL_FOR_EXPECT") // See https://youtrack.jetbrains.com/issue/KT-26333
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}