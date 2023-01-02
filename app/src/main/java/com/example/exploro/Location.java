package com.example.exploro;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Location implements Parcelable {

    private String name;
    private String address;
    private LatLng latlng;
    private byte[] referenceImage;

    public Location(String name, String address, LatLng latlng, byte[] referenceImage) {
        this.name = name;
        this.address = address;
        this.latlng = latlng;
        this.referenceImage = referenceImage;
    }

    public String getName() { return this.name; }
    public String getAddress() { return this.address; }
    public LatLng getLocation() { return this.latlng; }
    public byte[] getImage() { return this.referenceImage; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setLocation(LatLng latlng) { this.latlng = latlng; }



    // Parcelable functions and stuff ig
    protected Location(Parcel in) {
        name = in.readString();
        address = in.readString();
        latlng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeParcelable(latlng, i);
    }
}
