package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import com.hilifecare.ui.userdetailinfo.UserDetailInfoActivity;

// android-hipster-needle-module-provides-import

@Module
public class UserDetailInfoModule extends ActivityModule {

    public UserDetailInfoModule(UserDetailInfoActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
