package com.donutellko.stepikintern;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.mvp.IPresenter;
import com.donutellko.stepikintern.mvp.IView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IView {

    IPresenter presenter;

    ViewsController viewsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewsController = new ViewsController(findViewById(R.id.list_view));

        showInitial(); // Отобразить сообщение о том, что поиск не был выполнен

        presenter = new PresenterImpl(this);

        getStarred(); // изначально вывести избранное
    }

    private void getStarred() {
        setTitle("Избранное");

        viewsController.setVisibility(false, true, false, false, false);

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
        setTitle(query);
        showLoading();
        presenter.showSearch(query);
    }


    @Override
    public void showLoading() {
        viewsController.setVisibility(false, true, false, false, true);
    }

    @Override
    public void showList(List<Course> courseList, boolean hasNext) {
        if (courseList.size() == 0) {
            showEmptyList();
            return;
        }

        CourseListAdapter adapter = new CourseListAdapter(courseList, presenter);

        viewsController.recyclerView.setAdapter(adapter);

        viewsController.setVisibility(true, false, false, false, true);

    }

    @Override
    public void showText(String message) {
        viewsController.textView.setText(message);

        viewsController.setVisibility(false, false, true, false, true);
    }

    @Override
    public void showInitial() {
        showText("Нажмите на кнопку поиска, чтобы ввести запрос.");
    }

    @Override
    public void showEmptyList() {
        showText("Поиск не принёс результатов");
    }

    @Override
    public void showError(Throwable t) {
        showText("Произошла ошибка!");
        Toast.makeText(this,
                "Причина ошибки: \n" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoConnection() {
        showText("Ошибка поиска. \nПроверьте подключение к сети Интернет.");

        viewsController.setVisibility(false, false, true, true, true);
    }

    @Override
    public void showStarred(List<Course> starred) {
        if (starred.size() == 0) showText("Список избранного пуст.");
        else showList(starred, false);

        viewsController.setFabVisible(false);
    }

    @Override
    public void cleanList() {
        viewsController.recyclerView.setAdapter(null);
    }

    @Override
    public void appendList(List<Course> courses, boolean hasNext) {
        CourseListAdapter adapter = (CourseListAdapter) viewsController.recyclerView.getAdapter();
        adapter.appendCourses(courses);
        adapter.notifyDataSetChanged();

        viewsController.setScrollListener();
    }

    @Override
    public void showUpdating(boolean b) {
        viewsController.setProgressVisible(b);
    }

    private class ViewsController {
        TextView textView;
        ProgressBar progressBar;
        RecyclerView recyclerView;
        Button refreshButton;
        FloatingActionButton starredFab;

        public ViewsController(View view) {
            recyclerView = view.findViewById(R.id.list_recycler);
            textView = view.findViewById(R.id.list_text);
            progressBar = view.findViewById(R.id.list_progress);
            refreshButton = view.findViewById(R.id.list_refresh);
            starredFab = view.findViewById(R.id.starred);

            prepareRecyclerView(); // настроить RecycleView: установить количество колонок
            setScrollListener();
            setStaredFabListener();
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
         * Задаёт параметры для RecyclerView: количество колонок.
         */
        private void prepareRecyclerView() {
            boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), isPortrait ? 2 : 4));
        }

        private void setVisibility(boolean recycler, boolean progress, boolean text, boolean refresh, boolean starred) {
            setRecyclerVisible(recycler);
            setProgressVisible(progress);
            setTextVisible(text);
            setRefreshVisible(refresh);
            setFabVisible(starred);
        }

        void setRecyclerVisible(boolean recyclerVisible) {
            recyclerView.setVisibility(recyclerVisible ? View.VISIBLE : View.INVISIBLE);
        }

        void setProgressVisible(boolean progressVisible) {
            progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
        }

        void setTextVisible(boolean textVisible) {
            textView.setVisibility(textVisible ? View.VISIBLE : View.GONE);
        }

        void setRefreshVisible(boolean refreshVisible) {
            refreshButton.setVisibility(refreshVisible ? View.VISIBLE : View.INVISIBLE);
        }

        void setFabVisible(boolean fabVisible) {
            starredFab.setVisibility(fabVisible ? View.VISIBLE : View.INVISIBLE);
        }

        /**
         * Устанавливает ScrollListener, вызывающий метод подгрузки следующей страницы
         */
        private void setScrollListener() {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int toBottom = countToBottom();
                    if (toBottom <= 4)
                        presenter.appendSearch();
                    if (toBottom <= 1 && presenter.hasNext())
                        showUpdating(false);
                }
            });
        }

        /**
         * Определить количество курсов, которые отделяют пользователя от конца RecyclerView
         *
         * @return чисто курсов до конца
         */
        private int countToBottom() {

            LinearLayoutManager lm = (LinearLayoutManager) viewsController.recyclerView.getLayoutManager();
            int pos = lm.findLastVisibleItemPosition(); // номер последнего элемента на экране
//            int count = recyclerView.getChildCount(); // количество элементов
            int count = recyclerView.getAdapter().getItemCount();

            Log.i("countToBottom()", "pos=" + pos + ", count=" + count + "; result=" + (count - pos));
            return count - pos;
        }
    }
}
