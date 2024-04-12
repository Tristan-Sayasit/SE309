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
public class ProfileViewTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void profileViewTest() {
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
                allOf(withId(R.id.profilePageButton), withContentDescription("Profile Page Button"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout3),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                12),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.profile_fullnameText), withText("Anton Labi"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.profile_usernameText), withText("@user5"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.profile_editProfileBtn), withContentDescription("Edit Profile Button"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.profile_profilePictureView),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.profile_characteristicsTitle), withText("Characteristics"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView4.check(matches(withText("Characteristics")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.profile_heightTitle), withText("Height"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView5.check(matches(withText("Height")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.profile_weightTitle), withText("Weight"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView6.check(matches(withText("Weight")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.profile_birthdayTitle), withText("Birthday"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView7.check(matches(withText("Birthday")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.profile_statisticsTitle), withText("Statistics"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView8.check(matches(withText("Statistics")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.profile_PositionTitle), withText("Preferred Position"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView9.check(matches(withText("Preferred Position")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.profile_goalsTitle), withText("Goals"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView10.check(matches(withText("Goals")));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.profile_assistsTitle), withText("Assists"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView11.check(matches(withText("Assists")));

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.profile_gamesPlayedTitle), withText("Games Played"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView12.check(matches(withText("Games Played")));

        ViewInteraction textView13 = onView(
                allOf(withId(R.id.profile_goalsGameTitle), withText("Goals Per Game"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView13.check(matches(withText("Goals Per Game")));

        ViewInteraction textView14 = onView(
                allOf(withId(R.id.profile_goalsGameTitle), withText("Goals Per Game"),
                        withParent(withParent(allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")))),
                        isDisplayed()));
        textView14.check(matches(withText("Goals Per Game")));
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
