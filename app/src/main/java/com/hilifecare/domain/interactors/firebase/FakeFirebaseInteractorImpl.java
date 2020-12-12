package com.hilifecare.domain.interactors.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hilifecare.model.Exercise;
import com.hilifecare.model.LevelTest;
import com.hilifecare.model.Plan;
import com.hilifecare.model.PreviewProgram;
import com.hilifecare.model.Program;
import com.hilifecare.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.functions.Func0;
import timber.log.Timber;

/**
 * Created by imcreator on 2017. 5. 4..
 */

public class FakeFirebaseInteractorImpl implements FirebaseInteractor {

    @Override
    public void getUserInfo(FBCallback<Observable<UserInfo>> callback) {

    }

    @Override
    public void insertUserDefaultInfo() {

    }

    @Override
    public void insertUserDatailInfo(UserInfo userInfo) {

    }

    @Override
    public void getLevelTest(String levelOfDifficulty, FBCallback<Observable<ArrayList<LevelTest>>> callback) {
        Timber.d("get level test: "+levelOfDifficulty);
        ArrayList<LevelTest> levelTestArrayList = new ArrayList<LevelTest>();

        callback.onResultData(Observable.just(levelTestArrayList));
    }

    @Override
    public void getRecommendPlan(FBCallback<Observable<Plan>> callback) {

    }

    @Override
    public void getPlanList(FBCallback<Observable<ArrayList<Plan>>> callback) {
        Timber.d("get plan list");
        ArrayList<Plan> planArrayList = new ArrayList<Plan>();
        planArrayList.add(new Plan("0", "high", "2주 다이어트", "", "빡시게", "activity"));
        planArrayList.add(new Plan("0", "middle", "3주 다이어트", "", "적당히", "activity"));
        planArrayList.add(new Plan("0", "low", "4주 다이어트", "", "느슨하게", "activity"));
        callback.onResultData(Observable.just(planArrayList));
    }

    @Override
    public void getPreviewProgramList(Plan plan, FBCallback<Observable<ArrayList<PreviewProgram>>> callback) {
        ArrayList<PreviewProgram> previewProgramArrayList = new ArrayList<PreviewProgram>();
        previewProgramArrayList.add(new PreviewProgram(plan.getPlan_id(), plan.getName(), "뱃살 완전분해", 1, 0, "뱃살터치"));
        previewProgramArrayList.add(new PreviewProgram(plan.getPlan_id(), plan.getName(), "허벅지살 완전분해", 2, 0, "허벅지살터치"));
        previewProgramArrayList.add(new PreviewProgram(plan.getPlan_id(), plan.getName(), "종아리살 완전분해", 3, 0, "종아리살터치"));
        previewProgramArrayList.add(new PreviewProgram(plan.getPlan_id(), plan.getName(), "볼살 완전분해", 4, 0, "볼살터치"));
        previewProgramArrayList.add(new PreviewProgram(plan.getPlan_id(),plan.getName(), "팔살 완전분해", 5, 0, "팔살터치"));
        callback.onResultData(Observable.just(previewProgramArrayList));
    }

    @Override
    public void getPreviewProgram(PreviewProgram previewProgram, FBCallback<Observable<ArrayList<Program>>> callback) {
        ArrayList<PreviewProgram> previewProgramArrayList = new ArrayList<PreviewProgram>();
        previewProgramArrayList.add(new PreviewProgram(previewProgram.getUser_id(), previewProgram.getSchdule_name(), "preview_팔벌려 높이뛰기", previewProgram.getDay(), 1, "WarmUp"));
        previewProgramArrayList.add(new PreviewProgram(previewProgram.getUser_id(), previewProgram.getSchdule_name(), "preview_푸시업", previewProgram.getDay(), 1, "Main"));
        previewProgramArrayList.add(new PreviewProgram(previewProgram.getUser_id(), previewProgram.getSchdule_name(), "preview_윗몸일으키기", previewProgram.getDay(), 2, "Main"));
        previewProgramArrayList.add(new PreviewProgram(previewProgram.getUser_id(), previewProgram.getSchdule_name(), "preview_숨고르기", previewProgram.getDay(), 1, "WarmDown"));

        //callback.onResultData(Observable.just(previewProgramArrayList));
    }

