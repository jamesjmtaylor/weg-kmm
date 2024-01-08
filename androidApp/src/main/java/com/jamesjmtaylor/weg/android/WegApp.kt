package com.jamesjmtaylor.weg.android

import android.app.Activity
import android.app.Application
import com.jamesjmtaylor.weg.EquipmentSDK

class WegApp : Application() {
    val sdk = EquipmentSDK()
}

fun Activity.getWegApp(): WegApp {
    return this.application as WegApp
}