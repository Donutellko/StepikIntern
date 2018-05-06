package com.donutellko.stepikintern;

import android.app.Application;
import android.content.Context;
import android.os.Parcelable;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.api.StepikApi;
import com.donutellko.stepikintern.mvp.IModel;
import com.donutellko.stepikintern.mvp.IPresenter;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static StepikApi stepikApi;
    private AppState appState;

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("https://stepik.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        stepikApi = retrofit.create(StepikApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static StepikApi getStepikApi() {
        return stepikApi;
    }

    public void setAppState(AppState appState) {
        this.appState = appState;
    }

    public AppState getAppState() {
        return appState;
    }

    static class AppState {
        final IModel model;
        final Parcelable recyclerState;

        AppState(IModel model, Parcelable recyclerState) {
            this.model = model;
            this.recyclerState = recyclerState;
        }
    }
}
