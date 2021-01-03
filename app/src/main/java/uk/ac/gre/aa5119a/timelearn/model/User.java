package uk.ac.gre.aa5119a.timelearn.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Parcelable, Serializable {

    private int id;
    private String email;
    private String password;
//    private boolean loggedIn;

    protected User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
//        this.loggedIn = false;
    }

    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public boolean isLoggedIn() {
//        return loggedIn;
//    }
//
//    public void setLoggedIn(boolean loggedIn) {
//        this.loggedIn = loggedIn;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(password);
    }
}
