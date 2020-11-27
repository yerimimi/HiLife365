package com.hilifecare.domain.repository.firebase;

import com.google.firebase.database.DatabaseReference;

import rx.Observable;

/**
 * Created by Administrator on 2017-02-28.
 */

public interface FirebaseRepository {

    public Observable<DatabaseReference> fGet(String databaseName, String uuid);
}
