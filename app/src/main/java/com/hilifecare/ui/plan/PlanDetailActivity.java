package com.hilifecare.ui.plan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.google.firebase.auth.FirebaseAuth;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerPlanDetailComponent;
import com.hilifecare.di.components.PlanDetailComponent;
import com.hilifecare.di.modules.PlanDetailModule;
import com.hilifecare.model.Plan;
import com.hilifecare.model.Program;
import com.hilifecare.model.ReformProgram;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.exercise.ExerciseActivity;
import com.hilifecare.ui.program.ProgramAdapter;
import com.hilifecare.ui.view.CustomDialog;
import com.hilifecare.util.logging.Stopwatch;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
@DeepLink("hilife365://plandetail/{id}")
public class PlanDetailActivity extends BaseActivity<PlanDetailPresenter> implements PlanDetailView,
        HasComponent<PlanDetailComponent> {

    @Inject
    PlanDetailPresenter planDetailPresenter;

    PlanDetailComponent planDetailComponent;

    Context mContext;

    @Bind(R.id.toolbar_name)
    TextView toolbarName;
    @Bind(R.id.plan_image)
    ImageView plan_image;
    @Bind(R.id.plan_name)
    TextView plan_name;
    @Bind(R.id.plan_explain)
    TextView plan_explain;
    @Bind(R.id.schedule_recyclerview)
    RecyclerView schedule_recyclerview;
    @Bind(R.id.start_plan_button)
    Button start_plan_button;

    Plan plan;
    ProgramAdapter programAdapter;
    ProgressDialog pDialog;
    ArrayList<Program> programArrayList = new ArrayList<Program>();
    ArrayList<Plan> planArrayList = new ArrayList<>();

    Stopwatch stopwatch = new Stopwatch();


    protected void injectModule() {
        planDetailComponent = DaggerPlanDetailComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .planDetailModule(new PlanDetailModule(this))
                .build();
        planDetailComponent.inject(this);

    }

    public PresenterFactory<PlanDetailPresenter> getPresenterFactory() {
        return () -> planDetailPresenter;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarName.setText("나만의 운동처방");
        mContext = getApplicationContext();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        if(getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)){
            if(FirebaseAuth.getInstance().getCurrentUser().getProviders().size() > 0) {
                planDetailPresenter.initPlan(this);
            } else {
                Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        } else {
            Intent intent = getIntent();
            plan = (Plan) intent.getSerializableExtra("planinfo");
            setView();
        }
    }

    public void setView() {
        plan_name.setText(plan.getName());
        plan_explain.setText(plan.getExplain());

        schedule_recyclerview.setHasFixedSize(true);
        planDetailPresenter.setProgramList(plan, this);

        pDialog.dismiss();
    }

    protected int getLayoutResource() {
        return R.layout.activity_plan_detail;
    }

    @Override
    public PlanDetailComponent getComponent() {
        return planDetailComponent;
    }

    @OnClick(R.id.start_plan_button)
    public void onClick(View v){
        CustomDialog dialog = new CustomDialog(this,CustomDialog.TYPE_TWOBUTTON);
        dialog.setMessage(plan.getName() + "을" + "\n시작하시겠습니까?")
                .setCancelClickListener(view -> dialog.dismiss())
                .setOkClickListener(view -> {
                    //TODO: 스케줄 등록
                    Intent i = new Intent(getApplication(), ExerciseActivity.class);
                    i.putExtra("programinfo", programArrayList);
                    startActivity(i);
                    dialog.dismiss();
                }).showDialog();

    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    @Override
    public void getPlanDetailDataList(ArrayList<Program> data) {
        planDetailPresenter.setProgram(plan, this);
        ArrayList<ReformProgram> rData = reformProgram(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        schedule_recyclerview.setLayoutManager(layoutManager);
        programAdapter = new ProgramAdapter(getApplication(), rData, data);
        schedule_recyclerview.setAdapter(programAdapter);
        start_plan_button.setEnabled(true);
    }

    @Override
    public void getPlanDetailData(ArrayList<Program> data) {
        programArrayList = data;
    }

    @Override
    public ArrayList<ReformProgram> reformProgram(ArrayList<Program> data) {
        int day;
        int week;
        int pre_day = 0;
        int pre_week = 0;
        String explain = null;
        String image = null;
        int day_exercise_time = 0;
        int exercise_count = 0;
        ArrayList<ReformProgram> rData = new ArrayList<ReformProgram>();
        for(Program program : data){
            day = program.getDay();
            week = program.getWeek();
            if(day != pre_day) {
                //다음 일차
                rData.add(new ReformProgram(pre_day+1, explain, image, pre_week+1, day_exercise_time, exercise_count));
                pre_day = day;
                pre_week = week;
                day_exercise_time = 0;
                exercise_count = 1;
            } else {
                explain = program.getExplain();
                image = program.getImage();
                day_exercise_time += program.getPlay_tiem();
                exercise_count += 1;
            }
        }
        rData.add(new ReformProgram(pre_day+1, explain, image, pre_week+1, day_exercise_time, exercise_count));
        return rData;
    }

    @Override
    public void updatePlanList(ArrayList<Plan> planArrayList) {
        this.planArrayList = planArrayList;
        if(planArrayList.size() == 0) {
            Toast.makeText(this, "등록된 운동 처방이 없습니다. 플랜을 등록해주세요.", Toast.LENGTH_LONG).show();
        }

        plan = planArrayList.get(0);
        setView();
    }

    @Override
    protected void onStart() {
        stopwatch.printLog("PlanDetailActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopwatch.reset(); // 현재 화면이 없어질 때
    }
}
