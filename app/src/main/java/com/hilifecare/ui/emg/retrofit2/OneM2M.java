package com.hilifecare.ui.emg.retrofit2;

import com.hilifecare.ui.emg.retrofit2.entity.OneM2MCin;
import com.hilifecare.ui.emg.retrofit2.entity.OneM2MCinLatestRoot;
import com.hilifecare.ui.emg.retrofit2.service.OneM2MService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OneM2M {
    private String tag = getClass().getSimpleName();
    Callback<Void> createCInCallback;
    Callback<OneM2MCinLatestRoot> retrieveCInCallback;
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(OneM2MService.URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    OneM2MService service = retrofit.create(OneM2MService.class);

    public void createCin(
            String deviceMuid,
            String deviceToken,
            String field,
            OneM2MCin cin
    ) {
        service.createCin(deviceMuid, deviceToken, field, cin).enqueue(createCInCallback);
    }

    public void retrieveCinLatest(
            String deviceMuid,
            String deviceToken,
            String field
    ) {
        service.retrieveCin(deviceMuid, deviceToken, field).enqueue(retrieveCInCallback);
    }

    public void setCreateCInCallback(Callback<Void> callback) {
        this.createCInCallback = callback;
    }

    public void setRetrieveCInCallback(Callback<OneM2MCinLatestRoot> callback) {
        this.retrieveCInCallback = callback;
    }
    private HttpLoggingInterceptor httpLoggingInterceptor(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                android.util.Log.e(tag, message + "");
            }
        });

        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}