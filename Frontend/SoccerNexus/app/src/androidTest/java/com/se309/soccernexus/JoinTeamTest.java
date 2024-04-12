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
public class JoinTeamTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void joinTeamTest() {
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

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.multiple_view_team_joinTeamBtn), withText("Join Team"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout4),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                10),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.join_team_profilePageButton), withContentDescription("Profile Page Button"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.profile_teamPageButton), withContentDescription("Team Page Button"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout4),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.multiple_view_team_joinTeamBtn), withText("Join Team"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout4),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                10),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.join_team_backBtn), withContentDescription("Back Button"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.multiple_view_team_joinTeamBtn), withText("Join Team"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout4),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                10),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.title2), withText("Join Team"),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText("Join Team")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.join_team_recyclerView),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.join_team_homePageButton), withContentDescription("Home Button"),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.join_team_teamPageButton), withContentDescription("Team Page Button"),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.join_team_profilePageButton), withContentDescription("Profile Page Button"),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.join_team_settingsPageButton), withContentDescription("Settings Page Button"),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));
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
