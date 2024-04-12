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
public class MultipleViewTeamTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void multipleViewTeamTest() {
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
                allOf(withId(R.id.teamPageButton), withContentDescription("Team Page Button"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout3),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                11),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView2), withText("Your Teams"),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText("Your Teams")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.multipleTeamRecyclerView),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.multiple_view_team_createTeamBtn), withText("CREATE TEAM"),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.multiple_view_team_joinTeamBtn), withText("JOIN TEAM"),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.multiple_view_team_homePageButton2), withContentDescription("Home Button"),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.multiple_view_team_teamPageButton2), withContentDescription("Team Page Button"),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.multiple_view_team_profilePageButton2), withContentDescription("Profile Page Button"),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.multiple_view_team_settingsPageButton2), withContentDescription("Settings Page Button"),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));

        ViewInteraction view = onView(
                allOf(withId(R.id.homePgIconSelector5),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        view.check(matches(isDisplayed()));

        ViewInteraction view2 = onView(
                allOf(withId(R.id.homePgIconSelector5),
                        withParent(allOf(withId(R.id.linearLayout4),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        view2.check(matches(isDisplayed()));
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
