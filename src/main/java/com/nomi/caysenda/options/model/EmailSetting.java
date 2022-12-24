package com.nomi.caysenda.options.model;

import java.util.List;
import java.util.Map;

public class EmailSetting {
    String email;
    String password;
    String title;

    Map contents;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Map getContents() {
        return contents;
    }

    public void setContents(Map contents) {
        this.contents = contents;
    }
}
