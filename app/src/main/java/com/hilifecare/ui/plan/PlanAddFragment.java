package com.hilifecare.ui.plan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.hilifecare.util.logging.ResponseStopwatch;
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

    List<HiExerciseInfo> eiList;
    PlanAddAdapter planAddAdapter = null;

    public PlanAddFragment() {
    }

    @OnClick({R.id.plan_add_button})
    void planAdd() {
        ResponseStopwatch.getInstance().reset();

        HiExerciseFilterInput filter = new HiExerciseFilterInput();
        HiUserInfo userInfo = new HiUserInfo();

        filter.placeGym = exercise_place_gym_checkbox.isChecked();
        filter.placeHome = exercise_place_home_checkbox.isChecked();
        filter.placeIndoor = exercise_place_inside_checkbox.isChecked();
        filter.placeOutdoor = exercise_place_outside_checkbox.isChecked();
        try{
            userInfo.sleepingMinutes = Integer.valueOf(sleeping_time_edittext.getText().toString());
        }catch (NumberFormatException e){
            userInfo.sleepingMinutes = Integer.valueOf(480); // TODO: temp
        }catch (Exception e){
            userInfo.sleepingMinutes = Integer.valueOf(480);
        }

        ResponseStopwatch.getInstance().reset();
        List<HiExercise> eList = HiExerciseDbForJavaCode.getInstance().getExerciseList();
        List<HiExerciseInfo> inputEiList = new ArrayList<HiExerciseInfo>();
        HiExerciseInfo tmpEi;
        for (HiExercise item : eList) {
            // 버티기 운동이 아닌 운동만 추출
            if (item.getPerformedNoMax() > 0) {
                tmpEi = HiExerciseDbForJavaCode.getInstance().toExerciseInfoFrom(item);
                inputEiList.add(tmpEi);
            }
        }

        int countToFilter = 6;
        eiList = recommend(inputEiList, filter, countToFilter, userInfo);
        ResponseStopwatch.getInstance().printElapsedTimeLog("ExercisePrescription");
        planAddAdapter.setEiList(eiList);
        planAddAdapter.notifyDataSetChanged();

        Log.d("eiList: ", eiList.toString());

        user_plan_add_listview.setOnItemClickListener(new PlanAddFragment.ListViewItemClickListener());
        setListViewHeightBasedOnChildren(user_plan_add_listview);
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
		filter = new HiExerciseFilterInput();

        // Recommend exercises
        List<HiExerciseInfo> suggestedList = suggestExercises(eiList, filter, countToFilter);

        // Adjust intensities
        HiExerciseIntensityAdjuster.adjustExerciseThreshold(suggestedList, userInfo);

        // Adjust tool weight
        HiExerciseIntensityAdjuster.adjustToolWeight(suggestedList, userInfo);

        // Reflect sleeping time
        HiExerciseIntensityAdjuster.reflectSleepingMinutes(suggestedList,480, 480);

        // Reflect age
        HiExerciseIntensityAdjuster.reflectUserAge(suggestedList, 30);

        return suggestedList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_plan_add, container, false);
        ButterKnife.bind(this, view);
        planAddAdapter = new PlanAddAdapter();
        user_plan_add_listview.setAdapter(planAddAdapter);
        user_plan_add_listview.setOnItemClickListener(new PlanAddFragment.ListViewItemClickListener());
        return view;
    }

    public void setListViewHeightBasedOnChildren(ListView eiList) {
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(eiList.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < planAddAdapter.getCount(); i++) {
            view = planAddAdapter.getView(i, view, eiList);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = eiList.getLayoutParams();
        params.height = totalHeight + (eiList.getDividerHeight() * (planAddAdapter.getCount() - 1));
    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        }
    }

}