    @Override
    public void getProgramList(Plan plan, FBCallback<Observable<ArrayList<Program>>> callback) {
        ArrayList<Program> programArrayList = new ArrayList<Program>();
        programArrayList.add(new Program("", 0, "뱃살 완전분해", "", 0,       "WarmUp", "0", "0", "팔벌려 높이 뛰기", "뱃살터치",30));
        programArrayList.add(new Program("", 0, "허벅지살 완전분해", "", 0,   "WarmUp", "1", "0", "팔벌려 높이 뛰기", "허벅지살터치",30));
        programArrayList.add(new Program("", 0, "종아리살 완전분해", "", 0,   "Main", "0", "0", "팔벌려 높이 뛰기", "종아리살터치",30));
        programArrayList.add(new Program("", 0, "볼살 완전분해", "", 0,       "Main", "1", "0", "팔벌려 높이 뛰기", "볼살터치",30));
        programArrayList.add(new Program("", 0, "팔살 완전분해", "", 0,       "WarmDown", "0", "0", "팔벌려 높이 뛰기", "팔살터치",30));
        callback.onResultData(Observable.just(programArrayList));
    }

    @Override
    public void getProgram(Plan plan, FBCallback<Observable<ArrayList<Program>>> callback) {
        ArrayList<Program> programArrayList = new ArrayList<Program>();
        programArrayList.add(new Program("", 0, "뱃살 완전분해", "", 0,       "WarmUp", "0", "0", "팔벌려 높이 뛰기", "뱃살터치",30));
        programArrayList.add(new Program("", 0, "허벅지살 완전분해", "", 0,   "WarmUp", "1", "0", "팔벌려 높이 뛰기", "허벅지살터치",30));
        programArrayList.add(new Program("", 0, "종아리살 완전분해", "", 0,   "Main", "0", "0", "팔벌려 높이 뛰기", "종아리살터치",30));
        programArrayList.add(new Program("", 0, "볼살 완전분해", "", 0,       "Main", "1", "0", "팔벌려 높이 뛰기", "볼살터치",30));
        programArrayList.add(new Program("", 0, "팔살 완전분해", "", 0,       "WarmDown", "0", "0", "팔벌려 높이 뛰기", "팔살터치",30));

        callback.onResultData(Observable.just(programArrayList));
    }

    @Override
    public void insetLevelTestResult(HashMap<String, String> userTestResultMap, String time) {

    }

    @Override
    public void getExerciseList(FBCallback<Observable<ArrayList<Exercise>>> callback) {
        ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
        exerciseArrayList.add(new Exercise("push_up", "1", "가슴", ""));
        exerciseArrayList.add(new Exercise("push_up", "1", "가슴", ""));
        exerciseArrayList.add(new Exercise("push_up", "2", "등", ""));
        exerciseArrayList.add(new Exercise("push_up", "2", "등", ""));
        exerciseArrayList.add(new Exercise("push_up", "3", "허리", ""));
        exerciseArrayList.add(new Exercise("push_up", "3", "허리", ""));
        exerciseArrayList.add(new Exercise("push_up", "4", "복부", ""));
        exerciseArrayList.add(new Exercise("push_up", "4", "복부", ""));
        exerciseArrayList.add(new Exercise("push_up", "5", "허벅지", ""));
        exerciseArrayList.add(new Exercise("push_up", "5", "허벅지", ""));

        callback.onResultData(Observable.just(exerciseArrayList));
    }

    @Override
    public void insertSmartBandData(String dbName, String time, double value) {

    }

    @Override
    public void insertSmartBandData(String dbName, String time, String value) {

    }

    @Override
    public void insertSmartBandAddress(String mDeviceAddress) {

    }

    @Override
    public void getSmartBandAddress(FBCallback<Observable<String>> callback) {

    }

    @Override
    public Observable<DatabaseReference> fGet(String databaseName, String uuid) {

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
}
