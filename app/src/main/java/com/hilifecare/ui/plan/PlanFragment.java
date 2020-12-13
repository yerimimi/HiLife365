package com.hilifecare.ui.plan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.components.MainComponent;
import com.hilifecare.model.Plan;
import com.hilifecare.model.UserInfo;
import com.hilifecare.ui.base.BaseFragment;
import com.hilifecare.util.logging.ScreenStopwatch;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Administrator on 2017-02-20.
 */

@ActivityScope
public class PlanFragment extends BaseFragment<PlanPresenter> implements PlanView{
    PlanAdapter planAdapter;
    ArrayList<Plan> planArrayList;
    Plan recommendPlan;


    @Inject PlanPresenter planPresenter;

    @Bind(R.id.user_plan_listview)
    ListView user_plan_listview;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.user_name)
    TextView user_name;

    ProgressDialog pDialog;
    UserInfo userInfo;

    public PlanFragment() {
    }

    @Override
    public void init() {
        super.init();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        userInfo = getUserInfo();
        user_name.setText(userInfo.getName());

        planArrayList = new ArrayList<Plan>();
        planAdapter = new PlanAdapter(getActivity(), planArrayList);
        user_plan_listview.setAdapter(planAdapter);

        planPresenter.initRecommendPlan(this);
        planPresenter.initPlan(this);

        user_plan_listview.setOnItemClickListener(new PlanFragment.ListViewItemClickListener());
        setListViewHeightBasedOnChildren(user_plan_listview);
        scrollView.setVerticalScrollbarPosition(0);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < planAdapter.getCount(); i++) {
            view = planAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (planAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @OnClick(R.id.recommend_plan)
    public void setRecommend_plan() {
        Intent i = new Intent(getActivity(), PlanDetailActivity.class);
        i.putExtra("planinfo",recommendPlan);
        startActivity(i);
    }


    @Override
    protected void inject() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_plan;
    }

    @Override
    public void updateRecommendPlan(Plan recommendPlan) {
        this.recommendPlan = recommendPlan;
    }

    @Override
    public void updatePlanList(ArrayList<Plan> planArrayList) {
        Timber.d("update plan list");
        planAdapter.addAll(planArrayList);
        planAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(user_plan_listview);
        pDialog.dismiss();
    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            setPlanStartPopUp(position);
        }
    }

    private void setPlanStartPopUp(int position) {
        Intent i = new Intent(getActivity(), PlanDetailActivity.class);
        i.putExtra("planinfo",planArrayList.get(position));
        startActivity(i);
    }

    /*@OnClick(R.id.btn_plan_add)
    public void setBtn_plan_add() {
        Intent i = new Intent(getActivity().getApplicationContext(), PlanAddFragment.class);
        getActivity().startActivity(i);
    }
    */

    /*@OnClick(R.id.btn_plan_add)
    void goProgram(){
        Fragment fragment = new PlanAddFragment();
        ((MainActivity)getActivity()).setFragment(fragment, "플랜 추가하기");
        ((MainActivity)getActivity()).goFragment();
    }*/

    @Override
    public void onStart() {
        ScreenStopwatch.getInstance().printElapsedTimeLog("PlanFragment");
        super.onStart();
    }

    @Override
    public void onPause(){
        ScreenStopwatch.getInstance().reset();
        super.onPause();
    }
}