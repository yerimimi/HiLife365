package com.hilifecare.ui.signup;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerSignUpComponent;
import com.hilifecare.di.components.SignUpComponent;
import com.hilifecare.di.modules.SignUpModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.login.LoginActivity;
import com.hilifecare.ui.view.CustomEditText;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class SignUpActivity extends BaseActivity<SignUpPresenter> implements SignUpView, HasComponent<SignUpComponent> {

    @Inject
    SignUpPresenter signUpPresenter;

    SignUpComponent signUpComponent;

    @Bind(R.id.toolbar_name)
    TextView toolbarName;
    @Bind(R.id.signup_button)
    Button signup_button;
//    @Bind(R.id.name_edittext)
//    CustomEditText name_edittext;
    @Bind(R.id.email_edittext)
    CustomEditText email_edittext;
    @Bind(R.id.pw_edittext)
    CustomEditText pw_edittext;


    protected void injectModule() {
        signUpComponent = DaggerSignUpComponent.builder().applicationComponent(App.get(this).getComponent()).signUpModule(new SignUpModule(this)).build();
        signUpComponent.inject(this);
    }

    public PresenterFactory<SignUpPresenter> getPresenterFactory() {
        return () -> signUpPresenter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init() {
        toolbarName.setText("회원가입");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorSidePrimary,null));
        }
    }

    protected int getLayoutResource() {
        return R.layout.activity_sign_up;
    }

    @Override
    public SignUpComponent getComponent() {
        return signUpComponent;
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.signup_button)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.signup_button:
                signUpPresenter.signUp(email_edittext.getText().toString(),
                        pw_edittext.getText().toString());
                break;
        }
    }

//    @Override
//    public String getName() {
//        return name_edittext.getText().toString();
//    }

    @Override
    public String getEmail() {
        return email_edittext.getText().toString();
    }

    @Override
    public String getPassword() {
        return pw_edittext.getText().toString();
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "회원가입 되셨습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onValidationError() {
        //TODO:
    }

    @Override
    public void onSignUpFail(String message) {
        Toast.makeText(SignUpActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }
}
