package com.donutellko.stepikintern.mvp;

import com.donutellko.stepikintern.api.Course;

public interface IModel {

    /**
     * Получить результат поискового запроса
     *
     * @param query текст поискового запроса
     */
    void getSearch(String query);

    /**
     * Получить результат и информацию последнего запроса
     */
    void getLastSearch();

    /**
     * Добавить следующую страницу того же запроса
     */
    void getNextPage();

    /**
     * Вернуть список избранных
     */
    void getStarred();

    /**
     * Добавить или убрать курс из избранного
     */
    void setStarred(Course course, boolean b);

    /**
     * Находится ли курс в избранных
     */
    boolean isStarred(Course course);

    /**
     * Требуется ли догрузка страницы.
     * Вернёт true, если следующая страница есть и она не догружается в данный момент
     */
    boolean isNextAvailable();

    String getLastQuery();

    boolean getHasNext();

    int getPageNumber();
}
