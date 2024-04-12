package com.se309.soccernexus;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.se309.soccernexus.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityScenarioRule<SignUpActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(SignUpActivity.class);

    @Test
    public void signUpActivityTest() {
        ViewInteraction textInputEditText = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.signup_emailInput),
0),
0),
isDisplayed()));
        textInputEditText.perform(replaceText("user51"), closeSoftKeyboard());
        
        ViewInteraction textInputEditText2 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.signup_usernameInput),
0),
0),
isDisplayed()));
        textInputEditText2.perform(replaceText("user51@gmail.com"), closeSoftKeyboard());
        
        ViewInteraction textInputEditText3 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.signup_passwordInput),
0),
0),
isDisplayed()));
        textInputEditText3.perform(replaceText("123"), closeSoftKeyboard());
        
        ViewInteraction textInputEditText4 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.signup_confirmPasswordInput),
0),
0),
isDisplayed()));
        textInputEditText4.perform(replaceText("123"), closeSoftKeyboard());
        
        ViewInteraction materialButton = onView(
allOf(withId(R.id.signUpButton), withText("Sign Up"),
childAtPosition(
allOf(withId(R.id.signup_constraintLayout),
childAtPosition(
withId(android.R.id.content),
0)),
3),
isDisplayed()));
        materialButton.perform(click());
        }
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
