package com.example.teamddb.model;

public class acc {
    String AccName,Password,Name,Email,Admin;
    long Id;

    public String getAccName() {
        return AccName;
    }

    public void setAccName(String accName) {
        AccName = accName;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }

    public void setEmail(String email) {
        Email = email;
    }

}
