package com.atom.ezwords.data.entity

/**
 *  author : liuxe
 *  date : 2023/12/25 15:12
 *  description :
 */
data class WordExamEntity(
    val correctRank: String = "",
    val options: MutableList<Option> = mutableListOf(),
    val correctMean: String = "",
    val correctIndex: Int = 0
)

data class Option(
    val txt:String = "",
    var state:Int = 0
)