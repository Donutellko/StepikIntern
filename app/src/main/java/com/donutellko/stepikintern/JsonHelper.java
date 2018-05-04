package com.donutellko.stepikintern;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class JsonHelper {

    public static RequestResponse deserialize(String json) {
        RequestResponse requestResponse = null;
        try {
            requestResponse = new Gson().fromJson(json, RequestResponse.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return requestResponse;
    }

    /**
     * Класс представляет собой структуру JSON-объекта, возвращаемого на запрос %/api/search-results
     */
    static class RequestResponse {
        Meta meta; // информация о странице
        Course[] search_results; // информация о курсах

        /**
         * Содержит информацию, которая нужна для дозапроса следующей страницы результатов
         */
        class Meta {
            int page; // номер текущей страницы результатов
            boolean has_next; // наличие следующей страницы
        }

        /**
         * Имеет только те поля, которые требуются для отображения и хранения списка.
         */
        static class Course {
            int id; // уникальный идентификатор
            double score; // рейтинг курса
            int course; // ???
            int course_owner; // идентификатор владельца курса
            String course_title; // название курса
            String course_cover; // URL изображения обложки

            public Course(String s) {
                course_title = s;
            }
        }
    }
}
