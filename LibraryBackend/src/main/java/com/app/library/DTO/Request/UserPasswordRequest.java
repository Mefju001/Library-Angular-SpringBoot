package com.app.library.DTO.Request;

public class UserPasswordRequest {
    private String oldpassword;
    private String newpassword;
    private String confirmpassword;

    public UserPasswordRequest(String oldpassword, String newpassword, String confirmpassword) {
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
        this.confirmpassword = confirmpassword;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
}
