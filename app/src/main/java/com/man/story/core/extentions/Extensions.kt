package com.man.story.core.extentions

import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.man.story.R
import com.man.story.core.base.BaseResponse
import com.man.story.core.base.Resource
import com.man.story.core.utils.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

/**
 *
 * Created by Lukmanul Hakim on  27/09/22
 * devs.lukman@gmail.com
 */

fun <T> LifecycleOwner.observeData(observable: LiveData<T>, observer: (T) -> Unit) {
    observable.observe(this) {
        observer(it)
    }
}

fun Fragment.navigateTo(destination: Int) {
    try {
        NavHostFragment.findNavController(this).navigate(destination)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun Fragment.onBack() {
    NavHostFragment.findNavController(this).popBackStack()
}

val EditText.getString: String get() = text.toString()


fun <T, U> Response<T>.asFlowStateEvent(mapper: (T) -> U): Flow<Resource<U>> {
    return flow {
        val emitData = try {
            val body = body()
            if (isSuccessful && body != null) {
                val dataMapper = mapper.invoke(body)
                Resource.OnSuccess(dataMapper)
            } else {
                Resource.OnError(UIText.DynamicText(message()))
            }
        } catch (e: Throwable) {
            val errorMessage: UIText = when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<BaseResponse>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<BaseResponse>() {}.type
                        )
                        UIText.DynamicText(response.message)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.unknown)
                    }
                }
                else -> UIText.StringResource(R.string.unknown)
            }
            Resource.OnError(errorMessage)
        }
        emit(emitData)
    }
}

inline fun <T, U> Response<T>.asFlowStateEventWithAction(
    crossinline mapper: (T) -> U,
    crossinline action: suspend (T) -> Unit,
): Flow<Resource<U>> = flow {
    try {
        val body = body()
        if (isSuccessful && body != null) {
            val dataMapper = mapper.invoke(body)
            action.invoke(body)
            emit(Resource.OnSuccess(dataMapper))
        } else {
            emit(Resource.OnError(UIText.DynamicText(message())))
        }
    } catch (e: Throwable) {
        val errorMessage: UIText = when (e) {
            is HttpException -> {
                try {
                    val response = Gson().fromJson<BaseResponse>(
                        e.response()?.errorBody()?.charStream(),
                        object : TypeToken<BaseResponse>() {}.type
                    )
                    UIText.DynamicText(response.message)
                } catch (e: Exception) {
                    UIText.StringResource(R.string.unknown)
                }
            }
            else -> UIText.StringResource(R.string.unknown)
        }
        emit(Resource.OnError(errorMessage))
    }
}

inline fun Boolean.ifTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}

inline fun Boolean.whatIf(isTrue: () -> Unit, isFalse: () -> Unit) {
    if (this) {
        isTrue.invoke()
    } else isFalse.invoke()
}