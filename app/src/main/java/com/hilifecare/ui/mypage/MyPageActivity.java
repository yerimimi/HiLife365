package com.hilifecare.ui.mypage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.hilifecare.di.components.DaggerMyPageComponent;
import com.hilifecare.di.components.MyPageComponent;
import com.hilifecare.di.modules.MyPageModule;
import com.hilifecare.model.UserInfo;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.bluetooth.DeviceScanActivity;
import com.hilifecare.ui.userdetailinfo.UserDetailInfoActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class MyPageActivity extends BaseActivity<MyPagePresenter> implements MyPageView, HasComponent<MyPageComponent> {

    @Inject
    MyPagePresenter myPagePresenter;

    MyPageComponent myPageComponent;

    @Bind(R.id.toolbar_name)
    TextView toolbarName;
    @Bind(R.id.profile_image_container)
    ImageView profile_image;
    @Bind(R.id.profile_name)
    TextView profile_name;
    @Bind(R.id.profile_level)
    ImageView profile_level;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    ProgressDialog pDialog;
    UserInfo userInfo = new UserInfo();

    protected void injectModule() {
        myPageComponent = DaggerMyPageComponent.builder().applicationComponent(App.get(this).getComponent()).myPageModule(new MyPageModule(this)).build();
        myPageComponent.inject(this);
    }
    
    public PresenterFactory<MyPagePresenter> getPresenterFactory() {
        return () -> myPagePresenter;
    }

    protected int getLayoutResource() {
        return R.layout.activity_my_page;
    }

    @Override
    public MyPageComponent getComponent() {
        return myPageComponent;
    }

    @Override
    protected void init() {
        super.init();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        toolbarName.setText("마이페이지");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        setUserInfo();
    }

    @Override
    public void setUserInfo() {
        this.userInfo = getUserInfo();
        setUserImage(mUser.getPhotoUrl());
        setUserName(userInfo.getName());
        setUserLevel(userInfo.getGrade());

        pDialog.dismiss();
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    @OnClick({R.id.plan_reset_button, R.id.user_change_info_button, R.id.conn_bluetooth})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.plan_reset_button:
                break;
            case R.id.user_change_info_button:
                Intent i = new Intent(getApplicationContext(), UserDetailInfoActivity.class);
                startActivity(i);
                break;
            case R.id.conn_bluetooth:
                Intent j = new Intent(getApplicationContext(), DeviceScanActivity.class);
                startActivity(j);
                break;
        }
    }

    @OnClick(R.id.logout)
    public void logout(){
        //TODO: 로그아웃하시겠습니까?
//        myPagePresenter.signOut();
    }

    @Override
    public void setUserImage(Uri url) {
        //TODO: set user image from where?
//        Glide.with(this).fromUri().asBitmap().into(profile_image);
    }

    @Override
    public void setUserName(String name) {
        profile_name.setText(name+"님, 안녕하세요");
    }

    @Override
    public void setUserLevel(int level) {
        switch (level){
            case 1:
                profile_level.setImageResource(R.drawable.icon_level1);
                break;
            case 2:
                profile_level.setImageResource(R.drawable.icon_level2);
                break;
            case 3:
                profile_level.setImageResource(R.drawable.icon_level3);
                break;
            case 4:
                profile_level.setImageResource(R.drawable.icon_level4);
                break;
            case 5:
                profile_level.setImageResource(R.drawable.icon_level5);
                break;
        }
    }

    @Override
    public void onResetPlanComplete() {
        Toast.makeText(this, "플랜이 초기화되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignOutSuccess() {
        Toast.makeText(this, "로그아웃 완료하였습니다.", Toast.LENGTH_SHORT).show();
    }
}
