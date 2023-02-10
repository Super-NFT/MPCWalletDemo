package com.go23wallet.mpcwalletdemo.utils

import com.coins.app.util.OkhttpUtil
import com.go23wallet.mpcwalletdemo.data.CountryCodeResponse
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody

object RequestUtils {

    fun getCountryCode(callback: (CountryCodeResponse) -> Unit = {}) {
        Observable.create { emitter: ObservableEmitter<CountryCodeResponse> ->
            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(), ""
            )
            val request: Request = Request.Builder()
                .url(Constant.HOST + "/country")
                .post(body)
                .build()
            try {
                val response =
                    OkhttpUtil.getInstance().okHttpClient.newCall(request).execute()
                val result = if (response.body != null) response.body?.string() else ""
                val countryCodeResponse =
                    Gson().fromJson(result, CountryCodeResponse::class.java)
                emitter.onNext(countryCodeResponse)
            } catch (e: Exception) {
                emitter.onError(e)
            } finally {
                emitter.onComplete()
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CountryCodeResponse> {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    CustomToast.showShort("Request failed, please retry")
                }
                override fun onComplete() {}
                override fun onNext(t: CountryCodeResponse) {
                    callback.invoke(t)
                }
            })
    }
}