package com.hilifecare.ui.preview;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerPreviewProgramComponent;
import com.hilifecare.di.components.PreviewProgramComponent;
import com.hilifecare.di.modules.PreviewProgramModule;
import com.hilifecare.model.Plan;
import com.hilifecare.model.PreviewProgram;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.view.CustomDialog;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.ui.view.PlayingExercisePreviewtView;
import com.hilifecare.util.logging.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

/**
 * Created by imcreator on 2017. 4. 28..
 */

@ActivityScope
public class PreviewProgramActivity extends BaseActivity<PreviewProgramPresenter>
        implements PreviewProgramView, HasComponent<PreviewProgramComponent> {
    @Inject
    PreviewProgramPresenter previewProgramPresenter;

    PreviewProgramComponent previewProgramComponent;

    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
    @Bind(R.id.preview_start_container)
    LinearLayout previewStartContainer;
    @Bind(R.id.playing_view)
    PlayingExercisePreviewtView playingExerciseView;

    Stopwatch stopwatch = new Stopwatch();


    ArrayList<PreviewProgram> previewProgramArrayList;

    @Override
    protected void injectModule() {
        previewProgramComponent = DaggerPreviewProgramComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .previewProgramModule(new PreviewProgramModule(this)).build();
        previewProgramComponent.inject(this);
    }

    @Override
    public PresenterFactory<PreviewProgramPresenter> getPresenterFactory() {
        return () -> previewProgramPresenter;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onWindowFocusChanged(true);
        toolbar.setToolbarRightVisibility(View.GONE);
        toolbar.setName("운동 미리보기");

        previewProgramArrayList = new ArrayList<PreviewProgram>();
        Plan plan = new Plan("0", "high", "2주 다이어트", "", "빡시게", "activity");
        ArrayList<PreviewProgram> previewProgramArrayList = new ArrayList<PreviewProgram>();
        previewProgramArrayList.add(new PreviewProgram(getUid(), plan.getName(), "preview_팔벌려 높이뛰기", 1, 1, "WarmUp"));
        previewProgramArrayList.add(new PreviewProgram(getUid(), plan.getName(), "preview_푸시업", 1, 1, "Main"));
        previewProgramArrayList.add(new PreviewProgram(getUid(), plan.getName(), "preview_윗몸일으키기", 1, 2, "Main"));
        previewProgramArrayList.add(new PreviewProgram(getUid(), plan.getName(), "preview_숨고르기", 1, 1, "WarmDown"));

        playingExerciseView.initView(previewProgramArrayList, new PlayingExercisePreviewtView.OnPlayingPopUpViewCallback() {
            @Override
            public void onStateExerciseEnd(HashMap<String, String> userTestResultMap) {

            }

            @Override
            public void onSetExerciseTitle(String title) {
                if("".equals(title)) return;
                toolbar.setName(title);
            }

            @Override
            public void onToggleOrientation() {
                if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(playingExerciseView != null) playingExerciseView.close();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_preview_program;
    }

    @Override
    public PreviewProgramComponent getComponent() {
        return previewProgramComponent;
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        //TODO: popup
        CustomDialog dialog = new CustomDialog(this,CustomDialog.TYPE_TWOBUTTON);
        dialog.setMessage(getString(R.string.previewprogram_stop_message))
                .setOkText("종료")
            .setOkClickListener(v -> {
                dialog.dismiss();
                finish();
            }).showDialog();
    }

    @OnClick(R.id.preview_start)
    void startProgram(){
        previewStartContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        stopwatch.printLog("PreviewProgramActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopwatch.reset(); // 현재 화면이 없어질 때
    }
}