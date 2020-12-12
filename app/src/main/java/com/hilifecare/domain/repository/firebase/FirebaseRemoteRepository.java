package com.hilifecare.domain.repository.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import rx.Observable;
import rx.functions.Func0;

public class FirebaseRemoteRepository {

    private final FirebaseService service;

    @Inject
    public FirebaseRemoteRepository(Retrofit retrofit) {
        this.service = retrofit.create(FirebaseService.class);
    }

    public Observable<DatabaseReference> fGet(String databaseName, String uuid){
        System.out.println(" fget uuid : "+uuid);
        if(uuid != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(databaseName).child(uuid);

            Observable<DatabaseReference> observable = Observable.defer(new Func0<Observable<DatabaseReference>>() {
                @Override public Observable<DatabaseReference> call() {
                    return Observable.just(ref);
                }
            });
            return observable;
        }else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(databaseName);

            Observable<DatabaseReference> observable = Observable.defer(new Func0<Observable<DatabaseReference>>() {
                @Override public Observable<DatabaseReference> call() {
                    return Observable.just(ref);
                }
            });
            return observable;
        }
    }

    interface FirebaseService {
        @GET("api/foo")
        Observable<Result<Void>> foo();
    }
}
