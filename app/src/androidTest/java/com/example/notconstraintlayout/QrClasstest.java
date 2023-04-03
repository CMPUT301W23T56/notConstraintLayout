package com.example.notconstraintlayout;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)


public class QrClasstest {

    private Solo solo;
    private QrClass qrClass;

    @Before
    public void setQrClass(){
        this.qrClass = new QrClass();
    }

    @Test
    public void ScannedByTest(){
        int scannedBy = qrClass.getScannedBy();
        assertEquals(0,scannedBy);
        int newScannedBy  = qrClass.setScannedBy(5);
        assertEquals(5,newScannedBy);

    }

    @Test
    public void HashTest(){
        String hash = qrClass.getHash();
        assertEquals(null,hash);
    }

    @Test
    public void QrPointTest(){
        int points = qrClass.getPoints();
        assertEquals(0,points);
    }

    @Test
    public void QrNameTest(){
        String name = qrClass.getName();
        assertEquals(null,name);
    }

    @Test
    public void LocationImage(){
        Bitmap image = qrClass.getLocation_image();
        assertEquals(null,image);

    }

}