package com.donutellko.stepikintern;

import android.support.annotation.NonNull;
import android.util.Log;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.api.SearchRequestResult;
import com.donutellko.stepikintern.mvp.IModel;
import com.donutellko.stepikintern.mvp.IPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ModelImpl implements IModel {

    // Храним информацию о том, загружается ли сейчас что-то
    private boolean isLoading = false;

    private IPresenter presenter;

    private List<Course> starredList;

    private SearchInfo currentSearch;

    ModelImpl(PresenterImpl presenter) {
        this.presenter = presenter;
        starredList = loadStarredList();
    }

    @Override
    public void getSearch(String query) {
        currentSearch = new SearchInfo(query);

        Log.i("Presenter.getSearch", currentSearch.toString());

        currentSearch = new SearchInfo(query);
        appendSearch(currentSearch); // Отображает первую страницу результата запроса
    }

    @Override
    public void getLastSearch() {
        assert currentSearch != null;

        presenter.showSearch(currentSearch.courses, currentSearch.query);
    }

    @Override
    public void getNextPage() {
        assert currentSearch != null;
        currentSearch.page++;
        appendSearch(currentSearch);
    }

    private void appendSearch(final SearchInfo currentSearch) {

        assert !isLoading; // На момент начала загрузки не должна она уже идти
        isLoading = true;

        App.getStepikApi().getSearchResults(currentSearch.query, currentSearch.page).enqueue(new Callback<SearchRequestResult>() {
            @Override
            public void onResponse(@NonNull Call<SearchRequestResult> call, @NonNull Response<SearchRequestResult> response) {
                assert response.body() != null; // всегда должны получать какой-то ответ, иначе налажал

                SearchRequestResult requestResult = response.body();

                assert requestResult != null;
                currentSearch.hasNext = requestResult.getMeta().getHasNext();
                List<Course> courses = new ArrayList<>();

                // Сервер всегда возвращает 20 объектов, часть из них на самом деле уроки,
                // которые не нужны по заданию. Удаляем их
                for (Course c : requestResult.getCourses()) {
                    if (c.getCourseTitle() != null)
                        courses.add(c);
                }

                currentSearch.courses.addAll(courses);

                Log.i("Retrofit.onResponse",
                        "Got response with " + courses.size()
                                + " courses. Has more pages? " + currentSearch.hasNext);

                if (currentSearch.page == 1) {
                    presenter.showSearch(courses, currentSearch.query);
                } else {
                    presenter.showNextPage(courses);
                }

                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<SearchRequestResult> call, @NonNull Throwable t) {
                presenter.showError(t);

                t.printStackTrace();

                isLoading = false;
            }
        });
    }

    @Override
    public void getStarred() {
        currentSearch = null;
        isLoading = true;

        presenter.showStarred(starredList);
    }

    @Override
    public void setStarred(Course course, boolean b) {
        if (starredList.contains(course) && !b) starredList.remove(course);
        else if (b) starredList.add(course);
    }

    @Override
    public boolean isStarred(Course course) {
        return starredList.contains(course);
    }

    @Override
    public boolean isNextAvailable() {
        assert currentSearch.query != null;
        return currentSearch.hasNext && !isLoading;
    }

    @Override
    public String getLastQuery() {
        return currentSearch == null ? null : currentSearch.query;
    }

    @Override
    public boolean getHasNext() {
        assert currentSearch != null;
        return currentSearch.hasNext;
    }

    @Override
    public int getPageNumber() {
        assert currentSearch != null;
        return currentSearch.page;
    }

    /**
     * Получает сохранённый на устройстве список курсов
     */
    private List<Course> loadStarredList() {
        ArrayList<Course> list = new ArrayList<>();

        // TODO

        return list;
    }

    public void setPresenter(PresenterImpl presenter) {
        this.presenter = presenter;
    }

    private class SearchInfo {
        private String query;
        // Содержит текст последнего запроса

        // Указывает на то, есть ли следующая страница
        private boolean hasNext = false;

        // Указывает на то, какая последняя страница запроса сейчас выведена
        private int page = 1;

        public List<Course> courses;

        SearchInfo(String query) {
            this.query = query;
            this.courses = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "SearchInfo{" +
                    "query='" + query + '\'' +
                    ", page=" + page +
                    ", hasNext=" + hasNext +
                    '}';
        }
    }
}
