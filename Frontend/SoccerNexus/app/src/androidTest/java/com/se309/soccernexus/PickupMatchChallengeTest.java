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
public class PickupMatchChallengeTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void pickupMatchChallengeTest() {
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
allOf(withId(R.id.teams_items_enterBtn), withText("Enter"),
childAtPosition(
allOf(withId(R.id.linearLayout5),
childAtPosition(
withId(R.id.multipleTeamRecyclerView),
0)),
1),
isDisplayed()));
        materialButton2.perform(click());
        
        ViewInteraction appCompatImageButton2 = onView(
allOf(withId(R.id.view_team_challengeBtn), withContentDescription("Challenge Button"),
childAtPosition(
allOf(withId(R.id.linearLayout3),
childAtPosition(
withId(android.R.id.content),
0)),
15),
isDisplayed()));
        appCompatImageButton2.perform(click());

        
        ViewInteraction textView = onView(
allOf(withId(R.id.pickup_matches_titleTxt), withText("Challenge"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView.check(matches(withText("Challenge")));
        
        ViewInteraction button = onView(
allOf(withId(R.id.pickup_requestsBtn), withText("REQUESTS"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction recyclerView = onView(
allOf(withId(R.id.pickup_recyclerView),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
        
        ViewInteraction recyclerView2 = onView(
allOf(withId(R.id.pickup_recyclerView),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));
        
        ViewInteraction materialButton3 = onView(
allOf(withId(R.id.teams_items_enterBtn), withText("Challenge"),
childAtPosition(
allOf(withId(R.id.linearLayout5),
childAtPosition(
withId(R.id.pickup_recyclerView),
0)),
1),
isDisplayed()));
        materialButton3.perform(click());
        
        ViewInteraction textView2 = onView(
allOf(withId(R.id.pickup_modalTitle), withText("Challenge Bears"),
withParent(allOf(withId(R.id.pickup_modalLayout),
withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
isDisplayed()));
        textView2.check(matches(withText("Challenge Bears")));
        
        ViewInteraction linearLayout = onView(
allOf(withId(R.id.pickup_locationInput),
withParent(allOf(withId(R.id.pickup_modalLayout),
withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
isDisplayed()));
        linearLayout.check(matches(isDisplayed()));
        
        ViewInteraction spinner = onView(
allOf(withId(R.id.pickup_formatSpinner),
withParent(allOf(withId(R.id.pickup_relativeLayout),
withParent(withId(R.id.pickup_modalLayout)))),
isDisplayed()));
        spinner.check(matches(isDisplayed()));
        
        ViewInteraction linearLayout2 = onView(
allOf(withId(R.id.pickup_dayInput),
withParent(allOf(withId(R.id.pickup_modalLayout),
withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));
        
        ViewInteraction linearLayout3 = onView(
allOf(withId(R.id.pickup_monthInput),
withParent(allOf(withId(R.id.pickup_modalLayout),
withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
isDisplayed()));
        linearLayout3.check(matches(isDisplayed()));
        
        ViewInteraction linearLayout4 = onView(
allOf(withId(R.id.pickup_yearInput),
withParent(allOf(withId(R.id.pickup_modalLayout),
withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
isDisplayed()));
        linearLayout4.check(matches(isDisplayed()));
        
        ViewInteraction linearLayout5 = onView(
allOf(withId(R.id.pickup_timeHourInput),
withParent(allOf(withId(R.id.pickup_modalLayout),
withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
isDisplayed()));
        linearLayout5.check(matches(isDisplayed()));
        
        ViewInteraction button2 = onView(
allOf(withId(R.id.pickup_challengeBtn), withText("CHALLENGE"),
withParent(allOf(withId(R.id.pickup_modalLayout),
withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
isDisplayed()));
        button2.check(matches(isDisplayed()));
        
        ViewInteraction button3 = onView(
allOf(withId(R.id.pickup_modalCancelBtn), withText("CANCEL"),
withParent(allOf(withId(R.id.pickup_modalLayout),
withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
isDisplayed()));
        button3.check(matches(isDisplayed()));
        
        ViewInteraction textInputEditText3 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.pickup_locationInput),
0),
0),
isDisplayed()));
        textInputEditText3.perform(replaceText("Ames, IA"), closeSoftKeyboard());
        
        ViewInteraction appCompatSpinner = onView(
allOf(withId(R.id.pickup_formatSpinner),
childAtPosition(
allOf(withId(R.id.pickup_relativeLayout),
childAtPosition(
withId(R.id.pickup_modalLayout),
6)),
0),
isDisplayed()));
        appCompatSpinner.perform(click());
        
        DataInteraction appCompatCheckedTextView = onData(anything())
.inAdapterView(childAtPosition(
withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
0))
.atPosition(1);
        appCompatCheckedTextView.perform(click());
        
        ViewInteraction textInputEditText4 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.pickup_dayInput),
0),
0),
isDisplayed()));
        textInputEditText4.perform(replaceText("10"), closeSoftKeyboard());
        
        ViewInteraction textInputEditText5 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.pickup_monthInput),
0),
0),
isDisplayed()));
        textInputEditText5.perform(replaceText("12"), closeSoftKeyboard());
        
        ViewInteraction textInputEditText6 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.pickup_yearInput),
0),
0),
isDisplayed()));
        textInputEditText6.perform(replaceText("2023"), closeSoftKeyboard());
        
        ViewInteraction textInputEditText7 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.pickup_timeHourInput),
0),
0),
isDisplayed()));
        textInputEditText7.perform(replaceText("1800"), closeSoftKeyboard());
        
        ViewInteraction materialButton4 = onView(
allOf(withId(R.id.pickup_challengeBtn), withText("Challenge"),
childAtPosition(
allOf(withId(R.id.pickup_modalLayout),
childAtPosition(
withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
5)),
9),
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
