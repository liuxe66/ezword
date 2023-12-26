package com.atom.ezwords.data.bean

/**
 *  author : liuxe
 *  date : 2023/12/25 14:40
 *  description :
 */
data class ApiResponse<T>(
    val code: Int,
    val data: T,
    val reason: String
)