package com.example.notconstraintlayout;

public class Heart extends Shape{

    private String meaning;
    private String feature;


    public Heart(String meaning, String feature) {
        this.meaning = meaning;
        this.feature = feature;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
