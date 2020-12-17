package com.hilifecare.ui.program;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerProgramComponent;
import com.hilifecare.di.components.ProgramComponent;
import com.hilifecare.di.modules.ProgramModule;
import com.hilifecare.model.Program;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.exercise.ExerciseActivity;
import com.hilifecare.util.logging.ScreenStopwatch;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class ProgramActivity extends BaseActivity<ProgramPresenter> implements ProgramView, HasComponent<ProgramComponent> {

    @Inject
    ProgramPresenter programPresenter;

    ProgramComponent programComponent;

    @Bind(R.id.toolbar_name)
    TextView toolbarName;
    @Bind(R.id.program_image)
    ImageView program_image;
    @Bind(R.id.program_name)
    TextView program_name;
    @Bind(R.id.program_explain)
    TextView program_explain;
    @Bind(R.id.program_time)
    TextView program_time;
    @Bind(R.id.program_construction_button)
    Button program_construction_button;
    @Bind(R.id.program_start_button)
    Button program_start_button;
    @Bind(R.id.program_construction_container)
    RelativeLayout constructionContainer;

    private PopupWindow popupWindow;
    private ExpandableListView construction_listview;
    private ImageView cancelButton;

    private HashMap<String, Integer> info_program = new HashMap<String, Integer>();
    private int init_flag;

    @Inject
    ArrayList<Program> program;

    protected void injectModule() {
        programComponent = DaggerProgramComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .programModule(new ProgramModule(this, (ArrayList<Program>) getIntent().getSerializableExtra("schduleinfo")))
                .build();
        programComponent.inject(this);

    }

    public PresenterFactory<ProgramPresenter> getPresenterFactory() {
        return () -> programPresenter;
    }

    protected int getLayoutResource() {
        return R.layout.activity_program;
    }

    @Override
    public ProgramComponent getComponent() {
        return programComponent;
    }

    @Override
    protected void init() {
        super.init();
        toolbarName.setText("추천 트레이닝 플랜");
        init_flag = 0;
        programPresenter.initView();
    }

    @OnClick({R.id.program_construction_button, R.id.program_start_button})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.program_construction_button:
                programPresenter.setConstructionPopUp();
                break;
            case R.id.program_start_button:
                programPresenter.setProgram();
                break;
        }
    }

    public void showConstructionPopUp(ArrayList<Program> programArrayList) {
        try{
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.activity_program_construction,
                    (ViewGroup) findViewById(R.id.program_construction_background));
            popupWindow = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            popupWindow.showAtLocation(layout, Gravity.TOP, 0, 0);
            popupWindow.setAnimationStyle(R.style.Animation);
            popupWindow.update();

            construction_listview = (ExpandableListView) layout.findViewById(R.id.construction_listview);
            cancelButton = (ImageView) layout.findViewById(R.id.construction_cancel);

            ArrayList<String> mGroupList = new ArrayList<String>();
            ArrayList<ArrayList<HashMap<String, Integer>>> mChildList = new ArrayList<>();
            ArrayList<HashMap<String, Integer>> mChildListContent = new ArrayList<>();

            for(int i =0; i<programArrayList.size(); i++){
                if(Integer.parseInt(programArrayList.get(i).getStep()) == 0){
                    if(init_flag == 1) {
                        mChildList.add(mChildListContent);
                        mChildListContent = new ArrayList<>();
                    }
                    mGroupList.add(programArrayList.get(i).getType());
                    init_flag = 1;
                }
                info_program.put(programArrayList.get(i).getExercise_name(), programArrayList.get(i).getPlay_tiem());
                mChildListContent.add(info_program);
                info_program = new HashMap<>();
            }

            mChildList.add(mChildListContent);

            construction_listview.setAdapter(new ProgramConstructionAdapter(layout.getContext(), mGroupList, mChildList));
            cancelButton.setOnClickListener(v -> popupWindow.dismiss());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startProgram(ArrayList<Program> programArrayList) {
        Intent i = new Intent(getApplication(), ExerciseActivity.class);
        i.putExtra("programinfo", programArrayList);
        startActivity(i);
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    @Override
    public void initView(ArrayList<Program> program) {
        int play_time = 0;
        //TODO: init preview program
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FBent%20Knee%20Push-up.PNG?alt=media&token=6ee61d9d-3eba-4763-ae28-b7bb63f83d4a")
                .into(program_image);
        program_name.setText((program.get(0).getWeek()+1) + " WEEK / DAY " + (program.get(0).getDay()+1));
        program_explain.setText(program.get(0).getExplain());
        for(Program p : program) {
            play_time += p.getPlay_tiem();
        }
        program_time.setText(String.valueOf(play_time/60));
    }

    @Override
    protected void onResume() {
        ScreenStopwatch.getInstance().printElapsedTimeLog(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().printResetTimeLog(getClass().getSimpleName());
    }
}
