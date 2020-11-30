package com.hilifecare.ui.login;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerLoginComponent;
import com.hilifecare.di.components.LoginComponent;
import com.hilifecare.di.modules.LoginModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.signup.SignUpActivity;
import com.hilifecare.ui.view.CustomDialog;
import com.hilifecare.util.logging.Stopwatch;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView, HasComponent<LoginComponent> {

    @Inject
    LoginPresenter loginPresenter;

    LoginComponent loginComponent;

    @Bind(R.id.email_edittext)
    EditText email_edittext;
    @Bind(R.id.pw_edittext)
    EditText pw_edittext;
    @Bind(R.id.login_button)
    Button login_button;
    @Bind(R.id.login_facebook_button)
    LinearLayout login_facebook_button;
    @Bind(R.id.button_facebook_login)
    LoginButton login_facebook_real_button;
    @Bind(R.id.login_signup)
    LinearLayout sign_up_button;
    @Bind(R.id.login_forgot)
    TextView login_forgot;
    @Bind(R.id.progress)
    ProgressBar progressBar;

    Stopwatch stopwatch = new Stopwatch();


    protected void injectModule() {
        loginComponent = DaggerLoginComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .loginModule(new LoginModule(this)).build();
        loginComponent.inject(this);
    }

    public PresenterFactory<LoginPresenter> getPresenterFactory() {
        return () -> loginPresenter;
    }

    protected int getLayoutResource() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        return R.layout.activity_login;
    }

    @Override
    public LoginComponent getComponent() {
        return loginComponent;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init() {
        super.init();
        IconicsDrawable iconEmail = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_email)
//                .color(getColor(R.color.colorPrimary))
                .sizeDp(24);
        IconicsDrawable iconLock = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_lock)
//                .color(getColor(R.color.colorPrimary))
                .sizeDp(24);
        email_edittext.setCompoundDrawables(iconEmail,null,null,null);
        pw_edittext.setCompoundDrawables(iconLock,null,null,null);
    }

    @OnClick({R.id.login_button, R.id.login_facebook_button, R.id.login_signup})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.login_button:
                progressBar.setVisibility(View.VISIBLE);
                loginPresenter.signIn(email_edittext.getText().toString(), pw_edittext.getText().toString());
                break;
            case R.id.login_facebook_button:
                loginPresenter.signInWithFacebook();
                break;
            case R.id.login_signup:
                Intent i = new Intent(getApplication(), SignUpActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                break;
        }
    }

    @Override
    public String getEmail() {
        return email_edittext.getText().toString();
    }

    @Override
    public String getPassword() {
        return pw_edittext.getText().toString();
    }

    @Override
    public void onSignInSuccess() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(LoginActivity.this, "로그인 성공",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onValidationError() {
        //TODO: validation error control
    }

    @Override
    public void onSignInFail(String message) {
        progressBar.setVisibility(View.GONE);
        CustomDialog dialog = new CustomDialog(this, CustomDialog.TYPE_ONEBUTTON);
        dialog.setMessage(message)
                .setOkClickListener(v -> {
                email_edittext.requestFocus();
                dialog.dismiss();
            })
            .showDialog();
        Toast.makeText(LoginActivity.this, "로그인 실패",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEmailErrorText(String text) {
        email_edittext.setError(text);
    }

    @Override
    public void setPasswordErrorText(String text) {
        pw_edittext.setError(text);
    }

    @Override
    protected void onStart() {
        stopwatch.printLog("LoginActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopwatch.reset(); // 현재 화면이 없어질 때
    }
}
