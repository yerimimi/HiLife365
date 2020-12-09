package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import  com.hilifecare.ui.main.MainActivity;

@Module
public class MainModule extends ActivityModule {

    public MainModule(MainActivity activity) {
        super(activity);
      }

}
