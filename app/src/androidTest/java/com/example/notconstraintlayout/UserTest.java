package com.example.notconstraintlayout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)

public class UserTest {

    private Solo solo;
    private UserProfile userProfile;


    // set up a user profile
    @Before
    public void setUserProfile(){
        this.userProfile = new UserProfile();
    }


    @Test
    public void checkTotalScore(){
        int score = userProfile.getTotalScore();
        assertEquals(0,score);
        userProfile.setTotalScore(48);
        int actualScore = userProfile.getTotalScore();
        assertEquals(48,actualScore);
    }

    @Test
    public void checkUserName(){
        String name = userProfile.getUsername();
        assertEquals(null,name);
        userProfile.setUsername("Anna");
        assertEquals("Anna",userProfile.getUsername());
    }

    @Test
    public void checkPhoneNumber(){
        Long number = userProfile.getContactInfo();
//        assertEquals(null,number);
        userProfile.setContactInfo(123456789);
        assertEquals(123456789,userProfile.getContactInfo());
    }

    @Test
    public void checkTotalScanned(){
        int total = userProfile.getTotalScanned();
        assertEquals(0,total);
        userProfile.setTotalScanned(20);
        assertEquals(20,userProfile.getTotalScanned());
    }

    // alwasy include this at the end of the test casese
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();

    }

}