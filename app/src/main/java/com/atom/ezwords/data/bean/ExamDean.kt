package com.atom.ezwords.data.bean

data class ExamDean(
    val result: Result = Result()
) {
    data class Result(
        val wordExam: WordExam = WordExam()
    ) {
        data class WordExam(
            val correctRank: String = "",
            val options: List<String> = listOf(),
            val correctMean: String = "",
            val correctIndex: Int = 0
        )
    }
}