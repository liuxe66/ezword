package com.atom.ezwords.data.entity

/**
 *  author : liuxe
 *  date : 2023/12/27 15:22
 *  description :
 */

class VisionEntity(
    val vision: MutableList<Vision> = mutableListOf(),
){
    override fun equals(other: Any?): Boolean {
        return false
    }

}
data class Vision(
    val question: String = "",
    val options: MutableList<VisionOption> = mutableListOf(),
    var state: Int = 0,//0 未显示 1已显示 2已完成
    var answer:Int = 0

)

data class VisionOption(
    val txt: String = "",
    var state: Int = 0 //0 未选中 1已选中
)