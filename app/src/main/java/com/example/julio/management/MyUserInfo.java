package com.example.julio.management;

import com.jcraft.jsch.UserInfo;

public class MyUserInfo  implements UserInfo {

    public static String passwd;

    public void setPasswd(String passwd){
        this.passwd = passwd;
    }

    public String getPassword() {
        return passwd;
    }

    public boolean promptYesNo(String str) {
        return true;
    }

    public String getPassphrase() {
        return null;
    }

    public boolean promptPassphrase(String message) {
        return true;
    }

    public boolean promptPassword(String message) {
        return true;
    }

    public void showMessage(String message) {

    }
}
