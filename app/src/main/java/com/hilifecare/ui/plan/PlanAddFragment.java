package com.hilifecare.ui.plan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.hilifecare.R;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.components.MainComponent;
import com.hilifecare.model.HiExercise;
import com.hilifecare.ui.base.BaseFragment;
import com.hilifecare.util.data.HiExerciseDbForJavaCode;
import com.hilifecare.util.logging.ScreenStopwatch;
import com.hyoil.hipreslogic.filter.HiExerciseFilterInput;
import com.hyoil.hipreslogic.info.HiExerciseInfo;
import com.hyoil.hipreslogic.info.HiUserInfo;
import com.hyoil.hipreslogic.intensity.HiExerciseIntensityAdjuster;
import com.hyoil.hipreslogic.suggester.HiExerciseSuggester;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@ActivityScope
public class PlanAddFragment extends BaseFragment<PlanPresenter> {

    @Bind(R.id.user_plan_add_listview)
    ListView user_plan_add_listview;
    @Bind(R.id.plan_add_button)
    Button plan_add_button;
    @Bind(R.id.exercise_place_gym_checkbox)
    CheckBox exercise_place_gym_checkbox;
    @Bind(R.id.exercise_place_home_checkbox)
    CheckBox exercise_place_home_checkbox;
    @Bind(R.id.exercise_place_inside_checkbox)
    CheckBox exercise_place_inside_checkbox;
    @Bind(R.id.exercise_place_outside_checkbox)
    CheckBox exercise_place_outside_checkbox;
    @Bind(R.id.sleeping_time_edittext)
    EditText sleeping_time_edittext;

    List<HiExerciseInfo> eiList = new ArrayList<HiExerciseInfo>();
    //TODO: recommend List_ 어댑터로 넘겨줄때 list타입 어댑터들거나 함수에서 리턴받을떄 어레이리스트로 바꿔주기
    PlanProgramAdapter planProgramAdapter = null; // new PlanProgramAdapter(getContext(), eiList);

    public PlanAddFragment() {
    }

    @OnClick({R.id.plan_add_button})
    void planAdd() {
        HiExerciseFilterInput filter = new HiExerciseFilterInput();
        HiUserInfo userInfo = new HiUserInfo();

        filter.placeGym = exercise_place_gym_checkbox.isChecked();
        filter.placeHome = exercise_place_home_checkbox.isChecked();
        filter.placeIndoor = exercise_place_inside_checkbox.isChecked();
        filter.placeOutdoor = exercise_place_outside_checkbox.isChecked();
        userInfo.sleepingMinutes = Integer.valueOf(sleeping_time_edittext.getText().toString());
        Log.d("예림", "확인" + filter.placeGym + filter.placeOutdoor + userInfo.sleepingMinutes);

        List<HiExercise> eList = HiExerciseDbForJavaCode.getInstance().getExerciseList();
        List<HiExerciseInfo> inputEiList = new ArrayList<HiExerciseInfo>();
        HiExerciseInfo tmpEi;
        for (HiExercise item : eList) {
            tmpEi = HiExerciseDbForJavaCode.getInstance().toExerciseInfoFrom(item);
            inputEiList.add(tmpEi);
        }

        int countToFileter = 6;
        eiList = recommend(inputEiList, filter, countToFileter, userInfo);
        planProgramAdapter.notifyDataSetChanged();

        //TODO 1. List<HiExerciseInfo> 타입의 변수(x)를 선언한다.
        //TODO 2. 함수 리턴값으르 x에 저장한다.
        //TODO x를 보여줄 어댑터를 만든다
        //TODO 리사이클로 뷰 인가 리스트 뷰인가에 어댑터 달기
        //TODO 어댑터에 리스트 달기

//      filter.setBodyPart1("a");
//		filter.setBodyPart2("a");
//		filter.setFitness1("a");
//		filter.setFitness2("g");
//		filter.setPlaceGym(exercise_place_gym_checkbox.isChecked());
//		filter.setPlaceHome(exercise_place_home_checkbox.isChecked());
//		filter.setPlaceIndoor(exercise_place_inside_checkbox.isChecked());
//		filter.setPlaceOutdoor(exercise_place_outside_checkbox.isChecked());
//		filter.setMovementType(HiPresLogicConstants.HI_FILTER_MOVEMENT_TYPE_ALL);
//		filter.setEnduranceType(HiPresLogicConstants.HI_FILTER_ENDURANCE_TYPE_NON_ENDURANCE);
    }

    @Override
    protected void inject() {
        getComponent(MainComponent.class).inject(this);
    }

    protected int getLayoutResource() {
        return R.layout.fragment_plan_add;
    }


    @Override
    public void onStart() {;
        ScreenStopwatch.getInstance().printElapsedTimeLog("PlanAddFragment");
        planProgramAdapter = new PlanProgramAdapter(getContext(), eiList);
        super.onStart();
    }

    @Override
    public void onPause() {
        ScreenStopwatch.getInstance().reset();
        super.onPause();
    }

    private List<HiExerciseInfo> suggestExercises(List<HiExerciseInfo> eiList, HiExerciseFilterInput filter, int countToFilter) {
        // Recommend exercises
        List<HiExerciseInfo> suggestedList = HiExerciseSuggester.suggestExercises(eiList, filter, countToFilter);
        suggestedList = suggestedList != null ? suggestedList : new ArrayList<HiExerciseInfo>();
        return suggestedList;
    }

    public List<HiExerciseInfo> recommend(List<HiExerciseInfo> eiList, HiExerciseFilterInput filter, int countToFilter, HiUserInfo userInfo) {
//      BSON:BSON:TEST
		filter = new HiExerciseFilterInput();
//		filter.setBodyPart1("a");
//		//filter.setBodyPart2("a");
//		filter.setFitness1("a");
//		filter.setFitness2("g");
//		filter.setPlaceGym(true);
//		filter.setPlaceHome(true);
//		filter.setPlaceIndoor(true);
//		filter.setPlaceOutdoor(true);
//		filter.setMovementType(HiPresLogicConstants.HI_FILTER_MOVEMENT_TYPE_ALL);
//		filter.setEnduranceType(HiPresLogicConstants.HI_FILTER_ENDURANCE_TYPE_NON_ENDURANCE);

        // Recommend exercises
        List<HiExerciseInfo> suggestedList = suggestExercises(eiList, filter, countToFilter);

        // Adjust intensities
        HiExerciseIntensityAdjuster.adjustExerciseThreshold(suggestedList, userInfo);

        // Adjust tool weight
        HiExerciseIntensityAdjuster.adjustToolWeight(suggestedList, userInfo);

        return suggestedList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_add, container, false);
        ButterKnife.bind(this, view);
        user_plan_add_listview.setAdapter(planProgramAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}