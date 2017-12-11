package io.emaster.smashretrochat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by elezermaster on 04/09/16.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserNearby {
    String id;
    String name;
    String age;
    String place;
    String area;
    double lat;
    double lng;
    String isOnline;
    String urlFoto;
    ArrayList<String> imagesUrl;
    String email;
    String password;

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

    @JsonIgnoreProperties({ "imagesUrl" })
    public ArrayList<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(ArrayList<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    //@JsonIgnoreProperties({ “stackId" })
    public UserNearby(){
        if(imagesUrl ==null){
            imagesUrl = new ArrayList<String>();
            imagesUrl.add("http://www.clker.com/cliparts/b/1/f/a/1195445301811339265dagobert83_female_user_icon.svg.hi.png");
            imagesUrl.add("https://cdn2.iconfinder.com/data/icons/rcons-users-color/32/maid-512.png");
            imagesUrl.add("https://cdn2.iconfinder.com/data/icons/rcons-users-color/32/housewife-512.png");

        }
    }

    @JsonIgnoreProperties({ "area" })
    public String getArea() { return area; }

    @JsonIgnoreProperties({ "lat" })
    public double getLat() {
        return lat;
    }

    @JsonIgnoreProperties({ "lng" })
    public double getLng() {
        return lng;
    }

    @JsonIgnoreProperties({ "id" })
    public String getId() { return id; }

    @JsonIgnoreProperties({ "place" })
    public String getPlace() { return place; }

    @JsonIgnoreProperties({ "name" })
    public String getName() {
        return name;
    }

    @JsonIgnoreProperties({ "age" })
    public String getAge() {
        return age;
    }

    @JsonIgnoreProperties({ "isOnline" })
    public String getIsOnline() {
        return isOnline;
    }

    @JsonIgnoreProperties({ "urlFoto" })
    public String getUrlFoto() {
        if(this.urlFoto == null ){
            return "http://saqibalich.coolpage.biz/job/images/qdi-generic-testimonial-person.png";
        }
        return urlFoto;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setId(String id) { this.id = id; }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }


}
