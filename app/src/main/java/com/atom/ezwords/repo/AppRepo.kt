package com.atom.ezwords.repo

import com.atom.ezwords.api.AppApi
import com.atom.ezwords.base.BaseRepo

/**
 *  author : liuxe
 *  date : 2023/12/25 14:09
 *  description :
 */
class AppRepo : BaseRepo() {
    var api = createApi<AppApi>()

    suspend fun getExamWord(start: Int, end: Int) = request {
        api.getExamWord(start, end)
    }

}