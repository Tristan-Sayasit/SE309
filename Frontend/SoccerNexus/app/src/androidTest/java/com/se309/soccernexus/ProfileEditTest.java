package com.se309.soccernexus;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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
public class ProfileEditTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void profileEditTest() {
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

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.profile_editProfileBtn), withContentDescription("Edit Profile Button"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.scrollView2), withContentDescription("Scrollbar")),
                                        0),
                                2)));
        appCompatImageButton2.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.profile_edit_title), withText("Edit Profile"),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText("Edit Profile")));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.profile_edit_profilePictureView),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.profile_edit_selectImgBtn), withContentDescription("Select Profile Image Button"),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.profile_edit_firstNameInput),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.profile_edit_lastNameInput),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.profile_edit_heightTitle), withText("Height"),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView2.check(matches(withText("Height")));

        ViewInteraction spinner = onView(
                allOf(withId(R.id.profile_edit_feetSpinner),
                        withParent(allOf(withId(R.id.profile_edit_relativeLayout2),
                                withParent(withId(R.id.constraintLayout6)))),
                        isDisplayed()));
        spinner.check(matches(isDisplayed()));

        ViewInteraction spinner2 = onView(
                allOf(withId(R.id.profile_edit_inchesSpinner),
                        withParent(allOf(withId(R.id.profile_edit_relativeLayout3),
                                withParent(withId(R.id.constraintLayout6)))),
                        isDisplayed()));
        spinner2.check(matches(isDisplayed()));

        ViewInteraction linearLayout3 = onView(
                allOf(withId(R.id.profile_edit_weightInput),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        linearLayout3.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.profile_edit_birthdayButton), withText("10/25/1961"),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.profile_edit_preferredPosTitle), withText("Preferred Position"),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView3.check(matches(withText("Preferred Position")));

        ViewInteraction spinner3 = onView(
                allOf(withId(R.id.profile_edit_positionSpinner),
                        withParent(allOf(withId(R.id.profile_edit_relativeLayout),
                                withParent(withId(R.id.constraintLayout6)))),
                        isDisplayed()));
        spinner3.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.profile_edit_saveBtn), withText("SAVE CHANGES"),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.profile_edit_cancelBtn), withText("CANCEL"),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.profile_edit_cancelBtn), withText("CANCEL"),
                        withParent(allOf(withId(R.id.constraintLayout6),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction textInputEditText3 = onView(
                allOf(withText("Anton"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profile_edit_firstNameInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("Anton"));

        ViewInteraction textInputEditText4 = onView(
                allOf(withText("Anton"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profile_edit_firstNameInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withText("Labi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profile_edit_lastNameInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("Labi"));

        ViewInteraction textInputEditText6 = onView(
                allOf(withText("Labi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profile_edit_lastNameInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText6.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText7 = onView(
                allOf(withText("156"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profile_edit_weightInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText7.perform(replaceText("156"));

        ViewInteraction textInputEditText8 = onView(
                allOf(withText("156"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profile_edit_weightInput),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText8.perform(closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.profile_edit_saveBtn), withText("Save Changes"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout6),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                9),
                        isDisplayed()));
        materialButton2.perform(click());
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
