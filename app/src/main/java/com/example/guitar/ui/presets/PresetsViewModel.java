package com.example.guitar.ui.presets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PresetsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PresetsViewModel() {
        // Initialize the MutableLiveData in the constructor
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        // Return LiveData (not MutableLiveData) to ensure immutability
        return mText;
    }
}
