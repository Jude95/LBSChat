package com.jude.lbschat.domain.entities;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhuchenxi on 16/4/22.
 */
public class Account extends PersonBrief implements Serializable{
    private String token;
    private String number;
    @SerializedName("rc_token")
    private String rongCloudToken;
    public Account(int id, String name, long birth, int gender, String avatar, String intro, double lat, double lng, String addressBrief, String address, String token, String number) {
        super(id, name, birth, gender, avatar, intro, lat, lng, addressBrief, address);
        this.token = token;
        this.number = number;
    }

    public String getRongCloudToken() {
        return rongCloudToken;
    }

    public void setRongCloudToken(String rongCloudToken) {
        this.rongCloudToken = rongCloudToken;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (o!=null&&o instanceof Account)
            return getName().equals(((Account) o).getName())
            && getNumber().equals(((Account) o).getNumber())
            && getAvatar().equals(((Account) o).getAvatar())
            && getToken().equals(((Account) o).getToken())
            && getIntro().equals(((Account) o).getIntro())
            && getGender() == ((Account) o).getGender()
            && getLat() == ((Account) o).getLat()
            && getLng() == ((Account) o).getLng()
            && getBirth() == ((Account) o).getBirth()
            && getAddressBrief() == ((Account) o).getAddressBrief();
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.token);
        dest.writeString(this.number);
        dest.writeString(this.rongCloudToken);
    }

    protected Account(Parcel in) {
        super(in);
        this.token = in.readString();
        this.number = in.readString();
        this.rongCloudToken = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
