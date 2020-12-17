package com.hilifecare.ui.leveltest;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.application.Constants;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.components.LevelTestComponent;
import com.hilifecare.model.LevelTest;
import com.hilifecare.ui.base.BaseFragment;
import com.hilifecare.util.logging.ScreenStopwatch;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.hilifecare.ui.leveltest.LevelTestActivity.LEVEL_TEST_PLAYING;

@ActivityScope
public class LevelTestListFragment extends BaseFragment<LevelTestListPresenter> implements LevelTestListView{

    @Inject
    LevelTestListPresenter levelTestListPresenter;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.level_test_time_min)
    TextView level_test_time_min;
    @Bind(R.id.level_test_time_sec)
    TextView level_test_time_sec;
    @Bind(R.id.leveltest_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.leveltest_guideline_button)
    Button leveltest_guideline_button;
    @Bind(R.id.leveltest_start_button)
    Button leveltest_start_button;
    private RecyclerView guideline_listview;

    LevelTestAdapter levelTestAdapter;
    LevelTestGuidelineAdapter levelTestGuidelineAdapter;
    ArrayList<LevelTest> levelTestArrayList = new ArrayList<LevelTest>();
    private PopupWindow popupWindow;
    private ImageView close_button;

    int level_test_total_time;
    String levelOfDifficulty;
    ProgressDialog pDialog;

    @Override
    public void init() {
        super.init();
        Timber.d("recyclerview"+recyclerView);
        Timber.d("guideline button "+leveltest_guideline_button);
        if(levelTestArrayList.size() == 0) choiceLevelTestPopup();
    }

    private void choiceLevelTestPopup() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        levelTestListPresenter.setFragment(this);
        levelOfDifficulty=Constants.DIFFICULTY_LEVEL_TEST_LOW;
        levelTestListPresenter.initLevelTest(levelOfDifficulty);
    }

    @Override
    public void getLevelTestList(ArrayList<LevelTest> levelTestHistory) {
        Timber.d("get level test data by array "+recyclerView);
        levelTestArrayList.addAll(levelTestHistory);
        levelTestAdapter = new LevelTestAdapter(getContext(), levelTestArrayList);
        recyclerView.setAdapter(levelTestAdapter);

        if(levelTestHistory != null) {
            for (LevelTest lt : levelTestHistory) {
                level_test_total_time += lt.getMeasure_secs();
            }
            level_test_time_min.setText(String.valueOf(level_test_total_time / 60));
            level_test_time_sec.setText(String.valueOf(level_test_total_time % 60));
        }

        if(pDialog != null) pDialog.dismiss();
    }

    @OnClick({R.id.leveltest_guideline_button, R.id.leveltest_start_button})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.leveltest_guideline_button:
                setGuideLinePopUp();
                break;
            case R.id.leveltest_start_button:
                onButtonPressed(LEVEL_TEST_PLAYING, levelTestArrayList);
                break;
        }
    }

    private void setGuideLinePopUp() {
        try{
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.level_test_guideline, (ViewGroup) getView().findViewById(R.id.leveltest_guideline_background));
            guideline_listview = (RecyclerView) layout.findViewById(R.id.guideline_listview);
            popupWindow = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            popupWindow.update();

            View container = (View) popupWindow.getContentView().getParent();
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0.7f;
            wm.updateViewLayout(container, p);

            levelTestGuidelineAdapter = new LevelTestGuidelineAdapter(getContext(), levelTestArrayList);
            guideline_listview.setAdapter(levelTestGuidelineAdapter);

            close_button = (ImageView)layout.findViewById(R.id.close_button);
            close_button.setOnClickListener(close_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener close_button_click_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            };

    public void onButtonPressed(int where, Object test) {
        if (mListener != null) {
            mListener.onFragmentInteraction(where, test);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    protected void inject() {

        getComponent(LevelTestComponent.class).inject(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_level_test_list;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int where, Object test);
    }

    @Override
    public void onResume() {
        ScreenStopwatch.getInstance().printElapsedTimeLog(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause(){

        ScreenStopwatch.getInstance().printResetTimeLog(getClass().getSimpleName());

        super.onPause();
    }
}
