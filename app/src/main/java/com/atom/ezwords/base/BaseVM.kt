package com.atom.ezwords.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atom.ezwords.data.bean.ApiResponse
import com.atom.ezwords.net.ApiException
import com.atom.ezwords.net.ApiResult
import com.atom.ezwords.net.doFailure
import com.atom.ezwords.net.doSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 *  author : liuxe
 *  date : 2023/12/25 14:54
 *  description :
 */
abstract class BaseVM : ViewModel() {
    //消息提示
    val mException: MutableLiveData<ApiException> = MutableLiveData()

    /**
     * 普通网络请求
     * @receiver Flow<RemoteResult<RemoteResponse<T>>>
     * @param success SuspendFunction1<T, Unit>
     * @param error SuspendFunction1<ApiException?, Unit>
     */
    suspend fun <T> Flow<ApiResult<ApiResponse<T>>>.load(
        success: suspend (T) -> Unit, error: suspend (ApiException?) -> Unit = {}
    ) {
        collectLatest { res ->
            res.doSuccess {
                success(it.data)
            }
            res.doFailure {
                error(it)
                mException.value = it
            }
        }
    }

}