package com.jamesjmtaylor.weg.android

import android.app.Activity
import android.app.Application
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory

class WegApp : Application() {
    val sdk = EquipmentSDK(DatabaseDriverFactory(this))
}

fun Activity.wegApp(): WegApp {
    return this.application as WegApp
}