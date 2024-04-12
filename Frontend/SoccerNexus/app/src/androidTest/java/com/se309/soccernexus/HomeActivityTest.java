package com.se309.soccernexus;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public ActivityScenarioRule<HomeActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(HomeActivity.class);

    @Test
    public void homeActivityTest() {
        ViewInteraction imageButton = onView(
                allOf(withId(R.id.homePageButton), withContentDescription("Home Button"),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.teamPageButton), withContentDescription("Team Page Button"),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.profilePageButton), withContentDescription("Profile Page Button"),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.settingsPageButton), withContentDescription("Settings Page Button"),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));

        ViewInteraction view = onView(
                allOf(withId(R.id.homePgIconSelector),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        view.check(matches(isDisplayed()));

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.home_notificationLayout),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction imageButton5 = onView(
                allOf(withId(R.id.home_chatButton), withContentDescription("Chat Button"),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton5.check(matches(isDisplayed()));

        ViewInteraction imageButton6 = onView(
                allOf(withId(R.id.home_notificationsBtn), withContentDescription("Notifications Button"),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton6.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.home_pickupTitle), withText("Upcoming Pickup Matches"),
                        withParent(allOf(withId(R.id.home_scrollLayout),
                                withParent(withId(R.id.nestedScrollView)))),
                        isDisplayed()));
        textView.check(matches(withText("Upcoming Pickup Matches")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.home_pickupTitle), withText("Upcoming Pickup Matches"),
                        withParent(allOf(withId(R.id.home_scrollLayout),
                                withParent(withId(R.id.nestedScrollView)))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.home_pickupRecycler),
                        withParent(allOf(withId(R.id.home_scrollLayout),
                                withParent(withId(R.id.nestedScrollView)))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.home_postsTitle), withText("Posts"),
                        withParent(allOf(withId(R.id.home_scrollLayout),
                                withParent(withId(R.id.nestedScrollView)))),
                        isDisplayed()));
        textView3.check(matches(withText("Posts")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.home_postsTitle), withText("Posts"),
                        withParent(allOf(withId(R.id.home_scrollLayout),
                                withParent(withId(R.id.nestedScrollView)))),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.home_postsRecyclerView),
                        withParent(allOf(withId(R.id.home_scrollLayout),
                                withParent(withId(R.id.nestedScrollView)))),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.imageView3),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.imageView3),
                        withParent(allOf(withId(R.id.constraintLayout3),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));
    }
}
