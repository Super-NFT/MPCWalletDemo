package com.go23wallet.mpcwalletdemo.utils;

import com.blankj.utilcode.util.SPUtils;
import com.coins.app.BaseCallBack;
import com.coins.app.Go23WalletManage;
import com.coins.app.bean.user.MerchantResponse;
import com.coins.app.bean.user.UserResponse;
import com.coins.app.util.OkhttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.go23wallet.mpcwalletdemo.utils.Constant.HOST;

public class KeygenUtils {

    private static KeygenUtils utils;

    private KeygenUtils() {
    }

    public static KeygenUtils getInstance() {
        if (utils == null) {
            utils = new KeygenUtils();
        }
        return utils;
    }

    public void requestMerchantKey(String address, BaseCallBack<MerchantResponse> callBack) {
        Observable.create((ObservableOnSubscribe<MerchantResponse>) emitter -> {
                    JsonObject object = new JsonObject();
                    object.addProperty("wallet_address", address);
                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json"), object.toString());
                    Request request = new Request.Builder()
                            .url(HOST + "/merchant/get_keygen")
                            .addHeader("Authorization", "Bearer " + Go23WalletManage.getInstance().getGameCenterToken().getAccess_token())
                            .addHeader("Uuid", Go23WalletManage.getInstance().getUser().getUuid())
                            .post(body)
                            .build();
                    try {
                        Response response = OkhttpUtil.getInstance().getOkHttpClient().newCall(request).execute();
                        String result = response.body() != null ? response.body().string() : "";
                        MerchantResponse userResponse = new Gson().fromJson(result, MerchantResponse.class);
                        emitter.onNext(userResponse);
                    } catch (Exception e) {
                        emitter.onError(e);
                    } finally {
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<MerchantResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MerchantResponse o) {
                        callBack.success(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void uploadMerchantKey(String address, String key) {
        Observable.create((ObservableOnSubscribe<UserResponse>) emitter -> {
                    JsonObject object = new JsonObject();
                    object.addProperty("wallet_address", address);
                    object.addProperty("keygen", key);
                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json"), object.toString());
                    Request request = new Request.Builder()
                            .url(HOST+ "/merchant/create_keygen")
                            .addHeader("Authorization", "Bearer " + Go23WalletManage.getInstance().getGameCenterToken().getAccess_token())
                            .addHeader("Uuid", Go23WalletManage.getInstance().getUser().getUuid())
                            .post(body)
                            .build();
                    try {
                        Response response = OkhttpUtil.getInstance().getOkHttpClient().newCall(request).execute();
                        String result = response.body() != null ? response.body().string() : "";
                        UserResponse userResponse = new Gson().fromJson(result, UserResponse.class);
                        emitter.onNext(userResponse);
                    } catch (Exception e) {
                        emitter.onError(e);
                    } finally {
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserResponse o) {
                        SPUtils.getInstance().put(address, "", true);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void updateMerchantKey(String address, String key) {
        Observable.create((ObservableOnSubscribe<UserResponse>) emitter -> {
                    JsonObject object = new JsonObject();
                    object.addProperty("wallet_address", address);
                    object.addProperty("keygen", key);
                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json"), object.toString());
                    Request request = new Request.Builder()
                            .url(HOST + "/merchant/update_keygen")
                            .addHeader("Authorization", "Bearer " + Go23WalletManage.getInstance().getGameCenterToken().getAccess_token())
                            .addHeader("Uuid", Go23WalletManage.getInstance().getUser().getUuid())
                            .post(body)
                            .build();
                    try {
                        Response response = OkhttpUtil.getInstance().getOkHttpClient().newCall(request).execute();
                        String result = response.body() != null ? response.body().string() : "";
                        UserResponse userResponse = new Gson().fromJson(result, UserResponse.class);
                        emitter.onNext(userResponse);
                    } catch (Exception e) {
                        emitter.onError(e);
                    } finally {
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserResponse o) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
