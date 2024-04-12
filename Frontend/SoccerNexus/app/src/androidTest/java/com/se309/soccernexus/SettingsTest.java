package com.se309.soccernexus;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void settingsTest() {
        ViewInteraction textInputEditText = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_usernameInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("user5"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_passwordInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.loginButton), withText("Login"),
                        childAtPosition(
                                allOf(withId(R.id.login_constraintLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.settingsPageButton), withContentDescription("Settings Page Button"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout3),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                13),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView3), withText("Settings"),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText("Settings")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textView4), withText("Change Email:"),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView2.check(matches(withText("Change Email:")));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.settings_emailInput),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.settings_saveEmailBtn), withText("SAVE EMAIL"),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textView5), withText("Change Password:"),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView3.check(matches(withText("Change Password:")));

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.settings_passwordInput),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.settings_savePasswordBtn), withText("SAVE PASSWORD"),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.settings_logOutBtn), withText("LOG OUT"),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.settings_logOutBtn), withText("LOG OUT"),
                        withParent(allOf(withId(R.id.linearLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction textInputEditText3 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.settings_emailInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("user5@gmail.com"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.settings_passwordInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.settings_saveEmailBtn), withText("Save Email"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout6),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.settings_savePasswordBtn), withText("Save Password"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout6),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.settings_logOutBtn), withText("Log Out"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout6),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        materialButton4.perform(click());
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
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
