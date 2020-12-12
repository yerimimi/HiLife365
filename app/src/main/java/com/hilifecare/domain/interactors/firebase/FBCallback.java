package com.hilifecare.domain.interactors.firebase;

/**
 * Created by imcreator on 2017. 6. 8..
 */

public interface FBCallback<T> {
    void onResultData(T data);
}
