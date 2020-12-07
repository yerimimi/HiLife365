package com.hilifecare.ui.userdetailinfo;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerUserDetailInfoComponent;
import com.hilifecare.di.components.UserDetailInfoComponent;
import com.hilifecare.di.modules.UserDetailInfoModule;
import com.hilifecare.model.UserInfo;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.util.logging.ScreenStopwatch;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class UserDetailInfoActivity extends BaseActivity<UserDetailInfoPresenter> implements UserDetailInfoView, HasComponent<UserDetailInfoComponent> {

    @Inject
    UserDetailInfoPresenter userDetailInfoPresenter;

    UserDetailInfoComponent userDetailInfoComponent;

    @Bind(R.id.user_detail_container)
    RelativeLayout container;
    @Bind(R.id.name_edittext)
    EditText name_edittext;
    @Bind(R.id.year_edittext)
    EditText year_edittext;
    @Bind(R.id.month_edittext)
    EditText month_edittext;
    @Bind(R.id.day_edittext)
    EditText day_edittext;
    @Bind(R.id.tall_edittext)
    EditText tall_edittext;
    @Bind(R.id.weight_edittext)
    EditText weight_edittext;
    @Bind(R.id.muscle_edittext)
    EditText muscle_edittext;
    @Bind(R.id.fat_edittext)
    EditText fat_edittext;
    @Bind(R.id.male_radiobutton)
    RadioButton male_radiobutton;
    @Bind(R.id.female_radiobutton)
    RadioButton female_radiobutton;

    private PopupWindow popupWindow;
    private UserDetailQnAView qnaView;
    UserInfo userInfo = new UserInfo();

    protected void injectModule() {
        userDetailInfoComponent = DaggerUserDetailInfoComponent.builder().applicationComponent(App.get(this).getComponent()).userDetailInfoModule(new UserDetailInfoModule(this)).build();
        userDetailInfoComponent.inject(this);
        
    }
    
    public PresenterFactory<UserDetailInfoPresenter> getPresenterFactory() {
        return () -> userDetailInfoPresenter;
    }

    public void init() {
        userInfo = getUserInfo();
        setUserDetailInfo();
    }

    public void setUserDetailInfo() {
        name_edittext.setText(String.valueOf(userInfo.getName()));
        String birthday[] = userInfo.getAge().split("/");
        year_edittext.setText(birthday[0]);
        month_edittext.setText(birthday[1]);
        day_edittext.setText(birthday[2]);
        if(userInfo.getGender().equals("male"))
            male_radiobutton.setChecked(true);
        else
            female_radiobutton.setChecked(true);
        tall_edittext.setText(String.valueOf(userInfo.getTall()));
        weight_edittext.setText(String.valueOf(userInfo.getWeight()));
        muscle_edittext.setText(String.valueOf(userInfo.getMuscle()));
        fat_edittext.setText(String.valueOf(userInfo.getFat()));
    }

    @Override
    public void insertUserDetailInfo() {
        UserInfo userinfo = new UserInfo();
        userinfo.setName(name_edittext.getText().toString());
        userinfo.setAge(year_edittext.getText()+"/"+month_edittext.getText()+"/"+day_edittext.getText());
        if(male_radiobutton.isChecked())
            userinfo.setGender("male");
        else
            userinfo.setGender("female");
        userinfo.setTall(Integer.parseInt(tall_edittext.getText().toString()));
        userinfo.setWeight(Integer.parseInt(weight_edittext.getText().toString()));
        userinfo.setMuscle(Integer.parseInt(muscle_edittext.getText().toString()));
        userinfo.setFat(Integer.parseInt(fat_edittext.getText().toString()));

        userDetailInfoPresenter.insertUserDetailInfo(userinfo);
    }

    @OnClick({R.id.user_info_modify_button, R.id.user_info_illness_button})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.user_info_modify_button:
                insertUserDetailInfo();
                Toast.makeText(this, "정상적으로 수정되었습니다.", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.user_info_illness_button:
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("https://www.naver.com/");
                i.setData(u);
                startActivity(i);

//                showQandA();
                break;
        }
    }

    private void showQandA() {
        qnaView = new UserDetailQnAView(this, new UserDetailQnAView.QnACallback() {
            @Override
            public void onCancel() {
                container.removeView(qnaView);
                qnaView = null;
            }
        });
        container.addView(qnaView);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        View layout = inflater.inflate(R.layout.user_detail_qna,null);
////                (ViewGroup) findViewById(R.id.user_detail_container));
//        popupWindow = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//        popupWindow.showAtLocation(layout, Gravity.TOP, 0, 0);
//        popupWindow.setAnimationStyle(R.style.Animation);
//        popupWindow.update();
//
//        cancel = (ImageView) layout.findViewById(R.id.cancel);

    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    @Override
    public void onBackPressed() {
        if(qnaView != null){
            container.removeView(qnaView);
            qnaView = null;
        }else{
            super.onBackPressed();
        }
    }

    protected int getLayoutResource() {
        return R.layout.activity_user_detail_info;
    }

    @Override
    public UserDetailInfoComponent getComponent() {
        return userDetailInfoComponent;
    }

    @Override
    protected void onStart() {
        ScreenStopwatch.getInstance().printElapsedTimeLog("UserDetailInfoActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().reset(); // 현재 화면이 없어질 때
    }




}
