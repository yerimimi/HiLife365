package com.hilifecare.ui.signup;

import com.hilifecare.ui.base.PresenterView;

public interface SignUpView extends PresenterView {
//    String getName();
    String getEmail();
    String getPassword();
    void onSignUpSuccess();
    void onValidationError();
    void onSignUpFail(String message);
}
