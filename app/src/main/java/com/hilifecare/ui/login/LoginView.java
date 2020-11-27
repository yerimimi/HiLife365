package com.hilifecare.ui.login;

import com.hilifecare.ui.base.PresenterView;

public interface LoginView extends PresenterView {
    String getEmail();
    String getPassword();
    void onSignInSuccess();
    void onValidationError();
    void onSignInFail(String message);

    void setEmailErrorText(String text);

    void setPasswordErrorText(String text);
}
