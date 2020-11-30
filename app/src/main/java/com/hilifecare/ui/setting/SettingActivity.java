package com.hilifecare.ui.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerSettingComponent;
import com.hilifecare.di.components.SettingComponent;
import com.hilifecare.di.modules.SettingModule;
import com.hilifecare.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;
import com.hilifecare.util.logging.Stopwatch;


/**
 * Created by imcreator on 2017. 4. 17..
 */

public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingView, HasComponent<SettingComponent> {
    @Inject
    SettingPresenter settingPresenter;

    SettingComponent settingComponent;

    @Bind(R.id.toolbar_name)
    TextView toolbarName;
    @Bind(R.id.version)
    TextView version;
    Stopwatch stopwatch = new Stopwatch();

    @Override
    public SettingComponent getComponent() {return settingComponent;}

    @Override
    protected void injectModule() {
        settingComponent = DaggerSettingComponent.builder().applicationComponent(App.get(this).getComponent())
                .settingModule(new SettingModule(this)).build();
        settingComponent.inject(this);
    }

    @Override
    protected int getLayoutResource() {return R.layout.activity_setting;}

    @Override
    protected void init() {
        super.init();
        toolbarName.setText("설 정");
        version.setText("V "+getAppVersion());
    }

    public String getAppVersion() {

        // application version
        String versionName = "";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e.getMessage());
        }

        return versionName;
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    @OnClick(R.id.contract)
    void showContract(){
        //TODO: 이용약관 보여주기
    }

    @OnClick(R.id.privacy)
    void showPrivacy() {
        //TODO: 개인정보이용내역 보여주기
    }

    @Override
    protected void onStart() {
        stopwatch.printLog("SettingActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopwatch.reset(); // 현재 화면이 없어질 때
    }


}
