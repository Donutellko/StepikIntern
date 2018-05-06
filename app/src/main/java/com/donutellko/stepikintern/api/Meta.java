package com.donutellko.stepikintern.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Содержит информацию, которая нужна для дозапроса следующей страницы результатов
 */

@SuppressWarnings("unused") // Некоторые методы используются только в рантайме из GSON'а
public class Meta {

    @SerializedName("page")
    @Expose
    private Integer page;

    @SerializedName("has_next")
    @Expose
    private Boolean hasNext;

    @SerializedName("has_previous")
    @Expose
    private Boolean hasPrevious;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

}
