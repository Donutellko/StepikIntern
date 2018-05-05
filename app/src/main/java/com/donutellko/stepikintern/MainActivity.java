package com.donutellko.stepikintern;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.donutellko.stepikintern.api.Course;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IView {
    TextView textView;
    ProgressBar progressBar;
    RecyclerView recyclerView;

    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.list_recycler);
        textView = findViewById(R.id.list_text);
        progressBar = findViewById(R.id.list_progress);

        showInitial(); // Отобразить сообщение о том, что поиск не был выполнен
        prepareRecyclerView(); // настроить RecycleView: установить количество колонок

        presenter = new Presenter(this);
//        presenter.showSearch("алгоритмы");
    }

    /**
     * Задаёт параметры для RecyclerView: количество колонок.
     */
    private void prepareRecyclerView() {
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), isPortrait ? 2 : 4));
    }

    /**
     * Отображает меню и кнопку поиска.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public void showLoading() {
        recyclerView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showList(List<Course> courseList, boolean hasNext) {
        if (courseList.size() == 0) {
            showEmptyList();
            return;
        }

        CourseListAdapter adapter = new CourseListAdapter(courseList);

        recyclerView.setAdapter(adapter);

        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showText(String message) {
        textView.setText(message);

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
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
        showText("Ошибка поиска. Проверьте подключение к сети Интернет.");
    }

//    private void setVisibility(boolean recycler, boolean progress, boolean text) {
//        recyclerView.setVisibility(recycler ? View.VISIBLE : View.INVISIBLE);
//        progressBar.setVisibility(progress ? View.VISIBLE : View.GONE);
//        textView.setVisibility(text ? View.VISIBLE : View.GONE);
//    }
}
