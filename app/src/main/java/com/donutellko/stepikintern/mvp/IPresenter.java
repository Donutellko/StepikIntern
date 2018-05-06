package com.donutellko.stepikintern.mvp;

import com.donutellko.stepikintern.api.Course;

import java.util.List;

public interface IPresenter {

    /**
     * Получить результат поискового запроса
     *
     * @param query текст поискового запроса
     */
    void getSearch(String query);

    /**
     * Отобразить результат запроса и его информацию
     */
    void showSearch(List<Course> courses, String query);

    /**
     * Получить повторно результат прошлого запроса
     */
    void getLastSearch();

    /**
     * Добавить определённую страницу поискового запроса в конец списка
     */
    void getNextPage();

    /**
     * Отобразить ещё одну страницу
     */
    void showNextPage(List<Course> courses);

    /**
     * Получить избранные
     */
    void getStarred();

    /**
     * Отобразить избранные
     *
     * @param starred список избранных для отображения
     */
    void showStarred(List<Course> starred);

    /**
     * Определяет, находится ли курс в избранных
     */
    boolean isStarred(Course course);

    void setStarred(Course course, boolean b);


    boolean getHasNext();

    int getPageNumber();

    String getLastQuery();

    void showError(Throwable t);
}
