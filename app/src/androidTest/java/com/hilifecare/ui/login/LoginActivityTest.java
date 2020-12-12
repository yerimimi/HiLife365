package com.hilifecare.ui.login;

import android.os.SystemClock;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hilifecare.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by imcreator on 2017. 4. 12..
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public final ActivityTestRule<LoginActivity> login = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void shouldLoginSuccess(){
        onView(withId(R.id.email_edittext)).perform(typeText("tester@test.com"));
        onView(withId(R.id.pw_edittext)).perform(typeText("abcdefg"));
        onView(withId(R.id.activity_login)).perform(click());
    }

    @Test
    public void shouldShowNoIdPopup(){
        onView(withId(R.id.email_edittext)).perform(typeText("wrongtester@test.com"));
        onView(withId(R.id.pw_edittext)).perform(typeText("abcdefg"));
        onView(withId(R.id.activity_login)).perform(click());
        SystemClock.sleep(1000);
        onView(withText("아이디가 존재하지 않습니다.")).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void shouldShowWrongPasswordPopup(){
        onView(withId(R.id.email_edittext)).perform(typeText("tester@test.com"));
        onView(withId(R.id.pw_edittext)).perform(typeText("abcdefghi"));
        onView(withId(R.id.activity_login)).perform(click());
        SystemClock.sleep(1000);
        onView(withText("비밀번호가 잘못되었습니다.")).check(ViewAssertions.matches(isDisplayed()));
    }
}