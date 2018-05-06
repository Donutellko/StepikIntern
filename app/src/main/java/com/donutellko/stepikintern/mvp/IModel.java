package com.donutellko.stepikintern.mvp;

import com.donutellko.stepikintern.api.Course;

public interface IModel {

    /**
     * Выводит список избранных
     */
    void getStarred();

    /**
     * Добавляет курс в избранное
     */
    void addStarred(Course course);

    /**
     * Удаляет курс из избранного
     */
    void removeStarred(Course course);

    void setStarred(Course course, boolean b);

    boolean isStarred(Course course);
}
