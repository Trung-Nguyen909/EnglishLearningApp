package com.example.englishlearningapp.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (original.header("No-Authentication") != null) {
            Request newRequest = original.newBuilder()
                    .removeHeader("No-Authentication")
                    .build();
            return chain.proceed(newRequest);
        }

        SharedPreferences prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token != null) {
            Request newRequest = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(original);
    }
}
