package com.example.notconstraintlayout;

import static androidx.fragment.app.testing.FragmentScenario.launchInContainer;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.ListView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.notconstraintlayout.ui.dashboard.DashboardFragment;
import com.robotium.solo.Solo;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DashboardFragmentTest {

    private FragmentScenario<DashboardFragment> mFragmentScenario;
    private Solo solo;

    @Before
    public void setup() {
        // Launch the fragment in a container
        mFragmentScenario = launchInContainer(DashboardFragment.class);
    }


    @Test
    public void testClickOnQRCodeButton() {
        onView(withId(R.id.my_fab)).perform(click());
        onView(withId(R.id.my_fab)).check(matches(isDisplayed()));
    }

    @Test
    public void testScanQRCode() {
        onView(withId(R.id.my_fab)).perform(click());
        onView(withId(R.id.my_fab)).check(matches(isDisplayed()));

        // Verify that the scanned QR code is displayed in the UI
        onView(withId(R.id.my_fab)).check(matches(withText("https://example.com/qr-code")));
    }


    private ViewAssertion matches(Matcher<View> withText) {
        return null;
    }

    @Test
    public void DashBoardFragmentTest() {
        mFragmentScenario.onFragment(fragment -> {
            // Perform your tests on the fragment
            assertTrue(fragment instanceof DashboardFragment);
            assertTrue(fragment.isVisible());
            solo.clickInList(0);
            solo.clickOnButton("CANCEL");
        });
    }

    @Test
    public void DeleteQr(){
        mFragmentScenario.onFragment(fragment -> {
            // Perform your tests on the fragment
            assertTrue(fragment instanceof DashboardFragment);
            assertTrue(fragment.isVisible());
            solo.clickInList(0);
            solo.clickOnButton("Delete");
            assertTrue("Empty list",solo.getCurrentViews(ListView.class).get(0).getCount() == 0);
        });
    }
}
