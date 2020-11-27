package com.hilifecare.domain.repository.firebase;

import com.google.firebase.database.DatabaseReference;

import rx.Observable;

/**
 * Created by Administrator on 2017-02-28.
 */

public class FirebaseRepositoryImpl implements FirebaseRepository {

    FirebaseLocalRepository localRepository;

    FirebaseRemoteRepository remoteRepository;

    public FirebaseRepositoryImpl(FirebaseRemoteRepository remoteRepository, FirebaseLocalRepository localRepository) {
        this.remoteRepository = remoteRepository;
        this.localRepository = localRepository;
    }


    @Override
    public Observable<DatabaseReference> fGet(String databaseName, String uuid) {
        return remoteRepository.fGet(databaseName, uuid);
    }
}
