package com.hilifecare.di.modules;

import com.hilifecare.model.Program;
import com.hilifecare.ui.program.ProgramActivity;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

// android-hipster-needle-module-provides-import

@Module
public class ProgramModule extends ActivityModule {
    ArrayList<Program> program;
    ProgramActivity activity;

    public ProgramModule(ProgramActivity activity, ArrayList<Program> program) {
        super(activity);
        this.activity = activity;
        this.program = program;
    }

    @Provides ProgramActivity provideActivity(){
        return activity;
    }

    @Provides
    public ArrayList<Program> provideProgram(){
        return this.program;
    }

    // android-hipster-needle-module-provides-method

}
