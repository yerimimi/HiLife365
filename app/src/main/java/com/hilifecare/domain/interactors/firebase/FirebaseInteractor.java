package com.hilifecare.domain.interactors.firebase;

import com.google.firebase.database.DatabaseReference;
import com.hilifecare.model.Exercise;
import com.hilifecare.model.LevelTest;
import com.hilifecare.model.Plan;
import com.hilifecare.model.PreviewProgram;
import com.hilifecare.model.Program;
import com.hilifecare.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;

public interface FirebaseInteractor {

//    DatabaseReference getLevelTest();
    void getUserInfo(FBCallback<Observable<UserInfo>> callback);
    void insertUserDatailInfo(UserInfo userInfo);
    void getLevelTest(String levelOfDifficulty, FBCallback<Observable<ArrayList<LevelTest>>> callback);
    void getPlanList(FBCallback<Observable<ArrayList<Plan>>> callback);
    void getRecommendPlan(FBCallback<Observable<Plan>> callback);
    void getPreviewProgramList(Plan plan, FBCallback<Observable<ArrayList<PreviewProgram>>> callback);
    void getPreviewProgram(PreviewProgram previewProgram, FBCallback<Observable<ArrayList<Program>>> callback);
    void getProgramList(Plan plan, FBCallback<Observable<ArrayList<Program>>> callback);
    void getProgram(Plan plan, FBCallback<Observable<ArrayList<Program>>> callback);
    void insetLevelTestResult(HashMap<String, String> userTestResultMap, String time);
    void insertSmartBandData(String dbName, String time, double value);
    void insertSmartBandData(String dbName, String time, String value);
    void insertSmartBandAddress(String mDeviceAddress);
    void getSmartBandAddress(FBCallback<Observable<String>> callback);

    void getExerciseList(FBCallback<Observable<ArrayList<Exercise>>> callback);

    //좋아요 가져오기
    public Observable<DatabaseReference> fGet(String databaseName, String uuid);

    void insertUserDefaultInfo();
}
