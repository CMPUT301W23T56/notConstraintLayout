package com.example.notconstraintlayout.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
    }

//    public LiveData<String> getText() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
//        String name = sharedPreferences.getString("name", "");
//        mText.setValue(name);
//        return mText;
//    }
}