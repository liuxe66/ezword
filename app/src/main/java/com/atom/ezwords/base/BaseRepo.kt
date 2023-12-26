package com.atom.ezwords.base

import com.atom.ezwords.data.bean.ApiResponse
import com.atom.ezwords.net.ApiException
import com.atom.ezwords.net.ApiResult
import com.atom.ezwords.net.NetExceptionHandle
import com.atom.ezwords.net.NetWorkHelper
import com.atom.ezwords.utils.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 *  author : liuxe
 *  date : 2023/12/25 14:09
 *  description :
 */
abstract class BaseRepo {

    /**
     * 网络请求
     * @param request SuspendFunction0<RemoteResponse<T>>
     * @return Flow<RemoteResult<RemoteResponse<T>>>
     */
    suspend fun <T> request(request: suspend () -> ApiResponse<T>) = flow {
        try {
            val data: ApiResponse<T> = request()

            if (data.code == 200) {
                emit(ApiResult.Success(data))
            } else {
                emit(ApiResult.Failure(ApiException(data.code, data.reason)))
            }


        } catch (e: Exception) {
            e.printStackTrace()
            val response = NetExceptionHandle.handleException(e)
            emit(ApiResult.Failure(response))
            ("Exception：${response.code} ${response.msg}").logE()
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 初始化默认的apiService
     */
    inline fun <reified Api> createApi(): Api {
        return NetWorkHelper.provideRetrofit().create(Api::class.java)
    }


}