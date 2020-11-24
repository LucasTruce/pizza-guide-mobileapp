package com.app.pizza.service;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String BASE_URL = "http://pizzaguiderest-env.eba-wkwwthm5.us-east-1.elasticbeanstalk.com";

    private static final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();



    public static <S> S createService(Class<S> serviceClass) {
        httpClient.addInterceptor(logging).build();
        logging.level(HttpLoggingInterceptor.Level.BASIC);
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, final String token) {
        logging.level(HttpLoggingInterceptor.Level.BASIC);
        if (token != null) {
            httpClient.interceptors().clear();
            httpClient.addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder().header("Authorization", token);
                    Request request = builder.build();
                    return chain.proceed(request);
                }
            });
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
