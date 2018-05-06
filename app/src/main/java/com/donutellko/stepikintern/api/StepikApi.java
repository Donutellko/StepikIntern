package com.donutellko.stepikintern.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StepikApi {

    @GET("/api/search-results")
    Call<SearchRequestResult> getSearchResults(@Query("query") String query, @Query("page") int page);


}
