package com.example.phonewallet11.okhttpClientManager;

import com.example.phonewallet11.cookie.CookieJarImpl;
import com.example.phonewallet11.cookie.MemoryCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class clientManager {

    public final OkHttpClient mOkHttpClient;

    private clientManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cookieJar(new CookieJarImpl(new MemoryCookieStore()))
                .build();
    }

    //静态内部类单例模式
    private static class SingleHolder {
        private static final clientManager mInstance = new clientManager();
    }

    public static clientManager getInstance() {
        return SingleHolder.mInstance;
    }
}
