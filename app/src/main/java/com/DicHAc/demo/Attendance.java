package com.DicHAc.demo;

public class Attendance {
    Boolean attend;
    User user;

    public Attendance(Boolean attend, User user) {
        this.attend = attend;
        this.user = user;
    }

    public Boolean getAttend() {
        return attend;
    }

    public void setAttend(Boolean attend) {
        this.attend = attend;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Attendance() {
    }
}
