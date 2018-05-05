package com.donutellko.stepikintern;

import com.donutellko.stepikintern.api.Course;

import java.util.List;

public interface IView {

    /**
     * Отобразить экран загрузки.
     * Скрыть RecyclerView или TextView и отобразить ProgressBar.
     */
    void showLoading();

    /**
     * Отобразить переданный список.
     * Наполнить RecyclerView, скрыть ProgressBar или TextView и отобразить RecyclerView.
     *
     * @param courseList список.
     * @param hasNext сообщает, может ли быть подгружена ещё одна страница
     */
    void showList(List<Course> courseList, boolean hasNext);

    /**
     * Отобразить сообщение.
     * Скрыть RecyclerView и ProgressBar, отобразить TextView.
     *
     * @param message Текст сообщения (поиск не был выполнен или не принёс результата)
     */
    void showText(String message);

    /**
     * Отобразить изначальное состояние (приглашение ввести запрос или вывести Favorites)
     */
    void showInitial();

    /**
     * Отобразить информацию о том, что поиск не принёс результатов
     */
    void showEmptyList();

    /**
     * Сообщить, что произошла ошибка при выполнении запроса
     *
     * @param t Throwable, который был возвращён.
     */
    void showError(Throwable t);

    /**
     * Сообщить о том, что не удалось подключиться
     */
    void showNoConnection();
}