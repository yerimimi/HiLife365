package com.hilifecare.domain.interactors.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hilifecare.domain.executors.ThreadExecutor;
import com.hilifecare.domain.interactors.base.BaseInteractor;
import com.hilifecare.domain.repository.firebase.FirebaseRepository;
import com.hilifecare.model.Exercise;
import com.hilifecare.model.LevelTest;
import com.hilifecare.model.Plan;
import com.hilifecare.model.PreviewProgram;
import com.hilifecare.model.Program;
import com.hilifecare.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import timber.log.Timber;

public class FirebaseInteractorImpl extends BaseInteractor implements FirebaseInteractor {
    FirebaseRepository firebaseRepository;
    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public FirebaseInteractorImpl(FirebaseRepository firebaseRepository, ThreadExecutor executor) {
        super(executor);
        this.firebaseRepository = firebaseRepository;
    }



    public Observable<Object> invoke() {
        return Observable.just(null);
    }

    @Override
    public void insertUserDefaultInfo() {
        Timber.d("insert user default info");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member").child(id);
        ref.child("name").setValue("");
        ref.child("age").setValue("2017/07/19");
        ref.child("gender").setValue("male");
        ref.child("tall").setValue(0);
        ref.child("weight").setValue(0);
        ref.child("fat").setValue(0);
        ref.child("muscle").setValue(0);
        ref.child("my_grade").setValue(0);
    }

