package com.donutellko.stepikintern;

import android.os.Bundle;
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
import com.donutellko.stepikintern.mvp.IPresenter;
import com.donutellko.stepikintern.mvp.IView;

import java.util.List;

public class MainActivity extends CourseListActivity implements IView {

    IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView вызван в суперклассе

        presenter = new PresenterImpl(this);

        getStarred(); // изначально вывести избранное
    }

    private void getStarred() {
        setTitle("Избранное");

        setVisibility(false, true, false, false, false);

        setScrollListener();

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

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        Log.i("Intent", intent.getAction());
//
//        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            Log.i("Search", "query=" + query);
//
//            getSearch(query);
//        }
//    }

    private void getSearch(String query) {
        showUpdating(true);
        setTitle(query);
        showLoading();
        presenter.showSearch(query);
    }

    @Override
    public void showLoading() {
        setVisibility(false, true, false, false, true);
    }

    @Override
    public void showList(List<Course> courseList, boolean hasNext) {
        if (courseList.size() == 0) {
            showEmptyList();
            return;
        }

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
        }
    }

    @Override
    public void showStarred(List<Course> starred) {
        if (starred.size() == 0) showText("Список избранного пуст.");
        else showList(starred, false);

        setFabVisible(false);
    }

    @Override
    public void cleanList() {
        recyclerView.setAdapter(null);
    }

    @Override
    public void appendList(List<Course> courses, boolean hasNext) {
        CourseListAdapter adapter = (CourseListAdapter) recyclerView.getAdapter();
        adapter.appendCourses(courses);
        adapter.notifyDataSetChanged();

        setScrollListener();
    }

    @Override
    public void showUpdating(boolean b) {
        setProgressVisible(b);
    }

    /**
     * Устанавливает ScrollListener, вызывающий метод подгрузки следующей страницы
     */
    private void setScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!presenter.hasNext()) return;

                int toBottom = countToBottom();
                if (toBottom <= 8) // Если пользователь приблизился к концу
                    presenter.appendSearch(); // Подгружаем ещё страницу
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
}
