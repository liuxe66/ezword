package com.atom.ezwords.app

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

/**
 *  author : liuxe
 *  date : 2023/12/27 10:48
 *  description :
 */
class EzWordsApp:Application() {
    companion object {
        var context: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}