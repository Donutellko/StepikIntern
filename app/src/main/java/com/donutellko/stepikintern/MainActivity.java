package com.donutellko.stepikintern;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.mvp.IModel;
import com.donutellko.stepikintern.mvp.IPresenter;
import com.donutellko.stepikintern.mvp.IView;

import java.util.List;

public class MainActivity extends CourseListActivity implements IView {

    IPresenter presenter;
    private long backPressedTime; // Время последнего нажатия кнопки назад
    private static final long exitTimeout = 500; // Таймаут двойного нажатия кнопки назад

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView вызван в суперклассе

        App.AppState savedState = ((App) getApplicationContext()).getAppState();
        if (savedState != null) { // Если сохранили информацию при повороте и т.п.
            // Восстанавливаем состояние модели
            IModel model = savedState.model;

            presenter = new PresenterImpl(this, model);

            presenter.getLastSearch();
            // Восстанавливаем положение RecyclerView
//            recyclerView.getLayoutManager().onRestoreInstanceState(savedState.recyclerState);
        } else {
            presenter = new PresenterImpl(this);
            getStarred(); // изначально вывести избранное
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Parcelable recyclerState = recyclerView.getLayoutManager().onSaveInstanceState();
        App.AppState savedState = new App.AppState(((PresenterImpl) presenter).getModel(), recyclerState);

        ((App) getApplicationContext()).setAppState(savedState);
    }

    private void getStarred() {
        setTitle("Избранное");

        setVisibility(false, true, false, false, false);

        presenter.getStarred();
    }

    /**
     * Отображает меню и кнопку поиска.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchMenuItem.collapseActionView();

                Log.i("Search", "query=" + query);
                getSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        setStaredFabListener();

        return true;
    }

    private void getSearch(String query) {
        showUpdating(true);
        setTitle(query);
        showLoading();
        presenter.getSearch(query);
    }

    @Override
    public void showLoading() {
        setVisibility(false, true, false, false, true);
    }

    @Override
    public void showList(List<Course> courseList, String query) {
        setTitle(query);

        if (courseList.size() == 0) {
            showEmptyList();
            return;
        }

        setScrollListener();

        CourseListAdapter adapter = new CourseListAdapter(courseList, presenter);

        recyclerView.setAdapter(adapter);

        setVisibility(true, false, false, false, true);

    }

    @Override
    public void showText(String message) {
        textView.setText(message);

        setVisibility(false, false, true, false, true);
    }

    @Override
    public void showEmptyList() {
        showText("Поиск не принёс результатов");
    }

    @Override
    public void showError(Throwable t) {
        showText("Произошла ошибка!");

        showNotification("Причина ошибки: \n" + t.getLocalizedMessage());
    }

    @Override
    public void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoConnection() {
        String text = "Ошибка поиска. \nПроверьте подключение к сети Интернет.";

        if (presenter.getPageNumber() > 1) { // Если уже выведены какие-то результаты
            showNotification(text); // Выводим уведомление, не скрывая их
        } else {
            showText(text); // Отображаем вместо выдачи
            setVisibility(false, false, true, true, true);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.getSearch(presenter.getLastQuery());
                }
            });
        }
    }

    @Override
    public void showStarred(List<Course> starred) {
        if (starred.size() == 0) showText("Список избранного пуст.");
        else showList(starred, "Избранное");

        setFabVisible(false);
    }

    @Override
    public void cleanList() {
        recyclerView.setAdapter(null);
    }

    @Override
    public void appendList(List<Course> courses) {
        CourseListAdapter adapter = (CourseListAdapter) recyclerView.getAdapter();
        adapter.appendCourses(courses);
        adapter.notifyDataSetChanged();

        setScrollListener();
    }

    @Override
    public void showUpdating(boolean b) {
        setProgressVisible(b);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    /**
     * Устанавливает ScrollListener, вызывающий метод подгрузки следующей страницы
     */
    private void setScrollListener() {
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Подгружаем только если есть что подгружать...
                if (presenter.getState() != IModel.State.SEARCH || !presenter.getHasNext()) return;

                int toBottom = countToBottom();
                if (toBottom <= 8) // Если пользователь приблизился к концу
                    presenter.getNextPage(); // Подгружаем ещё страницу
                if (toBottom <= 4) // Если пользователь уже почти в самом конце,
                    showUpdating(true); // Показываем ему, что будут ещё результаты
            }
        });
    }

    private void setStaredFabListener() {
        starredFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStarred();
            }
        });
    }

    /**
     * Определить количество курсов, которые отделяют пользователя от конца RecyclerView
     *
     * @return чисто курсов до конца
     */
    private int countToBottom() {

        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        int pos = lm.findLastVisibleItemPosition(); // номер последнего элемента на экране
//            int count = recyclerView.getChildCount(); // количество элементов
        int count = recyclerView.getAdapter().getItemCount();

        Log.i("countToBottom()", "pos=" + pos + ", count=" + count + "; result=" + (count - pos));
        return count - pos;
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < exitTimeout)
            super.onBackPressed();

        backPressedTime = System.currentTimeMillis();

        switch (presenter.getState()) {
            case ERROR:
            case SEARCH:
            case LOADING:
                getStarred();
                break;
            case STARRED:
                presenter.getLastSearch();
                break;
        }
    }
}
