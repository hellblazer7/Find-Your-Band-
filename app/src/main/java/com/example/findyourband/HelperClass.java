package com.example.findyourband;

public class HelperClass {
    String username,instrument,name,id,email,password;

    public HelperClass(String username,String fullname,String id,String email,String instrument,String password) {
        this.name = fullname;
        this.instrument=instrument;
        this.username=username;
        this.id=id;
        this.email=email;
        this.password=password;
    }
    public HelperClass(){

    }

    public String getFullname() {
        return name;
    }

    public void setFullname(String fullname) {
        this.name = fullname;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
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
}
