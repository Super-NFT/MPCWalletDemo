package com.go23wallet.mpcwalletdemo.utils

import com.coins.app.Go23WalletManage
import com.coins.app.bean.user.UserResponse
import com.coins.app.util.OkhttpUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody

object RequestUtils {

    fun getCountryCode() {
        Observable.create { emitter: ObservableEmitter<UserResponse?> ->
            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(), ""
            )
            val request: Request = Request.Builder()
                .url(KeygenUtils.HOST + "/country")
                .post(body)
                .build()
            try {
                val response =
                    OkhttpUtil.getInstance().okHttpClient.newCall(request).execute()
                val result = if (response.body != null) response.body?.string() else ""
                val userResponse =
                    Gson().fromJson(result, UserResponse::class.java)
                emitter.onNext(userResponse)
            } catch (e: Exception) {
                emitter.onError(e)
            } finally {
                emitter.onComplete()
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<UserResponse?> {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
                override fun onNext(t: UserResponse) {
                }
            })
    }
}