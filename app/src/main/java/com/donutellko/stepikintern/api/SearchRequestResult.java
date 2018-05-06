package com.donutellko.stepikintern.api;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Класс представляет собой структуру JSON-объекта, возвращаемого на запрос %/api/search-results
 *
 * Этот и вложенные классы сгенерированы при помощи http://www.jsonschema2pojo.org
 */

@SuppressWarnings("unused") // Некоторые методы используются только в рантайме из GSON'а
public class SearchRequestResult {

    @SerializedName("meta")
    @Expose
    private Meta meta;

    @SerializedName("search-results")
    @Expose
    private List<Course> courses = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

}


