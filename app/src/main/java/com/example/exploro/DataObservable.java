package com.example.exploro;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.List;

public class DataObservable extends BaseObservable {

    private JSONArray bottomSheetLocationData;
    private List<Byte[]> bottomSheetLocationImageByteData;

    @Bindable
    public JSONArray getBottomSheetLocationData() {
        return this.bottomSheetLocationData;
    }

    @Bindable
    public List<Byte[]> getBottomSheetLocationImageByteData() {
        return this.bottomSheetLocationImageByteData;
    }

    public void setBottomSheetLocationData(JSONArray data) {
        bottomSheetLocationData = data;
        notifyPropertyChanged(BR.bottomSheetLocationData);
    }

    public void setImageByteData(List<Byte[]> data) {
        bottomSheetLocationImageByteData = data;
        notifyPropertyChanged(BR.bottomSheetLocationImageByteData);
    }

}
