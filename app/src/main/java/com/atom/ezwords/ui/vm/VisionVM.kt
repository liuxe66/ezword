package com.atom.ezwords.ui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.atom.ezwords.base.BaseVM
import com.atom.ezwords.data.entity.VisionEntity
import com.atom.ezwords.repo.AppRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 *  author : liuxe
 *  date : 2023/12/25 14:58
 *  description :
 */
class VisionVM : BaseVM() {
    private val repo = AppRepo()

    var visionState = MutableStateFlow(VisionEntity())
    var visionAll = VisionEntity()
    var visionEntity = VisionEntity()

    var finished by mutableStateOf(false)
    var isStart by mutableStateOf(false)
    var totalScore = 0
    var totalResult= ""
    var totalTitle = ""
    var totalLevel = 0

    fun initState(){
         totalScore = 0
        totalResult= ""
        totalTitle = ""
        totalLevel = 0
        finished  = false
        isStart  = false
        visionState = MutableStateFlow(VisionEntity())
        visionEntity = VisionEntity()
    }

    /**
     * 获得问题
     * @return Job
     */
    fun getVision() = viewModelScope.launch {
        visionAll = repo.getVision()
    }

    fun start() = viewModelScope.launch {
        visionAll.vision[0].state = 1
        visionEntity.vision.add(visionAll.vision[0])
        visionState.emit(visionEntity)
    }

    fun getNextVision(index: Int) = viewModelScope.launch {
        visionEntity.vision.removeAt(index = index)
        visionEntity.vision.add(visionAll.vision[index])
        if (index != 9){
            visionEntity.vision.add(visionAll.vision[index+1])
        }

        visionState.emit(visionEntity)
    }

    /**
     * 选择答案
     * @param option VisionOption
     */
    fun onSelect(index: Int, optionIndex: Int) = viewModelScope.launch {
        visionAll.vision[index].state = 2
        visionAll.vision[index].options[optionIndex].state = 1
        visionAll.vision[index].answer = optionIndex
        if (index == 9) {
            //已完成
            calcScore()

        } else {
            //显示下一题
            visionAll.vision[index + 1].state = 1

        }
        getNextVision(index)
    }

    /**
     * 计算得分
     */
    private fun calcScore() {
        visionAll.vision.forEach {
            if (it.state == 2) {
                it.options.forEachIndexed { index, visionOption ->
                    if (visionOption.state == 1){
                        when(index){
                            0 ->totalScore+=1
                            1 ->totalScore+=2
                            2 ->totalScore+=3
                        }
                    }
                }
            }
        }

        totalResult = when(totalScore){
            in 10..16 -> "你的眼睛状态不错，要继续保持良好的用眼习惯哦！"
            in 17 .. 24-> "即便你平时已经有护眼意识和动作了，但是繁忙的工作和生活的压力山大，还是让你的眼睛偶尔出现干涩、有血丝等问题。所以要记得：每隔1-2小时闭目养神一下，或远眺绿色植物，养成睡眠充足的习惯，让眼睛的疲劳能得以缓解。"
            in 25 .. 33-> "你的眼睛常被疲劳缠绕，挥之不去、不胜烦扰。要知道，眼睛疲劳不仅会让人看起来没精神、感觉不舒服、还很容易造成眼部疾病！所以舒缓眼部肌肉的紧张感、改善眼睛疲劳症状，是你目前的首要任务！学会闭目养神、暂别电子产品、走到户外远眺，都是很能“养眼”的哦！"
            else -> ""
        }

        totalTitle = when(totalScore){
            in 10..16 -> "正常"
            in 17 .. 24-> "轻微疲劳"
            in 25 .. 33-> "严重疲劳"
            else -> ""
        }

        totalLevel =when(totalScore){
            in 10..16 -> 1
            in 17 .. 24-> 2
            in 25 .. 33-> 3
            else -> 0
        }
        finished = true
    }

}