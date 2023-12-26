package com.atom.ezwords.api

import com.atom.ezwords.data.bean.ApiResponse
import com.atom.ezwords.data.bean.ExamDean
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  author : liuxe
 *  date : 2023/12/25 14:03
 *  description :
 */
interface AppApi {
    /**
     * https://reciteword.youdao.com/appapi/wordExam/getExamWord?start=2138&end=2638
     * @return QuestionBean
     */
    @GET("appapi/wordExam/getExamWord")
    suspend fun getExamWord(
        @Query("start") start: Int, @Query("end") end: Int
    ): ApiResponse<ExamDean>
}