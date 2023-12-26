package com.atom.ezwords.ui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.atom.ezwords.base.BaseVM
import com.atom.ezwords.data.entity.WordExamEntity
import com.atom.ezwords.data.mapper.WordExamIO
import com.atom.ezwords.repo.AppRepo
import com.atom.ezwords.utils.logE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 *  author : liuxe
 *  date : 2023/12/25 14:58
 *  description :
 */
class ExamVM : BaseVM() {
    private val repo = AppRepo()
    val wordExamFlow = MutableStateFlow(WordExamEntity())
    var finished by mutableStateOf(false)

    var wordExam = WordExamEntity()
    var qNum by mutableIntStateOf(1)
    var rank by mutableIntStateOf(0)
    var allQ = 20

    var start = 500
    var end = 1000

    fun initState() {
        qNum = 1
        finished = false
        rank = 0
        start = 500
        end = 1000
    }

    /**
     * 获得题
     * @return Job
     */
    fun getExam(start: Int, end: Int) = viewModelScope.launch {
        repo.getExamWord(start, end).load({
            wordExam = WordExamIO.trans(it.result.wordExam)
            wordExamFlow.emit(wordExam)
        })
    }

    /**
     * 获得题
     * @return Job
     */
    fun getNextExam(start: Int, end: Int) = viewModelScope.launch {
        repo.getExamWord(start, end).load({
            wordExam = WordExamIO.trans(it.result.wordExam)
            delay(800)
            wordExamFlow.emit(wordExam)
        })
    }

    /**
     * 点击不认识按钮
     * @return Job
     */
    fun onClickDont()= viewModelScope.launch{
        wordExam.options[wordExam.correctIndex].state = 1

        try {
            wordExamFlow.emit(wordExam)
        } catch (e: Exception) {
            e.message?.logE()
        }

        if (qNum == allQ) {
            //查看结果
            finished = true
        } else {
            //判断结果
            qNum++
            getNextExam(start, end)
        }


    }

    /**
     * 做题
     * @param index Int
     */
    fun onClick(index: Int) = viewModelScope.launch {

        if (wordExam.correctIndex == index) {
            wordExam.options[index].state = 1
        } else {
            wordExam.options[index].state = 2
            wordExam.options[wordExam.correctIndex].state = 1
        }

        try {
            wordExamFlow.emit(wordExam)
        } catch (e: Exception) {
            e.message?.logE()
        }


        if (qNum == allQ) {
            //查看结果
            finished = true
        } else {
            //判断结果
            qNum++
            if (wordExam.correctIndex == index) {
                rank = wordExam.correctRank.toInt()
                start = rank + 500
                end = start + 500

                getNextExam(start, end)
            } else {

                if (rank < 700) {
                    rank =
                        (0..rank).random()
                    start = rank + 500
                    end = start + 500

                    getNextExam(start, end)
                } else {
                    rank =
                        ((rank - 600)..(rank - 400)).random()
                    start = rank + 500
                    end = start + 500
                    getNextExam(start, end)
                }

            }

        }

    }
}