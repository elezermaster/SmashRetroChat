package io.emaster.smashretrochat.model;

import android.os.Parcel;
import android.os.Parcelable;

///import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by elezermaster on 01/09/2017.
 */

//@JsonIgnoreProperties(ignoreUnknown = true) //this help you to ignore provider
public class Users implements Parcelable {
    String user_name;
    String user_email;
    String user_id_email;
    String user_image_url;
    String user_thumb_image;
    String user_status;
    String user_lat;
    String user_lng;
    String user_place;
    String user_area;
    String user_sex;
    String user_age;
    String user_height;
    String user_weight;
    String user_date_birth;
    String user_marriage_status;
    String user_date_created;
    String user_last_online;
    String user_online;
    String user_last_message_from_current;

    public Users() {
    }



    protected Users(Parcel in) {
        user_name = in.readString();
        user_email = in.readString();
        user_id_email = in.readString();
        user_image_url = in.readString();
        user_thumb_image = in.readString();
        user_status = in.readString();
        user_lat = in.readString();
        user_lng = in.readString();
        user_place = in.readString();
        user_area = in.readString();
        user_sex = in.readString();
        user_age = in.readString();
        user_height = in.readString();
        user_weight = in.readString();
        user_date_birth = in.readString();
        user_marriage_status = in.readString();
        user_date_created = in.readString();
        user_last_online = in.readString();
        user_online = in.readString();
        user_last_message_from_current = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_id_email(String user_id_email) {
        this.user_id_email = user_id_email;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }

    public void setUser_thumb_image(String user_thumb_image) {
        this.user_thumb_image = user_thumb_image;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public void setUser_lat(String user_lat) {
        this.user_lat = user_lat;
    }

    public void setUser_lng(String user_lng) {
        this.user_lng = user_lng;
    }

    public void setUser_place(String user_place) {
        this.user_place = user_place;
    }

    public void setUser_area(String user_area) {
        this.user_area = user_area;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public void setUser_height(String user_height) {
        this.user_height = user_height;
    }

    public void setUser_weight(String user_weight) {
        this.user_weight = user_weight;
    }

    public void setUser_date_birth(String user_date_birth) {
        this.user_date_birth = user_date_birth;
    }

    public void setUser_marriage_status(String user_marriage_status) {
        this.user_marriage_status = user_marriage_status;
    }

    public void setUser_date_created(String user_date_created) {
        this.user_date_created = user_date_created;
    }

    public void setUser_last_online(String user_last_online) {
        this.user_last_online = user_last_online;
    }

    public String getUser_last_message_from_current() {
        return user_last_message_from_current;
    }

    public void setUser_last_message_from_current(String user_last_message_from_current) {
        this.user_last_message_from_current = user_last_message_from_current;
    }

    public String getUser_online() {
        return user_online;
    }

    public void setUser_online(String user_online) {
        this.user_online = user_online;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_id_email() {
        return user_id_email;
    }

    public String getUser_image_url() {
        return user_image_url;
    }

    public String getUser_thumb_image() {
        return user_thumb_image;
    }

    public String getUser_status() {
        return user_status;
    }

    public String getUser_lat() {
        return user_lat;
    }

    public String getUser_lng() {
        return user_lng;
    }

    public String getUser_place() {
        return user_place;
    }

    public String getUser_area() {
        return user_area;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public String getUser_age() {
        return user_age;
    }

    public String getUser_height() {
        return user_height;
    }

    public String getUser_weight() {
        return user_weight;
    }

    public String getUser_date_birth() {
        return user_date_birth;
    }

    public String getUser_marriage_status() {
        return user_marriage_status;
    }

    public String getUser_date_created() {
        return user_date_created;
    }

    public String getUser_last_online() {
        return user_last_online;
    }

    public Users(String user_name, String user_email, String user_id_email, String user_image_url, String user_thumb_image, String user_status, String user_lat, String user_lng, String user_place, String user_area, String user_sex, String user_age, String user_height, String user_weight, String user_date_birth, String user_marriage_status, String user_date_created, String user_last_online) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_id_email = user_id_email;
        this.user_image_url = user_image_url;
        this.user_thumb_image = user_thumb_image;
        this.user_status = user_status;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        this.user_place = user_place;
        this.user_area = user_area;
        this.user_sex = user_sex;
        this.user_age = user_age;
        this.user_height = user_height;
        this.user_weight = user_weight;
        this.user_date_birth = user_date_birth;
        this.user_marriage_status = user_marriage_status;
        this.user_date_created = user_date_created;
        this.user_last_online = user_last_online;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(user_name);
        dest.writeString(user_email);
        dest.writeString(user_id_email);
        dest.writeString(user_image_url);
        dest.writeString(user_thumb_image);
        dest.writeString(user_status);
        dest.writeString(user_lat);
        dest.writeString(user_lng);
        dest.writeString(user_place);
        dest.writeString(user_area);
        dest.writeString(user_sex);
        dest.writeString(user_age);
        dest.writeString(user_height);
        dest.writeString(user_weight);
        dest.writeString(user_date_birth);
        dest.writeString(user_marriage_status);
        dest.writeString(user_date_created);
        dest.writeString(user_last_online);
        dest.writeString(user_online);
        dest.writeString(user_last_message_from_current);
    }
}