    @Override
    public void getUserInfo(FBCallback<Observable<UserInfo>> callback) {
        Timber.d("get user info");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member");
        ref.child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfo userInfo;
                        try {
                            String name = (String) dataSnapshot.child("name").getValue();
                            String age = (String) dataSnapshot.child("age").getValue();
                            String gender = (String) dataSnapshot.child("gender").getValue();
                            int tall = Integer.parseInt((String) dataSnapshot.child("tall").getValue().toString());
                            int weight = Integer.parseInt((String) dataSnapshot.child("weight").getValue().toString());
                            int fat = Integer.parseInt((String) dataSnapshot.child("fat").getValue().toString());
                            int muscle = Integer.parseInt((String) dataSnapshot.child("muscle").getValue().toString());
                            int grade = Integer.parseInt((String) dataSnapshot.child("my_grade").getValue().toString());
                            userInfo = new UserInfo(name, age, gender, tall, weight, fat, muscle, grade);
                        } catch (Exception e) {
                            insertUserDefaultInfo();
                            userInfo = new UserInfo("", "1990/09/28", "male", 0, 0, 0, 0, 0);
                        }

                        callback.onResultData(Observable.just(userInfo));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e("set user info: "+ databaseError.getMessage());
                        callback.onResultData(null);
                    }
                }
        );
    }

    @Override
    public void insertUserDatailInfo(UserInfo userInfo) {
        Timber.d("insert user detail info");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member").child(id);
        ref.child("name").setValue(userInfo.getName());
        ref.child("age").setValue(userInfo.getAge());
        ref.child("gender").setValue(userInfo.getGender());
        ref.child("tall").setValue(userInfo.getTall());
        ref.child("weight").setValue(userInfo.getWeight());
        ref.child("fat").setValue(userInfo.getFat());
        ref.child("muscle").setValue(userInfo.getMuscle());
    }

    @Override
    public void getLevelTest(String levelOfDifficulty, FBCallback<Observable<ArrayList<LevelTest>>> callback) {
        Timber.d("get level test: "+levelOfDifficulty);
        ArrayList<LevelTest> levelTestArrayList = new ArrayList<LevelTest>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("level_test");
        ref.child(levelOfDifficulty).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ltSnapshot : dataSnapshot.getChildren()) {
                            try {
                                ArrayList<Integer> guideline = new ArrayList<>();
                                String name = (String) ltSnapshot.child("exercise_name").getValue();
                                String descriptions = (String) ltSnapshot.child("descriptions").getValue();
                                String image = (String) ltSnapshot.child("image").getValue();
                                String repeat_video = (String) ltSnapshot.child("repeat_video").getValue();
                                int measure_sec = Integer.parseInt((String) ltSnapshot.child("measure_secs").getValue().toString());
                                for(DataSnapshot ltSnapshot1 : ltSnapshot.child("guide_line").getChildren()) {
                                    guideline.add(Integer.parseInt(ltSnapshot1.child("value").getValue().toString()));
                                }
                                levelTestArrayList.add(new LevelTest(name, descriptions, measure_sec, guideline, image, repeat_video));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onResultData(Observable.just(levelTestArrayList));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e("set level test: "+databaseError.getMessage());
                        callback.onResultData(null);
                    }
                }
        );
    }

    @Override
    public void getRecommendPlan(FBCallback<Observable<Plan>> callback) {
        Timber.d("get recommend plan");
        final Plan[] plan = new Plan[1];
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("plan");
        ref.child("-KrFR3C64zI1rud6ro-9").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = (String) dataSnapshot.getKey();
                        String grade = (String) dataSnapshot.child("grade").getValue().toString();
                        String name = (String) dataSnapshot.child("name").getValue().toString();
                        String image = (String) dataSnapshot.child("image").getValue().toString();
                        String explaination = (String) dataSnapshot.child("explaination").getValue().toString();
                        String disclosure = (String) dataSnapshot.child("disclosure").getValue().toString();
                        plan[0] = new Plan(key, grade, name, image, explaination, disclosure);
                        callback.onResultData(Observable.just(plan[0]));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public void getPlanList(FBCallback<Observable<ArrayList<Plan>>> callback) {
        Timber.d("get plan list");
        ArrayList<Plan> planArrayList = new ArrayList<Plan>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("schdule");
        ref.child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ltSnapshot : dataSnapshot.getChildren()) {
                            String key = (String) ltSnapshot.getKey();
                            String grade = (String) ltSnapshot.child("grade").getValue().toString();
                            String name = (String) ltSnapshot.child("name").getValue().toString();
                            String image = (String) ltSnapshot.child("image").getValue().toString();
                            String explaination = (String) ltSnapshot.child("explaination").getValue().toString();
                            String disclosure = (String) ltSnapshot.child("disclosure").getValue().toString();
                            planArrayList.add(new Plan(key, grade, name, image, explaination, disclosure));
                        }
                        callback.onResultData(Observable.just(planArrayList));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e("set plan: "+databaseError.getMessage());
                        callback.onResultData(null);
                    }
                }
        );

    }

    @Override
    public void getPreviewProgramList(Plan plan, FBCallback<Observable<ArrayList<PreviewProgram>>> callback) {

    }

    @Override
    public void getPreviewProgram(PreviewProgram previewProgram, FBCallback<Observable<ArrayList<Program>>> callback) {

    }

    @Override
    public void getProgramList(Plan plan, FBCallback<Observable<ArrayList<Program>>> callback) {
        Timber.d("get program list");
        ArrayList<Program> programArrayList = new ArrayList<Program>();
        if(plan.getPlan_id().equals("-KrFR3C64zI1rud6ro-9")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("plan");
            ref.child("-KrFR3C64zI1rud6ro-9").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ltSnapshot1 : dataSnapshot.child("program").getChildren()) {
                                String schdule_id = dataSnapshot.getKey();
                                int day = Integer.parseInt((String) ltSnapshot1.child("day").getValue().toString());
                                String explain = (String) ltSnapshot1.child("explain").getValue().toString();
                                String image = (String) ltSnapshot1.child("image").getValue().toString();
                                int week = Integer.parseInt((String) ltSnapshot1.child("step").getValue().toString());

                                //WarmUp 등록
                                for (DataSnapshot ltSnapshot : ltSnapshot1.child("warm_up").getChildren()) {
                                    String type = "warm_up";
                                    String step = ltSnapshot.getKey();
                                    String level = (String) ltSnapshot.child("level").getValue();
                                    String exercise_name = (String) ltSnapshot.child("name").getValue();
                                    String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                    int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                    programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                }
                                //Main 등록
                                for (DataSnapshot ltSnapshot : ltSnapshot1.child("main").getChildren()) {
                                    String type = "main";
                                    String step = ltSnapshot.getKey();
                                    String level = (String) ltSnapshot.child("level").getValue();
                                    String exercise_name = (String) ltSnapshot.child("name").getValue();
                                    String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                    int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                    programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                }
                                //WarmDown 등록
                                for (DataSnapshot ltSnapshot : ltSnapshot1.child("warm_down").getChildren()) {
                                    String type = "warm_down";
                                    String step = ltSnapshot.getKey();
                                    String level = (String) ltSnapshot.child("level").getValue();
                                    String exercise_name = (String) ltSnapshot.child("name").getValue();
                                    String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                    int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                    programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                }
                            }
                            callback.onResultData(Observable.just(programArrayList));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e("set program: " + databaseError.getMessage());
                            callback.onResultData(null);
                        }
                    }
            );
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member");
            ref.child(id).child("schdule").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ltSnapshot0 : dataSnapshot.getChildren()) {
                                if (ltSnapshot0.child("key").getValue().equals(plan.getPlan_id())) {
                                    for (DataSnapshot ltSnapshot1 : ltSnapshot0.child("program").getChildren()) {
                                        String schdule_id = ltSnapshot0.getKey();
                                        int day = Integer.parseInt((String) ltSnapshot1.child("day").getValue().toString());
                                        String explain = (String) ltSnapshot1.child("explain").getValue().toString();
                                        String image = (String) ltSnapshot1.child("image").getValue().toString();
                                        int week = Integer.parseInt((String) ltSnapshot1.child("step").getValue().toString());

                                        //WarmUp 등록
                                        for (DataSnapshot ltSnapshot : ltSnapshot1.child("warm_up").getChildren()) {
                                            String type = "warm_up";
                                            String step = ltSnapshot.getKey();
                                            String level = (String) ltSnapshot.child("level").getValue();
                                            String exercise_name = (String) ltSnapshot.child("name").getValue();
                                            String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                            int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                            programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                        }
                                        //Main 등록
                                        for (DataSnapshot ltSnapshot : ltSnapshot1.child("main").getChildren()) {
                                            String type = "main";
                                            String step = ltSnapshot.getKey();
                                            String level = (String) ltSnapshot.child("level").getValue();
                                            String exercise_name = (String) ltSnapshot.child("name").getValue();
                                            String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                            int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                            programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                        }
                                        //WarmDown 등록
                                        for (DataSnapshot ltSnapshot : ltSnapshot1.child("warm_down").getChildren()) {
                                            String type = "warm_down";
                                            String step = ltSnapshot.getKey();
                                            String level = (String) ltSnapshot.child("level").getValue();
                                            String exercise_name = (String) ltSnapshot.child("name").getValue();
                                            String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                            int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                            programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                        }
                                    }
                                }
                            }
                            callback.onResultData(Observable.just(programArrayList));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e("set program: " + databaseError.getMessage());
                            callback.onResultData(null);
                        }
                    }
            );
        }
    }

    @Override
    public void getProgram(Plan plan, FBCallback<Observable<ArrayList<Program>>> callback) {
        Timber.d("get program list");
        ArrayList<Program> programArrayList = new ArrayList<Program>();
        if(plan.getPlan_id().equals("-KrFR3C64zI1rud6ro-9")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("plan");
            ref.child("-KrFR3C64zI1rud6ro-9").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String schdule_id = dataSnapshot.getKey();
                            String progress = "0";
                            for (DataSnapshot ltSnapshot1 : dataSnapshot.child("program").getChildren()) {
                                if (ltSnapshot1.getKey().equals(progress)) {
                                    int day = Integer.parseInt((String) ltSnapshot1.child("day").getValue().toString());
                                    String explain = (String) ltSnapshot1.child("explain").getValue();
                                    String image = (String) ltSnapshot1.child("image").getValue();
                                    int week = Integer.parseInt((String) ltSnapshot1.child("step").getValue().toString());

                                    //WarmUp 등록
                                    for (DataSnapshot ltSnapshot : ltSnapshot1.child("warm_up").getChildren()) {
                                        String type = "warm_up";
                                        String step = ltSnapshot.getKey();
                                        String level = (String) ltSnapshot.child("level").getValue();
                                        String exercise_name = (String) ltSnapshot.child("name").getValue();
                                        String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                        int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                        programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                    }
                                    //Main 등록
                                    for (DataSnapshot ltSnapshot : ltSnapshot1.child("main").getChildren()) {
                                        String type = "main";
                                        String step = ltSnapshot.getKey();
                                        String level = (String) ltSnapshot.child("level").getValue();
                                        String exercise_name = (String) ltSnapshot.child("name").getValue();
                                        String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                        int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                        programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                    }
                                    //WarmDown 등록
                                    for (DataSnapshot ltSnapshot : ltSnapshot1.child("warm_down").getChildren()) {
                                        String type = "warm_down";
                                        String step = ltSnapshot.getKey();
                                        String level = (String) ltSnapshot.child("level").getValue();
                                        String exercise_name = (String) ltSnapshot.child("name").getValue();
                                        String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                        int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                        programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                    }
                                }
                            }
                            callback.onResultData(Observable.just(programArrayList));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e("set program: " + databaseError.getMessage());
                            callback.onResultData(null);
                        }
                    }
            );
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member");
            ref.child(id).child("schdule").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ltSnapshot0 : dataSnapshot.getChildren()) {
                                if (ltSnapshot0.child("key").getValue().equals(plan.getPlan_id())) {
                                    String schdule_id = ltSnapshot0.getKey();
                                    String progress = ltSnapshot0.child("progress").getValue().toString();
                                    for (DataSnapshot ltSnapshot1 : ltSnapshot0.child("program").getChildren()) {
                                        if (ltSnapshot1.getKey().equals(progress)) {
                                            int day = Integer.parseInt((String) ltSnapshot1.child("day").getValue().toString());
                                            String explain = (String) ltSnapshot1.child("explain").getValue();
                                            String image = (String) ltSnapshot1.child("image").getValue();
                                            int week = Integer.parseInt((String) ltSnapshot1.child("step").getValue().toString());

                                            //WarmUp 등록
                                            for (DataSnapshot ltSnapshot : ltSnapshot1.child("warm_up").getChildren()) {
                                                String type = "warm_up";
                                                String step = ltSnapshot.getKey();
                                                String level = (String) ltSnapshot.child("level").getValue();
                                                String exercise_name = (String) ltSnapshot.child("name").getValue();
                                                String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                                int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                                programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                            }
                                            //Main 등록
                                            for (DataSnapshot ltSnapshot : ltSnapshot1.child("main").getChildren()) {
                                                String type = "main";
                                                String step = ltSnapshot.getKey();
                                                String level = (String) ltSnapshot.child("level").getValue();
                                                String exercise_name = (String) ltSnapshot.child("name").getValue();
                                                String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                                int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                                programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                            }
                                            //WarmDown 등록
                                            for (DataSnapshot ltSnapshot : ltSnapshot1.child("warm_down").getChildren()) {
                                                String type = "warm_down";
                                                String step = ltSnapshot.getKey();
                                                String level = (String) ltSnapshot.child("level").getValue();
                                                String exercise_name = (String) ltSnapshot.child("name").getValue();
                                                String simple_expalin = (String) ltSnapshot.child("simple_explain").getValue();
                                                int play_time = Integer.parseInt((String) ltSnapshot.child("prescription").child("0").child("calculate_play_time").getValue().toString());

                                                programArrayList.add(new Program(schdule_id, day, explain, image, week, type, step, level, exercise_name, simple_expalin, play_time));
                                            }
                                        }
                                    }
                                }
                            }
                            callback.onResultData(Observable.just(programArrayList));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e("set program: " + databaseError.getMessage());
                            callback.onResultData(null);
                        }
                    }
            );
        }
    }

    @Override
    public void insetLevelTestResult(HashMap<String, String> userTestResultMap, String time) {
        Timber.d("inset LevelTest Result");
        Set<Map.Entry<String, String>> set = userTestResultMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member").child(id).child("level_test_history");
        ref.push().child("date").setValue(time);
        ref.orderByChild("date").equalTo(time).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        while(iterator.hasNext()) {
                            Map.Entry<String, String> entry = iterator.next();
                            ref.child(dataSnapshot.getChildren().iterator().next().getKey()).child(entry.getKey()).setValue(entry.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e("inset LevelTest Result: "+databaseError.getMessage());
                    }
                }
        );
    }

    @Override
    public void getExerciseList(FBCallback<Observable<ArrayList<Exercise>>> callback) {
        Timber.d("get Exercise List");
        ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
        ArrayList<String> place = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("exercise");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ltSnapshot : dataSnapshot.getChildren()) {
                            String id = ltSnapshot.getKey().toString();
                            String name = ltSnapshot.child("name").getValue().toString();
                            String level = ltSnapshot.child("level").getValue().toString();
                            String simple_explain = ltSnapshot.child("simple_explain").getValue().toString();
                            String category = ltSnapshot.child("category").getChildren().iterator().next().getKey();
                            String category_value = ltSnapshot.child("category").getChildren().iterator().next().getValue().toString();
                            String image = ltSnapshot.child("image").getValue().toString();
                            String video = ltSnapshot.child("video").getValue().toString();
                            String repeat_video = ltSnapshot.child("repeat_video").getValue().toString();
                            if(Integer.valueOf(ltSnapshot.child("place").child("gym").getValue().toString()) == 1)
                                place.add("gym");
                            if(Integer.valueOf(ltSnapshot.child("place").child("home").getValue().toString()) == 1)
                                place.add("home");
                            if(Integer.valueOf(ltSnapshot.child("place").child("outdoor").getValue().toString()) == 1)
                                place.add("outdoor");
                            if(Integer.valueOf(ltSnapshot.child("place").child("school").getValue().toString()) == 1)
                                place.add("school");
                            if(Integer.valueOf(ltSnapshot.child("place").child("workplace").getValue().toString()) == 1)
                                place.add("workplace");
                            exerciseArrayList.add(new Exercise(id, name, level, simple_explain, category, category_value, image, video, repeat_video, place));
                        }
                        callback.onResultData(Observable.just(exerciseArrayList));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e("get Exercise List: "+databaseError.getMessage());
                    }
                }
        );
    }

    @Override
    public void insertSmartBandData(String dbName, String time, double value) {
        Timber.d("inset SmartBand Data(Integer data)");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member").child(id).child("smart_band").child(dbName);
        ref.child(time).setValue(value);
    }

    @Override
    public void insertSmartBandData(String dbName, String time, String value) {
        Timber.d("inset SmartBand Data(String data)");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member").child(id).child("smart_band").child(dbName);
        ref.child(time).setValue(value);
    }

    @Override
    public void insertSmartBandAddress(String mDeviceAddress) {
        Timber.d("inset SmartBand Address");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member").child(id).child("smart_band");

        ref.child("address").setValue(mDeviceAddress);
    }

    @Override
    public void getSmartBandAddress(FBCallback<Observable<String>> callback) {
        Timber.d("get SmartBand Address");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member").child(id).child("smart_band");
        ref.child("address").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String mDeviceAddress = dataSnapshot.getValue().toString();
                            callback.onResultData(Observable.just(mDeviceAddress));
                        } catch (Exception e) {
                            callback.onResultData(Observable.just(null));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e("get SmartBand Address: " + databaseError.getMessage());
                    }
                }
        );
    }

    @Override
    public Observable<DatabaseReference> fGet(String databaseName, String uuid) {
        return firebaseRepository.fGet(databaseName,uuid);
    }

}
