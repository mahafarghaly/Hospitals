package com.example.hospitals.model;

public class UserModel {

    private String email;
    private String password;
    private String phone;
    private String image;
    private String kind;
    private String id;


    public UserModel() {
    }

    public UserModel(String email, String password, String phone, String image, String kind, String id) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.kind = kind;
        this.id = id;

    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
