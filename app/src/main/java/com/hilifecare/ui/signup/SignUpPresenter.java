package com.hilifecare.ui.signup;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;
import timber.log.Timber;


@ActivityScope
public class SignUpPresenter extends BasePresenter<SignUpView> {
    private static final int SIGN_UP = 1;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Named("real")
    @Inject
    FirebaseInteractor interactor;

    @Inject
    public SignUpPresenter() {
        restartableLatestCache(SIGN_UP,
                new Func0<Observable<Object>>() {
                    @Override
                    public Observable<Object> call() {
                        return null;
                    }
                }, new Action2<SignUpView, Object>() {
                    @Override
                    public void call(SignUpView signUpView, Object o) {

                    }
                }, new Action2<SignUpView, Throwable>() {
                    @Override
                    public void call(SignUpView loginView, Throwable throwable) {

                    }
                });

        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        user.getProviders().size();
    }

    public void signUp(String email, String password){
        //TODO: sign up
        // Make sure form is valid
        if (!validateForm()) {
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(task -> {
                    Timber.d("linkWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Timber.w("Authentication failed.", task.getException());
                        if(task.getException().getMessage().equals("An internal error has occured. [ CREDENTIAL_TOO_OLD_LOGIN_AGAIN ]")){
                            System.out.println("재인증");
                            //로그인 한적이 오래 되었을 경우 재인증
                            AuthCredential credential1 = EmailAuthProvider
                                    .getCredential(email, password);

                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });

                        }
                        if(task.getException().getMessage().startsWith("The email address is badly formatted.")) {
                            getView().onSignUpFail("이메일 주소가 적합하지 않습니다.");
                        }
                        if(task.getException().getMessage().startsWith("An internal error has occured. [ WEAK_PASSWORD  ]")) {
                            getView().onSignUpFail("패스워드의 길이가 6자 이하입니다.");
                        }
                        if(task.getException().getMessage().startsWith("The email address is already in use by another account.")){
                            System.out.println("계정 연결됨");
                            getView().onSignUpFail("이메일 주소는 다른 계정에서 이미 사용 중입니다.");
                        }
                    }else{
//                        interactor.insertUserDefaultInfo();
                        getView().onSignUpSuccess();
                    }
                });
    }

    public boolean validateForm(){
        //TODO: validate signup form
        boolean valid = true;

        String email = getView().getEmail();
        if (TextUtils.isEmpty(email)) {
            getView().onSignUpFail("이메일을 입력해주세요.");
            valid = false;
        } else {
        }

        String password = getView().getPassword();
        if (TextUtils.isEmpty(password)) {
            getView().onSignUpFail("패스워드를 입력해주세요.");
            valid = false;
        } else {
        }

        return valid;
    }
}
