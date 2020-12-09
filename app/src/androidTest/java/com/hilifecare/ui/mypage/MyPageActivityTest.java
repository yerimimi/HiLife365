package com.hilifecare.ui.mypage;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hilifecare.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by imcreator on 2017. 4. 12..
 */
@RunWith(AndroidJUnit4.class)
public class MyPageActivityTest {
    @Rule
    public final ActivityTestRule<MyPageActivity> mypage = new ActivityTestRule<>(MyPageActivity.class);

    @Test
    public void shouldBeShowMyPageTitleOnMainScreen(){
        onView(withText(R.string.mypage_title))
                .check(ViewAssertions.matches(isDisplayed()));
    }
}