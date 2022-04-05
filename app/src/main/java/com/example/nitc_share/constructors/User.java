package com.example.nitc_share.constructors;

public class User {
    String Name,Email,Password,Phone;
    float rating;
    Integer ratecount;

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getRatecount() {
        return ratecount;
    }

    public void setRatecount(Integer ratecount) {
        this.ratecount = ratecount;
    }
}
