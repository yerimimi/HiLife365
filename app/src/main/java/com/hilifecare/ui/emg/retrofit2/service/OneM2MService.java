package com.hilifecare.ui.emg.retrofit2.service;

import com.hilifecare.ui.emg.retrofit2.entity.OneM2MCin;
import com.hilifecare.ui.emg.retrofit2.entity.OneM2MCinLatestRoot;
import com.hilifecare.ui.emg.retrofit2.url.ServerInformation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OneM2MService {

    String URL = ServerInformation.oneM2MUrl;

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json;ty=4",
            "X-M2M-RI: application"})
    @POST("{deviceMuid}/fields/{field}")
    Call<Void> createCin(
            @Path("deviceMuid") String deviceMuid,
            @Header("X-M2M-Origin") String deviceToken,
            @Path("field") String manifest,
            @Body OneM2MCin cin
    );

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json;ty=4",
            "X-M2M-RI: application"})
    @GET("{deviceMuid}/fields/{field}/latest")
    Call<OneM2MCinLatestRoot> retrieveCin(
            @Path("deviceMuid") String deviceMuid,
            @Header("X-M2M-Origin") String deviceToken,
            @Path("field") String manifest
    );
}

