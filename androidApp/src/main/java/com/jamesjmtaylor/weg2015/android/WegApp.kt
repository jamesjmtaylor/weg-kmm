package com.jamesjmtaylor.weg2015.android

import android.app.Activity
import android.app.Application
import com.jamesjmtaylor.weg2015.EquipmentSDK

class WegApp : Application() {
    val sdk = EquipmentSDK()
}

fun Activity.getWegApp(): WegApp {
    return this.application as WegApp
}