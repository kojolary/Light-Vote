package net.vokacom.lightvote.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AuthenticatedUser implements Parcelable {

    private String id;
    private String telephone;

    protected AuthenticatedUser(Parcel in) {
        id = in.readString();
        telephone = in.readString();
    }

    public static final Creator<AuthenticatedUser> CREATOR = new Creator<AuthenticatedUser>() {
        @Override
        public AuthenticatedUser createFromParcel(Parcel in) {
            return new AuthenticatedUser(in);
        }

        @Override
        public AuthenticatedUser[] newArray(int size) {
            return new AuthenticatedUser[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(telephone);
    }
}
