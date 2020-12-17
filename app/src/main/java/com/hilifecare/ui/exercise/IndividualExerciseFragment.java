package com.hilifecare.ui.exercise;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.hilifecare.R;
import com.hilifecare.model.Exercise;
import com.hilifecare.ui.base.BaseFragment;
import com.hilifecare.ui.view.SpinnerAdapter;
import com.hilifecare.util.logging.ScreenStopwatch;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017-02-20.
 */

public class IndividualExerciseFragment extends BaseFragment<IndividualExercisePresenter> implements IndividualExerciseView, AdapterView.OnItemSelectedListener{

    @Bind(R.id.level_container)
    RadioGroup levelGroup;
    @Bind(R.id.program_selector)
    Spinner programSelector;
    SpinnerAdapter spinnerAdapter;
    @Bind(R.id.individual_program_list)
    RecyclerView recyclerView;
    IndividualExcerciseAdapter adapter;
    private ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
    private ArrayList<Exercise> exerciseArrayListPart = new ArrayList<>();
    private ArrayList<Exercise> exerciseArrayListProps = new ArrayList<>();
    private ArrayList<Exercise> exerciseArrayListFunc = new ArrayList<>();

    List<String> dataPart = new ArrayList<>();
    List<String> dataProps = new ArrayList<>();
    List<String> dataFunc = new ArrayList<>();

    public IndividualExerciseFragment() {
    }

    @Override
    protected void inject() {
//        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public void init() {
        super.init();
        Bundle bundle = getArguments();
        exerciseArrayList = (ArrayList<Exercise>) bundle.getSerializable("exerciselist");

        dataPart.add("전체");
        dataProps.add("전체");
        dataFunc.add("전체");

        for(Exercise exercise : exerciseArrayList) {
            int addFlag = 0;
            switch (exercise.getCategory()) {
                case "부위별":
                    exerciseArrayListPart.add(exercise);
                    for(String data : dataPart) {
                        if(exercise.getCategory_value().equals(data)) { addFlag=0;  break; }
                        addFlag = 1;
                    }
                    if(addFlag == 1 || dataPart.size() == 0)
                        dataPart.add(exercise.getCategory_value());
                    break;
                case "소도구별":
                    exerciseArrayListProps.add(exercise);
                    for(String data : dataProps) {
                        if(exercise.getCategory_value().equals(data)) { addFlag=0;  break; }
                        addFlag = 1;
                    }
                    if(addFlag == 1 || dataProps.size() == 0)
                        dataProps.add(exercise.getCategory_value());
                    break;
                case "기능성별":
                    exerciseArrayListFunc.add(exercise);
                    for(String data : dataFunc) {
                        if(exercise.getCategory_value().equals(data)) { addFlag=0;  break; }
                        addFlag = 1;
                    }
                    if(addFlag == 1 || dataFunc.size() == 0)
                        dataFunc.add(exercise.getCategory_value());
                    break;
            }
        }

        spinnerAdapter = new SpinnerAdapter(getContext(), dataPart);
        programSelector.setAdapter(spinnerAdapter);
        programSelector.setOnItemSelectedListener(this);
        levelGroup.check(R.id.level_part);
        levelGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.level_part:
                    spinnerAdapter = new SpinnerAdapter(getContext(), dataPart);
                    programSelector.setAdapter(spinnerAdapter);
                    updateIndividualExcerciseList(exerciseArrayListPart);
                    break;
                case R.id.level_props:
                    spinnerAdapter = new SpinnerAdapter(getContext(), dataProps);
                    programSelector.setAdapter(spinnerAdapter);
                    updateIndividualExcerciseList(exerciseArrayListProps);
                    break;
                case R.id.level_function:
                    spinnerAdapter = new SpinnerAdapter(getContext(), dataFunc);
                    programSelector.setAdapter(spinnerAdapter);
                    updateIndividualExcerciseList(exerciseArrayListFunc);
                    break;
            }
        });
        updateIndividualExcerciseList(exerciseArrayListFunc);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_exercise_program;
    }

    @Override
    public void updateIndividualExcerciseList(ArrayList<Exercise> exerciseArrayList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IndividualExcerciseAdapter(getContext(), exerciseArrayList, exercise -> {
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
        String category = ((RadioButton)levelGroup.findViewById(levelGroup.getCheckedRadioButtonId())).getText().toString();
        String category_value = parent.getSelectedItem().toString();

        switch (category) {
            case "부위별":
                for(Exercise exercise : exerciseArrayListPart) {
                    if(category_value.equals("전체")) {
                        exerciseArrayList.add(exercise);
                    } else {
                        if (exercise.getCategory_value().equals(category_value))
                            exerciseArrayList.add(exercise);
                    }
                }
                updateIndividualExcerciseList(exerciseArrayList);
                break;
            case "소도구별":
                for(Exercise exercise : exerciseArrayListProps) {
                    if(category_value.equals("전체")) {
                        exerciseArrayList.add(exercise);
                    } else {
                        if (exercise.getCategory_value().equals(category_value))
                            exerciseArrayList.add(exercise);
                    }
                }
                updateIndividualExcerciseList(exerciseArrayList);
                break;
            case "기능별":
                for(Exercise exercise : exerciseArrayListFunc) {
                    if(category_value.equals("전체")) {
                        exerciseArrayList.add(exercise);
                    } else {
                        if (exercise.getCategory_value().equals(category_value))
                            exerciseArrayList.add(exercise);
                    }
                }
                updateIndividualExcerciseList(exerciseArrayList);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        ScreenStopwatch.getInstance().printElapsedTimeLog(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause(){
        ScreenStopwatch.getInstance().printResetTimeLog(getClass().getSimpleName());
        super.onPause();
    }

}
