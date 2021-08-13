package com.example.tekotest2.asyns

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class HandleAsyncTask<T> {
    companion object {
        public const val RUN_ON_IO: Int = 1
        public const val RUN_ON_UI: Int = 2
        public const val RUN_ON_WORKER: Int = 3
    }

     var mContext: Context? = null
        set(value) {
            field = value
        }
     var mWork: IWorkCallback<T>? = null
        set(value) {
            field = value
        }
     var mType = RUN_ON_WORKER
    set(value) {
        field = value
    }
    private val job = kotlinx.coroutines.Job()
    private val mScope = CoroutineScope(when (mType) {
        RUN_ON_IO -> Dispatchers.IO + job
        RUN_ON_WORKER -> Dispatchers.Default + job
        RUN_ON_UI -> Dispatchers.Main + job
        else -> Dispatchers.Main + job
    })

    private fun handleAction(): Flow<T?> = flow {
        emit(mWork?.run())
    }

    @InternalCoroutinesApi
    fun run() {
        if (mWork === null || mContext === null)
            return
        mScope.launch {
            handleAction().collect { value ->
                run {
                    if (value != null) {
                        withContext(Dispatchers.Main) {
                            mWork?.onFinish(value)
                        }
                    }
                }
            }
        }
    }
}