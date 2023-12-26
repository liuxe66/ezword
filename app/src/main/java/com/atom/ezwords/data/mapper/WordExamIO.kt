package com.atom.ezwords.data.mapper

import com.atom.ezwords.data.bean.ExamDean
import com.atom.ezwords.data.entity.Option
import com.atom.ezwords.data.entity.WordExamEntity

/**
 *  author : liuxe
 *  date : 2023/12/25 15:14
 *  description :
 */
object WordExamIO{
    fun trans(i: ExamDean.Result.WordExam): WordExamEntity {
        var options: MutableList<Option> = mutableListOf()
        i.options.forEach {
            options.add(Option(it, 0))
        }

        return WordExamEntity(
            correctIndex = i.correctIndex,
            correctMean = i.correctMean,
            correctRank = i.correctRank,
            options = options
        )
    }


}