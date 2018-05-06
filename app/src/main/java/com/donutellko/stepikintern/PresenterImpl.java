package com.donutellko.stepikintern;

import android.util.Log;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.api.SearchRequestResult;
import com.donutellko.stepikintern.mvp.IModel;
import com.donutellko.stepikintern.mvp.IPresenter;
import com.donutellko.stepikintern.mvp.IView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresenterImpl implements IPresenter {

    // Содержит текст последнего запроса
    String lastSearch = null;

    // Храним информацию о том, загружается ли сейчас что-то
    boolean isLoading = false;

    // Указывает на то, есть ли следующая страница
    boolean hasNext = false;

    //Указывает на то, какая последняя страница запроса сейчас выведена
    int pageNumber = 1;

    IView view;
    IModel model;

    PresenterImpl(IView view) {
        this.view = view;
        model = new ModelImpl(this);
    }

    @Override
    public void showSearch(String query) {
        Log.i("Presenter.showSearch", "lastSearch=" + lastSearch + "; hasNext=" + hasNext);

        isLoading = false;

        view.cleanList(); // При выведении первой страницы очищаем список
        view.showLoading();

        lastSearch = query;
        pageNumber = 1;
        appendSearch(); // Отображает первую страницу результата запроса
    }

    @Override
    public void appendSearch() {
        Log.i("Presenter.appendSearch", "lastSearch=" + lastSearch + "; hasNext=" + hasNext);

        if (isLoading || lastSearch == null || lastSearch != null && hasNext)
            return;

        isLoading = true;
        view.showUpdating(true);

        Log.i("Presenter.appendSearch", "searching...");
        App.getStepikApi().getSearchResults(lastSearch, pageNumber).enqueue(new Callback<SearchRequestResult>() {
            @Override
            public void onResponse(Call<SearchRequestResult> call, Response<SearchRequestResult> response) {
                if (response.body() == null) {
                    Log.w("Retrofit.onResponse", "Got EMPTY response.");
                    view.showEmptyList();
                    return;
                }

                List<Course> courses = new ArrayList<>();
                SearchRequestResult requestResult = response.body();

                for (Course c : requestResult.getCourses()) {
                    if (c.getCourseTitle() != null)
                        courses.add(c);
                }

                Log.i("Retrofit.onResponse", "Got response with " + courses.size() + " courses. ");

                if (courses.size() == 0) {
                    view.showUpdating(false);
                    return;
                }

                if (pageNumber == 1)
                    view.showList(courses, requestResult.getMeta().getHasNext());
                else
                    view.appendList(courses, requestResult.getMeta().getHasNext());

                pageNumber++;
                isLoading = false;
                view.showUpdating(false);
            }

            @Override
            public void onFailure(Call<SearchRequestResult> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve"))
                    view.showNoConnection();
                else
                    view.showError(t);

                t.printStackTrace();

                isLoading = false;
                view.showUpdating(false);
            }
        });
    }

    @Override
    public void getStarred() {
        pageNumber = 1;
        hasNext = false;
        lastSearch = null;
        isLoading = true;

        model.getStarred();
    }

    @Override
    public void showStarred(List<Course> starred) {
        isLoading = false;
        view.showStarred(starred);
    }

    @Override
    public boolean isStarred(Course course) {
        return model.isStarred(course);
    }

    @Override
    public void setStarred(Course course, boolean b) {
        model.setStarred(course, b);
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }
}
