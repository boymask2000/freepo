package com.boymask.freepo.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("/api/v2/video/search/?query=teen&per_page=1&page=2&thumbsize=big&order=top-weekly&gay=1&lq=1&format=json")
    Call<RetroFoto> getAllPhotos();
}
