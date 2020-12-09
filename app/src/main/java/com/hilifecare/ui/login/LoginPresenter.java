package com.hilifecare.ui.login;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.ui.base.BaseNonRxPresenter;

import javax.inject.Inject;

import timber.log.Timber;


@ActivityScope
public class LoginPresenter extends BaseNonRxPresenter<LoginView> {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email;
    private String password;

    @Override
    public void destroy() {
        super.destroy();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Inject
    public LoginPresenter() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Timber.d("onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Timber.d("onAuthStateChanged:signed_out");
            }
            // ...
        };
    }

    public void signIn(String email, String password){
        //TODO: sign in with email
        this.email = email;
        this.password = password;
        Timber.d("signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    Timber.d("signInWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        String message = task.getException().getMessage();
                        if(message.startsWith("The email address is badly formatted")){
                            message = "이메일 형식이 맞지 않습니다.";
                        }else if(message.startsWith("There is no user record corresponding")){
                            message = "아이디가 존재하지 않습니다.";
                        }else if(message.startsWith("The password is invalid or")){
                            message = "비밀번호가 잘못되었습니다.";
                        }else{
                            message = "로그인 형식이 맞지 않습니다.";
                        }
                        getView().onSignInFail(message);
                        Timber.w("signInWithEmail:failed", task.getException());
                    } else {
                        getView().onSignInSuccess();
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            getView().setEmailErrorText("Required.");
            valid = false;
        } else {
            getView().setEmailErrorText(null);
        }

        if (TextUtils.isEmpty(password)) {
            getView().setPasswordErrorText("Required.");
            valid = false;
        } else {
            getView().setPasswordErrorText(null);
        }

        return valid;
    }

    public void signInWithFacebook() {
        //TODO: sign in with facebook
    }

    public void signInWithGoogle() {
        //TODO: sign in with google
    }

}
