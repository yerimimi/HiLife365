package com.hilifecare.ui.program;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerProgramConstructionComponent;
import com.hilifecare.di.components.ProgramConstructionComponent;
import com.hilifecare.di.modules.ProgramConstructionModule;
import com.hilifecare.model.Program;
import com.hilifecare.ui.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class ProgramConstructionActivity extends BaseActivity<ProgramConstructionPresenter> implements ProgramConstructionView, HasComponent<ProgramConstructionComponent> {

    @Inject
    ProgramConstructionPresenter programConstructionPresenter;

    ProgramConstructionComponent programConstructionComponent;

    @Bind(R.id.construction_listview)
    ExpandableListView construction_listview;

    ArrayList<Program> constructionArrayList;

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;

    protected void injectModule() {
        programConstructionComponent = DaggerProgramConstructionComponent.builder().applicationComponent(App.get(this).getComponent()).programConstructionModule(new ProgramConstructionModule(this)).build();
        programConstructionComponent.inject(this);
    }

    public PresenterFactory<ProgramConstructionPresenter> getPresenterFactory() {
        return () -> programConstructionPresenter;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        constructionArrayList = (ArrayList<Program>)intent.getSerializableExtra("constructioninfo");

        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<ArrayList<String>>();
        mChildListContent = new ArrayList<String>();

        for(int i =0; i<constructionArrayList.size(); i++){
            if(Integer.parseInt(constructionArrayList.get(i).getStep()) == 1){
                mGroupList.add(constructionArrayList.get(i).getType());
            }
            mChildListContent.add(constructionArrayList.get(i).getExercise_name());
        }

        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);

        //construction_listview.setAdapter(new ProgramConstructionAdapter(this, mGroupList, mChildList));
    }

    @OnClick(R.id.construction_cancel)
    public void onClicl(View v){
        switch(v.getId()){
            case R.id.construction_cancel:
                finish();
                break;
        }
    }

    protected int getLayoutResource() {
        return R.layout.activity_program_construction;
    }

    @Override
    public ProgramConstructionComponent getComponent() {
        return programConstructionComponent;
    }



}
