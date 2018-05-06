package com.donutellko.stepikintern.mvp;

import com.donutellko.stepikintern.api.Course;

public interface IModel {

    /**
     * Выводит список избранных
     */
    void getStarred();

    /**
     * Добавляет или удаляет курс из избранного
     */
    void setStarred(Course course, boolean b);

    boolean isStarred(Course course);
}
