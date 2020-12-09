package com.hilifecare.ui.signup;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hilifecare.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by imcreator on 2017. 4. 21..
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {
    @Rule
    public final ActivityTestRule<SignUpActivity> login = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void shouldGoBackToLoginActivity(){
        onView(withId(R.id.toolbar_left)).perform(ViewActions.click());
        onView(withText("LOGIN")).check(ViewAssertions.matches(isDisplayed()));
    }

}