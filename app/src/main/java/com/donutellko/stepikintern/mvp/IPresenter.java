package com.donutellko.stepikintern.mvp;

import com.donutellko.stepikintern.api.Course;

import java.util.List;

public interface IPresenter {

    /**
     * Отобразить результат поискового запроса
     *
     * @param query текст поискового запроса
     */
    void showSearch(String query);

    /**
     * Добавить определённую страницу поискового запроса в конец списка
     */
    void appendSearch();

    /**
     * Вывести список избранных
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

    boolean hasNext();
}
