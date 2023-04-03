package com.example.notconstraintlayout;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.notconstraintlayout.ui.dashboard.DashboardFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class LoginTest {

    private Solo solo;



    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);
    private FragmentScenario<Fragment> mFragmentScenario;


    @Before
    public void setUp() throws Exception{
        solo = new Solo (InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }


    @Test
    public void start(){
        Activity activity = rule.getActivity();
    }

    @Test
    public void loginTest(){
        solo.assertCurrentActivity("Wrong Activity",LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.User_name),"Prateek");
        solo.enterText((EditText) solo.getView(R.id.User_phone),"123456" );
        solo.clickOnButton("Continue");
        solo.clickInList(0);
        solo.getCurrentActivity();
        mFragmentScenario.onFragment(fragment -> {
            // Check that the current fragment is an instance of DashboardFragment
            assertTrue(fragment instanceof DashboardFragment);
        });
    }

}