package com.donutellko.stepikintern.mvp;

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
     * @param query текст поискового запроса
     */
    void showList(List<Course> courseList, String query);

    /**
     * Отобразить сообщение.
     * Скрыть RecyclerView и ProgressBar, отобразить TextView.
     *
     * @param message Текст сообщения (поиск не был выполнен или не принёс результата)
     */
    void showText(String message);

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
     * Отобразить уведомление, не скрывая имеющиеся результаты
     */
    void showNotification(String text);

    /**
     * Сообщить о том, что не удалось подключиться
     */
    void showNoConnection();

    /**
     * Отобразить избранные курсы
     *
     * @param starred Список избранных курсов
     */
    void showStarred(List<Course> starred);

    /**
     * Очищает RecycleView
     */
    void cleanList();

    /**
     * Добавляет в конец списка следующий набор страниц
     *
     * @param courses список добавляемых курсов
     */
    void appendList(List<Course> courses);

    /**
     * Сообщает о том, что информация подгружается, не пряча уже выведенное, если true
     */
    void showUpdating(boolean b);
}
