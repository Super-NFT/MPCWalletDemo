package com.go23wallet.mpcwalletdemo.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import androidx.preference.PreferenceManager;

import com.coins.app.Go23WalletManage;
import com.coins.app.bean.Authorize;
import com.coins.app.bean.AuthorizeResp;
import com.coins.app.bean.OauthClientInfo;
import com.coins.app.bean.OauthClientInfoResp;
import com.coins.app.ui.CreateKeyActivity;
import com.coins.app.util.OkhttpUtil;
import com.go23wallet.mpcwalletdemo.wallet.CoinsOauthDialog;
import com.google.gson.Gson;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizeManage {
    private AuthorizeManage() {
    }

    private static volatile AuthorizeManage instance = null;

    public static AuthorizeManage getInstance() {
        if (instance == null) {
            synchronized (AuthorizeManage.class) {
                if (instance == null) {
                    instance = new AuthorizeManage();
                }
            }
        }
        return instance;
    }

    private void getClientInfo(Activity activity) {
        Observable.create((ObservableOnSubscribe<OauthClientInfo>) emitter -> {
            Request request = new Request.Builder().url("https://serv4.be.test.dbytothemoon.com/api/oauth2/client?client_id=1").addHeader("Authorization", "Bearer " + Go23WalletManage.getInstance().getGameCenterToken().getAccess_token()).get().build();
            try {
                Response response = OkhttpUtil.getInstance().getOkHttpClient().newCall(request).execute();
                //get result
                String result = response.body() != null ? response.body().string() : "";
                OauthClientInfoResp baseResponse = new Gson().fromJson(result, OauthClientInfoResp.class);
                if (baseResponse != null && baseResponse.getData() != null && 0 == baseResponse.getCode()) {
                    emitter.onNext(baseResponse.getData());
                }

            } catch (Exception e) {
                emitter.onError(e);
            } finally {
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<OauthClientInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(OauthClientInfo o) {
                new CoinsOauthDialog(activity).setView(Objects.requireNonNull(o.getClientName()), new Function1<Boolean, Unit>() {
                    @Override
                    public Unit invoke(Boolean aBoolean) {
                        if (aBoolean) {
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean(o.getClientId(), false).apply();
                            authorize(activity);
                        } else {
                            finishA(activity);
                        }
                        return null;
                    }
                }).show();

            }

            @Override
            public void onError(Throwable e) {
                finishA(activity);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void finishA(Activity activity) {
        if (activity instanceof CreateKeyActivity) {
            new Handler().postDelayed(activity::finish
                    , 3000);
        }
    }

    private void authorize(Activity activity) {
        Observable.create((ObservableOnSubscribe<Authorize>) emitter -> {
            Request request = new Request.Builder().url("https://serv4.be.test.dbytothemoon.com/api/oauth2/authorize_direct?scope=read" +
                    "&&response_type=code" + "&&wallet_client_id=1" + "&&unique_id=4d50bad18537456998a9270ea7eac077" +
                    "&&wallet_client_secret=40ad7c25" + "&&client_id=1").addHeader("Authorization", "Bearer " + Go23WalletManage.getInstance().getGameCenterToken().getAccess_token()).get().build();
            try {
                Response response = OkhttpUtil.getInstance().getOkHttpClient().newCall(request).execute();
                //get result
                String result = response.body() != null ? response.body().string() : "";
                AuthorizeResp baseResponse = new Gson().fromJson(result, AuthorizeResp.class);
                if (baseResponse != null && baseResponse.getData() != null && 0 == baseResponse.getCode()) {
                    emitter.onNext(baseResponse.getData());
                } else {
                    finishA(activity);
                }

            } catch (Exception e) {
                emitter.onError(e);
            } finally {
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Authorize>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Authorize o) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("cs1://coins-oauth?code=" + o.getCode());
                intent.setData(uri);
                activity.startActivity(intent);
                finishA(activity);
            }

            @Override
            public void onError(Throwable e) {
                finishA(activity);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void startAuthorize(Activity activity) {
        getClientInfo(activity);
    }
}
