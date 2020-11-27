package com.hilifecare.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerSplashComponent;
import com.hilifecare.di.components.SplashComponent;
import com.hilifecare.di.modules.SplashModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.main.MainActivity;

import javax.inject.Inject;

import nucleus.factory.PresenterFactory;

@ActivityScope
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView, HasComponent<SplashComponent> {

    @Inject
    SplashPresenter splashPresenter;

    SplashComponent splashComponent;

    private static final String TAG = SplashActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void injectModule() {
        splashComponent = DaggerSplashComponent.builder().applicationComponent(App.get(this).getComponent()).splashModule(new SplashModule(this)).build();
        splashComponent.inject(this);

    }

    public PresenterFactory<SplashPresenter> getPresenterFactory() {
        return () -> splashPresenter;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        onFirebaseLoginOrRegister();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void onFirebaseLoginOrRegister(){
        // [START auth_state_listener]

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            System.out.println("로그인"+ user.getUid());
            goMainActivity();
        } else {
            // No user is signed in
            System.out.println("로그아웃 했음");
            signInAnonymously();
        }
    }

    private void signInAnonymously(){
        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.signInAnonymously()
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInAnonymously", task.getException());
                        Toast.makeText(SplashActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    }
                    goMainActivity();
                }
            });
        // [END signin_anonymously]
    }

    private void goMainActivity() {
        new Handler().post(() -> {
            overridePendingTransition(0, 0);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });
    }

    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashComponent getComponent() {
        return splashComponent;
    }

}
