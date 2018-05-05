package com.donutellko.stepikintern;

import android.app.Application;

import com.donutellko.stepikintern.api.StepikApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private Retrofit retrofit;
    private static StepikApi stepikApi;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://stepik.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        stepikApi = retrofit.create(StepikApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static StepikApi getStepikApi() {
        return stepikApi;
    }
}
