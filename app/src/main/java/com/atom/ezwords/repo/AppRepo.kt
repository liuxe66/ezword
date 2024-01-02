package com.atom.ezwords.repo

import com.atom.ezwords.api.AppApi
import com.atom.ezwords.base.BaseRepo
import com.atom.ezwords.data.entity.Vision
import com.atom.ezwords.data.entity.VisionEntity
import com.atom.ezwords.data.entity.VisionOption

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

    fun getVision(): VisionEntity {
        var visions = mutableListOf<Vision>()
        visions.add(
            Vision(
                question = "哪一处是你看书的理想位置？", options = mutableListOf(
                    VisionOption(txt = "书桌"),
                    VisionOption(txt = "沙发"),
                    VisionOption(txt = "睡床")
                )
            )
        )
        visions.add(
            Vision(
                question = "喜欢在哪种环境下看电视？", options = mutableListOf(
                    VisionOption(txt = "小灯"),
                    VisionOption(txt = "明亮"),
                    VisionOption(txt = "关灯")
                )
            )
        )
        visions.add(
            Vision(
                question = "看电视的频率是？", options = mutableListOf(
                    VisionOption(txt = "偶尔看，每次不超过2小时"),
                    VisionOption(txt = "经常看，每次都超过2小时"),
                    VisionOption(txt = "天天看，每天都超过2小时")
                )
            )
        )
        visions.add(
            Vision(
                question = "看屏幕时，你的视线角度是？", options = mutableListOf(
                    VisionOption(txt = "稍稍俯视"),
                    VisionOption(txt = "平视"),
                    VisionOption(txt = "仰视")
                )
            )
        )
        visions.add(
            Vision(
                question = "每天和电脑打交道的时间？", options = mutableListOf(
                    VisionOption(txt = "小于2小时"),
                    VisionOption(txt = "2-6小时"),
                    VisionOption(txt = "6小时以上")
                )
            )
        )
        visions.add(
            Vision(
                question = "使用电脑期间，有休息的习惯吗？", options = mutableListOf(
                    VisionOption(txt = "每小时固定休息一次"),
                    VisionOption(txt = "看累了就休息"),
                    VisionOption(txt = "不休息")
                )
            )
        )
        visions.add(
            Vision(
                question = "爱玩手机游戏或者手机聊天吗？", options = mutableListOf(
                    VisionOption(txt = "从来不玩"),
                    VisionOption(txt = "偶尔玩一下"),
                    VisionOption(txt = "每天玩")
                )
            )
        )
        visions.add(
            Vision(
                question = "每天的睡眠时间有多长？", options = mutableListOf(
                    VisionOption(txt = "大于8小时"),
                    VisionOption(txt = "5到8小时"),
                    VisionOption(txt = "5小时以下")
                )
            )
        )
        visions.add(
            Vision(
                question = "入眠之前会看手机吗？", options = mutableListOf(
                    VisionOption(txt = "不看手机，只看书"),
                    VisionOption(txt = "偶尔看手机"),
                    VisionOption(txt = "每天睡前必看手机")
                )
            )
        )
        visions.add(
            Vision(
                question = "用眼时间长了，会出现哪些症状？", options = mutableListOf(
                    VisionOption(txt = "没有什么感觉"),
                    VisionOption(txt = "看东西短暂模糊，眼睛酸涩"),
                    VisionOption(txt = "注意力不集中，头晕肩酸，严重时头痛恶心")
                )
            )
        )

        return VisionEntity(vision = visions)
    }

}