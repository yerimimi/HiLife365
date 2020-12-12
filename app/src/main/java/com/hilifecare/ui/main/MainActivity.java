package com.hilifecare.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerMainComponent;
import com.hilifecare.di.components.MainComponent;
import com.hilifecare.di.modules.MainModule;
import com.hilifecare.model.Exercise;
import com.hilifecare.model.Plan;
import com.hilifecare.model.UserInfo;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.exercise.IndividualExerciseFragment;
import com.hilifecare.ui.leveltest.LevelTestActivity;
import com.hilifecare.ui.login.LoginActivity;
import com.hilifecare.ui.mypage.MyPageActivity;
import com.hilifecare.ui.myrecord.MyRecordFragment;
import com.hilifecare.ui.plan.PlanAddFragment;
import com.hilifecare.ui.plan.PlanDetailActivity;
import com.hilifecare.ui.plan.PlanFragment;
import com.hilifecare.ui.setting.SettingActivity;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.util.logging.ScreenStopwatch;
import com.hyoil.hipreslogic.filter.HiExerciseFilter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class MainActivity extends BaseActivity<MainPresenter>
        implements MainView, HasComponent<MainComponent>, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    MainPresenter mainPresenter;

    MainComponent component;

    TextView user_name;
    TextView login_logout;
    TextView my_page;
    private ImageView setting;
    private ImageView cancel;

    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;
    private boolean isAnonymous = true;
    FirebaseUser user;
    ProgressDialog progressDialog;

    Fragment fragment = null;
    ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
    ArrayList<Plan> planArrayList = new ArrayList<>();

    protected void injectModule() {
        component = DaggerMainComponent.builder()
                .applicationComponent(((App) getApplication()).getComponent())
                .mainModule(new MainModule(this))
                .build();
        component.inject(this);
    }

    public PresenterFactory<MainPresenter> getPresenterFactory() {
        return () -> mainPresenter;
    }

    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public MainComponent getComponent() {
        return component;
    }

    public void init() {
        toolbar.setToolbarLeftVisibility(View.INVISIBLE);
        toolbar.setToolbarLeft(v -> goBack());
        toolbar.setToolbarRight(v -> {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        View headerview = navigationView.inflateHeaderView(R.layout.nav_header_main);

        user_name = (TextView) headerview.findViewById(R.id.user_name);
        login_logout = (TextView) headerview.findViewById(R.id.login_logout);
        my_page = (TextView) headerview.findViewById(R.id.my_page);
        setting = (ImageView) headerview.findViewById(R.id.setting);
        cancel = (ImageView) headerview.findViewById(R.id.cancel);

        login_logout.setOnClickListener(login_logout_click_listener);
        my_page.setOnClickListener(my_page_click_listener);
        setting.setOnClickListener(setting_click_listener);
        cancel.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.RIGHT);
        });

        navigationView.removeHeaderView(navigationView.getHeaderView(0));
        navigationView.setNavigationItemSelectedListener(this);

        mainPresenter.setUserInfo(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            if (user.getProviders().size() > 0) {
                isAnonymous = false;
            } else {
                isAnonymous = true;
            }
        } catch (Exception e) {
            isAnonymous = true;
        }

        if (isAnonymous) {
            user_name.setText("Guest");
            login_logout.setText("로그인");
        } else {
            user_name.setText(user.getEmail());
            login_logout.setText("로그아웃");
        }
    }


    @OnClick(R.id.toolbar_left)
    void goBack() {
        if (fragment instanceof MainFragment) {
            onBackPressed();
        } else {
            setFragment(new MainFragment(), "");
            goFragment(fragment);
        }
    }

    private View.OnClickListener setting_click_listener = v -> {
        if (isAnonymous) {

        } else {
            Intent i = new Intent(getApplicationContext(), SettingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }
    };

    private View.OnClickListener login_logout_click_listener = v -> {
        if (isAnonymous) {
            Intent i = new Intent(getApplication(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "로그아웃",
                    Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            onResume();
        }
    };

    private View.OnClickListener my_page_click_listener = v -> {
        if (isAnonymous) {

        } else {
            Intent i = new Intent(getApplicationContext(), MyPageActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int flag = 0;
        switch (item.getItemId()) {
            case R.id.plan_fragment:
                /*if (isAnonymous) {
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mainPresenter.initPlan(this);
                }
                */
                Intent i = new Intent(getApplicationContext(), LevelTestActivity.class);
                startActivity(i);
                break;
            case R.id.plan_recommend:
                setFragment(new PlanAddFragment(), "운동 처방");
                break;
            case R.id.exercise_program:
                setFragment(new PlanFragment(), "운동 프로그램");
                break;
            case R.id.basic_exercise_fragment:
                //db작업
                flag = 1;
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                setFragment(new IndividualExerciseFragment(), "운동 영상");
                mainPresenter.getExercise(this, progressDialog, fragment);
                break;
            case R.id.my_record_fragment:
                if (getmDeviceAddress() == null) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    mainPresenter.getSmartBandAddress(this, progressDialog);
                }
                setFragment(new MyRecordFragment(), "나의 기록");
                break;
        }

        if (fragment != null && flag == 0) {
            goFragment(fragment);
            return true;
        } else
            return false;
    }

    public void setFragment(Fragment fragment, String title) {
        this.fragment = fragment;
        if (fragment instanceof MainFragment) {
            toolbar.initView("blue");
            toolbar.setToolbarLeftVisibility(View.INVISIBLE);
            drawer.setBackground(this.getDrawable(R.drawable.bg_fragment_main));
        } else if (fragment instanceof MyRecordFragment) {
            toolbar.initView("orange");
            toolbar.setToolbarLeftVisibility(View.VISIBLE);
            drawer.setBackgroundColor(this.getResources().getColor(R.color.white));
        } else {
            toolbar.initView("purple");
            toolbar.setToolbarLeftVisibility(View.VISIBLE);
            drawer.setBackgroundColor(this.getResources().getColor(R.color.white));
        }
        toolbar.setName(title);
    }

    public void goFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();

        drawer.closeDrawer(Gravity.RIGHT);
    }

    public void goFragment() {
        if (fragment != null) goFragment(fragment);
    }

    @Override
    public void setUserName(String name) {
        user_name.setText(name);
    }

    @Override
    public void updatePlanList(ArrayList<Plan> planArrayList) {
        this.planArrayList = planArrayList;
        if (planArrayList.size() == 0) {
            Toast.makeText(this, "등록된 운동 처방이 없습니다. 플랜을 등록해주세요.", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(this, PlanDetailActivity.class);
            i.putExtra("planinfo", planArrayList.get(0));
            startActivity(i);
        }
    }

    @Override
    public void setSmartBandAddress(String mDeviceAddress) {
        setmDeviceAddress(mDeviceAddress);
    }

    @Override
    public void setIndividualExercise(ArrayList<Exercise> exerciseArrayList, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("exerciselist", exerciseArrayList);
        fragment.setArguments(bundle);
        goFragment(fragment);
    }

    public void setUserinfo(UserInfo userInfo) {
        setUserInfo(userInfo);

        setFragment(new MainFragment(), "");
        Bundle bundle = new Bundle();
        bundle.putSerializable("userinfo", getUserInfo());
        fragment.setArguments(bundle);
        goFragment(fragment);
    }

    @Override
    protected void onStart() {
        ScreenStopwatch.getInstance().printElapsedTimeLog("MainActivity"); // 다른 화면이 나타날 때
        HiExerciseFilter filterForTest = new HiExerciseFilter();
        Log.i("TEST", filterForTest.toString());
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().reset(); // 현재 화면이 없어질 때
    }
}
