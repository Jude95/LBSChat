package com.jude.lbschat.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mr.Jude on 2016/4/21.
 */
public class PersonBrief implements Serializable, Parcelable {
    private int id;
    private String name;
    private long birth;
    private int gender;//0男 1女
    private String avatar;
    private String intro;
    private double lat;//纬度
    private double lng;//经度
    @SerializedName("address_brief")
    private String addressBrief;
    private String address;


    public PersonBrief(int id, String name, long birth, int gender, String avatar, String intro, double lat, double lng, String addressBrief, String address) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.avatar = avatar;
        this.intro = intro;
        this.lat = lat;
        this.lng = lng;
        this.addressBrief = addressBrief;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressBrief() {
        return addressBrief;
    }

    public void setAddressBrief(String addressBrief) {
        this.addressBrief = addressBrief;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    @Override
    public String toString() {
        return id+":"+name+":"+birth+":"+gender+":"+avatar+":"+intro;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.birth);
        dest.writeInt(this.gender);
        dest.writeString(this.avatar);
        dest.writeString(this.intro);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.addressBrief);
        dest.writeString(this.address);
    }

    protected PersonBrief(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.birth = in.readLong();
        this.gender = in.readInt();
        this.avatar = in.readString();
        this.intro = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.addressBrief = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<PersonBrief> CREATOR = new Parcelable.Creator<PersonBrief>() {
        @Override
        public PersonBrief createFromParcel(Parcel source) {
            return new PersonBrief(source);
        }

        @Override
        public PersonBrief[] newArray(int size) {
            return new PersonBrief[size];
        }
    };
}
