package com.hilifecare.ui.exercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilifecare.R;
import com.hilifecare.ui.main.MainActivity;
import com.hilifecare.ui.plan.PlanFragment;
import com.hilifecare.util.logging.ScreenStopwatch;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017-02-20.
 */

public class BasicExerciseFragment extends Fragment{

    public BasicExerciseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basic_exercise, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.program_button)
    void goProgram(){
        ((MainActivity)getActivity()).setFragment(new PlanFragment(), "운동 프로그램");
        ((MainActivity)getActivity()).goFragment();
    }

    @OnClick(R.id.individual_program_button)
    void goIndividualProgram(){
        ((MainActivity)getActivity()).setFragment(new IndividualExerciseFragment(), "개별 운동영상");
        ((MainActivity)getActivity()).goFragment();
    }

    @Override
    public void onResume() {
        ScreenStopwatch.getInstance().printElapsedTimeLog(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause(){
       super.onPause();
       ScreenStopwatch.getInstance().printResetTimeLog(getClass().getSimpleName());
    }
}