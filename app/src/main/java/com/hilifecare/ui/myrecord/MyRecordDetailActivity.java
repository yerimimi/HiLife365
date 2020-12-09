package com.hilifecare.ui.myrecord;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerMyRecordDetailComponent;
import com.hilifecare.di.components.MyRecordDetailComponent;
import com.hilifecare.di.modules.MyRecordDetailModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.util.logging.ScreenStopwatch;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class MyRecordDetailActivity extends BaseActivity<MyRecordDetailPresenter> implements MyRecordDetailView, HasComponent<MyRecordDetailComponent> {

    @Inject
    MyRecordDetailPresenter myRecordPresenter;

    MyRecordDetailComponent myRecordComponent;

    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
    @Bind(R.id.myrecord_detail_title)
    TextView recordTitle;
    @Bind(R.id.myrecord_detail_explain)
    TextView explain;
    @Bind(R.id.myrecord_detail_subtitle)
    TextView recordSubTitle;
    @Bind(R.id.myrecord_human_score)
    FrameLayout humanScoreView;
    @Bind(R.id.human_score_image)
    ImageView humanScoreImage;
    @Bind(R.id.human_score_value)
    TextView humanScore;
    @Bind(R.id.myrecord_progress_score)
    LinearLayout progressScoreView;
    @Bind(R.id.progress_score_text)
    TextView progressText;
    @Bind(R.id.progress_score_progress)
    ProgressBar progressScoreBar;
    @Bind(R.id.myrecord_detail_level)
    ImageView level;
    @Bind(R.id.myrecord_detail_percentage_text)
    TextView percentageText;
    @Bind(R.id.myrecord_detail_upper_percentage_text)
    TextView upperPercentageText;

    //draw percentageScope
    @Bind(R.id.percent_minus_3)
    View minus3;
    @Bind(R.id.percent_minus_2)
    View minus2;
    @Bind(R.id.percent_minus_1)
    View minus1;
    @Bind(R.id.percent_center)
    View center;
    @Bind(R.id.percent_plus_1)
    View plus1;
    @Bind(R.id.percent_plus_2)
    View plus2;
    @Bind(R.id.percent_plus_3)
    View plus3;

    protected void injectModule() {
        myRecordComponent = DaggerMyRecordDetailComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .myRecordDetailModule(new MyRecordDetailModule(this))
                .build();
        myRecordComponent.inject(this);
    }

    public PresenterFactory<MyRecordDetailPresenter> getPresenterFactory() {
        return () -> myRecordPresenter;
    }

    public void init() {
        toolbar.setName(getTitle().toString());
        toolbar.setShadowVisibility(View.VISIBLE);
        toolbar.setToolbarRightVisibility(View.INVISIBLE);

        String type = getIntent().getStringExtra("title");
        if(type != null) recordTitle.setText(type+" 상세 정보");
    }

    public void setExplain(String explain) {
        this.explain.setText(explain);
    }

    public void setRecordSubTitle(String recordSubTitle) {
        this.recordSubTitle.setText(recordSubTitle);
    }

    public void setHumanScoreImage(String type) {
        switch (type){
            case "복근":
                this.humanScoreImage.setImageResource(R.drawable.human_abdominal);
                break;
            case "상완근":
                this.humanScoreImage.setImageResource(R.drawable.human_brachial);
                break;
            case "허벅지 근육":
                this.humanScoreImage.setImageResource(R.drawable.human_thigh);
                break;
            case "종아리 근육":
                this.humanScoreImage.setImageResource(R.drawable.human_calf);
                break;
            case "가슴 근육":
                this.humanScoreImage.setImageResource(R.drawable.human_pectoralis);
                break;
            case "전완근":
                this.humanScoreImage.setImageResource(R.drawable.human_forearm);
                break;
            case "등근육":
                //TODO: 이미지 필요
                break;
        }
    }

    public void setHumanScore(String humanScore) {
        this.humanScore.setText(humanScore);
    }

    public void setProgressText(int progress) {
        this.progressText.setText(progress+"%");
        this.progressScoreBar.setMax(100);
        this.progressScoreBar.setProgress(progress);
    }

    public void setLevel(int level) {
        switch (level){
            case 1:
                this.level.setImageResource(R.drawable.icon_level1);
                break;
            case 2:
                this.level.setImageResource(R.drawable.icon_level2);
                break;
            case 3:
                this.level.setImageResource(R.drawable.icon_level3);
                break;
            case 4:
                this.level.setImageResource(R.drawable.icon_level4);
                break;
            case 5:
                this.level.setImageResource(R.drawable.icon_level5);
                break;
        }
    }

    public void setPercentageText(int percentage) {
        this.percentageText.setText("당신의 백분위는 "+percentage+"% 입니다.");
    }

    public void setUpperPercentageText(int upperPercentage) {
        this.upperPercentageText.setText("상위 "+upperPercentage+"%에 속합니다.");
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    protected int getLayoutResource() {
        return R.layout.activity_my_record_detail;
    }

    @Override
    public MyRecordDetailComponent getComponent() {
        return myRecordComponent;
    }

    @Override
    protected void onStart() {
        ScreenStopwatch.getInstance().printElapsedTimeLog("MyRecordDetailActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().reset(); // 현재 화면이 없어질 때
    }
}
